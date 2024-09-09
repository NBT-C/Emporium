package me.nbtc.premieremporium.repositories;

import lombok.Getter;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public class MarketRepository {
    private final List<MarketItem> marketItems = new ArrayList<>();

    public void addItem(Player owner, ItemStack item, double price) {
        owner.getInventory().setItemInMainHand(null);

        UUID ownerId = owner.getUniqueId();
        String ownerName = owner.getName();
        ItemOwner itemOwner = new ItemOwner(ownerName, ownerId);
        MarketItem marketItem = new MarketItem(item, price, itemOwner);

        marketItems.add(marketItem);
    }

}
