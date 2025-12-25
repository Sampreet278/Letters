package com.sampreet.letters.listeners;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import java.util.concurrent.ThreadLocalRandom;
import me.clip.placeholderapi.PlaceholderAPI;
import org.jetbrains.annotations.NotNull;
import org.bukkit.event.EventHandler;
import com.sampreet.letters.Letters;
import org.bukkit.event.Listener;
import java.lang.reflect.Field;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Bukkit;
import java.util.List;

public class PlayerAdvancementDoneListener implements Listener {
    private final Letters plugin;

    public PlayerAdvancementDoneListener(Letters plugin) {
        this.plugin = plugin;
    }

    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    @EventHandler
    public void onPlayerAdvancementDone(@NotNull PlayerAdvancementDoneEvent event) {
        // Check if the player has the permission to have custom advancement messages
        if (!event.getPlayer().hasPermission("letters.advancement")) return;

        // Store all custom advancement messages in a list
        List<String> messages = plugin.getConfig().getStringList("messages.default.advancement");

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

        // Return if the advancement has no display
        if (event.getAdvancement().getDisplay() == null) return;

        // Disable it so the vanilla message does not show
        GameRule<Boolean> showAdvancementMessages = getAdvancementGameRule();
        if (showAdvancementMessages != null && Boolean.TRUE.equals(event.getPlayer().getWorld().getGameRuleValue(showAdvancementMessages))) {
            event.getPlayer().getWorld().setGameRule(showAdvancementMessages, false);
        }

        // Get advancement color
        ChatColor color = event.getAdvancement().getDisplay().getType().getColor();

        // Get advancement name
        String name = event.getAdvancement().getDisplay().getTitle();

        // Insert player name into placeholder
        message = message
                .replace("%player_name%", event.getPlayer().getName())
                .replace("%advancement_name%", name);

        // Apply PlaceholderAPI placeholders if installed
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            message = PlaceholderAPI.setPlaceholders(event.getPlayer(), message);
        }

        // Deserialize MiniMessage string to a Component and add advancement color
        message = LegacyComponentSerializer.legacyAmpersand().serialize(miniMessage.deserialize(message))
                .replace("%advancement_color%", color.toString());

        // Broadcast the custom message to the server
        event.getPlayer().getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    @SuppressWarnings("unchecked")
    private GameRule<Boolean> getAdvancementGameRule() {
        try {
            Field oldRule = GameRule.class.getField("ANNOUNCE_ADVANCEMENTS");
            return (GameRule<Boolean>) oldRule.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e1) {
            try {
                //noinspection JavaReflectionMemberAccess
                Field newRule = GameRule.class.getField("SHOW_ADVANCEMENT_MESSAGES");
                return (GameRule<Boolean>) newRule.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e2) {
                return null;
            }
        }
    }
}
