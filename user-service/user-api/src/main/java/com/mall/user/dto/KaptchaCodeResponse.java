package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/8/6-14:43
 */
@Data
public class KaptchaCodeResponse extends AbstractResponse {

    private String imageCode; // 是不是刚刚产生的图片

    private String uuid; //


}
