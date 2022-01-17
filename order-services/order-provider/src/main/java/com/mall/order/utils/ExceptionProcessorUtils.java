package com.mall.order.utils;

import com.mall.commons.result.AbstractResponse;
import com.mall.commons.tool.exception.ExceptionUtil;
import com.mall.order.constant.OrderRetCode;

/**
 *  ciggar
 * create-date: 2019/7/22-15:48
 */
public class ExceptionProcessorUtils {

    public static void wrapperHandlerException(AbstractResponse response,Exception e){
        try {
            ExceptionUtil.handlerException4biz(response,e);
        } catch (Exception ex) {
            response.setCode(OrderRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(OrderRetCode.SYSTEM_ERROR.getMessage());
        }
    }
}
