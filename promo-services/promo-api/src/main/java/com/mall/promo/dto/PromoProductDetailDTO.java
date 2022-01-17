package com.mall.promo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoProductDetailDTO implements Serializable {

    private static final long serialVersionUID = -228392123369014230L;
    private Long productId;

    private BigDecimal salePrice;

    private String productName;

    private String subTitle;

    private Long limitNum;

    private String productImageBig;

    private String detail;

    private List<String> productImageSmall;

    private BigDecimal promoPrice;
}