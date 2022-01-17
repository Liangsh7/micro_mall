package com.mall.shopping.dal.persistence;

import com.mall.commons.tool.tkmapper.TkMapper;
import com.mall.shopping.dal.entitys.Item;

import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ItemMapper extends TkMapper<Item> {

    List<Item> selectItemFront(@Param("cid") Long cid,
                                 @Param("orderCol") String orderCol,@Param("orderDir") String orderDir,
                                 @Param("priceGt") Integer priceGt,@Param("priceLte") Integer priceLte);
}