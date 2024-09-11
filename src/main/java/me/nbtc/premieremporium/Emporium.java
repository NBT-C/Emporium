package me.nbtc.premieremporium;

import com.google.gson.Gson;
import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.nbtc.premieremporium.base.User;
import me.nbtc.premieremporium.commands.MarketPlaceCommand;
import me.nbtc.premieremporium.commands.SellCommand;
import me.nbtc.premieremporium.commands.TransactionsCommand;
import me.nbtc.premieremporium.listener.ConnectionListener;
import me.nbtc.premieremporium.manager.ConfigManager;
import me.nbtc.premieremporium.manager.MarketManager;
import me.nbtc.premieremporium.manager.MenuManager;
import me.nbtc.premieremporium.manager.UserManager;
import me.nbtc.premieremporium.storage.DataManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

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

            commandMap.register("emporium", new MarketPlaceCommand());
            commandMap.register("emporium", new SellCommand());
            commandMap.register("emporium", new TransactionsCommand());
        } catch (Exception e) {
            getLogger().severe("Failed to register commands");
            e.printStackTrace();
        }
    }
    private void enrollListeners(){
        getServer().getPluginManager().registerEvents(new ConnectionListener(), this);
    }

}
