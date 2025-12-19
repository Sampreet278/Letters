package com.sampreet.letters;

import com.sampreet.letters.listeners.*;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import com.sampreet.letters.commands.RootCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import java.util.Objects;
import org.bukkit.Bukkit;

public final class Letters extends JavaPlugin {

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public void onEnable() {
        // Save default config.yml to plugin folder
        saveDefaultConfig();

        // Check whether plugin is enabled or disabled in config.yml
        if (!getConfig().getBoolean("enabled", true)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Only register PlaceholderAPI expansion pack loading listener if PlaceholderAPI is present
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getServer().getPluginManager().registerEvents(new PapiExpansionListener(this), this);
        }

        // Log whether PlaceholderAPI was found or not
        systemMessage(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null
                ? "messages.system.lifecycle.placeholder-api-detected"
                : "messages.system.lifecycle.placeholder-api-notfound");

        // Register the root command executor
        RootCommand rootCommand = new RootCommand(this);
        Objects.requireNonNull(this.getCommand("letters")).setExecutor(rootCommand);
        // Register the root command tab completer
        Objects.requireNonNull(this.getCommand("letters")).setTabCompleter(rootCommand);

        // Register the event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerAdvancementDoneListener(this), this);

        // Log that the plugin has successfully loaded and is ready
        systemMessage("messages.system.lifecycle.enable");
    }

    @Override
    public void onDisable() {
        // Log that the plugin has been disabled
        systemMessage("messages.system.lifecycle.disable");
    }

    // Helper function to log a system message from config.yml if it exists and is not empty.
    private void systemMessage(String path) {
        String message = getConfig().getString(path);

        // Don't log if no message is set in config.yml
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        // Insert current plugin version into placeholder
        message = message.replace("%version%", getDescription().getVersion());

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Log the message to the console
        Bukkit.getConsoleSender().sendMessage(String.format("[%s] ", getDescription().getPrefix()) + ChatColor.translateAlternateColorCodes('&', message));
    }
}
