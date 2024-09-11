package me.nbtc.premieremporium.menus.transactions;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.Transaction;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
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
        List<String> lore = Emporium.getInstance().getConfigManager().getTransactionsGui().getConfig().getStringList("item-lore");
        Component[] finalLore = new Component[lore.size()];

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < lore.size(); i++) {
            String s = lore.get(i)
                    .replace("{price}", transaction.getItem().getPrice() + "")
                    .replace("{date}", dateFormat.format(new Date(transaction.getPurchaseDate())))
                    .replace("{seller}", transaction.getItem().getItemOwner().getName())
                    .replace("{amount}", transaction.getItem().getItemBehaviour().getAmount() + "");
            finalLore[i] = LegacyComponentSerializer.legacyAmpersand().deserialize(s);
        }

        return ItemBuilder.modern(transaction.getItem().getItemBehaviour().getType(), transaction.getItem().getItemBehaviour().getAmount())
                .setDisplay(Component.text(transaction.getItem().getItemBehaviour().getType().name().replace("_", " ")))
                .setLore(finalLore)
                .build();
    }

    @Override
    public void onClick(PageView pageView, InventoryClickEvent event) {
        event.setCancelled(true);
    }
}
