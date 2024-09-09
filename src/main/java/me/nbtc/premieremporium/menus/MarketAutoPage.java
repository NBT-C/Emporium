package me.nbtc.premieremporium.menus;

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
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class MarketAutoPage extends Page {

    @Override
    public int getPageButtonsCount(@Nullable PageView pageView, Player player) {
        return 3;
    }

    @Override
    public ItemStack nextPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW)
                .setDisplay("&aNext page").build();
    }

    @Override
    public ItemStack previousPageItem(Player player) {
        return ItemBuilder.legacy(Material.ARROW)
                .setDisplay("&ePrevious page").build();
    }

    @Override
    public String getName() {
        return "MarketPlace";
    }

    @Override
    public @NotNull MenuTitle getTitle(DataRegistry dataRegistry, Player player) {
        int index = dataRegistry.getData("index");
        return MenuTitles.createModern("Market #" + (index+1));
    }

    @Override
    public @NotNull Capacity getCapacity(DataRegistry dataRegistry, Player player) {
        return Capacity.ofRows(5);
    }

    @Override
    public @NotNull Content getContent(DataRegistry extraData, Player opener, Capacity capacity) {
        var builder = Content.builder(capacity);

        Button btn = Button.clickable(
                ItemBuilder.modern(Material.GRAY_STAINED_GLASS_PANE, 1, (short)0).build(),
                ButtonClickAction.plain((menu, event) -> event.setCancelled(true))
        );
        TextLayoutPane pane = new TextLayoutPane(
                capacity,
                TextLayout.builder().set('#', btn).build(),
                "#########",
                "         ",
                "         ",
                "         ",
                "#########"
        );

        builder = builder.applyPane(pane);
        return builder.build();
    }
}
