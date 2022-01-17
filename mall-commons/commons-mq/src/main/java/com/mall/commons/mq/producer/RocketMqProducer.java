package com.mall.commons.mq.producer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author: jia.xue
 * @create: 2020-03-31 16:29
 * @Description
 **/
@Slf4j
@Component
public class RocketMqProducer {

    private DefaultMQProducer mqProducer;

    @Value("${mq.nameserver.addr}")
    private String addr;
    @Value("${mq.topicname}")
    private String topicName;
    // 消息延迟级别
    private static final Integer delayLevel = 14;

    @PostConstruct
    public void init(){

        log.info("mqProducer ->初始化...addr:{}, topicName:{}",addr,topicName);
        mqProducer = new DefaultMQProducer("producer_group");
        mqProducer.setNamesrvAddr(addr);
        try {
            mqProducer.start();
        } catch (MQClientException e) {
            log.info("初始化rocketMQ失败……addr:{}",addr);
            e.printStackTrace();
        }

    }

    //发送订单延迟消息
    public void sendOrderMessage(String context) {
        Message message = new Message(topicName,context.getBytes(Charset.forName("utf-8")));
        //延迟十分钟
//        messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h
        message.setDelayTimeLevel(delayLevel);
        SendResult sendResult = null;
        try {
             sendResult = mqProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("订单消息发送失败…context:{}",context);
        } catch (RemotingException e) {
            e.printStackTrace();
            log.info("订单消息发送失败…context:{}",context);
        } catch (MQBrokerException e) {
            e.printStackTrace();
            log.info("订单消息发送失败…context:{}",context);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("订单消息发送失败…context:{}",context);
        }
        if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
            log.info("延迟取消订单消息发送成功…context:{}",context);
            return;
        }else {
            log.info("订单消息发送失败…context:{}",context);
        }
    }

    //发送注册成功消息
    public void sendRegisterSuccMessage(Map map) {
        Message message = new Message(topicName,JSON.toJSONString(map).getBytes(Charset.forName("utf-8")));
        SendResult sendResult = null;
        try {
            sendResult = mqProducer.send(message);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("注册成功消息发送失败…map:{}",JSON.toJSONString(map));
        } catch (RemotingException e) {
            e.printStackTrace();
            log.info("注册成功消息发送失败…map:{}",JSON.toJSONString(map));
        } catch (MQBrokerException e) {
            e.printStackTrace();
            log.info("注册成功消息发送失败…map:{}",JSON.toJSONString(map));
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.info("注册成功消息发送失败…map:{}",JSON.toJSONString(map));
        }
        if (sendResult != null && SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
            log.info("注册成功消息发送成功…map:{}",JSON.toJSONString(map));
            return;
        }else {
            log.info("注册成功消息发送失败…map:{}",JSON.toJSONString(map));
        }

    }

}