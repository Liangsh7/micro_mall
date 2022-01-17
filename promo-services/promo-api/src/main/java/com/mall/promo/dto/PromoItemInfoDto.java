package com.mall.promo.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoItemInfoDto implements Serializable {
    private static final long serialVersionUID = -5095742524491498034L;

    /**
     *  **"id"**:**100057401**,
     *         **"inventory"**:**10**,
     *         **"price"**:**149**,
     *         **"seckillPrice"**:**49**,
     *         **"picUrl"**:**"https://resource.smartisan.com/resource/005c65324724692f7c9ba2fc7738db13.png"**,
     *         **"productName"**:**"Smartisan T恤 迪特拉姆斯"**
     */

    //商品id
    private Long id;

    //秒杀库存
    private Integer inventory;

    //原价
    private BigDecimal price;

    //秒杀价格
    private BigDecimal seckillPrice;

    //商品图片
    private String picUrl;

    //商品名称
    private String productName;

}