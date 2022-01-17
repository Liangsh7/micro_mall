//package com.mall.user.registerVerification;
//
//import com.mall.commons.tool.email.DefaultEmailSender;
//import com.mall.commons.tool.email.MailData;
//import com.mall.commons.tool.email.emailConfig.EmailConfig;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.support.Acknowledgment;
//import org.springframework.stereotype.Component;
//
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Administrator
// * 2019/8/22 0022
// * 18:32
// */
//@Component
//@Slf4j
//public class KafKaRegisterSuccMailConsumer {
//
//    private final static String topic = "user-register-succ-topic";
//    private final static String group_id = "mail-group-id";
//
//    @Autowired
//    EmailConfig emailConfig;
//
//    @Autowired
//    DefaultEmailSender defaultEmailSender;
//
//    /**
//     * 指定消费某一个分区
//     * @KafkaListener(id = "",topicPartitions ={@TopicPartition(topic=topic,partitions = {"1"})},containerFactory = "userRegisterSuccKafkaListenerContainerFactory",groupId = group_id)
//     */
//    @KafkaListener(id = "",topics = topic,containerFactory = "userRegisterSuccKafkaListenerContainerFactory",groupId = group_id)
//    public void receiveInfo(Map userVerifyMap, Acknowledgment acknowledgment){
//        try {
//            log.info("收到一条注册消息"+userVerifyMap);
//            sendMail(userVerifyMap);
//            acknowledgment.acknowledge();//手动提交消息
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//}
