package me.nbtc.premieremporium;

import com.google.gson.Gson;
import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.nbtc.premieremporium.base.User;
import me.nbtc.premieremporium.commands.BlackMarketCommand;
import me.nbtc.premieremporium.commands.MarketPlaceCommand;
import me.nbtc.premieremporium.commands.SellCommand;
import me.nbtc.premieremporium.commands.TransactionsCommand;
import me.nbtc.premieremporium.listener.ConnectionListener;
import me.nbtc.premieremporium.manager.*;
import me.nbtc.premieremporium.storage.DataManager;
import me.nbtc.premieremporium.utils.DiscordWebhook;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
public final class Emporium extends JavaPlugin {
    private static @Getter Emporium instance;
    private DataManager dataManager;
    private Lotus lotus;

    private BukkitAudiences adventure;
    private final Gson gson = new Gson();

    private ConfigManager configManager;
    private MarketManager marketManager;
    private MenuManager menuManager;
    private UserManager userManager;

    private DiscordManager discordManager;

    private final HashMap<UUID, List<Component>> pendingMessages = new HashMap<>();

    private final DecimalFormat doubleFormat = new DecimalFormat("##.##");

    @Override
    public void onEnable() {
        instance = this;

        this.adventure = BukkitAudiences.create(this);

        enrollManagers();
        enrollCommands();
        enrollListeners();

        this.dataManager = new DataManager();

        if (!this.dataManager.connect()){
            getLogger().severe("Failed to connect to database, Stopping plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getMarketManager().loadFromMongo();
        getMarketManager().generateBlackMarket();
        this.lotus = new Lotus(this, EventPriority.NORMAL);
    }

    @Override
    public void onDisable() {
        getMarketManager().saveToMongo();

        this.dataManager.disconnect();

        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    private void enrollManagers(){
        configManager = new ConfigManager();
        marketManager = new MarketManager();
        menuManager = new MenuManager();
        userManager = new UserManager();
        discordManager = new DiscordManager();
    }

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    private void enrollCommands() {
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap) commandMapField.get(getServer());

            String fallbackPrefix = "emporium";
            commandMap.register(fallbackPrefix, new MarketPlaceCommand());
            commandMap.register(fallbackPrefix, new SellCommand());
            commandMap.register(fallbackPrefix, new TransactionsCommand());
            commandMap.register(fallbackPrefix, new BlackMarketCommand());
        } catch (Exception e) {
            getLogger().severe("Failed to register commands");
            e.printStackTrace();
        }
    }
    private void enrollListeners(){
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
    }

}
