package com.stockmarket.stock_management.projection.repository;

import com.stockmarket.stock_management.domain.Stock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockProjectionRepository extends MongoRepository<Stock, String> {
    List<Stock> findByCompanyCode(String companyCode);

    void deleteAllByCompanyCode(String companyCode);
}
