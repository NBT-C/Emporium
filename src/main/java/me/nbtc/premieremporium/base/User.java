package me.nbtc.premieremporium.base;

import lombok.Data;
import me.nbtc.premieremporium.Emporium;

import java.util.*;

@Data
public class User {
    private final UUID uuid;
    private final String name;
    private double money = 100.0;
    private List<Transaction> transactions;

    public User(UUID uuid, String name){
        this.uuid = uuid;
        this.name = name;
        this.transactions = new ArrayList<>();
    }

}
