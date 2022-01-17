package com.mall.order.consumer;

import com.mall.order.constant.OrderConstants;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.StockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: jia.xue
 * @create: 2020-03-31 16:48
 * @Description 消费rocketMQ取消订单延迟消息
 **/
@Slf4j
@Component
@Transactional
public class NewOrderCancelConsumer {

    private DefaultMQPushConsumer mqConsumer;

    @Value("${mq.nameserver.addr}")
    private String addr;
    @Value("${mq.topicname}")
    private String topicName;

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private StockMapper stockMapper;
//    @Autowired
//    private OrderMessageListenerConcurrently orderMessageListenerConcurrently;


 /**
 * 初始化mqConsumer
 * @throws MQClientException
 */
    @PostConstruct
    public void init() throws MQClientException {
        log.info("NewOrderCancelConsumer ->初始化...,topic:{},addre:{} ",topicName,addr);
        mqConsumer = new DefaultMQPushConsumer("stock_consumer_group");
        mqConsumer.setNamesrvAddr(addr);
        mqConsumer.subscribe(topicName, "*");


        mqConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info("开始执行订单[{}]的支付超时订单关闭......", context);
                Order order=new Order();
                String orderId = new String(msgs.get(0).getBody());
                try {
                    order.setOrderId(orderId);
                    //先查询订单是否是待支付状态
                    Order order1=orderMapper.selectByPrimaryKey(order);
                    //未付款才去走逻辑
                    if(order1.getStatus()==0){
                        order.setStatus(OrderConstants.ORDER_STATUS_TRANSACTION_CANCEL);
                        //将订单状态改为取消
                        orderMapper.updateByPrimaryKey(order);
                        //将订单商品的库存状态改为释放
                        orderItemMapper.updateStockStatus(2,orderId);
                        //将库存还回去
                        List<OrderItem> list=orderItemMapper.queryByOrderId(orderId);
                        List<Long> itemIds=list.stream().map(OrderItem::getItemId).sorted().collect(Collectors.toList());
                        //锁 itemIds
                        List<Stock> stocks=stockMapper.findStocksForUpdate(itemIds);
                        stocks.forEach(stock -> {
                            list.forEach(one->{
                                if(Objects.equals(one.getItemId(),stock.getItemId())){
                                    stock.setLockCount(-one.getNum());
                                    stock.setStockCount(one.getNum().longValue());
                                    //释放库存
                                    stockMapper.updateStock(stock);
                                    return;
                                }
                            });
                        });
                    }
                    log.info("超时订单{}处理完毕",orderId);
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }catch (Exception e){
                    log.error("超时订单处理失败:{}",orderId);
                    e.printStackTrace();
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
            }
        });

        mqConsumer.start();
    }

}