//package com.mall.user.registerVerification;
//
//import com.alibaba.fastjson.JSON;
//import com.mall.commons.tool.email.DefaultEmailSender;
//import com.mall.commons.tool.email.MailData;
//import com.mall.commons.tool.email.emailConfig.EmailConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
//import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
//import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
//import org.apache.rocketmq.client.exception.MQClientException;
//import org.apache.rocketmq.common.message.MessageExt;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.util.*;
//
///**
// * @author: jia.xue
// * @create: 2020-03-31 16:48
// * @Description 注册成功，发送激活邮件
// **/
//@Slf4j
//@Component
//@Transactional
//public class RegisterSuccConsumer {
//
//    private DefaultMQPushConsumer mqConsumer;
//
//    @Value("${mq.nameserver.addr}")
//    private String addr;
//    @Value("${mq.topicname}")
//    private String topicName;
//
//    @Autowired
//    EmailConfig emailConfig;
//
//    @Autowired
//    DefaultEmailSender defaultEmailSender;
//
//
// /**
// * 初始化mqConsumer
// * @throws MQClientException
// */
//    @PostConstruct
//    public void init() throws MQClientException {
//        log.info("mqConsumer ->初始化...,topic:{},addre:{} ",topicName,addr);
//        mqConsumer = new DefaultMQPushConsumer("register_succ_consumer_group");
//        mqConsumer.setNamesrvAddr(addr);
//        mqConsumer.subscribe(topicName, "*");
//
//
//        mqConsumer.registerMessageListener(new MessageListenerConcurrently() {
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                MessageExt message = msgs.get(0);
//                byte[] body = message.getBody();
//                Map map = JSON.parseObject(body.toString(), Map.class);
//                log.info("开始执行注册邮件发送成功消息......map:{}", JSON.toJSONString(map));
//                try {
//                    sendMail(map);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    log.info("发送注册成功消息失败，map:{}",JSON.toJSONString(map));
//                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
//                }
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
//            }
//        });
//
//        mqConsumer.start();
//    }
//
//
//
//    public void sendMail(Map userVerifyMap) throws Exception {
//            MailData mailData = new MailData();
//            mailData.setToAddresss(Arrays.asList((String)userVerifyMap.get("email")));
//            mailData.setSubject(emailConfig.getSubject());
//            mailData.setContent("用户激活邮件");
//            Map<String,Object> viewObj  = new HashMap<>();
//            viewObj.put("url",emailConfig.getUserMailActiveUrl()+"?username="+userVerifyMap.get("username")+"&email="+userVerifyMap.get("key"));
//            viewObj.put("title",emailConfig.getSubject());
//            mailData.setDataMap(viewObj);
//            defaultEmailSender.sendHtmlMailUseTemplate(mailData);
//    }
//
//}