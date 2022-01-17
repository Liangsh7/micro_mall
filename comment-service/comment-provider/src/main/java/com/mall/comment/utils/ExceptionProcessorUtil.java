package com.mall.comment.utils;

import com.mall.comment.constant.CommentRetCode;
import com.mall.commons.result.AbstractResponse;
import com.mall.commons.tool.exception.ExceptionUtil;

/**
 * @author heps
 * @date 2019/8/12 21:22
 */
public class ExceptionProcessorUtil {

    private ExceptionProcessorUtil() {}

    public static void handleException(AbstractResponse response, Exception e) {
        try {
            ExceptionUtil.handlerException4biz(response, e);
        } catch (Exception ex) {
            response.setCode(CommentRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(CommentRetCode.SYSTEM_ERROR.getMessage());
        }
    }
}
