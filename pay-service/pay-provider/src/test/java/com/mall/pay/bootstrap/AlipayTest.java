package com.mall.pay.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.pay.PayCoreService;
import com.mall.pay.constants.PayChannelEnum;
import com.mall.pay.dto.PaymentRequest;
import com.mall.pay.dto.PaymentResponse;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;
import com.mall.pay.utils.GlobalIdGeneratorUtil;
import net.bytebuddy.asm.Advice;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

/**
 * @author: jia.xue
 * @create: 2020-03-31 14:42
 * @Description
 **/
public class AlipayTest extends PayProviderApplicationTests {

    @Autowired
    private PayCoreService payCoreService;

    @Autowired
    private GlobalIdGeneratorUtil globalIdGeneratorUtil;

    @Test
    public void testPay(){


        PaymentRequest request = new PaymentRequest();

//        JSONObject object= JSON.parseObject(userInfo);

        Long uid=Long.parseLong("66");
        request.setUserId(uid);
        BigDecimal money= new BigDecimal(0.1);
        request.setOrderFee(money);
        request.setPayChannel(PayChannelEnum.ALI_PAY.getCode());
        request.setSubject("测试数据");
        request.setSpbillCreateIp("115.195.125.164");
        request.setTradeNo("19081913521928025");
        request.setTotalFee(money);

        AlipaymentResponse response = payCoreService.aliPay(request);

        System.err.println("=============" + response);

    }
    @Test
    public void testPayQuery(){

        PaymentRequest request = new PaymentRequest();

        request.setTradeNo("19081913521928023");

        AlipayQueryRetResponse retResponse = payCoreService.queryAlipayRet(request);
        System.out.println(JSON.toJSONString(retResponse));

    }


    @Test
    public void test03() throws ExecutionException, InterruptedException {

        String maxSeq = globalIdGeneratorUtil.getMaxSeq();
        System.out.println(maxSeq);

    }
}