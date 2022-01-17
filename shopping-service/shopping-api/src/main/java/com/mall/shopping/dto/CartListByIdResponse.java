package com.mall.shopping.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/23-18:59
 */
@Data
public class CartListByIdResponse extends AbstractResponse {

    private List<CartProductDto> cartProductDtos;
}
