package com.cskaoyan.gateway.controller.user;

/**
 * create by ciggar on 2020/04/05
 */

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.commons.tool.utils.CookieUtil;
import com.mall.user.IKaptchaService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.KaptchaCodeRequest;
import com.mall.user.dto.KaptchaCodeResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class CaptchaController {

    @Reference(timeout = 3000,check = false)
    IKaptchaService kaptchaService;

    /**
     * 获取验证码
     */
    @Anoymous
    @GetMapping("/kaptcha")
    public ResponseData getKaptchaCode(HttpServletResponse response) {
        KaptchaCodeRequest kaptchaCodeRequest=new KaptchaCodeRequest();
        KaptchaCodeResponse kaptchaCodeResponse=kaptchaService.getKaptchaCode(kaptchaCodeRequest);
        if(kaptchaCodeResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())){
            Cookie cookie=CookieUtil.genCookie("kaptcha_uuid",kaptchaCodeResponse.getUuid(),"/",60);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            return new ResponseUtil<>().setData(kaptchaCodeResponse.getImageCode());
        }
        return new ResponseUtil<>().setErrorMsg(kaptchaCodeResponse.getCode());
    }

    /**
     * 验证验证码
     * @param code
     * @param httpServletRequest
     * @return
     */
    @Anoymous
    @PostMapping("/kaptcha")
    public ResponseData validKaptchaCode(@RequestBody String code,HttpServletRequest httpServletRequest) {
        KaptchaCodeRequest request = new KaptchaCodeRequest();
        String uuid = CookieUtil.getCookieValue(httpServletRequest, "kaptcha_uuid");
        request.setUuid(uuid);
        request.setCode(code);
        KaptchaCodeResponse response = kaptchaService.validateKaptchaCode(request);
        if (response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setData(null);
        }
        return new ResponseUtil<>().setErrorMsg(response.getCode());
    }
}
