package com.mall.user.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.user.constants.SysRetCodeConstants;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/31-19:24
 */
@Data
public class DeleteAddressRequest extends AbstractRequest {
    private Long addressId;

    @Override
    public void requestCheck() {
        if(addressId==null) {
            throw new ValidateException(
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getCode(),
                    SysRetCodeConstants.REQUEST_CHECK_FAILURE.getMessage());
        }
    }
}
