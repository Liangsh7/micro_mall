package com.mall.promo.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.promo.dal.entitys.PromoItem;
import org.apache.ibatis.annotations.Param;

public interface PromoItemMapper extends TkMapper<PromoItem> {

    Integer decreaseStock(@Param(value = "productId") Long productId, @Param(value = "psId") Long psId);
}