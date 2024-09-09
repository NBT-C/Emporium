package me.nbtc.premieremporium.utils;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.repositories.ConfigRepository;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Map;

public class MessageUtil {
    public static void msg(Player player, String msg_path){
        Emporium.getInstance().adventure().player(player).sendMessage(Emporium.getInstance().getRepository(ConfigRepository.class).getMessage(msg_path));
    }
    public static void msg(CommandSender sender, String msg_path){
        Emporium.getInstance().adventure().sender(sender).sendMessage(Emporium.getInstance().getRepository(ConfigRepository.class).getMessage(msg_path));
    }
}
