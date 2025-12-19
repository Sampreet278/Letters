package com.sampreet.letters.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.entity.PlayerDeathEvent;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jspecify.annotations.NonNull;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.List;

public class PlayerDeathListener implements Listener {
    private final Letters plugin;

    public PlayerDeathListener(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerDeath(@NonNull PlayerDeathEvent event) {
        // Check if the player has the permission to have custom death messages
        if (!event.getEntity().hasPermission("letters.death")) return;

        // Store all custom death messages in a list
        List<String> messages = plugin.getConfig().getStringList("messages.default.death");

        // Filter out all null or empty strings like ones which are just whitespaces
        List<String> validMessages = messages.stream()
                .filter(msg -> msg != null && !msg.trim().isEmpty())
                .toList();

        // Don't proceed if no valid messages are found
        if (validMessages.isEmpty()) {
            return;
        }

        // Don't proceed if no valid messages are set in config.yml
        String message = validMessages.get(
                ThreadLocalRandom.current().nextInt(validMessages.size())
        );

        // Insert player name into placeholder
        message = message.replace("%player_name%", event.getEntity().getName());
        // Insert death message into placeholder
        if (event.getDeathMessage() != null) message = message.replace("%death_message%", event.getDeathMessage());

        // Apply PlaceholderAPI placeholders if installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(event.getEntity(), message);
        }

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Block the default death message
        event.setDeathMessage(null);
        // Broadcast the custom message to the server
        event.getEntity().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
