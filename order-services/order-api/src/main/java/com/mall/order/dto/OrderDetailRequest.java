package com.mall.order.dto;/**
 * Created by mic on 2019/7/31.
 */

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.constant.OrderRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *  ciggar
 * create-date: 2019/7/31-上午9:40
 */
@Data
public class OrderDetailRequest extends AbstractRequest{
    private String orderId;

    @Override
    public void requestCheck() {
        if(StringUtils.isBlank(orderId)){
            throw new ValidateException(OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),OrderRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }
    }
}
