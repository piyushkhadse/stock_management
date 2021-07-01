package com.stockmarket.stock_management.domain;

import com.stockmarket.core.events.StockPriceAddedEvent;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Document("stockLocalEventStore")
public class StockLocalEventStore {
    private String _id;
    private StockPriceAddedEvent event;
    private Boolean sent;
    private Instant createdDate;

    public StockLocalEventStore(StockPriceAddedEvent event) {
        this._id = UUID.randomUUID().toString();
        this.event = event;
        this.sent = Boolean.FALSE;
        this.createdDate = Instant.now();
    }

    public StockLocalEventStore() {
        this._id = UUID.randomUUID().toString();
        this.sent = Boolean.FALSE;
        this.createdDate = Instant.now();
    }

    public String getId() {
        return this._id;
    }
}
