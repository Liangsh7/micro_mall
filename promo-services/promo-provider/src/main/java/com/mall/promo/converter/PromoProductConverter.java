package com.mall.promo.converter;

import com.mall.promo.dal.entitys.PromoItem;
import com.mall.promo.dto.PromoProductDetailDTO;
import com.mall.shopping.dto.ProductDetailDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Component
public class PromoProductConverter {

    public PromoProductDetailDTO convert2DetailDTO(PromoItem promoItem, ProductDetailDto productDetailDto) {

        PromoProductDetailDTO promoProductDetailDTO = new PromoProductDetailDTO();
        BeanUtils.copyProperties(productDetailDto,promoProductDetailDTO);

        promoProductDetailDTO.setPromoPrice(promoItem.getSeckillPrice());
        return promoProductDetailDTO;
    }
}