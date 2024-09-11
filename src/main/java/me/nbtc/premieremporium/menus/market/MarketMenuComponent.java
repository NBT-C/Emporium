package me.nbtc.premieremporium.menus.market;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.menus.ConfirmPage;
import me.nbtc.premieremporium.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


import java.util.List;

public class MarketMenuComponent implements PageComponent {
    private final MarketItem marketItem;
    private final ItemOwner itemOwner;
    public MarketMenuComponent(ItemOwner owner, MarketItem marketItem){
        this.marketItem = marketItem;
        this.itemOwner = owner;
    }

    @Override
    public ItemStack toItem() {
        List<String> lore = Emporium.getInstance().getConfigManager().getMarketPlaceGui().getConfig().getStringList("item-lore");
        Component[] finalLore = new Component[lore.size()];

        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i)
                    .replace("{price}", marketItem.getPrice() + "")
                    .replace("{seller}", itemOwner.getName())
                    .replace("{amount}", marketItem.getItem().getAmount() + "");
            finalLore[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        }

        return ItemBuilder.modern(marketItem.getItem().getType(), marketItem.getItem().getAmount())
                .setDisplay(Component.text(marketItem.getItem().getItemMeta() == null ? "&7" + marketItem.getItem().getType().name().replace("_", " ") : marketItem.getItem().getItemMeta().getDisplayName()))
                .setLore(finalLore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        new ConfirmPage(player, marketItem);
    }

}
