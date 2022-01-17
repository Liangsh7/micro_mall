package com.mall.promo.dal.entitys;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Table;
import java.util.Date;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Table(name = "tb_promo_session")
@Data
@ToString
public class PromoSession {

    private Long id;

    // 场次id 1:上午十点场 2：下午四点场
    private Integer sessionId;

    private Date startTime;

    private Date endTime;

    // 场次日期
    private String yyyymmdd;
}