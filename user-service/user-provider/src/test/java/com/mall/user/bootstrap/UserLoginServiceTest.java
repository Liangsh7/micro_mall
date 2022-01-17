package com.mall.user.bootstrap;

import com.alibaba.fastjson.JSON;
import com.mall.user.IUserLoginService;
import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author: jia.xue
 * @create: 2020-04-14 21:10
 * @Description
 **/
public class UserLoginServiceTest extends UserProviderApplicationTests {


    @Autowired
    private IUserLoginService userLoginService;

    /**
     * 用户登录
     * @param
     * @return
     */
    @Test
    public void test01(){
        UserLoginRequest userLoginRequest = new UserLoginRequest();
        userLoginRequest.setUserName("test");
        userLoginRequest.setPassword("test");
        UserLoginResponse loginResponse = userLoginService.login(userLoginRequest);
        System.out.println(JSON.toJSONString(loginResponse));
    }

    @Test
    public void test02(){

        CheckAuthRequest checkAuthRequest = new CheckAuthRequest();
        checkAuthRequest.setToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJ3bGd6cyIsImV4cCI6MTU4Njk1NzQ3NSwidXNlciI6IjRCNUM1RkY5OTQ2RjZCQ0M3NkNGODI2RTlCN0UwQjVBQ0Y1RkE0QkNFNzNCRTQ2MkI0QjQ4OTUzNkZEMTZBNjVCNkMwMDY0ODA1N0JDMjlFRDBFMjRCQkVDMDg5MzZGRkIwRkRFMDI2OUJFQzNDMjZGMzdGNkQ3NjY2RjRBN0RFNDNFNUY2RURBNjk3NDg3RTFBNzk4NDdDNjE3Nzg3MEVGODAxRTRBNEQ2RUREMTdCQkE4ODIwODI4NTM3MjJBQTE2RDRDOTE5REM0MzgzMkMwNDQ0M0I1OTlBRENENjNCIn0.7eG8d0LDtB9D_GHAMbt0V9dry0PZyhsqOnq2LH-J5dY");
        CheckAuthResponse checkAuthResponse = userLoginService.validToken(checkAuthRequest);
        System.out.println(JSON.toJSONString(checkAuthResponse));
    }
}