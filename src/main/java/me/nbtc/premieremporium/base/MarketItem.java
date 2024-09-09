package me.nbtc.premieremporium.base;

import lombok.Data;
import org.bukkit.inventory.ItemStack;


@Data
public class MarketItem {
    private final ItemStack item;
    private final double price;
    private final ItemOwner itemOwner;

    public MarketItem(ItemStack item, double price, ItemOwner itemOwner) {
        this.item = item;
        this.price = price;
        this.itemOwner = itemOwner;
    }
}
