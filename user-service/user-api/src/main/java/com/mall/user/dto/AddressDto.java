package com.mall.user.dto;

import lombok.Data;

import java.io.Serializable;

/**
 *  ciggar
 * create-date: 2019/7/31-19:34
 */
@Data
public class AddressDto implements Serializable {

    private Long addressId;

    private Long userId;

    private String userName;

    private String tel;

    private String streetName;

    private Integer isDefault;
}
