package com.mall.order.biz.factory;/**
 * Created by mic on 2019/8/2.
 */

import com.mall.order.biz.TransOutboundInvoker;

/**
 *  ciggar
 * create-date: 2019/8/2-下午10:28
 */
public interface TransPipelineFactory<T> {

    TransOutboundInvoker build(T obj);
}
