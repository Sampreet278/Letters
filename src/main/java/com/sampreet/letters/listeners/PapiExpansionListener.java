package com.sampreet.letters.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;
import org.bukkit.Bukkit;

public class PapiExpansionListener implements Listener {
    private final Letters plugin;

    public PapiExpansionListener(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onExpansionsLoaded(ExpansionsLoadedEvent event) {
        // Save whether the player expansion pack is installed
        boolean playerExpansionFound = false;

        // Iterate through list of loaded expansion packs and check whether the player pack is present
        for (PlaceholderExpansion exp : event.getExpansions()) {
            if ("player".equalsIgnoreCase(exp.getIdentifier())) {
                playerExpansionFound = true;
            }
        }

        // Send a warning message in the console if the player expansion pack was not found
        if (!playerExpansionFound) {
            // Bukkit.getScheduler().runTask(plugin, () -> {});
            String message = plugin.getConfig().getString("messages.system.lifecycle.papi-player-expansion-notfound");

            // Don't log if no message is set in config.yml
            if (message == null || message.trim().isEmpty()) {
                return;
            }

            // Deserialize MiniMessage string to a Component
            message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

            // Log the message to the console
            Bukkit.getConsoleSender().sendMessage(String.format("[%s] ", plugin.getDescription().getPrefix()) + ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
