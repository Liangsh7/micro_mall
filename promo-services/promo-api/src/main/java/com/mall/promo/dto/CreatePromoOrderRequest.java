package com.mall.promo.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.promo.constant.PromoRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description  秒杀下单接口请求参数
 **/
@Data
public class CreatePromoOrderRequest extends AbstractRequest {

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 秒杀场次id
     */
    private Long psId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 补充 商品秒杀价格
     */
    private BigDecimal promoPrice;

    /**
     * 地址信息
     */
    private Long addressId;

    /**
     * 电话信息
     */
    private String tel;

    /**
     * 街道信息
     */
    private String streetName;


    @Override
    public void requestCheck() {

        if (productId == null || userId == null || StringUtils.isBlank(username) || psId == null) {
            throw new ValidateException(PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(), PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }

    }
}