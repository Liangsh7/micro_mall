package com.mall.user;

import com.mall.user.dto.KaptchaCodeRequest;
import com.mall.user.dto.KaptchaCodeResponse;

/**
 *  ciggar
 * create-date: 2019/8/6-14:42
 */
public interface IKaptchaService {

    /**
     * 获取图形验证码
     * @param request
     * @return
     */
    KaptchaCodeResponse getKaptchaCode(KaptchaCodeRequest request);

    /**
     * 验证图形验证码
     * @param request
     * @return
     */
    KaptchaCodeResponse validateKaptchaCode(KaptchaCodeRequest request);

}
