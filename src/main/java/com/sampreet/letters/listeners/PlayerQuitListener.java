package com.sampreet.letters.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.List;

public class PlayerQuitListener implements Listener {
    private final Letters plugin;

    public PlayerQuitListener(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        // Check if the player has the permission to have custom leave messages
        if (!event.getPlayer().hasPermission("letters.leave")) return;

        // Store all custom leave messages in a list
        List<String> messages = plugin.getConfig().getStringList("messages.default.leave");

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

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Block the default leave message
        event.setQuitMessage(null);
        // Broadcast the custom message to the server
        event.getPlayer().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
