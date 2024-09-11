package me.nbtc.premieremporium.listener;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.base.User;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.List;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try {
            Emporium.getInstance().getUserManager().load(player);
        } catch (Exception exception) {
            System.out.println("MongoDB error [" + player.getUniqueId() + "]: " + exception.getMessage());
        }

        if (!Emporium.getInstance().getPendingMessages().containsKey(player.getUniqueId())) return;

        List<Component> pendingMessages = Emporium.getInstance().getPendingMessages().get(player.getUniqueId());
        for (Component message : pendingMessages) {
            Emporium.getInstance().adventure().player(player).sendMessage(message);
        }
        Emporium.getInstance().getPendingMessages().remove(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        try {
            Emporium.getInstance().getUserManager().save(player);
        } catch (Exception exception) {
            System.out.println("MongoDB error [" + player.getUniqueId() + "]: " + exception.getMessage());
        }
    }
}
