package com.mall.user.bootstrap;

import com.mall.commons.mq.producer.RocketMqProducer;
import com.mall.user.dal.entitys.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafaSendTest {
    @Autowired
    RocketMqProducer rocketMqProducer;

    @Test
    public void sendMesg(){
        User user = new User();
        user.setUsername("test");
        user.setAddress("北京");
        user.setEmail("sssss@163.com");
        rocketMqProducer.sendRegisterSuccMessage(null);
    }

}
