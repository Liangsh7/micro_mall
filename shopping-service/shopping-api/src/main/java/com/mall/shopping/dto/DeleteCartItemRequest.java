package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.shopping.constants.ShoppingRetCode;
import com.mall.shopping.constants.ShoppingRetCode;
import lombok.Data;

/**
 * Created by mic on 2019/7/23.
 */
@Data
public class DeleteCartItemRequest extends AbstractRequest{
    private Long userId;
    private Long itemId;

    @Override
    public void requestCheck() {
        if(userId==null||itemId==null){
            throw new ValidateException(ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),ShoppingRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
