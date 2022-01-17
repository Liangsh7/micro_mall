package com.mall.user;

import com.mall.user.dto.UserVerifyRequest;
import com.mall.user.dto.UserVerifyResponse;

public interface IUserVerifyService {



    /**
     * 激活邮件
     * @param userVerifyRequest
     * @return
     */
    UserVerifyResponse verifyMemer(UserVerifyRequest userVerifyRequest);
}
