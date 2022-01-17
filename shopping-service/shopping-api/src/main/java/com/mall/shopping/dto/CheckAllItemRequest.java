package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.constants.ShoppingRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by mic on 2019/7/23.
 */
@Data
public class CheckAllItemRequest extends AbstractRequest {
    private Long userId;
    private String checked;

    @Override
    public void requestCheck() {
        if(userId==null|| StringUtils.isBlank(checked)){
            throw new ValidateException(ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());

        }
    }
}
