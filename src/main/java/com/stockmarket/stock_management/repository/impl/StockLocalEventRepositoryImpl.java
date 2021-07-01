package com.stockmarket.stock_management.repository.impl;

import com.stockmarket.stock_management.domain.StockLocalEventStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockLocalEventRepositoryImpl {
    private final MongoTemplate mongoTemplate;

    @Value("${localEvent.batch.size.limit}")
    private int localEventBatchSizeLimit;

    @Value("${stockLocalEventStore.collectionName}")
    private String localEventStore;

    /**
     * returns events based on sent flag in a sorted order
     *
     * @param sent
     * @return
     */
    public List<StockLocalEventStore> findBySent(Boolean sent) {
        Query query = new Query();
        Criteria queryCriteria = null;
        queryCriteria = Criteria.where("sent").is(sent);
        query.addCriteria(queryCriteria).with(Sort.by(new Sort.Order(Sort.Direction.ASC, "createdDate"))).limit(localEventBatchSizeLimit);
        return mongoTemplate.find(query, StockLocalEventStore.class);
    }
}
