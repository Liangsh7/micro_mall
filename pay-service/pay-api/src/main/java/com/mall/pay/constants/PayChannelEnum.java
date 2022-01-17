package com.mall.pay.constants;


import com.mall.pay.IEnum;


public enum PayChannelEnum implements IEnum{

    ALI_PAY("alipay","支付宝支付渠道"),
    WECHAT_PAY("wechat_pay","微信支付渠道");


    private String code;

    private String desc;

    PayChannelEnum(String code, String desc){
        this.code=code;
        this.desc=desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
