package com.mall.order.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.order.dal.entitys.Order;

public interface OrderMapper extends TkMapper<Order> {
    Long countAll();
}