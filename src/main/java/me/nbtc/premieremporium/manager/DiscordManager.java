package me.nbtc.premieremporium.manager;

import me.nbtc.premieremporium.Emporium;
import me.nbtc.premieremporium.utils.DiscordWebhook;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DiscordManager {
    YamlConfiguration config = Emporium.getInstance().getConfigManager().getSettings().getConfig();

    public void sendSoldItemNotification(String seller, String item, String buyer, String price) {
        Emporium.getInstance().getServer().getScheduler().runTaskAsynchronously(Emporium.getInstance(), () -> {
            String webhookLink = config.getString("discord.webhook");
            String botName = config.getString("discord.bot-name");
            String botAvatar = config.getString("discord.bot-avatar");

            DiscordWebhook discordWebhook = new DiscordWebhook(webhookLink);
            discordWebhook.setUsername(botName);
            discordWebhook.setAvatarUrl(botAvatar);

            String contentTemplate = config.getString("discord.content");
            if (contentTemplate == null) {
                throw new RuntimeException("Please edit discord field well, or re-generate yaml file");
            }
            String content = contentTemplate.replace("{seller}", seller);

            discordWebhook.setContent(content);

            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject()
                    .setFooter(config.getString("discord.embed.footer.text"), config.getString("discord.embed.footer.icon"))
                    .setColor(Color.decode(config.getString("discord.embed.color")))
                    .setTitle(config.getString("discord.embed.title"))
                    .setDescription(config.getString("discord.embed.description"))
                    .setAuthor(config.getString("discord.embed.author.text"), config.getString("discord.embed.author.url"), config.getString("discord.embed.author.icon"));

            List<Map<String, Object>> fields = (List<Map<String, Object>>) config.get("discord.embed.fields");
            for (Map<String, Object> field : fields) {
                String name = (String) field.get("name");
                String value = ((String) field.get("value"))
                        .replace("{item}", item)
                        .replace("{buyer}", buyer)
                        .replace("{price}", price);
                boolean inline = (boolean) field.get("inline");
                embed.addField(name, value, inline);
            }

            discordWebhook.addEmbed(embed);

            try {
                discordWebhook.execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
