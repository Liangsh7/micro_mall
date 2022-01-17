package com.mall.shopping.utils;

import com.mall.commons.result.AbstractResponse;
import com.mall.commons.tool.exception.ExceptionUtil;
import com.mall.shopping.constants.ShoppingRetCode;

/**
 *  ciggar
 * create-date: 2019/7/22-15:48
 */
public class ExceptionProcessorUtils {

    public static void wrapperHandlerException(AbstractResponse response,Exception e){
        try {
            ExceptionUtil.handlerException4biz(response,e);
        } catch (Exception ex) {
            response.setCode(ShoppingRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(ShoppingRetCode.SYSTEM_ERROR.getMessage());
        }
    }
}
