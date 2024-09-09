package me.nbtc.premieremporium.base;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class User {
    private final UUID uuid;
    private final String name;
    private double money = 100.0;
    private List<Transaction> transactions = new ArrayList<>();
    public User(UUID uuid, String name){
        this.uuid = uuid;
        this.name = name;
    }
}
