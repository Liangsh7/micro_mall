package com.cskaoyan.gateway.form.pay;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * create-date: 2019/8/9-下午4:24
 */
@Data
public class PayForm {

    private String nickName;
    private BigDecimal money;
    private String info;
    private String orderId;
    private String payType;
}
