package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/31-19:11
 */
@Data
public class HeadImageRequest extends AbstractRequest {
    private Long userId;
    private String imageData;

    @Override
    public void requestCheck() {

    }
}
