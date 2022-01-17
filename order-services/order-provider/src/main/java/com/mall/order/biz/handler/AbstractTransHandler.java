package com.mall.order.biz.handler;

import com.mall.order.biz.callback.TransCallback;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/8/2-下午10:01
 */
@Data
public abstract class AbstractTransHandler implements TransHandler {

    public static final String DEFAULT = "default";

    public TransCallback getTransCallback(){return null;}
}
