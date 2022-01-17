
package com.mall.order.biz.convert;


import com.mall.commons.result.AbstractRequest;
import com.mall.commons.result.AbstractResponse;
import com.mall.order.biz.context.TransHandlerContext;

/**
 *  ciggar
 * create-date: 2019/8/2-下午9:55
 */
public interface TransConvert {
    /**
     * 请求转上下文
     * 
     * @param req
     * @return
     */
    TransHandlerContext convertRequest2Ctx(AbstractRequest req, TransHandlerContext context);
    
    /**
     * 上下文转应答
     * 
     * @param ctx
     * @return
     */
    AbstractResponse convertCtx2Respond(TransHandlerContext ctx);
}
