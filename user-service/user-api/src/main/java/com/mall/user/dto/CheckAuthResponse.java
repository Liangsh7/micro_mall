package com.mall.user.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/22-13:12
 */
@Data
public class CheckAuthResponse extends AbstractResponse {

    private String userinfo;
}
