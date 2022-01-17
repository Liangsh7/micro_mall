package com.mall.user.dal.entitys;

import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/8/6-14:35
 */
@Data
public class ImageResult {
    String img;  // 这个img返回给前端可以显示出一个图片
    String code; // 就是验证码 xj14
}