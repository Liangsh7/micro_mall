package com.mall.order.services;

import com.alibaba.fastjson.JSON;
import com.mall.order.OrderCoreService;
import com.mall.order.biz.TransOutboundInvoker;
import com.mall.order.biz.context.AbsTransHandlerContext;
import com.mall.order.biz.factory.OrderProcessPipelineFactory;
import com.mall.order.constant.OrderConstants;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.OrderMapper;
import com.mall.order.dal.persistence.OrderShippingMapper;
import com.mall.order.dto.*;
import com.mall.order.utils.ExceptionProcessorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 *  ciggar
 * create-date: 2019/7/30-上午10:05
 */
@Slf4j
@Component
@Service(cluster = "failfast")
public class OrderCoreServiceImpl implements OrderCoreService {

	@Autowired
	OrderMapper orderMapper;

	@Autowired
	OrderItemMapper orderItemMapper;

	@Autowired
	OrderShippingMapper orderShippingMapper;

	@Autowired
    OrderProcessPipelineFactory orderProcessPipelineFactory;

//    @Autowired
//    OrderCoreService orderCoreService;


	/**
	 * 创建订单的处理流程
	 *
	 * @param request
	 * @return
	 */
	@Override
	public CreateOrderResponse createOrder(CreateOrderRequest request) {
		CreateOrderResponse response = new CreateOrderResponse();
		try {
			TransOutboundInvoker invoker = orderProcessPipelineFactory.build(request);
			invoker.start(); //启动流程（pipeline来处理）
			AbsTransHandlerContext context = invoker.getContext();
			response = (CreateOrderResponse) context.getConvert().convertCtx2Respond(context);
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.createOrder Occur Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}

	/**
	 * 取消订单
	 *
	 * @param request
	 * @return
	 */
	@Override
	public CancelOrderResponse cancelOrder(CancelOrderRequest request) {
		CancelOrderResponse response = new CancelOrderResponse();
		try {
			Order order = new Order();
			order.setOrderId(request.getOrderId());
			order.setStatus(OrderConstants.ORDER_STATUS_TRANSACTION_CANCEL);
			order.setCloseTime(new Date());
			log.info("修改訂單狀態 order:{}", JSON.toJSONString(order));
			int num = orderMapper.updateByPrimaryKeySelective(order);
			log.info("cancelOrder,effect Row:" + num);
			response.setCode(OrderRetCode.SUCCESS.getCode());
			response.setMsg(OrderRetCode.SUCCESS.getMessage());
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.cancelOrder Occur Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}



	/**
	 * 删除订单
	 *
	 * @param request
	 * @return
	 */
	@Override
	public DeleteOrderResponse deleteOrder(DeleteOrderRequest request) {
		DeleteOrderResponse response = new DeleteOrderResponse();
		try {
			request.requestCheck();
			deleteOrderWithTransaction(request);
			response.setCode(OrderRetCode.SUCCESS.getCode());
			response.setMsg(OrderRetCode.SUCCESS.getMessage());
		} catch (Exception e) {
			log.error("OrderCoreServiceImpl.deleteOrder Occur Exception :" + e);
			ExceptionProcessorUtils.wrapperHandlerException(response, e);
		}
		return response;
	}



	@Override
	public void updateOrder(Integer status, String orderId) {
		Order order = new Order();
		order.setOrderId(orderId);
		order.setStatus(status);
		orderMapper.updateByPrimaryKeySelective(order);
	}


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteOrderWithTransaction(DeleteOrderRequest request){
        orderMapper.deleteByPrimaryKey(request.getOrderId());
        Example example = new Example(Order.class);
        example.createCriteria().andEqualTo("orderId",request.getOrderId());
        orderItemMapper.deleteByExample(example);
        orderShippingMapper.deleteByPrimaryKey(request.getOrderId());
    }
}
