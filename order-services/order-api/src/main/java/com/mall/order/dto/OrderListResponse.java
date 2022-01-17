package com.mall.order.dto;

import com.mall.commons.result.AbstractResponse;
import lombok.Data;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/7/30-上午10:02
 */
@Data
public class OrderListResponse extends AbstractResponse{

    private List<OrderDetailInfo> detailInfoList;

    /**
     * 总记录数
     */
    private Long total;

}
