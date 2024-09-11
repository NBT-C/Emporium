package me.nbtc.premieremporium.menus.market;

import io.github.mqzen.menus.base.Content;
import io.github.mqzen.menus.base.pagination.Page;
import io.github.mqzen.menus.base.pagination.PageView;
import io.github.mqzen.menus.base.style.TextLayout;
import io.github.mqzen.menus.base.style.TextLayoutPane;
import io.github.mqzen.menus.misc.Capacity;
import io.github.mqzen.menus.misc.DataRegistry;
import io.github.mqzen.menus.misc.button.Button;
import io.github.mqzen.menus.misc.button.actions.ButtonClickAction;
import io.github.mqzen.menus.misc.itembuilder.ItemBuilder;
import io.github.mqzen.menus.titles.MenuTitle;
import io.github.mqzen.menus.titles.MenuTitles;
import lombok.var;
import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.manager.ConfigManager;
import me.nbtc.premieremporium.manager.enums.MarketType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MarketAutoPage extends Page {
    final MarketType marketType;

    public MarketAutoPage(MarketType marketType){
        this.marketType = marketType;
    }
    ConfigManager configManager = Emporium.getInstance().getConfigManager();

    private String parseBlackString(){
        return marketType == MarketType.BLACK ? "black-" : "";
    }

    @Override
    public int getPageButtonsCount(@Nullable PageView pageView, Player player) {
        return configManager.getMarketPlaceGui().getConfig().getInt(marketType.name().toLowerCase() + "-items-per-page");
    }

    @Override
    public ItemStack nextPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW)
                .setDisplay(configManager.getMPGString("next-page")).build();
    }

    @Override
    public ItemStack previousPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW)
                .setDisplay(configManager.getMPGString("previous-page")).build();
    }

    @Override
    public String getName() {
        return marketType.name();
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        int index = dataRegistry.getData("index");
        return MenuTitles.createModern(configManager.getMPGString(marketType.name().toLowerCase() + "-name").replace("{page}", (index+1) + ""));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(
                configManager.getMarketPlaceGui().getConfig().getInt(marketType.name().toLowerCase() + "-rows")
        );
    }

    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        var builder = Content.builder(capacity);

        Button btn = Button.clickable(
                ItemBuilder.legacy(Material.valueOf(configManager.getMPGString(parseBlackString() + "style-item")), 1, (short)0)
                        .setDisplay(configManager.getMPGString(parseBlackString() + "style-item-name")).build(),
                ButtonClickAction.plain((menu, event) -> event.setCancelled(true))
        );
        String[] stylePattern = configManager.getMarketPlaceGui().getConfig().getStringList(parseBlackString() + "style-pattern").toArray(new String[0]);
        TextLayoutPane pane = new TextLayoutPane(
                capacity,
                TextLayout.builder().set('#', btn).build(),
                stylePattern
        );

        builder = builder.applyPane(pane);
        return builder.build();
    }
}
