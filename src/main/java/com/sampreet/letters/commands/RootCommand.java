package com.sampreet.letters.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NonNull;
import org.bukkit.command.TabCompleter;
import com.sampreet.letters.Letters;
import org.bukkit.command.Command;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import java.util.List;

public class RootCommand implements CommandExecutor, TabCompleter {
    private final Letters plugin;

    public RootCommand(Letters plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        // Check if no subcommand was written.
        if (args.length == 0) {
            systemMessage(sender, "messages.system.commands.invalid-command");
            return true;
        }

        // Check if the reload command was run
        if (args[0].equalsIgnoreCase("reload")) {
            // Check if the sender has the required permission
            if (!sender.hasPermission("letters.reload")) {
                systemMessage(sender, "messages.system.commands.no-permission");
                return true;
            }
            // Reload the config and send a confirmation message
            plugin.reloadConfig();
            systemMessage(sender, "messages.system.lifecycle.reload");
            return true;
        }

        // If the subcommand was not found
        systemMessage(sender, "messages.system.commands.invalid-command");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String alias, String @NonNull [] args) {
        // Create a list to store possible completions
        List<String> completions = new ArrayList<>();

        // Only provide completions for the first argument (the subcommand)
        if (args.length == 1) {
            // If the user has started typing "reload", suggest it
            if ("reload".startsWith(args[0].toLowerCase()) && sender.hasPermission("letters.reload")) {
                completions.add("reload");
            }
        }

        // Return the list of suggestions
        return completions;
    }

    // Helper function to log a system message from config.yml if it exists and is not empty.
    public void systemMessage(CommandSender sender, String path) {
        String message = plugin.getConfig().getString(path);

        // Don't log if no message is set in config.yml
        if (message == null || message.trim().isEmpty()) {
            return;
        }

        // Insert current plugin version into placeholder
        message = message.replace("%version%", plugin.getDescription().getVersion());
        // Send the message to the player or console
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
}
