package com.mall.promo.service;

import com.mall.order.OrderPromoService;
import com.mall.order.dto.CreateSeckillOrderRequest;
import com.mall.order.dto.CreateSeckillOrderResponse;
import com.mall.promo.PromoService;
import com.mall.promo.cache.CacheManager;
import com.mall.promo.constant.PromoRetCode;
import com.mall.promo.converter.PromoInfoConverter;
import com.mall.promo.converter.PromoProductConverter;
import com.mall.promo.dal.entitys.PromoItem;
import com.mall.promo.dal.entitys.PromoSession;
import com.mall.promo.dal.persistence.PromoItemMapper;
import com.mall.promo.dal.persistence.PromoSessionMapper;
import com.mall.promo.dto.*;
import com.mall.promo.mq.MqTransactionProducer;
import com.mall.shopping.IProductService;
import com.mall.shopping.dto.ProductDetailDto;
import com.mall.shopping.dto.ProductDetailRequest;
import com.mall.shopping.dto.ProductDetailResponse;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: jia.xue
 * @Email: xuejia@cskaoyan.onaliyun.com
 * @Description
 **/
@Service
@Component
public class PromoServiceImpl implements PromoService {

    @Autowired
    private PromoSessionMapper promoSessionMapper;

    @Autowired
    private PromoItemMapper promoItemMapper;

    @Autowired
    private PromoInfoConverter promoInfoConverter;

    @Reference(check = false)
    private IProductService productService;

    @Reference(check = false)
    private OrderPromoService orderPromoService;

    @Autowired
    private PromoProductConverter promoProductConverter;

    @Autowired
    private CacheManager cacheManager;


    @Autowired
    private MqTransactionProducer mqTransactionProducer;


    @Override
    public PromoInfoResponse getPromoList(PromoInfoRequest request) {
        request.requestCheck();
        PromoInfoResponse response = new PromoInfoResponse();

        //??????????????????
        Example example = new Example(PromoSession.class);
        example.createCriteria()
                .andEqualTo("sessionId",request.getSessionId())
                .andEqualTo("yyyymmdd",request.getYyyymmdd());
        List<PromoSession> sessionList = promoSessionMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(sessionList)) {
            response.setCode(PromoRetCode.PROMO_NOT_EXIST.getCode());
            response.setMsg(PromoRetCode.PROMO_NOT_EXIST.getMessage());
            return response;
        }
        PromoSession promoSession = sessionList.get(0);
        Long promoSessionId = promoSession.getId();
        // ???????????????????????????
        Example exampleItem = new Example(PromoItem.class);
        exampleItem.createCriteria()
                .andEqualTo("psId",promoSessionId);
        List<PromoItem> promoItems = promoItemMapper.selectByExample(exampleItem);
        if (CollectionUtils.isEmpty(promoItems)) {
            response.setCode(PromoRetCode.PROMO_ITEM_NOT_EXIST.getCode());
            response.setMsg(PromoRetCode.PROMO_ITEM_NOT_EXIST.getMessage());
            return response;
        }

        List<PromoItemInfoDto> productList = new ArrayList<>();
        // ????????????????????????

        promoItems.stream().forEach(promoItem -> {

            Long itemId = promoItem.getItemId();
            ProductDetailRequest productDetailRequest = new ProductDetailRequest();
            productDetailRequest.setId(itemId);
            ProductDetailResponse productDetailResponse = productService.getProductDetail(productDetailRequest);

            ProductDetailDto productDetailDto = productDetailResponse.getProductDetailDto();
            PromoItemInfoDto promoItemInfoDto = promoInfoConverter.convert2InfoDto(productDetailDto);

            promoItemInfoDto.setInventory(promoItem.getItemStock());
            promoItemInfoDto.setSeckillPrice(promoItem.getSeckillPrice());

            productList.add(promoItemInfoDto);

        });

        //????????????
        response.setPsId(promoSessionId);
        response.setSessionId(request.getSessionId());
        response.setProductList(productList);
        response.setCode(PromoRetCode.SUCCESS.getCode());
        response.setMsg(PromoRetCode.SUCCESS.getMessage());

        return response;
    }




    @Override
    @Transactional
    public CreatePromoOrderResponse createPromoOrder(CreatePromoOrderRequest request) {
        CreatePromoOrderResponse response = new CreatePromoOrderResponse();

        request.requestCheck();
        // ????????????
        Integer effectedRows = promoItemMapper.decreaseStock(request.getProductId(),request.getPsId());
        if (effectedRows < 1) {
            response.setCode(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getCode());
            response.setMsg(PromoRetCode.PROMO_ITEM_STOCK_NOT_ENOUGH.getMessage());
            return response;
        }

        //???????????????????????????
        Example example = new Example(PromoItem.class);
        example.createCriteria()
                .andEqualTo("psId",request.getPsId())
                .andEqualTo("itemId",request.getProductId());
        List<PromoItem> promoItems = promoItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(promoItems)) {
            response.setCode(PromoRetCode.PROMO_ITEM_NOT_EXIST.getCode());
            response.setMsg(PromoRetCode.PROMO_ITEM_NOT_EXIST.getMessage());
            return response;
        }

        PromoItem promoItem = promoItems.get(0);
        // ????????????
        CreateSeckillOrderRequest createSeckillOrderRequest = new  CreateSeckillOrderRequest();
        createSeckillOrderRequest.setUsername(request.getUsername());
        createSeckillOrderRequest.setUserId(request.getUserId());
        createSeckillOrderRequest.setProductId(request.getProductId());
        createSeckillOrderRequest.setPrice(promoItem.getSeckillPrice());
        CreateSeckillOrderResponse createSeckillOrderResponse = orderPromoService.createPromoOrder(createSeckillOrderRequest);


        if (!createSeckillOrderResponse.getCode().equals(PromoRetCode.SUCCESS.getCode())) {
            response.setCode(createSeckillOrderResponse.getCode());
            response.setMsg(createSeckillOrderResponse.getMsg());
            return response;
        }

        response.setProductId(request.getProductId());
        response.setInventory(promoItem.getItemStock());
        response.setCode(PromoRetCode.SUCCESS.getCode());
        response.setMsg(PromoRetCode.SUCCESS.getMessage());
        return response;
    }


    @Override
    public CreatePromoOrderResponse createPromoOrderInTransaction(CreatePromoOrderRequest request) {
        CreatePromoOrderResponse response = new CreatePromoOrderResponse();
        request.requestCheck();

        Example example = new Example(PromoItem.class);
        example.createCriteria()
                .andEqualTo("psId",request.getPsId())
                .andEqualTo("itemId",request.getProductId());
        List<PromoItem> promoItems1 = promoItemMapper.selectByExample(example);
        PromoItem promoItem = promoItems1.get(0);
        request.setPromoPrice(promoItem.getSeckillPrice());

        /**
         * ??????????????????????????????????????????????????????????????????
         *
         * ???????????????Redis????????????????????????
         */
        Integer itemStock = promoItem.getItemStock();
        if (itemStock < 1) {
            String key = "promo_item_stock_not_enough_" + request.getPsId() +"_" + request.getProductId();
            cacheManager.setCache(key,"stock_not_enough",1);
        }

        //?????????????????????
        Boolean ret = mqTransactionProducer.sendPromoOrderTransaction(request);

        if (ret) {

            List<PromoItem> promoItems2  = promoItemMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(promoItems2)) {
                response.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
                response.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
                return response;
            }
            response.setProductId(request.getProductId());
            response.setInventory(promoItems2.get(0).getItemStock());
            response.setCode(PromoRetCode.SUCCESS.getCode());
            response.setMsg(PromoRetCode.SUCCESS.getMessage());
            return response;
        }else {
            response.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            response.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
            return response;
        }
    }


    @Override
    public PromoProductDetailResponse getPromoProductProduct(PromoProductDetailRequest request) {
        PromoProductDetailResponse promoProductDetailResponse = new PromoProductDetailResponse();

        ProductDetailRequest productDetailRequest = new ProductDetailRequest();
        productDetailRequest.setId(request.getProductId());
        ProductDetailResponse productDetailResponse = productService.getProductDetail(productDetailRequest);
        if (productDetailResponse == null || !productDetailResponse.getCode().equals(PromoRetCode.SUCCESS.getCode())){
            promoProductDetailResponse.setMsg(productDetailResponse.getMsg());
            return promoProductDetailResponse;
        }
        Example example = new Example(PromoItem.class);
        example.createCriteria()
                .andEqualTo("psId",request.getPsId())
                .andEqualTo("itemId",request.getProductId());
        List<PromoItem> promoItems = promoItemMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(promoItems)) {
            promoProductDetailResponse.setCode(PromoRetCode.SYSTEM_ERROR.getCode());
            promoProductDetailResponse.setMsg(PromoRetCode.SYSTEM_ERROR.getMessage());
            return promoProductDetailResponse;
        }
        PromoItem promoItem = promoItems.get(0);

        PromoProductDetailDTO promoProductDetailDTO = promoProductConverter.convert2DetailDTO(promoItem, productDetailResponse.getProductDetailDto());
        promoProductDetailResponse.setPromoProductDetailDTO(promoProductDetailDTO);
        promoProductDetailResponse.setCode(PromoRetCode.SUCCESS.getCode());
        promoProductDetailResponse.setMsg(PromoRetCode.SUCCESS.getMessage());
        return promoProductDetailResponse;

    }
}