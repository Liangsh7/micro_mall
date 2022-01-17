package com.mall.shopping;

import com.mall.shopping.dto.AllProductCateRequest;
import com.mall.shopping.dto.AllProductCateResponse;
import com.mall.shopping.dto.AllProductCateRequest;

/**
 * Created by oahnus on 2019/8/8
 * 21:38.
 */
public interface IProductCateService {
    /**
     * 获取所有产品分类
     * @param request
     * @return
     */
    AllProductCateResponse getAllProductCate(AllProductCateRequest request);
}
