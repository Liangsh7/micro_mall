package com.mall.shopping;

import com.mall.shopping.dto.*;
import com.mall.shopping.dto.*;

/**
 *  ciggar
 * create-date: 2019/7/23-18:57
 */
public interface ICartService {


    /**
     * 获得购物车商品列表
     * @param request
     * @return
     */
    CartListByIdResponse getCartListById(CartListByIdRequest request);

    /**
     * 添加商品到购物车
     * @param request
     * @return
     */
    AddCartResponse addToCart(AddCartRequest request);


    /**
     * 更新购物车中商品的数量
     * @param request
     * @return
     */
    UpdateCartNumResponse updateCartNum(UpdateCartNumRequest request);

    /**
     * 选择购物车中的所有商品
     * @param request
     * @return
     */
    CheckAllItemResponse checkAllCartItem(CheckAllItemRequest request);

    /**
     * 删除购物车中的商品
     * @param request
     * @return
     */
    DeleteCartItemResponse deleteCartItem(DeleteCartItemRequest request);

    /**
     * 删除选中的商品
     * @param request
     * @return
     */
    DeleteCheckedItemResposne deleteCheckedItem(DeleteCheckedItemRequest request);


    /**
     * 清空指定用户的购物车缓存(用户下完订单之后清理）
     * @param request
     * @return
     */
    ClearCartItemResponse clearCartItemByUserID(ClearCartItemRequest request);


}
