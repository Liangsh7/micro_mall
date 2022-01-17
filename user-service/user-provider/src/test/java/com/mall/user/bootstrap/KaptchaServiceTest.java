package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.user.IKaptchaService;
import com.mall.user.dto.KaptchaCodeRequest;
import com.mall.user.dto.KaptchaCodeResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: jia.xue
 * @create: 2020-04-14 20:12
 * @Description 验证码测试
 **/
public class KaptchaServiceTest extends UserProviderApplicationTests {

    @Autowired
    private IKaptchaService kaptchaService;


    /**
     * 获取图形验证码
     * @param request
     * @return
     */
    @Test
    public void test01(){
        KaptchaCodeRequest request = new KaptchaCodeRequest();
        KaptchaCodeResponse kaptchaCode = kaptchaService.getKaptchaCode(request);
        System.out.println(JSON.toJSONString(kaptchaCode));
        System.err.println("=====================");
    }

    /**
     * 验证图形验证码
     * @param request
     * @return
     */
    @Test
    public void test02(){
        KaptchaCodeRequest request = new KaptchaCodeRequest();
        request.setCode("nmsq");
        request.setUuid("45d5c981-9d89-4095-8a2b-ffc1fdd234ea");
        KaptchaCodeResponse kaptchaCodeResponse = kaptchaService.validateKaptchaCode(request);
        System.out.println(JSON.toJSONString(kaptchaCodeResponse));

    }


}