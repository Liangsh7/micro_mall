package com.mall.promo.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Data
public class PromoInfoResponse extends AbstractResponse {

    private Integer sessionId;

    private Long psId;

    private List<PromoItemInfoDto> productList;

}