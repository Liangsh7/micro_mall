package com.mall.promo;

import com.mall.promo.dto.*;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
public interface PromoService {
     /**
      * 获取秒杀列表接口
      * @param request
      * @return
      */
     PromoInfoResponse getPromoList(PromoInfoRequest request);

     /**
      * 秒杀下单接口
      * @param request
      * @return
      */
     CreatePromoOrderResponse createPromoOrder(CreatePromoOrderRequest request);

     /**
      * 使用分布式事务控制的秒杀下单接口
      * @param request
      * @return
      */
     CreatePromoOrderResponse createPromoOrderInTransaction(CreatePromoOrderRequest request);

     /**
      * 获取秒杀商品详情
      * @param request
      * @return
      */
    PromoProductDetailResponse getPromoProductProduct(PromoProductDetailRequest request);
}