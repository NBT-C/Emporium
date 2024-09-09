package me.nbtc.premieremporium;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.nbtc.premieremporium.base.User;
import me.nbtc.premieremporium.commands.MarketPlaceCommand;
import me.nbtc.premieremporium.commands.SellCommand;
import me.nbtc.premieremporium.commands.TransactionsCommand;
import me.nbtc.premieremporium.listener.ConnectionListener;
import me.nbtc.premieremporium.repositories.ConfigRepository;
import me.nbtc.premieremporium.repositories.MarketRepository;
import me.nbtc.premieremporium.repositories.MenuRepository;
import me.nbtc.premieremporium.repositories.Registry;
import me.nbtc.premieremporium.storage.DataManager;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.UUID;

@Getter
public final class Emporium extends JavaPlugin {
    private static @Getter Emporium instance;
    private final Registry registry = new Registry();
    private final HashMap<String, String> configValues = new HashMap<>();
    private final HashMap<UUID, User> users = new HashMap<>();
    private DataManager dataManager;
    private Lotus lotus;

    private BukkitAudiences adventure;
    private final Gson gson = new Gson();

    @Override
    public void onEnable() {
        instance = this;

        this.adventure = BukkitAudiences.create(this);

        enrollRepositories();
        enrollCommands();
        enrollListeners();

        this.dataManager = new DataManager();

        if (!this.dataManager.connect()){
            getLogger().severe("Failed to connect to database, Stopping plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getRepository(MarketRepository.class).loadFromMongo();
        this.lotus = new Lotus(this, EventPriority.NORMAL);
    }

    @Override
    public void onDisable() {
        getRepository(MarketRepository.class).saveToMongo();

        this.dataManager.disconnect();

        if(this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

    public @NotNull BukkitAudiences adventure() {
        if(this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.adventure;
    }

    private void enrollRepositories(){
        getRegistry().registerRepository(ConfigRepository.class, new ConfigRepository());
        getRegistry().registerRepository(MarketRepository.class, new MarketRepository());
        getRegistry().registerRepository(MenuRepository.class, new MenuRepository());
    }
    public <T> T getRepository(Class<T> repositoryClass) {
        return getRegistry().getRepository(repositoryClass);
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
    public User getUser(@NotNull Player p) {

        User u = this.users.computeIfAbsent(p.getUniqueId(), k -> (new User(p.getUniqueId(), p.getName())).load());
        if (u == null){
            u = new User(p.getUniqueId(), p.getName());
            u.load();
        }
        return u;
    }
}
