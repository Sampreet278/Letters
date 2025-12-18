package com.sampreet.letters;

import org.bukkit.plugin.java.JavaPlugin;

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

        // Log that the plugin has successfully loaded and is ready
        enableDisableMessage("messages.system.enable");
    }

    @Override
    public void onDisable() {
        // Log that the plugin has been disabled
        enableDisableMessage("messages.system.disable");
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
        getLogger().info(message);
    }
}
