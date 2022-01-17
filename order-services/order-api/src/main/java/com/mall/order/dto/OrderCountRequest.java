package com.mall.order.dto;/**
 * Created by mic on 2019/7/30.
 */

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.constant.OrderRetCode;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/30-上午9:59
 */
@Data
public class OrderCountRequest extends AbstractRequest{

    private Long userId;

    @Override
    public void requestCheck() {
        if(userId==null){
            throw new ValidateException(OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
