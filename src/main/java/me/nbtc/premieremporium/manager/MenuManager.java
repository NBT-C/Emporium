package me.nbtc.premieremporium.manager;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.base.Transaction;
import me.nbtc.premieremporium.menus.transactions.TransactionAutoPage;
import me.nbtc.premieremporium.menus.transactions.TransactionMenuComponent;
import me.nbtc.premieremporium.menus.market.MarketAutoPage;
import me.nbtc.premieremporium.menus.market.MarketMenuComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuManager {
    public void openMarketPlace(Player player){
        List<PageComponent> components = new ArrayList<>();
        List<MarketItem> items = Emporium.getInstance().getMarketManager().getMarketItems();
        List<MarketItem> reversedItems = new ArrayList<>(items);
        Collections.reverse(reversedItems);
        for (MarketItem marketItem : reversedItems){
            ItemOwner owner = marketItem.getItemOwner();

            components.add(new MarketMenuComponent(owner, marketItem));
        }
        Pagination pagination = Pagination.auto(Emporium.getInstance().getLotus())
                .creator(new MarketAutoPage())
                .componentProvider(() ->components)
                .build();
        try {
            pagination.open(player);
        } catch (InvalidPageException ex) {
            player.sendMessage("There is no components or pages to display !!");
        }
    }
    public void openTransactions(Player player){
        List<PageComponent> components = new ArrayList<>();
        List<Transaction> transactions = Emporium.getInstance().getUserManager().getUser(player).getTransactions();
        List<Transaction> reversedTransactions = new ArrayList<>(transactions);
        Collections.reverse(reversedTransactions);
        for (Transaction transaction : reversedTransactions){
            components.add(new TransactionMenuComponent(transaction));
        }
        Pagination pagination = Pagination.auto(Emporium.getInstance().getLotus())
                .creator(new TransactionAutoPage())
                .componentProvider(() ->components)
                .build();
        try {
            pagination.open(player);
        } catch (InvalidPageException ex) {
            player.sendMessage("There is no components or pages to display !!");
        }
    }

}
