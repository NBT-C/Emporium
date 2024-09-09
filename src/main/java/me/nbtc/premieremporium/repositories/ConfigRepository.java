package me.nbtc.premieremporium.repositories;

import lombok.Getter;
import me.nbtc.premieremporium.utils.config.SimpleConfig;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@Getter
public class ConfigRepository {
    private final SimpleConfig messages;

    public ConfigRepository() {
        this.messages = new SimpleConfig("messages.yml");
    }

    public Component getMessage(String messageKey){
        String message = messages.getConfig().getString(messageKey);
        if (message == null) return Component.empty();
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }
}
