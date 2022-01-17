package com.mall.order.biz.handler;

import com.alibaba.fastjson.JSON;
import com.mall.commons.tool.exception.BaseBusinessException;
import com.mall.order.biz.context.CreateOrderContext;
import com.mall.order.biz.context.TransHandlerContext;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.OrderItemMapper;
import com.mall.order.dal.persistence.StockMapper;
import com.mall.order.dto.CartProductDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 扣减库存处理器
 * @Author： wz
 * @Date: 2019-09-16 00:03
 **/
@Component
@Slf4j
public class SubStockHandler extends AbstractTransHandler {
	@Autowired
	StockMapper stockMapper;
	@Autowired
	OrderItemMapper orderItemMapper;

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@Transactional
	public boolean handle(TransHandlerContext context) {
		CreateOrderContext createOrderContext = (CreateOrderContext) context;
		List<CartProductDto> cartProductDtoList = createOrderContext.getCartProductDtoList();
		//item_ids
		List<Long> itemIds = createOrderContext.getBuyProductIds();
		// 修改逻辑 update by ciggar 不然报空指针异常
		if (CollectionUtils.isEmpty(itemIds)){
			itemIds = cartProductDtoList.stream().map(u -> u.getProductId()).collect(Collectors.toList());
		}
		//排序
		itemIds.sort(Long::compareTo);
		//一次性锁 ids
		List<Stock> list = stockMapper.findStocksForUpdate(itemIds);
		log.info("购物车列表 list:{}",JSON.toJSONString(list));
		if(CollectionUtils.isEmpty(list)){
			throw new BaseBusinessException("库存未初始化");
		}
		if(list.size()!=itemIds.size()){
			throw new BaseBusinessException("有商品未初始化库存,请在如下商品id中检查库存状态："+itemIds.toString());

		}
		log.info("cartProductDtoList:{}",JSON.toJSONString(cartProductDtoList));
		list.forEach(stock -> {
			cartProductDtoList.forEach(one -> {
				if (Objects.equals(one.getProductId(), stock.getItemId())) {
					if (stock.getStockCount() < one.getProductNum()) {
						throw new BaseBusinessException(stock.getItemId()+"库存不足");
					}
					if (stock.getRestrictCount() < one.getProductNum()) {
						log.info("商品{}超出限购数量，限购:{},购买:{}",one.getProductName(),stock.getRestrictCount(),one.getProductNum());
						throw new BaseBusinessException("超出限购数量");
					}
					stock.setLockCount(one.getProductNum().intValue());
					stock.setStockCount(-one.getProductNum());
					//更改库存状态
					stockMapper.updateStock(stock);
					return;
				}
			});
		});
		return true;
	}
}