package com.mall.promo.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.promo.constant.PromoRetCode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoInfoRequest extends AbstractRequest {

    private Integer sessionId;

    private String yyyymmdd;

    @Override
    public void requestCheck() {

        if (sessionId == null || StringUtils.isBlank(yyyymmdd)) {
            throw new ValidateException(PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getCode(),PromoRetCode.REQUISITE_PARAMETER_NOT_EXIST.getMessage());
        }

    }
}