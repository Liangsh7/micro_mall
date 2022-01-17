package com.cskaoyan.gateway.form.promo;

import lombok.Data;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class CreatePromoOrderInfo {

    private Long psId;

    private Long productId;

    private Long addressId;

    private String tel;

    private String streetName;
}