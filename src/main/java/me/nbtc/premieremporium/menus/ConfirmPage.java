package me.nbtc.premieremporium.menus;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.Menu;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.MarketItem;
import me.nbtc.premieremporium.repositories.ConfigRepository;
import me.nbtc.premieremporium.repositories.MarketRepository;
import me.nbtc.premieremporium.repositories.MenuRepository;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ConfirmPage implements Menu {
    private final Player buyer;
    private final MarketItem product;
    public ConfirmPage(Player buyer, MarketItem item){
        this.buyer = buyer;
        this.product = item;

        Emporium.getInstance().getLotus().openMenu(buyer, this);
    }
    ConfigRepository configRepository = Emporium.getInstance().getRepository(ConfigRepository.class);
    @Override
    public String getName() {
        return "Confirm";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry extraData, Player opener) {
        return MenuTitles.createModern(configRepository.getMPGString("confirm-purchase-gui"));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry extraData, Player opener) {
        return Capacity.ofRows(3);
    }

    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        Button yesButton = Button.clickable(
                ItemBuilder.legacy(Material.valueOf(configRepository.getMPGString("yes-item")), 1, (short)0)
                        .setDisplay(configRepository.getMPGString("yes-string")).build(),
                ButtonClickAction.plain((menuView, event)-> {
                    event.setCancelled(true);
                    Emporium.getInstance().getRepository(MarketRepository.class).handleItemPurchase(buyer, product);
                })
        );

        Button noButton = Button.clickable(
                ItemBuilder.legacy(Material.valueOf(configRepository.getMPGString("no-item")), 1, (short)0)
                        .setDisplay(configRepository.getMPGString("no-string")).build(),
                ButtonClickAction.plain((menuView, event)-> {
                    event.setCancelled(true);
                    Emporium.getInstance().getRepository(MenuRepository.class).openMarketPlace(buyer);
                })
        );
        return Content.builder(capacity)
                .apply(content -> {
                    content.setButton(configRepository.getMarketPlaceGui().getConfig().getInt("yes-slot"), yesButton);
                    content.setButton(configRepository.getMarketPlaceGui().getConfig().getInt("no-slot"), noButton);
                })
                .build();
    }
}