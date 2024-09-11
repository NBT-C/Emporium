package me.nbtc.premieremporium.commands;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.manager.MarketManager;
import me.nbtc.premieremporium.utils.MessageUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SellCommand extends Command {


    public SellCommand() {
        super("sell");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            MessageUtil.msg(sender, "need-a-player");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("marketplace.sell")){
            MessageUtil.msg(player, "no-permission");
            return false;
        }

        if (args.length != 1){
            MessageUtil.msg(player, "invalid-argument-sell");
            return false;
        }

        if (player.getInventory().getItemInMainHand().getType() == Material.AIR){
            MessageUtil.msg(player, "no-item-in-hand");
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(args[0]);
        }catch (NumberFormatException ignored){
            MessageUtil.msg(player, "invalid-price");
            return false;
        }

        if (price <= 0){
            MessageUtil.msg(player, "invalid-price-higher");
            return false;
        }

        Emporium.getInstance().getMarketManager().addItem(player, player.getInventory().getItemInMainHand(), price);
        return false;
    }
}
