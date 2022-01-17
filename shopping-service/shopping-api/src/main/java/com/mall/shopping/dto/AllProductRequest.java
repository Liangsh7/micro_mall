package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 *  ciggar
 * create-date: 2019/7/24-16:29
 */
@Data
public class AllProductRequest extends AbstractRequest {

    private Integer page;
    private Integer size;
    private String sort;
    private Long cid;
    private Integer priceGt;
    private Integer priceLte;

    @Override
    public void requestCheck() {
        if(page<=0){
            setPage(1);
        }
    }
}
