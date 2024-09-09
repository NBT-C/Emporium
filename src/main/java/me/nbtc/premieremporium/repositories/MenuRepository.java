package me.nbtc.premieremporium.repositories;

import io.github.mqzen.menus.base.pagination.PageComponent;
import io.github.mqzen.menus.base.pagination.Pagination;
import io.github.mqzen.menus.base.pagination.exception.InvalidPageException;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.ItemOwner;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.menus.MarketAutoPage;
import me.nbtc.premieremporium.menus.MarketMenuComponent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MenuRepository {
    public void openMarketPlace(Player player){
        List<PageComponent> components = new ArrayList<>();
        for (MarketItem marketItem : Emporium.getInstance().getRepository(MarketRepository.class).getMarketItems()){
            ItemOwner owner = marketItem.getItemOwner();

            components.add(new MarketMenuComponent(owner, marketItem.getItem(), marketItem.getPrice()));
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

}
