package com.mall.pay;

import com.mall.pay.dto.*;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;

/**
 *  ciggar
 * create-date: 2019/7/30-13:46
 * 支付操作相关的服务
 */
public interface PayCoreService {


    /**
     * 执行支付操作
     * @param request
     * @return
     */
    PaymentResponse execPay(PaymentRequest request);


    /**
     * 支付、退款结果通知处理(等待微信或者支付宝异步通知支付结果）
     * @param request
     * @return
     */
    PaymentNotifyResponse paymentResultNotify(PaymentNotifyRequest request);




    /**
     * 微信支付执行支付操作
     * @param request
     * @return
     */
    WechatPaymentResopnse wechatPay(PaymentRequest request);

    /**
     * 支付宝支付执行支付操作
     * @param request
     * @return
     */
    AlipaymentResponse aliPay(PaymentRequest request);

    /**
     * 获取支付宝支付结果
     * @param request
     * @return
     */
    AlipayQueryRetResponse queryAlipayRet(PaymentRequest request);


}
