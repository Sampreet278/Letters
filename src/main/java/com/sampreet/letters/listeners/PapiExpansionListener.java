package com.sampreet.letters.listeners;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.events.ExpansionsLoadedEvent;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import org.bukkit.ChatColor;

public class PapiExpansionListener implements Listener {
    private final Letters plugin;

    public PapiExpansionListener(Letters plugin) {
        this.plugin = plugin;
    }

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

            // Translate color codes
            message = ChatColor.translateAlternateColorCodes('&', message);

            // Log the message to the console
            plugin.getLogger().warning(message);
        }
    }
}
