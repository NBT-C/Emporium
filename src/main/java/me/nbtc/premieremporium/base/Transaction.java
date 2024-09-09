package me.nbtc.premieremporium.base;

import lombok.Data;
import lombok.Value;

@Data
public class Transaction {
    private long purchaseDate;
    private MarketItem item;
    public Transaction(long purchaseDate, MarketItem item){
        this.purchaseDate = purchaseDate;
        this.item = item;
    }
}
