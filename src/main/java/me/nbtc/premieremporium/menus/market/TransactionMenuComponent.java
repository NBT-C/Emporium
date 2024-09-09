package me.nbtc.premieremporium.menus.market;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.base.Transaction;
import me.nbtc.premieremporium.menus.ConfirmPage;
import me.nbtc.premieremporium.repositories.ConfigRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TransactionMenuComponent implements PageComponent {
    private final Transaction transaction;
    public TransactionMenuComponent(Transaction transaction){
        this.transaction = transaction;
    }

    @Override
    public ItemStack toItem() {
        List<String> lore = Emporium.getInstance().getRepository(ConfigRepository.class).getTransactionsGui().getConfig().getStringList("item-lore");
        Component[] finalLore = new Component[lore.size()];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i)
                    .replace("{price}", transaction.getItem().getPrice() + "")
                    .replace("{date}", dateFormat.format(new Date(transaction.getPurchaseDate())))
                    .replace("{seller}", transaction.getItem().getItemOwner().getName())
                    .replace("{amount}", transaction.getItem().getItem().getAmount() + "");
            finalLore[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        }

        return ItemBuilder.modern(transaction.getItem().getItem().getType(), transaction.getItem().getItem().getAmount())
                .setDisplay(Component.text(transaction.getItem().getItem().getItemMeta() == null ? "&7" + transaction.getItem().getItem().getType().name().replace("_", " ") : transaction.getItem().getItem().getItemMeta().getDisplayName()))
                .setLore(finalLore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
    }
}