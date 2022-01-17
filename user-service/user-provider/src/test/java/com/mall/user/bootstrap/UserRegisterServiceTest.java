package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.commons.mq.producer.RocketMqProducer;
import com.mall.user.IUserRegisterService;
import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: jia.xue
 * @create: 2020-04-14 21:33
 * @Description
 **/
public class UserRegisterServiceTest extends UserProviderApplicationTests {

    @Autowired
    private IUserRegisterService registerService;

    @Autowired
    private RocketMqProducer rocketMqProducer;

//    @Autowired
//    private RegisterSuccConsumer consumer;


    @Test
    public void test01(){

        UserRegisterRequest request = new UserRegisterRequest();
        request.setUserName("ciggar03");
        request.setUserPwd("ciggar03");
        request.setEmail("xuejia@cskaoyan.onaliyun.com");

        UserRegisterResponse response = registerService.register(request);
        System.out.println(JSON.toJSONString(response));
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试发送激活邮件
     */
    @Test
    public void test02() throws Exception {
        Map map = new HashMap();

        map.put("username","ciggar");
        map.put("key","987de5a9a1c8e44d072713472d8893c4");
        map.put("email","xuejia@cskaoyan.onaliyun.com");

//        consumer.sendMail(map);

    }
}