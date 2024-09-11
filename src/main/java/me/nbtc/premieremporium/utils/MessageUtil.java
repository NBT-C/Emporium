package me.nbtc.premieremporium.utils;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.manager.ConfigManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessageUtil {
    public static void msg(Player player, String msg_path, Map<String, String> replacements){
        Component finalMessage = Emporium.getInstance().getConfigManager().getMessage(msg_path);

        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                String replace = entry.getKey();
                String value = entry.getValue();

                finalMessage = finalMessage.replaceText(text -> text.matchLiteral(replace).replacement(value));
            }
        }

        Emporium.getInstance().getAdventure().player(player).sendMessage(finalMessage);
    }
    public static void msg(UUID uuid, String msg_path, Map<String, String> replacements){
        Player player = Bukkit.getPlayer(uuid);
        if (player == null || !player.isOnline()) {
            pendingMessage(uuid, msg_path, replacements);
            return;
        }
        msg(player, msg_path, replacements);
    }
    private static void pendingMessage(UUID uuid, String msg_path, Map<String, String> replacements){
        Component finalMessage = Emporium.getInstance().getConfigManager().getMessage(msg_path);

        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                String replace = entry.getKey();
                String value = entry.getValue();

                finalMessage = finalMessage.replaceText(text -> text.matchLiteral(replace).replacement(value));
            }
        }

        List<Component> messages = Emporium.getInstance().getPendingMessages().computeIfAbsent(uuid, k -> new ArrayList<>());
        messages.add(finalMessage);
    }
    public static void msg(Player player, String msg_path){
        Emporium.getInstance().getAdventure().player(player).sendMessage(Emporium.getInstance().getConfigManager().getMessage(msg_path));
    }

    public static void msg(CommandSender sender, String msg_path){
        Emporium.getInstance().adventure().sender(sender).sendMessage(Emporium.getInstance().getConfigManager().getMessage(msg_path).asComponent());
    }
}
