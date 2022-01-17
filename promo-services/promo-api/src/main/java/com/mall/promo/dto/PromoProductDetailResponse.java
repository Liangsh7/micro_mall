package com.mall.promo.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoProductDetailResponse extends AbstractResponse {

    private static final long serialVersionUID = -5147108571773101261L;

    private PromoProductDetailDTO promoProductDetailDTO;

}