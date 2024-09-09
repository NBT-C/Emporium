package me.nbtc.premieremporium.base;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemOwner {
    private String name;
    private UUID uuid;

    public ItemOwner(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
