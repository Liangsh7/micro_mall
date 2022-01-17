package com.mall.pay.biz.payment;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.mall.commons.result.AbstractRequest;
import com.mall.commons.result.AbstractResponse;
import com.mall.commons.tool.exception.BizException;
import com.mall.commons.tool.utils.UtilDate;
import com.mall.order.OrderCoreService;
import com.mall.pay.biz.abs.Context;
import com.mall.pay.biz.abs.Validator;
import com.mall.pay.biz.payment.channel.wechatpay.WeChatBuildRequest;
import com.mall.pay.biz.payment.commons.HttpClientUtil;
import com.mall.pay.biz.payment.constants.PayResultEnum;
import com.mall.pay.dal.entitys.Payment;
import com.mall.pay.dal.persistence.PaymentMapper;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;
import com.mall.pay.utils.GlobalIdGeneratorUtil;
import com.mall.pay.biz.payment.constants.PaymentConstants;
import com.mall.pay.biz.payment.constants.WechatPaymentConfig;
import com.mall.pay.biz.payment.context.WechatPaymentContext;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dto.PaymentNotifyRequest;
import com.mall.pay.dto.PaymentNotifyResponse;
import com.mall.pay.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Service("wechatPayment")
public class WechatPayment {

	@Autowired
	private WechatPaymentConfig wechatPaymentConfig;

	@Resource(name = "wechatPaymentValidator")
	private Validator validator;

	@Autowired
	private PaymentMapper paymentMapper;

	@Autowired
	GlobalIdGeneratorUtil globalIdGeneratorUtil;

	private final String COMMENT_GLOBAL_ID_CACHE_KEY = "COMMENT_ID";

	@Reference(timeout = 3000,check = false)
	OrderCoreService orderCoreService;

	public Validator getValidator() {
		return validator;
	}

	public WechatPaymentContext createContext(AbstractRequest request) {
		WechatPaymentContext wechatPaymentContext = new WechatPaymentContext();
		PaymentRequest paymentRequest = (PaymentRequest) request;
		wechatPaymentContext.setOutTradeNo(paymentRequest.getTradeNo());
		wechatPaymentContext.setProductId(paymentRequest.getTradeNo());
		wechatPaymentContext.setSpbillCreateIp(paymentRequest.getSpbillCreateIp());
		wechatPaymentContext.setTradeType(PaymentConstants.TradeTypeEnum.NATIVE.getType());
		wechatPaymentContext.setTotalFee(paymentRequest.getTotalFee());
		wechatPaymentContext.setBody(paymentRequest.getSubject());
		return wechatPaymentContext;
	}

	public void prepare(WechatPaymentContext context) throws BizException {

		SortedMap<String, Object> sParaTemp = new TreeMap<String, Object>();
		context.setsParaTemp(sParaTemp);

		SortedMap paraMap = context.getsParaTemp();

		paraMap.put("body", context.getBody());
		paraMap.put("out_trade_no", context.getOutTradeNo());
		//单位分
		paraMap.put("total_fee", context.getTotalFee().multiply(new BigDecimal("100")).intValue());
		paraMap.put("spbill_create_ip", context.getSpbillCreateIp());
		paraMap.put("appid", wechatPaymentConfig.getWechatAppid());
		paraMap.put("mch_id", wechatPaymentConfig.getWechatMch_id());
		paraMap.put("nonce_str", WeChatBuildRequest.getNonceStr());
		paraMap.put("trade_type", context.getTradeType());
		paraMap.put("product_id", context.getProductId());
		// 此路径是微信服务器调用支付结果通知路径
		paraMap.put("device_info", "WEB");
		paraMap.put("notify_url", wechatPaymentConfig.getWechatNotifyurl());
		//二维码的失效时间（5分钟）
		paraMap.put("time_expire", UtilDate.getExpireTime(30 * 60 * 1000L));
		String sign = WeChatBuildRequest.createSign(paraMap, wechatPaymentConfig.getWechatMchsecret());
		paraMap.put("sign", sign);
		log.info("微信生成sign:{}", JSON.toJSONString(paraMap));
		String xml = WeChatBuildRequest.getRequestXml(paraMap);
		context.setXml(xml);
	}

	public WechatPaymentResopnse generalProcess(WechatPaymentContext context) throws BizException {
		WechatPaymentResopnse response = new WechatPaymentResopnse();

		log.info("微信支付组装的请求参数:{}", context.getXml());
		String xml = HttpClientUtil.httpPost(wechatPaymentConfig.getWechatUnifiedOrder(), context.getXml());
		log.info("微信支付同步返回的结果:{}", xml);
		Map<String, String> resultMap = WeChatBuildRequest.doXMLParse(xml);
		if ("SUCCESS".equals(resultMap.get("return_code"))) {
			if ("SUCCESS".equals(resultMap.get("result_code"))) {
				response.setPrepayId(resultMap.get("prepay_id"));
				response.setCodeUrl(resultMap.get("code_url"));
				response.setCode(PayReturnCodeEnum.SUCCESS.getCode());
				response.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());
			} else {
				String errMsg = resultMap.get("err_code") + ":" + resultMap.get("err_code_des");
				response.setCode(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getCode());
				response.setMsg(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getMsg(errMsg));
			}
		} else {
			response.setCode(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getCode());
			response.setMsg(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getMsg(resultMap.get("return_msg")));
		}
		return response;
	}

	public void afterProcess(AbstractRequest request, WechatPaymentResopnse respond, Context context) throws BizException {
		//插入支付表
		log.info("WechatPayment begin - afterProcess -request:" + request + "\n response:" + respond);
		PaymentRequest paymentRequest = (PaymentRequest) request;
		//插入支付记录表
		Payment payment = new Payment();
		payment.setCreateTime(new Date());
		BigDecimal amount = paymentRequest.getOrderFee();
		payment.setOrderAmount(amount);
		payment.setOrderId(paymentRequest.getTradeNo());
		payment.setPayerAmount(amount);
		payment.setPayerUid(paymentRequest.getUserId());
		payment.setPayerName("ciggar");//TODO
		payment.setPayWay(paymentRequest.getPayChannel());
		payment.setProductName(paymentRequest.getSubject());
		payment.setStatus(PayResultEnum.TRADE_PROCESSING.getCode());//
		payment.setRemark("微信支付");
		payment.setPayNo(respond.getPrepayId());//第三方的交易id
		payment.setUpdateTime(new Date());
		paymentMapper.insert(payment);
	}





	public AbstractResponse completePayment(PaymentNotifyRequest request) throws BizException {
		request.requestCheck();
		PaymentNotifyResponse response = new PaymentNotifyResponse();
		Map xmlMap = new HashMap();
		String xml = request.getXml();
		try {
			xmlMap = WXPayUtil.xmlToMap(xml);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SortedMap<Object, Object> paraMap = new TreeMap<>();
		xmlMap.forEach(paraMap::put);
		//组装返回的结果的签名字符串
		String rsSign = paraMap.remove("sign").toString();
		String sign = WeChatBuildRequest.createSign(paraMap, wechatPaymentConfig.getWechatMchsecret());
		//验证签名
		if (rsSign.equals(sign)) {
			//SUCCESS、FAIL
			String resultCode = paraMap.get("return_code").toString();
			if ("SUCCESS".equals(resultCode)) {
				if ("SUCCESS".equals(paraMap.get("result_code"))) {
					//更新支付表
					Payment payment = new Payment();
					payment.setStatus(PayResultEnum.TRADE_SUCCESS.getCode());
					payment.setPaySuccessTime((UtilDate.parseStrToDate(UtilDate.simple,paraMap.get("time_end").toString(),new Date())));
					Example example = new Example(Payment.class);
					example.createCriteria().andEqualTo("orderId", paraMap.get("out_trade_no"));
					paymentMapper.updateByExampleSelective(payment, example);
					//更新订单表状态
					orderCoreService.updateOrder(1, paraMap.get("out_trade_no").toString());
					response.setResult(WeChatBuildRequest.setXML("SUCCESS", "OK"));
				}
			}
		} else {
			throw new BizException("微信返回结果签名验证失败");
		}
		return response;
	}
}
