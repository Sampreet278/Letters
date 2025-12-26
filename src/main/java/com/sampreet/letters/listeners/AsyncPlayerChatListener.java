package com.sampreet.letters.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import java.util.List;

public class AsyncPlayerChatListener implements Listener {
    private final Letters plugin;

    public AsyncPlayerChatListener(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onAsyncPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        // Check if the player has the permission to have custom server chat messages
        if (!event.getPlayer().hasPermission("letters.chat")) return;

        // Get player name to check for player-specific chat message in config.yml
        String playerName = event.getPlayer().getName();

        // Try getting the player-specific chat message from config.yml
        String playerPath = "messages.players." + playerName + ".chat";
        List<String> messages = plugin.getConfig().getStringList(playerPath);

        // If none found, fall back to default chat messages
        if (messages.isEmpty()) {
            messages = plugin.getConfig().getStringList("messages.default.chat");
        }

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

        // Insert player name and chat message into placeholder
        message = message
                .replace("%player_name%", "%1$s")
                .replace("%chat_message%", "%2$s");

        // Apply PlaceholderAPI placeholders if installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Translate color codes
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Broadcast the custom message to the server
        event.setFormat(message);
    }
}
