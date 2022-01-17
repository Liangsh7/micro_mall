package com.mall.search.repository;

import com.mall.search.entity.ItemDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author jin
 */
public interface ProductRepository extends ElasticsearchRepository<ItemDocument, Integer> {
}
