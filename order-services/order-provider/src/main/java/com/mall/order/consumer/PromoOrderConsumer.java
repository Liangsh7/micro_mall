package com.mall.order.consumer;

import com.alibaba.fastjson.JSON;
import com.mall.order.OrderPromoService;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
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

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Component
@Slf4j
public class PromoOrderConsumer {

    @Value("${mq.nameserver.addr}")
    private String addr;

    private final static String topicName = "promo_order";

    private DefaultMQPushConsumer mqConsumer;

    @Autowired
    private OrderPromoService orderPromoService;

    @PostConstruct
    public void init() throws MQClientException {
        log.info("PromoOrderConsumer ->初始化...,topic:{},addre:{} ", topicName, addr);
        mqConsumer = new DefaultMQPushConsumer("promo_order_group");
        mqConsumer.setNamesrvAddr(addr);
        mqConsumer.subscribe(topicName, "*");

        // 消费一个创建秒杀订单的消息
        mqConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                log.info("收到秒杀订单创建消息...");
                try {
                    MessageExt messageExt = msgs.get(0);
                    byte[] body = messageExt.getBody();
                    String bodyStr = new String(body);


                    Map<String,Object> map = JSON.parseObject(bodyStr,Map.class);
                    String username = (String) map.get("username");
                    Integer userId = (Integer) map.get("userId");
                    Integer productId = (Integer) map.get("productId");
                    BigDecimal price = (BigDecimal) map.get("price");

                    Integer addressId = (Integer) map.get("addressId");
                    String tel = (String) map.get("tel");
                    String streetName = (String) map.get("streetName");


                    CreateSeckillOrderRequest createSeckillOrderRequest = new  CreateSeckillOrderRequest();
                    createSeckillOrderRequest.setUsername(username);
                    createSeckillOrderRequest.setUserId(Long.valueOf(userId));
                    createSeckillOrderRequest.setProductId(Long.valueOf(productId));
                    createSeckillOrderRequest.setPrice(price);

                    createSeckillOrderRequest.setAddressId(Long.valueOf(addressId));
                    createSeckillOrderRequest.setTel(tel);
                    createSeckillOrderRequest.setStreetName(streetName);

                    log.info("秒杀创建订单接口参数request:{}",JSON.toJSONString(createSeckillOrderRequest));

                    CreateSeckillOrderResponse response = orderPromoService.createPromoOrder(createSeckillOrderRequest);

                    if (response.getCode().equals(OrderRetCode.SUCCESS.getCode())) {
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }else {
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
        });

        mqConsumer.start();
    }
}