package com.cskaoyan.gateway.controller.user;

import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.user.IUserRegisterService;
import com.mall.user.IUserVerifyService;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.dto.UserVerifyRequest;
import com.mall.user.dto.UserVerifyResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * ciggar 19-09-02
 * 用户注册激活
 */
@RestController
@RequestMapping("/user")
public class UserVerifyController {

    @Reference(timeout = 3000,check = false)
    IUserRegisterService iUserRegisterService;

    @Reference(timeout = 3000,check = false)
    IUserVerifyService iUserVerifyService;

    @Anoymous
    @GetMapping("/verify")
    public ResponseData register(@RequestParam String uuid,@RequestParam String username, HttpServletRequest request){
        if(!(StringUtils.isNotBlank(uuid) &&  StringUtils.isNotBlank(username))){
            return new ResponseUtil<>().setErrorMsg("注册序号/用户名不允许为空");
        }
        UserVerifyRequest userVerifyRequest = new UserVerifyRequest();
        userVerifyRequest.setUserName(username);
        userVerifyRequest.setUuid(uuid);
        UserVerifyResponse userVerifyResponse = iUserVerifyService.verifyMemer(userVerifyRequest);
        if(userVerifyResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil().setData(null);
        }else{
            return new ResponseUtil().setData(userVerifyResponse.getMsg());
        }
    }
}
