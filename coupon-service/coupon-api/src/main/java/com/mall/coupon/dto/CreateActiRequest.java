package com.mall.coupon.dto;

import com.mall.commons.result.AbstractRequest;
import com.mall.commons.tool.exception.ValidateException;
import com.mall.coupon.SaleTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by ciggar on 2019/8/19
 * 23:37.
 */
@Data
public class CreateActiRequest extends AbstractRequest {
    private String name;
    private SaleTypeEnum saleType;
    private String desc;

    @Override
    public void requestCheck() {
        if (StringUtils.isEmpty(name)) {
            throw new ValidateException("缺少name参数");
        }
        if (saleType == null) {
            throw new ValidateException("缺少type参数");
        }
    }
}
