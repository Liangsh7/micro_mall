package com.mall.shopping.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  ciggar
 * create-date: 2019/7/23-19:01
 *
 * 这个就是购物车对应的商品的bean
 *
 * 一个购物车就应该是一个这样的List集合
 * key = cart_userId
 * value = List<CartProductDto>
 *
 *     用最简单的String  String jsonStr = JSON.toJSONString(List<CartProductDto>)
 *
 *     List<CartProductDto>  list= JSON.paresObject(jsonStr,List.class);
 *
 *     Jedis / redisTemplate / Redisson 这个只是客户端
 */
@Data
public class CartProductDto implements Serializable {
    private static final long serialVersionUID = -809047960626248847L;

    private Long productId;

    private BigDecimal salePrice;

    private Long productNum;

    private Long limitNum;

    private String checked;

    private String productName;

    private String productImg;
}
