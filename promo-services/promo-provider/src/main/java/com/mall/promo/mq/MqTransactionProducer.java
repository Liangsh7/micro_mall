package com.mall.promo.mq;

import com.alibaba.fastjson.JSON;
import com.mall.promo.cache.CacheManager;
import com.mall.promo.constant.PromoRetCode;
import com.mall.promo.dal.persistence.PromoItemMapper;
import com.mall.promo.dto.CreatePromoOrderRequest;
import com.mall.promo.service.PromoServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Component
@Slf4j
public class MqTransactionProducer {

    @Value("${mq.nameserver.addr}")
    private String addr;
    @Value("${mq.topicname}")
    private String topic;

    private TransactionMQProducer transactionMQProducer;

    @Autowired
    private PromoItemMapper promoItemMapper;

    @Autowired
    private CacheManager cacheManager;


    @PostConstruct
    public void init(){

        transactionMQProducer = new TransactionMQProducer("promo_group");
        transactionMQProducer.setNamesrvAddr(addr);

        try {
            transactionMQProducer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
        }


        //注册一个事务监听器
        transactionMQProducer.setTransactionListener(new TransactionListener() {

            // 执行本地事务 = 扣减库存
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {

                HashMap<String,Object> argsMap = (HashMap<String, Object>) arg;
                Long productId = (Long) argsMap.get("productId");
                Long psId = (Long) argsMap.get("psId");

                // 扣减库存
                Integer effectedRows = promoItemMapper.decreaseStock(productId,psId);
                String key = "promo_order_id_"  + msg.getTransactionId();
                if (effectedRows < 1) {

                    String value = "fail";
                    cacheManager.setCache(key,value,1);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                String value = "success";
                cacheManager.setCache(key,value,1);

                return LocalTransactionState.COMMIT_MESSAGE;
            }


            // 检查本地事务 这个实现手段有很多种
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {

                // 怎样去检查本地事务的执行结果呢？

                String key = "promo_order_id_" + msg.getTransactionId();

                String value = cacheManager.checkCache(key);
                if ("fail".equals(value)) {
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                if ("success".equals(value)) {
                    return LocalTransactionState.COMMIT_MESSAGE;
                }

                return LocalTransactionState.UNKNOW;
            }
        });
    }

    public Boolean sendPromoOrderTransaction(CreatePromoOrderRequest request) {

        // 构建参数
        HashMap<String, Object> map = new HashMap<>();
        map.put("username",request.getUsername());
        map.put("userId",request.getUserId());
        map.put("productId",request.getProductId());
        map.put("price",request.getPromoPrice());
        map.put("addressId",request.getAddressId());
        map.put("tel",request.getTel());
        map.put("streetName",request.getStreetName());

        Message message = new Message(topic,JSON.toJSONString(map).getBytes(Charset.forName("utf-8")));

        HashMap<String, Object> argsMap = new HashMap<>();
        argsMap.put("productId",request.getProductId());
        argsMap.put("psId",request.getPsId());

        TransactionSendResult transactionSendResult = null;
        try {
            // 发送事务型消息
            transactionSendResult = transactionMQProducer.sendMessageInTransaction(message, argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
        }

        if (transactionSendResult == null || transactionSendResult.getLocalTransactionState() == null) {
            return false;
        }
        // 查看事务型消息的发送结果
        LocalTransactionState localTransactionState = transactionSendResult.getLocalTransactionState();
        if (localTransactionState.equals(LocalTransactionState.COMMIT_MESSAGE)) {
            return true;
        }else if (localTransactionState.equals(LocalTransactionState.ROLLBACK_MESSAGE)) {
            return false;
        }else {
            return false;
        }
    }
}