package com.stockmarket.stock_management.repository;

import com.stockmarket.stock_management.domain.StockLocalEventStore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockLocalEventRepository extends MongoRepository<StockLocalEventStore, String> {
    List<StockLocalEventStore> findByEvent_CompanyCode(String companyCode);

    void deleteAllByEvent_CompanyCode(String companyCode);
}
