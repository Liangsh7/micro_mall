package com.mall.pay.biz.abs;


import com.mall.commons.result.AbstractRequest;

/**
 * 数据验证接口类
 * @author
 */
public interface Validator {
    /**
     * 数据验证
     * @param request
     */
    void validate(AbstractRequest request);
}
