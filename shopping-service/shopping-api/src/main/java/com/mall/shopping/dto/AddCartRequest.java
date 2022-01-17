package com.mall.shopping.dto;

import com.mall.commons.result.AbstractRequest;
import lombok.Data;

/**
 * Created by mic on 2019/7/23.
 */
@Data
public class AddCartRequest extends AbstractRequest{

    private Long userId;
    private Long itemId;
    private Integer num;

    @Override
    public void requestCheck() {

    }
}
