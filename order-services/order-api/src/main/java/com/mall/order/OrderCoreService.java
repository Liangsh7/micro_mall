package com.mall.order;/**
 * Created by mic on 2019/7/30.
 */

import com.mall.order.dto.*;

/**
 *  ciggar
 * create-date: 2019/7/30-上午9:13
 * 订单相关业务
 */
public interface OrderCoreService {

    /**
     * 创建订单
     * @param request
     * @return
     */
    CreateOrderResponse createOrder(CreateOrderRequest request);

    /**
     * 取消订单
     * @param request
     * @return
     */
    CancelOrderResponse cancelOrder(CancelOrderRequest request);


    /**
     * 删除订单
     * @param request
     * @return
     */
    DeleteOrderResponse deleteOrder(DeleteOrderRequest request);

    /**
     * 修改订单状态
     * @param status
     * @param orderId
     */
    void updateOrder(Integer status,String orderId);

    /**
     * 删除订单与交易
     * @param request
     */
    void deleteOrderWithTransaction(DeleteOrderRequest request);


}
