package com.mall.coupon.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by ciggar on 2019/8/20
 * 0:00.
 */
@Data
public class OrderItemDto implements Serializable {
    private String orderId;
    private BigDecimal price;
    private Integer num;
}
