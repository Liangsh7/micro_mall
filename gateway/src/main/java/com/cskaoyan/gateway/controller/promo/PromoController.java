package com.cskaoyan.gateway.controller.promo;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cskaoyan.gateway.config.CacheManager;
import com.cskaoyan.gateway.form.promo.CreatePromoOrderInfo;
import com.google.common.util.concurrent.RateLimiter;
import com.mall.commons.result.ResponseData;
import com.mall.commons.result.ResponseUtil;
import com.mall.promo.PromoService;
import com.mall.promo.dto.*;
import com.mall.user.annotation.Anoymous;
import com.mall.user.constants.SysRetCodeConstants;
import com.mall.user.intercepter.TokenIntercepter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@RestController
@RequestMapping("/shopping")
public class PromoController {

    @Reference(check = false)
    private PromoService promoService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private CacheManager cacheManager;

    private RateLimiter rateLimiter;

    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        rateLimiter = RateLimiter.create(100);
        //
        //        //带有定时任务的线程池
        ////        executorService = Executors.newScheduledThreadPool();
        //
        //        //新建一个单线程的线程池
        ////        executorService = Executors.newSingleThreadExecutor();
        //
        //        //新建一个缓存类型的线程池
        ////        executorService = Executors.newCachedThreadPool();
        //
        //        //新建一个固定大小的线程池
        //        // LinkedBlockingQueue 无界的阻塞队列
        executorService = Executors.newFixedThreadPool(100);



    }


    @GetMapping("/seckilllist")
    @Anoymous
    public ResponseData getPromoList(@RequestParam Integer sessionId) {

        PromoInfoRequest promoInfoRequest = new PromoInfoRequest();

        promoInfoRequest.setSessionId(sessionId);
        String yyyyMMdd = DateFormatUtils.format(new Date(), "yyyyMMdd");
        promoInfoRequest.setYyyymmdd(yyyyMMdd);
        PromoInfoResponse promoInfoResponse = promoService.getPromoList(promoInfoRequest);
        if (!promoInfoResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setErrorMsg(promoInfoResponse.getMsg());
        }
        return new ResponseUtil<>().setData(promoInfoResponse);

    }

    @PostMapping("/seckill")
    public ResponseData seckill(HttpServletRequest request, @RequestBody CreatePromoOrderInfo createPromoOrderInfo) throws ExecutionException, InterruptedException {

        // 获取一个令牌 返回值是什么呢？ 返回值可以理解为等待时间
        /**
         *      public double acquire(int permits) {
         *         // 需要等待的时间
         *         long microsToWait = this.reserve(permits);
         *
         *         // 让线程去等待
         *         this.stopwatch.sleepMicrosUninterruptibly(microsToWait);
         *
         *         // 100/1000 = 0.1 (0.1 秒)
         *         return 1.0D * (double)microsToWait / (double)TimeUnit.SECONDS.toMicros(1L);
         *     }
         */
        rateLimiter.acquire();


        String userInfo = (String) request.getAttribute(TokenIntercepter.USER_INFO_KEY);
        JSONObject jsonObject = JSON.parseObject(userInfo);
        String username = (String) jsonObject.get("username");
        Integer uid = (Integer)jsonObject.get("uid");
        CreatePromoOrderRequest createPromoOrderRequest = new CreatePromoOrderRequest();
        createPromoOrderRequest.setProductId(createPromoOrderInfo.getProductId());
        createPromoOrderRequest.setPsId(createPromoOrderInfo.getPsId());
        createPromoOrderRequest.setUserId(uid.longValue());
        createPromoOrderRequest.setUsername(username);

        //增加地址信息
        createPromoOrderRequest.setAddressId(createPromoOrderInfo.getAddressId());
        createPromoOrderRequest.setStreetName(createPromoOrderInfo.getStreetName());
        createPromoOrderRequest.setTel(createPromoOrderInfo.getTel());

        /**
         * 可以去Redis里面看一下有没有库存售罄的标记
         *
         * 如果有 表示库存已经售罄
         *
         * 如果没有，表示我们的库存还有剩余
         */
        String key = "promo_item_stock_not_enough_" + createPromoOrderInfo.getPsId() +"_" + createPromoOrderInfo.getProductId();
        String cache = cacheManager.checkCache(key);
        if (! StringUtils.isBlank(cache)) {
            return new ResponseUtil<>().setErrorMsg("库存已经售罄");
        }

        // 通过线程池去限制派发给下游的秒杀服务的流量
        // 线程池的作用主要是为了去保护下游的系统
        Future<CreatePromoOrderResponse> future = executorService.submit(new Callable<CreatePromoOrderResponse>() {
            @Override
            public CreatePromoOrderResponse call() {
                CreatePromoOrderResponse promoOrderResponse = promoService.createPromoOrderInTransaction(createPromoOrderRequest);
                return promoOrderResponse;
            }
        });

        CreatePromoOrderResponse promoOrderResponse = future.get();

//        CreatePromoOrderResponse promoOrderResponse = promoService.createPromoOrderInTransaction(createPromoOrderRequest);

        if (!promoOrderResponse.getCode().equals(SysRetCodeConstants.SUCCESS.getCode())) {
            return new ResponseUtil<>().setErrorMsg(promoOrderResponse.getMsg());
        }

        return new ResponseUtil<>().setData(promoOrderResponse);

    }

    @PostMapping("/promoProductDetail")
    public ResponseData getPromoProductsDetails(@RequestBody PromoProductDetailRequest request) {

        PromoProductDetailResponse response = promoService.getPromoProductProduct(request);
        if (!response.getCode().equals(SysRetCodeConstants.SUCCESS.getCode()) ) {
            return new ResponseUtil<>().setErrorMsg(response.getMsg());
        }
        return new ResponseUtil<>().setData(response);

    }



}