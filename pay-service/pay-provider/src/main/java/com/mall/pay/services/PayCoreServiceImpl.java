package com.mall.pay.services;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.TradeFundBill;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.mall.order.OrderCoreService;
import com.mall.order.constant.OrderConstants;
import com.mall.pay.alipay.config.Configs;
import com.mall.pay.alipay.model.ExtendParams;
import com.mall.pay.alipay.model.GoodsDetail;
import com.mall.pay.alipay.model.builder.AlipayTradePrecreateRequestBuilder;
import com.mall.pay.alipay.model.builder.AlipayTradeQueryRequestBuilder;
import com.mall.pay.alipay.model.result.AlipayF2FPrecreateResult;
import com.mall.pay.alipay.model.result.AlipayF2FQueryResult;
import com.mall.pay.alipay.service.AlipayMonitorService;
import com.mall.pay.alipay.service.AlipayTradeService;
import com.mall.pay.alipay.service.impl.AlipayMonitorServiceImpl;
import com.mall.pay.alipay.service.impl.AlipayTradeServiceImpl;
import com.mall.pay.alipay.service.impl.AlipayTradeWithHBServiceImpl;
import com.mall.pay.alipay.utils.Utils;
import com.mall.pay.alipay.utils.ZxingUtils;
import com.mall.pay.biz.payment.WechatPayment;
import com.mall.pay.biz.payment.constants.PayResultEnum;
import com.mall.pay.biz.payment.constants.PaymentConstants;
import com.mall.pay.biz.payment.context.WechatPaymentContext;
import com.mall.pay.constants.PayReturnCodeEnum;
import com.mall.pay.dal.entitys.Payment;
import com.mall.pay.dal.persistence.PaymentMapper;
import com.mall.pay.dto.alipay.AlipayQueryRetResponse;
import com.mall.pay.dto.alipay.AlipaymentResponse;
import com.mall.pay.dto.wechat.WechatPaymentResopnse;
import com.mall.pay.PayCoreService;
import com.mall.pay.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/30-13:54
 */
@Slf4j
@Component
@Service(cluster = "failover",timeout = 2000)
public class PayCoreServiceImpl implements PayCoreService {

    @Autowired
    private WechatPayment wechatPayment;

//    @Autowired
//    private AliPayment aliPayment;

//    @Autowired
//    private WechatPaymentConfig wechatPaymentConfig;

//    @Autowired
//    private AliPaymentConfig aliPaymentConfig;

    @Autowired
    private PaymentMapper paymentMapper;

    @Reference(check = false)
    private OrderCoreService orderCoreService;

    @Value("${alipay.code.path}")
    private String codePath;

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
    private static AlipayTradeService   tradeWithHBService;

    // 支付宝交易保障接口服务，供测试接口api使用，请先阅读readme.txt
    private static AlipayMonitorService monitorService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();

        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
                .setFormat("json").build();
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse execPay(PaymentRequest request) {

        PaymentResponse paymentResponse=new PaymentResponse();
//        try {
//            paymentResponse=BasePayment.paymentMap.get(request.getPayChannel()).process(request);
//        }catch (Exception e){
//            log.error("PayCoreServiceImpl.execPay Occur Exception :"+e);
//            ExceptionProcessorUtils.wrapperHandlerException(paymentResponse,e);
//        }
        return paymentResponse;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentNotifyResponse paymentResultNotify(PaymentNotifyRequest request) {
        log.info("paymentResultNotify request:{}", JSON.toJSONString(request));
        PaymentNotifyResponse response=new PaymentNotifyResponse();
//        try{
//            response=BasePayment.paymentMap.get
//                    (request.getPayChannel()).completePayment(request);
//
//        }catch (Exception e){
//            log.error("paymentResultNotify occur exception:"+e);
//            ExceptionProcessorUtils.wrapperHandlerException(response,e);
//        }finally {
//            log.info("paymentResultNotify return result:{}",JSON.toJSONString(response));
//        }
        return response;
    }

    /**
     * 微信支付执行支付操作
     * @param request
     * @return
     */
    @Override
    public WechatPaymentResopnse wechatPay(PaymentRequest request) {

        //创建上下文
        WechatPaymentContext context = wechatPayment.createContext(request);
        //准备
        wechatPayment.prepare(context);

        //执行
        WechatPaymentResopnse resopnse = wechatPayment.generalProcess(context);

        //善后
        wechatPayment.afterProcess(request,resopnse,context);

        return resopnse;
    }

    /**
     * 支付宝支付执行支付操作
     * @param request
     * @return
     */
    @Override
    public AlipaymentResponse aliPay(PaymentRequest request) {

        AlipaymentResponse response = new AlipaymentResponse();

        String codePath = getCode(request);

        if (StringUtils.isBlank(codePath)) {
            response.setCode(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getCode());
            response.setCode(PayReturnCodeEnum.PAYMENT_PROCESSOR_FAILED.getMsg());
            return response;
        }
        //新增支付记录
        afterProcess(request);

        response.setCode(PayReturnCodeEnum.SUCCESS.getCode());
        response.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());
        response.setQrCode(codePath);
        return response;

    }

    private void afterProcess(PaymentRequest request) {

        //插入支付记录表
        Payment payment = new Payment();
        payment.setCreateTime(new Date());
        //订单号
        payment.setOrderId(request.getTradeNo());
        payment.setCreateTime(new Date());
        BigDecimal amount =request.getOrderFee();
        payment.setOrderAmount(amount);
        payment.setPayerAmount(amount);
        payment.setPayerUid(request.getUserId());
        payment.setPayerName("ciggar");//TODO
        payment.setPayWay(request.getPayChannel());
        payment.setProductName(request.getSubject());
        payment.setStatus(PayResultEnum.TRADE_PROCESSING.getCode());//
        payment.setRemark("支付宝支付");
        payment.setUpdateTime(new Date());
        paymentMapper.insert(payment);
    }

    public String getCode(PaymentRequest request) {
        String filePath = null;
        String fileName = "qr-%s.png";
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = request.getTradeNo();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = request.getSubject();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = String.valueOf(request.getTotalFee().doubleValue());
        System.err.println(totalAmount);

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买商品3件共20.00元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "ciggar_cskaoyan";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "ciggar_cskaoyan";

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                //                .setNotifyUrl("http://www.test-notify-url.com")//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                filePath = String.format(codePath, response.getOutTradeNo());
                fileName = String.format(fileName,response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return fileName;
    }
    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }


    @Override
    public AlipayQueryRetResponse queryAlipayRet(PaymentRequest request) {
        AlipayQueryRetResponse retResponse = new  AlipayQueryRetResponse();
        if (StringUtils.isBlank(request.getTradeNo())) {
            retResponse.setCode(PayReturnCodeEnum.REQUISITE_PARAMETER_NOT_EXIST.getCode());
            retResponse.setMsg(PayReturnCodeEnum.REQUISITE_PARAMETER_NOT_EXIST.getMsg());
            return retResponse;
        }
        Boolean ret = tradeQuery(request.getTradeNo());
        if (ret) {
            retResponse.setCode(PayReturnCodeEnum.SUCCESS.getCode());
            retResponse.setMsg(PayReturnCodeEnum.SUCCESS.getMsg());
            return retResponse;
        }else {
            retResponse.setCode(PayReturnCodeEnum.ORDER_HAD_NOT_PAY.getCode());
            retResponse.setMsg(PayReturnCodeEnum.ORDER_HAD_NOT_PAY.getMsg());
            return retResponse;
        }
    }

    public Boolean tradeQuery(String orderId) {
        // (必填) 商户订单号，通过此商户订单号查询当面付的交易状态
        String outTradeNo = orderId;
        Boolean flag = false;

        // 创建查询请求builder，设置请求参数
        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(outTradeNo);

        AlipayF2FQueryResult result = tradeService.queryTradeResult(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                //修改支付状态未成功

                //修改订单状态未已支付
                flag = true;
                updateOrderStatusAndPayMentStatus(flag,outTradeNo);

                log.info("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse response = result.getResponse();
                dumpResponse(response);

                log.info(response.getTradeStatus());
                if (Utils.isListNotEmpty(response.getFundBillList())) {
                    for (TradeFundBill bill : response.getFundBillList()) {
                        log.info(bill.getFundChannel() + ":" + bill.getAmount());
                    }
                }
                break;

            case FAILED:
                // 修改支付状态为订单支付失败

                //修改订单状态为订单支付失败
                updateOrderStatusAndPayMentStatus(flag,outTradeNo);

                log.error("查询返回该订单支付失败或被关闭!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单支付状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return flag;
    }

    /**
     * 修改订单状态
     * 修改支付状态
     * @param flag
     */
    private void updateOrderStatusAndPayMentStatus(Boolean flag,String orderId) {

        Example example = new Example(Payment.class);
        example.createCriteria().andEqualTo("orderId",orderId);
        Payment payment = new Payment();
        payment.setUpdateTime(new Date());
        if (flag) {
            payment.setStatus(PaymentConstants.PayStatusEnum.PAY_SUCCESS.getStatus().toString());
            payment.setPaySuccessTime(new Date());
            payment.setCompleteTime(new Date());
            orderCoreService.updateOrder(OrderConstants.ORDER_STATUS_PAYED,orderId);
        }else {
            payment.setStatus(PaymentConstants.PayStatusEnum.PAY_FAILED.getStatus().toString());
            payment.setCompleteTime(new Date());
            orderCoreService.updateOrder(OrderConstants.ORDER_STATUS_TRANSACTION_FAILED,orderId);
        }
        paymentMapper.updateByExampleSelective(payment,example);

    }
}
