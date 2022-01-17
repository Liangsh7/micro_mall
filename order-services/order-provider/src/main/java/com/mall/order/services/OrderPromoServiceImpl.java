package com.mall.order.services;

import com.mall.order.OrderPromoService;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.handler.InitOrderHandler;
import com.mall.order.biz.handler.LogisticalHandler;
import com.mall.order.constant.OrderRetCode;
import com.mall.order.dto.CartProductDto;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
import com.mall.shopping.IProductService;
import com.mall.shopping.dto.ProductDetailDto;
import com.mall.shopping.dto.ProductDetailRequest;
import com.mall.shopping.dto.ProductDetailResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/

@Component
public class OrderPromoServiceImpl implements OrderPromoService {


    @Autowired
    private InitOrderHandler initOrderHandler;

    @Autowired
    private LogisticalHandler logisticalHandler;

    @Reference(check = false)
    private IProductService productService;
    /**
     * 创建秒杀订单
     * @param request
     * @return
     */
    @Override
    @Transactional
    public CreateSeckillOrderResponse createPromoOrder(CreateSeckillOrderRequest request) {

        CreateSeckillOrderResponse response = new CreateSeckillOrderResponse();

        //查询商品详情
        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
        productDetailRequest.setId(request.getProductId());
        ProductDetailResponse productDetail = productService.getProductDetail(productDetailRequest);
        ProductDetailDto productDetailDto = productDetail.getProductDetailDto();


        //构建生成订单参数
        CreateOrderContext createOrderContext = new CreateOrderContext();


        List<CartProductDto> cartProductDtoList = new ArrayList<>();
        CartProductDto cartProductDto = new CartProductDto();

        createOrderContext.setUserId(request.getUserId());
        createOrderContext.setBuyerNickName(request.getUsername());
        createOrderContext.setOrderTotal(request.getPrice());

        cartProductDto.setProductId(request.getProductId());
        cartProductDto.setProductNum(1l);
        cartProductDto.setSalePrice(request.getPrice());
        cartProductDto.setProductName(productDetailDto.getProductName());
        cartProductDto.setProductImg(productDetailDto.getProductImageBig());

        cartProductDtoList.add(cartProductDto);
        createOrderContext.setCartProductDtoList(cartProductDtoList);

        // 初始化订单
        boolean ret = initOrderHandler.handle(createOrderContext);

        // TODO 初始化物流信息
        /**
         *             orderShipping.setOrderId(String.valueOf(createOrderContext.getOrderId()));
         *             orderShipping.setReceiverName(createOrderContext.getUserName());
         *             orderShipping.setReceiverAddress(createOrderContext.getStreetName());
         *             orderShipping.setReceiverPhone(createOrderContext.getTel());
         */
        createOrderContext.setStreetName(request.getStreetName());
        createOrderContext.setTel(request.getTel());
        logisticalHandler.handle(createOrderContext);

        if (ret) {
            response.setCode(OrderRetCode.SUCCESS.getCode());
            response.setMsg(OrderRetCode.SUCCESS.getMessage());
            return response;
        }
        response.setCode(OrderRetCode.INIT_ORDER_EXCEPTION.getCode());
        response.setMsg(OrderRetCode.INIT_ORDER_EXCEPTION.getMessage());
        return response;
    }
}