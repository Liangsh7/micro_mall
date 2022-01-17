package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/24-16:27
 */

@Data
public class ProductDetailResponse extends AbstractResponse {
    private ProductDetailDto productDetailDto;
}
