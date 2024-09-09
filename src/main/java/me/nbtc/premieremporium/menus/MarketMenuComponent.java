package me.nbtc.premieremporium.menus;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.premieremporium.base.ItemOwner;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class MarketMenuComponent implements PageComponent {
    private final ItemStack itemStack;
    private final double price;
    private final ItemOwner itemOwner;
    public MarketMenuComponent(ItemOwner owner, ItemStack itemStack, double price){
        this.itemStack = itemStack;
        this.price = price;
        this.itemOwner = owner;
    }

    @Override
    public ItemStack toItem() {
        return ItemBuilder.legacy(itemStack.getType(), itemStack.getAmount())
                .setDisplay(itemStack.getType().name())
                .setLore(Arrays.asList("Price: " + price, "Owner: " + itemOwner.getName()))
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent inventoryClickEvent) {

    }

}
