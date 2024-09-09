package me.nbtc.premieremporium;

import io.github.mqzen.menus.Lotus;
import lombok.Getter;
import me.nbtc.premieremporium.commands.MarketPlaceCommand;
import me.nbtc.premieremporium.commands.SellCommand;
import me.nbtc.premieremporium.repositories.ConfigRepository;
import me.nbtc.premieremporium.repositories.MarketRepository;
import me.nbtc.premieremporium.repositories.MenuRepository;
import me.nbtc.premieremporium.repositories.Registry;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.HashMap;

@Getter
public final class Emporium extends JavaPlugin {
    private static @Getter Emporium instance;
    private final Registry registry = new Registry();
    private final HashMap<String, String> configValues = new HashMap<>();
    private Lotus lotus;

    private BukkitAudiences adventure;

    @Override
    public void onEnable() {
        instance = this;

        this.adventure = BukkitAudiences.create(this);

        enrollRepositories();
        enrollCommands();

        lotus = new Lotus(this, EventPriority.NORMAL);
    }

    @Override
    public void onDisable() {
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
        } catch (Exception e) {
            getLogger().severe("Failed to register commands");
            e.printStackTrace();
        }
    }
}
