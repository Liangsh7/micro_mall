package com.mall.order.biz.callback;

import com.mall.order.biz.context.TransHandlerContext;

/**
 *  ciggar
 * create-date: 2019/8/2-下午9:52
 *
 * 交易回调
 */
public interface TransCallback {

    void onDone(TransHandlerContext context);
}
