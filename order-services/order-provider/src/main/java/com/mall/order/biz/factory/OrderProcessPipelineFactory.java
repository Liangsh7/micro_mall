package com.mall.order.biz.factory;

import com.mall.order.biz.handler.*;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.biz.convert.CreateOrderConvert;
import com.mall.order.biz.convert.TransConvert;
import com.mall.order.dto.CreateOrderRequest;
import com.mall.order.biz.handler.ClearCartItemHandler;
import com.mall.order.biz.handler.SendMessageHandler;
import com.mall.order.biz.handler.SubStockHandler;
import com.mall.order.biz.handler.ValidateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *  ciggar
 * create-date: 2019/8/2-下午10:52
 *
 * 构建订单处理链
 */
@Slf4j
@Service
public class OrderProcessPipelineFactory extends AbstranctTransPipelineFactory<CreateOrderRequest> {

    @Autowired
    private InitOrderHandler initOrderHandler;
    @Autowired
    private ValidateHandler validateHandler;
    @Autowired
    private LogisticalHandler logisticalHandler;
    @Autowired
    private ClearCartItemHandler clearCartItemHandler;
    @Autowired
    private SubStockHandler subStockHandler;
    @Autowired
    private SendMessageHandler sendMessageHandler;


    @Override
    protected TransHandlerContext createContext() {
        CreateOrderContext createOrderContext=new CreateOrderContext();
        return createOrderContext;
    }

    @Override
    protected void doBuild(TransPipeline pipeline) {
        pipeline.addLast(validateHandler);
        pipeline.addLast(subStockHandler);
        pipeline.addLast(initOrderHandler);
        pipeline.addLast(logisticalHandler);
        pipeline.addLast(clearCartItemHandler);

        // 发送一个订单延迟取消的消息，可以用Redis来做
        pipeline.addLast(sendMessageHandler);
    }

    @Override
    protected TransConvert createConvert() { //构建转换器
        return new CreateOrderConvert();
    }
}
