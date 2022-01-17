package com.mall.pay.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.pay.PayCoreService;
import com.mall.pay.constants.PayChannelEnum;
import com.mall.pay.dto.PaymentRequest;
import com.mall.pay.dto.PaymentResponse;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @create: 2020-03-31 14:43
 * @Description
 **/
public class WechatPayTest extends PayProviderApplicationTests {

    @Autowired
    private PayCoreService payCoreService;

    @Test
    public void testPay(){


        PaymentRequest request = new PaymentRequest();

//        JSONObject object= JSON.parseObject(userInfo);

        Long uid=Long.parseLong("66");
        request.setUserId(uid);
        BigDecimal money= new BigDecimal(0.1);
        request.setOrderFee(money);
        request.setPayChannel(PayChannelEnum.WECHAT_PAY.getCode());
        request.setSubject("csmall");
        request.setSpbillCreateIp("115.195.125.164");
        request.setTradeNo("19081913521928023");
        request.setTotalFee(money);
        WechatPaymentResopnse resopnse = payCoreService.wechatPay(request);
        System.out.println(JSON.toJSONString(resopnse));

    }


}