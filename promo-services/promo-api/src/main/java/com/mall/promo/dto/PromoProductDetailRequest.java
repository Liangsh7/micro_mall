package com.mall.promo.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.promo.constant.PromoRetCode;
import lombok.Data;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoProductDetailRequest extends AbstractRequest {

    private static final long serialVersionUID = -7785245314328622037L;
    private Long psId;

    private Long productId;

    @Override
    public void requestCheck() {

        if (psId == null || productId == null) {
            throw new ValidateException(PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }

    }
}