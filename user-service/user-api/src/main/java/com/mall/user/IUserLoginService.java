package com.mall.user;

import com.mall.user.dto.CheckAuthRequest;
import com.mall.user.dto.CheckAuthResponse;
import com.mall.user.dto.UserLoginRequest;
import com.mall.user.dto.UserLoginResponse;

public interface IUserLoginService {

    /**
     * 用户登录
     * @param request
     * @return
     */
    UserLoginResponse login(UserLoginRequest request);


    /**
     * 校验过程
     * @param request
     * @return
     */
    CheckAuthResponse validToken(CheckAuthRequest request);
}
