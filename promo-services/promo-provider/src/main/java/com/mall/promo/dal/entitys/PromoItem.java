package com.mall.promo.dal.entitys;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
@ToString
@Table(name = "tb_promo_item")
public class PromoItem {


    // 主键id
    private Integer id;

    /**
     * 秒杀场次主键id
     * @see com.mall.promo.dal.entitys.PromoSession
     */

    private Long  psId;

    // 商品id
    private Long itemId;

    //商品秒杀价格
    private BigDecimal seckillPrice;

    //商品秒杀库存
    private Integer itemStock;


}