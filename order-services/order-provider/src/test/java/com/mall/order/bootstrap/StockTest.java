package com.mall.order.bootstrap;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.mall.order.dal.entitys.Stock;
import com.mall.order.dal.persistence.StockMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

/**
 * @author: jia.xue
 * @create: 2020-02-19 13:07
 * @Description
 **/
public class StockTest extends OrderProviderApplicationTests{

    @Autowired
    private StockMapper stockMapper;
    @Test
    public void test01(){
        List<Long> list = Lists.newArrayList();
        list.add(100040607l);
        List<Stock> stocks = stockMapper.findStocksForUpdate(list);
        System.out.println(JSON.toJSONString(stocks));
    }
}