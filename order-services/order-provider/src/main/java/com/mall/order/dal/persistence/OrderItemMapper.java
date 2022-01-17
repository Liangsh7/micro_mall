package com.mall.order.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.order.dal.entitys.OrderItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderItemMapper extends TkMapper<OrderItem> {
    List<OrderItem> queryByOrderId(@Param("orderId") String orderId);
    void updateStockStatus(@Param("status") Integer status,@Param("orderId") String orderId);
}