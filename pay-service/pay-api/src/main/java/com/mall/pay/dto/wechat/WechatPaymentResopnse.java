package com.mall.pay.dto.wechat;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class WechatPaymentResopnse extends AbstractResponse {

    /**微信支付下单的返回id*/
    private String prepayId;
    /**微信支付下单构建的二维码地址*/
    private String codeUrl;

}