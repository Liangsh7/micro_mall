package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *  ciggar
 * create-date: 2019/8/6-14:43
 */
@Data
public class KaptchaCodeRequest extends AbstractRequest {

    private String uuid;

    private String code;

    @Override
    public void requestCheck() {
        if(StringUtils.isBlank(uuid)||StringUtils.isBlank(code)){
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
