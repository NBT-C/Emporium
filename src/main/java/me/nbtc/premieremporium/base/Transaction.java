package me.nbtc.premieremporium.base;

import lombok.Data;
import lombok.Value;

@Data @Value
public class Transaction {
    long purchaseDate;
    MarketItem item;
}
