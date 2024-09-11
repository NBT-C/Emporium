package me.nbtc.premieremporium.base;

import lombok.Data;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.utils.BukkitSerialization;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.UUID;


@Data
public class MarketItem {
    private String item;
    private double price;
    private ItemOwner itemOwner;
    private UUID randomId;
    private boolean isBlack;

    public MarketItem(ItemStack item, double price, ItemOwner itemOwner, UUID randomId) {
        this.item = BukkitSerialization.itemStackArrayToBase64(new ItemStack[]{item});
        this.price = price;
        this.itemOwner = itemOwner;
        this.randomId = randomId;
    }
    public ItemStack getItemBehaviour(){
        try {
            ItemStack[] itemStack = BukkitSerialization.itemStackArrayFromBase64(item);
            return itemStack[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public double getPrice() {
        return Double.parseDouble(Emporium.getInstance().getDoubleFormat().format(this.price));
    }
}
