package com.mall.user;

import com.mall.user.dto.UserRegisterRequest;
import com.mall.user.dto.UserRegisterResponse;

public interface IUserRegisterService {

    /**
     * 实现用户注册功能
     * @param request
     * @return
     *
     *  向用户表中插入一条记录
     *
     *  向用户验证表中插入一条记录
     *
     *  这种场景是需要事务控制的
     *
     *  发送用户激活邮件
     *
     */
    UserRegisterResponse register(UserRegisterRequest request);
}
