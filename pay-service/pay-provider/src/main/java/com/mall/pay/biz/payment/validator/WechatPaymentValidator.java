package com.mall.pay.biz.payment.validator;

import com.mall.commons.result.AbstractRequest;
import com.mall.order.OrderQueryService;
import com.mall.pay.biz.abs.BaseValidator;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Service;


@Service("wechatPaymentValidator")
public class WechatPaymentValidator extends BaseValidator {
     @Reference(timeout=3000,check = false)
     OrderQueryService orderQueryService;


    @Override
    public void specialValidate(AbstractRequest request) {
        commonValidate(request,orderQueryService);
    }
}
