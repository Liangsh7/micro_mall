package com.mall.order.converter;

import com.mall.order.dal.entitys.Order;
import com.mall.order.dal.entitys.OrderItem;
import com.mall.order.dal.entitys.OrderShipping;
import com.mall.order.dto.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/31-上午9:57
 */
@Mapper(componentModel = "spring")
public interface OrderConverter {

    @Mappings({})
    OrderDetailResponse order2res(Order order);

    @Mappings({})
    OrderDetailInfo order2detail(Order order);

    @Mappings({})
    OrderItemDto item2dto(OrderItem item);

    @Mappings({})
    OrderShippingDto shipping2dto(OrderShipping shipping);


    List<OrderItemDto> item2dto(List<OrderItem> items);

    @Mappings({})
    OrderItemResponse item2res(OrderItem orderItem);

    @Mappings({})
    OrderDto order2dto(Order order);
}
