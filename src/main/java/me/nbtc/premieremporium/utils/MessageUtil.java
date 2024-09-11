package me.nbtc.premieremporium.utils;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.manager.ConfigManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {
    public static void msg(Player player, String msg_path){
        Emporium.getInstance().adventure().player(player).sendMessage(Emporium.getInstance().getConfigManager().getMessage(msg_path));
    }
    public static void msg(CommandSender sender, String msg_path){
        Emporium.getInstance().adventure().sender(sender).sendMessage(Emporium.getInstance().getConfigManager().getMessage(msg_path));
    }
}
