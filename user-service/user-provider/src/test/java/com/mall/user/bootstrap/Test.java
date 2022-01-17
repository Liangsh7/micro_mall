package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
public class Test {

    public static void main(String[] args) {
        Map<String,SimpleMailMessage> map = new HashMap();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setText("text");

        map.put("email",message);

        String jsonString = JSON.toJSONString(map);

        Map maps = JSON.parseObject(jsonString, Map.class);


/**
 * 这里不能直接强转，取出来的类型是JSONObject
 */
//        SimpleMailMessage email = (SimpleMailMessage) maps.get("email");

        JSONObject jsonObject = (JSONObject) maps.get("email");

        String string = JSON.toJSONString(jsonObject);

        SimpleMailMessage mailMessage = JSON.parseObject(string, SimpleMailMessage.class);


        System.out.println(JSON.toJSONString(mailMessage));
    }
}