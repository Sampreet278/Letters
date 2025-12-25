package com.sampreet.letters.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import com.sampreet.letters.Letters;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import java.util.Arrays;
import java.util.List;

public class WhisperCommand implements CommandExecutor, TabCompleter {
    private final Letters plugin;

    public WhisperCommand(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String [] args) {
        // Check if the player has the permission to have custom join messages
        if (!sender.hasPermission("letters.whisper")) {
            // Redirect to vanilla /msg
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            Bukkit.dispatchCommand(sender, "minecraft:msg " + args[0] + " " + message);
            return true;
        }

        // Check if no player name was provided.
        if (args.length == 0) {
            systemMessage(sender, "messages.system.commands.no-player");
            return true;
        }

        // Find recipient player
        Player recipient = plugin.getServer().getPlayerExact(args[0]);
        if (recipient == null) {
            systemMessage(sender, "messages.system.commands.invalid-player");
            return true;
        }

        // Check if no message was provided
        if (args.length < 2) {
            systemMessage(sender, "messages.system.commands.no-message");
            return true;
        }

        // Build whisper message
        String whisperMessage = String.join(" ", args).substring(args[0].length()).trim();

        // Check if the built whisper message is empty
        if (whisperMessage.isEmpty()) {
            systemMessage(sender, "messages.system.commands.no-message");
            return true;
        }

        // Send message to sender
        whisperMessage(sender, sender, recipient, whisperMessage, "messages.default.whisper.sender");
        // Send message to recipient
        whisperMessage(recipient, sender, recipient, whisperMessage, "messages.default.whisper.recipient");
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String [] args) {
        // Create a list to store possible completions
        List<String> completions = new ArrayList<>();

        // Only provide completions for the first argument (the recipient name)
        if (args.length == 1) {
            for (Player player : plugin.getServer().getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(player.getName());
                }
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

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Translate color codes
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Send the message to the player or console
        sender.sendMessage(message);
    }

    // Sends a whisper message to a specific receiver, replacing placeholders with sender, recipient, and message.
    public void whisperMessage(CommandSender receiver, CommandSender sender, Player recipient, String whisperMessage, String path) {
        // Store all custom join messages in a list
        List<String> messages = plugin.getConfig().getStringList(path);

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

        // Replace placeholders
        if (sender != null) {
            message = message.replace("%sender_name%", sender.getName());
        }
        if (recipient != null) {
            message = message.replace("%recipient_name%", recipient.getName());
        }
        if (whisperMessage != null) {
            message = message.replace("%whisper_message%", whisperMessage);
        }

        // Apply PlaceholderAPI placeholders if installed and receiver is a player
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && receiver instanceof Player player) {
            message = PlaceholderAPI.setPlaceholders(player, message);
        }

        // Deserialize MiniMessage string to a Component
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message));

        // Translate color codes
        message = ChatColor.translateAlternateColorCodes('&', message);

        // Send the message
        receiver.sendMessage(message);
    }
}
