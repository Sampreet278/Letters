package com.sampreet.letters;

import com.sampreet.letters.listeners.PlayerDeathListener;
import com.sampreet.letters.listeners.PlayerJoinListener;
import com.sampreet.letters.listeners.PlayerQuitListener;
import com.sampreet.letters.commands.RootCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;
import java.util.Objects;

public final class Letters extends JavaPlugin {

    @Override
    public void onEnable() {
        // Save default config.yml to plugin folder
        saveDefaultConfig();

        // Check whether plugin is enabled or disabled in config.yml
        if (!getConfig().getBoolean("enabled", true)) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register the root command executor
        RootCommand rootCommand = new RootCommand(this);
        Objects.requireNonNull(this.getCommand("letters")).setExecutor(rootCommand);
        // Register the root command tab completer
        Objects.requireNonNull(this.getCommand("letters")).setTabCompleter(rootCommand);

        // Register the event listeners
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);

        // Log that the plugin has successfully loaded and is ready
        enableDisableMessage("messages.system.lifecycle.enable");
    }

    @Override
    public void onDisable() {
        // Log that the plugin has been disabled
        enableDisableMessage("messages.system.lifecycle.disable");
    }

    // Helper function to log a system message from config.yml if it exists and is not empty.
    private void enableDisableMessage(String path) {
        String message = getConfig().getString(path);

        // Don't log if no message is set in config.yml
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        // Insert current plugin version into placeholder
        message = message.replace("%version%", getDescription().getVersion());
        // Log the message to the console
        getLogger().info(ChatColor.translateAlternateColorCodes('&', message));
    }
}
