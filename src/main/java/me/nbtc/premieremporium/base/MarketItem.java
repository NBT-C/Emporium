package me.nbtc.premieremporium.base;

import lombok.Data;
import me.nbtc.premieremporium.utils.BukkitSerialization;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;


@Data
public class MarketItem {
    private String item;
    private double price;
    private ItemOwner itemOwner;

    public MarketItem(ItemStack item, double price, ItemOwner itemOwner) {
        this.item = BukkitSerialization.itemStackArrayToBase64(new ItemStack[]{item});
        this.price = price;
        this.itemOwner = itemOwner;
    }
    public ItemStack getItem(){
        try {
            ItemStack[] itemStack = BukkitSerialization.itemStackArrayFromBase64(item);
            return itemStack[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
