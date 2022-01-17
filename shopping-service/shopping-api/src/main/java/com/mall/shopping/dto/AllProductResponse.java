package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/24-16:29
 */
@Data
public class AllProductResponse extends AbstractResponse {

    private List<ProductDto> productDtoList;

    private Long total;
}
