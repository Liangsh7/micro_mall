package com.mall.order.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  ciggar
 * create-date: 2019/7/31-上午9:53
 */
@Data
public class OrderShippingDto implements Serializable{

    private String orderId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverState;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;
}
