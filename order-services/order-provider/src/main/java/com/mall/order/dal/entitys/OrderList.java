package com.mall.order.dal.entitys;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *  ciggar
 * create-date: 2019/8/12-13:53
 */
@Data
public class OrderList {

    private String orderId;
    private Integer status;
    private Date createTime;
    private BigDecimal payment;
    private String itemId;
    private Integer num;
    private String title;
    private BigDecimal price;
    private BigDecimal totalFee;
    private String picPath;
}
