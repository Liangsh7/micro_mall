package com.mall.search.services;

import com.mall.search.InitDataService;
import com.mall.search.converter.ProductConverter;
import com.mall.search.dal.entitys.Item;
import com.mall.search.dal.persistence.ItemMapper;
import com.mall.search.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *  ciggar
 * create-date: 2019/9/2-22:05
 */
@Slf4j
@Service
public class InitDataServiceImpl implements InitDataService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    ItemMapper itemMapper;

    @Autowired
    ProductConverter productConverter;

    @Override
    public void initItems() {
        List<Item> items=itemMapper.selectAll();
        items.parallelStream().forEach(item->{
            productRepository.save(productConverter.item2Document(item));
        });
    }
}
