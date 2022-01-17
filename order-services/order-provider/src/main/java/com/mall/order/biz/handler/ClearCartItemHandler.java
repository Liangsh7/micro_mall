package com.mall.order.biz.handler;/**
 * Created by mic on 2019/8/1.
 */

import com.mall.commons.tool.exception.BizException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.constant.OrderRetCode;
import com.mall.shopping.ICartService;
import com.mall.shopping.dto.ClearCartItemRequest;
import com.mall.shopping.dto.ClearCartItemResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

/**
 *  ciggar
 * create-date: 2019/8/1-下午5:05
 * 将购物车中的缓存失效
 */
@Slf4j
@Component
public class ClearCartItemHandler extends AbstractTransHandler {
    @Reference(check = false)
    ICartService cartService;

    //是否采用异步方式执行
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public boolean handle(TransHandlerContext context) {
        log.info("begin - ClearCartItemHandler-context:"+context);
        //TODO 缓存失效和下单是属于两个事物操作，需要保证成功，后续可以改造成消息队列的形式来实现
        ClearCartItemRequest request=new ClearCartItemRequest();
        CreateOrderContext createOrderContext=(CreateOrderContext)context;
        request.setProductIds(createOrderContext.getBuyProductIds());
        request.setUserId(createOrderContext.getUserId());
        ClearCartItemResponse response=cartService.clearCartItemByUserID(request);
        if(OrderRetCode.SUCCESS.getCode().equals(response.getCode())){
            return true;
        }else{
            throw new BizException(response.getCode(),response.getMsg());
        }
    }
}
