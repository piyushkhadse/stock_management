package com.stockmarket.stock_management.projection.repository.impl;

import com.stockmarket.core.domain.Error;
import com.stockmarket.core.exception.ApplicationException;
import com.stockmarket.core.logger.StockMarketApplicationLogger;
import com.stockmarket.stock_management.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockProjectionRepositoryImpl {
    private final MongoTemplate mongoTemplate;

    StockMarketApplicationLogger logger = StockMarketApplicationLogger.getLogger(this.getClass());

    @Value("${stocks.collectionName}")
    private String stocksCollectionName;

    /**
     * returns list of stock prices for a specific period from database
     *
     * @param companyCode
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Stock> getAllStockPrices(String companyCode, String startDate, String endDate) {
        try {
            Query query = new Query();
            Criteria queryCriteria = null;
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            Date fromDate = dateFormat.parse(startDate + " 00:00:00");
            Date toDate = dateFormat.parse(endDate + " 23:59:59");
            Criteria c1 = Criteria.where("createdDate").gte(fromDate);
            Criteria c2 = Criteria.where("createdDate").lte(toDate);
            Criteria c = new Criteria().andOperator(c1, c2);
            queryCriteria = Criteria.where("companyCode").is(companyCode).andOperator(c);
            query.addCriteria(queryCriteria).with(Sort.by(new Sort.Order(Sort.Direction.ASC, "createdDate")));
            return mongoTemplate.find(query, Stock.class);
        } catch (Exception e) {
            logger.error().log("Error while fetching all stock prices for company code:{}", companyCode, e);
            throw new ApplicationException(new Error("INTERNAL_SERVER_ERROR", "Internal Server Error"), 500);
        }
    }

    /**
     * returns latest stock price of a company from database
     *
     * @param companyCode
     * @return
     */
    public Stock getLatestStockPrice(String companyCode) {
        try {
            Query query = new Query();
            Criteria queryCriteria = null;
            queryCriteria = Criteria.where("companyCode").is(companyCode);
            query.addCriteria(queryCriteria).with(Sort.by(new Sort.Order(Sort.Direction.DESC, "createdDate")));
            return mongoTemplate.findOne(query, Stock.class);
        } catch (Exception e) {
            logger.error().log("Error while fetching latest stock price for company code:{}", companyCode, e);
            throw new ApplicationException(new Error("INTERNAL_SERVER_ERROR", "Internal Server Error"), 500);
        }
    }
}
