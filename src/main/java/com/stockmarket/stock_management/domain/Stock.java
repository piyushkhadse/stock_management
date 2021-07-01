package com.stockmarket.stock_management.domain;

import com.stockmarket.core.domain.AggregateRoot;
import com.stockmarket.core.domain.Error;
import com.stockmarket.core.events.StockPriceAddedEvent;
import com.stockmarket.stock_management.command.AddStockPrice;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document("stocks")
@NoArgsConstructor
public class Stock extends AggregateRoot {
    private String _id;
    private String companyCode;
    private Double stockPrice;
    private Instant createdDate;

    public Stock(AddStockPrice addStockPrice) {
        validate(addStockPrice);
        if (addStockPrice.getErrors().isEmpty()) {
            this._id = addStockPrice.getAggregateId();
            this.companyCode = addStockPrice.getCompanyCode();
            this.stockPrice = addStockPrice.getStockPrice();
            this.createdDate = Instant.now();
            StockPriceAddedEvent event = new StockPriceAddedEvent(
                    this._id, addStockPrice.getAggregateType(),
                    this.companyCode, stockPrice);
            this.registerEvent(event);
        }
    }

    public void validate(AddStockPrice addStockPrice) {
        List<Error> errors = new ArrayList<>();
        if (mandatoryCheck(addStockPrice.getCompanyCode())) {
            errors.add(new Error("INVALID_INPUT", "companyCode is invalid input"));
            addStockPrice.setErrors(errors);
        } else if (addStockPrice.getStockPrice() == null) {
            errors.add(new Error("INVALID_INPUT", "stockPrice is invalid input"));
            addStockPrice.setErrors(errors);
        } else if (addStockPrice.getStockPrice().compareTo(0.0d) == -1) {
            errors.add(new Error("INVALID_INPUT", "stockPrice is invalid input"));
            addStockPrice.setErrors(errors);
        }
    }

    private boolean mandatoryCheck(String field) {
        if (field == null || field.isEmpty()) {
            return true;
        }
        return false;
    }
}
