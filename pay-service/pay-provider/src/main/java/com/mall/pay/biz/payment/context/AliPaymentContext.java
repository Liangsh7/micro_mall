package com.mall.pay.biz.payment.context;


import com.mall.pay.biz.abs.PaymentContext;

import java.util.SortedMap;


public class AliPaymentContext extends PaymentContext {
    /** 商品名称（必填）*/
    private String subject;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
