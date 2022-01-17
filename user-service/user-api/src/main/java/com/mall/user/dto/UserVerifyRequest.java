package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *  ciggar
 * create-date: 2019/7/23-12:48
 */
@Data
public class UserVerifyRequest extends AbstractRequest {

    private String userName;
    /**
     * 注册时生产的唯一序号
     */
    private String uuid;

    @Override
    public void requestCheck() {
        if(StringUtils.isBlank(userName)||StringUtils.isBlank(uuid)){
            throw new ValidateException(SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
