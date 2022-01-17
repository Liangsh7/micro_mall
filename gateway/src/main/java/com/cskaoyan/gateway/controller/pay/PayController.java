package com.cskaoyan.gateway.controller.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.gateway.form.pay.PayForm;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.pay.constants.PayChannelEnum;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;
import com.mall.user.intercepter.TokenIntercepter;
import com.mall.pay.PayCoreService;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * create by ciggar on 2020/04/05
 */
@Slf4j
@RestController
@RequestMapping("/cashier")
public class PayController {

    @Reference(timeout = 3000,retries = 0,check = false)
    PayCoreService payCoreService;

    @PostMapping("/pay")
    public ResponseData pay(@RequestBody PayForm payForm, HttpServletRequest httpServletRequest){
        log.info("支付表单数据:{}",payForm);
        PaymentRequest request=new PaymentRequest();
        String userInfo=(String)httpServletRequest.getAttribute(TokenIntercepter.USER_INFO_KEY);
        JSONObject object= JSON.parseObject(userInfo);
        Long uid=Long.parseLong(object.get("uid").toString());
        request.setUserId(uid);
        BigDecimal money=payForm.getMoney();
        request.setOrderFee(money);
        request.setPayChannel(payForm.getPayType());
        request.setSubject("csmall_order");
        request.setSpbillCreateIp("115.195.125.164");
        request.setTradeNo(payForm.getOrderId());
        request.setTotalFee(money);
        //微信支付的调用微信支付的接口
        if (payForm.getPayType().equals(PayChannelEnum.WECHAT_PAY.getCode())) {
            WechatPaymentResopnse reponse = payCoreService.wechatPay(request);
            if(reponse.getCode().equals(PayReturnCodeEnum.SUCCESS.getCode())){
                return new ResponseUtil<>().setData(reponse.getCodeUrl());
            }else {
                return new ResponseUtil<>().setErrorMsg(reponse.getMsg());
            }
        //支付宝支付的调用支付宝支付的接口

        }else if (payForm.getPayType().equals(PayChannelEnum.ALI_PAY.getCode())){
            AlipaymentResponse response = payCoreService.aliPay(request);
            if(response.getCode().equals(PayReturnCodeEnum.SUCCESS.getCode())){
                String qrCode = response.getQrCode();
                String codeUrl = "http://115.29.141.32:8080/image/" + qrCode;
//                String codeUrl = "http://localhost:8080/image/" + qrCode;
                return new ResponseUtil<>().setData(codeUrl);
            }
            else {
                return new ResponseUtil<>().setErrorMsg(response.getMsg());
            }
        }
        return new ResponseUtil<>().setErrorMsg("PayType:[" + payForm.getPayType() + "]暂时不支持");

    }

    /**
     * 查询支付宝支付状态
     * @param orderId
     * @return
     */
    @GetMapping("/queryStatus")
    public ResponseData queryAlipayStatus(@RequestParam String orderId){

        if (StringUtils.isBlank(orderId)) {
            return new ResponseUtil<>().setErrorMsg("传入的参数不能为空");
        }
        PaymentRequest request = new PaymentRequest();
        request.setTradeNo(orderId);
        AlipayQueryRetResponse response = payCoreService.queryAlipayRet(request);
        if(response.getCode().equals(PayReturnCodeEnum.SUCCESS.getCode())){
            return new ResponseUtil<>().setData(null);
        }
        else {
            return new ResponseUtil<>().setErrorMsg(response.getMsg());
        }

    }



}
