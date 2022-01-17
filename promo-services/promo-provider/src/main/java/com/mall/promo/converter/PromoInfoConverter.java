package com.mall.promo.converter;

import com.mall.promo.dto.PromoItemInfoDto;
import com.mall.shopping.dto.ProductDetailDto;
import org.springframework.stereotype.Component;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Component
public class PromoInfoConverter {

    public PromoItemInfoDto convert2InfoDto(ProductDetailDto productDetailDto) {
        PromoItemInfoDto promoItemInfoDto = new PromoItemInfoDto();
        promoItemInfoDto.setProductName(productDetailDto.getProductName());
        promoItemInfoDto.setPrice(productDetailDto.getSalePrice());
        promoItemInfoDto.setPicUrl(productDetailDto.getProductImageBig());
        promoItemInfoDto.setId(productDetailDto.getProductId());
        return promoItemInfoDto;
    }
}