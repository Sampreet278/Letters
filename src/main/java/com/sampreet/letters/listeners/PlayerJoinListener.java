package com.sampreet.letters.listeners;

import org.bukkit.event.player.PlayerJoinEvent;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jspecify.annotations.NonNull;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.List;

public class PlayerJoinListener implements Listener {
    private final Letters plugin;

    public PlayerJoinListener(Letters plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(@NonNull PlayerJoinEvent event) {
        // Check if the player has the permission to have custom join messages
        if (!event.getPlayer().hasPermission("letters.join")) return;

        // Store all custom join messages in a list
        List<String> messages = plugin.getConfig().getStringList("messages.default.join");

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
        message = message.replace("%player_name%", event.getPlayer().getName());

        // Apply PlaceholderAPI placeholders if installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }

        // Block the default join message
        event.setJoinMessage(null);
        // Broadcast the custom message to the server
        event.getPlayer().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
