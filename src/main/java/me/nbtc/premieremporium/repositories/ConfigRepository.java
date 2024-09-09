package me.nbtc.premieremporium.repositories;

import lombok.Getter;
import me.nbtc.premieremporium.utils.config.SimpleConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@Getter
public class ConfigRepository {
    private final SimpleConfig messages;
    private final SimpleConfig marketPlaceGui;
    private final SimpleConfig transactionsGui;
    private final SimpleConfig settings;

    public ConfigRepository() {
        this.settings = new SimpleConfig("settings.yml");
        this.messages = new SimpleConfig("messages.yml");
        this.marketPlaceGui = new SimpleConfig("marketplacegui.yml");
        this.transactionsGui = new SimpleConfig("transactionsgui.yml");
    }

    public Component getMessage(String messageKey){
        String message = messages.getConfig().getString(messageKey);
        if (message == null) return Component.empty();
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
    public String getMPGString(String messageKey){
        String msg = marketPlaceGui.getConfig().getString(messageKey);
        if (msg == null) return "NOT_FOUND";
        return msg;
    }
    public String getTString(String messageKey){
        String msg = transactionsGui.getConfig().getString(messageKey);
        if (msg == null) return "NOT_FOUND";
        return msg;
    }
}
