package me.nbtc.premieremporium.listener;

import me.nbtc.premieremporium.Emporium;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        try {
            Emporium.getInstance().getUserManager().load(player);
        } catch (Exception exception) {
            System.out.println("MongoDB error [" + player.getUniqueId() + "]: " + exception.getMessage());
        }
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
