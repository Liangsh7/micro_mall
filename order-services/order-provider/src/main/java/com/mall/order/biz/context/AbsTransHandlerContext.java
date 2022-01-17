package com.mall.order.biz.context;

import com.mall.order.biz.convert.TransConvert;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/8/2-下午9:55
 * 交易相关的抽象
 */
@Data
public abstract class AbsTransHandlerContext implements TransHandlerContext {

    private String orderId;

    private TransConvert convert = null;


}
