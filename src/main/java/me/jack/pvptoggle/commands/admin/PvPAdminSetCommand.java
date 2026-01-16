package me.jack.pvptoggle.commands.admin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.config.PvPToggleConfig;

import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;

public class PvPAdminSetCommand extends AbstractCommand {

    private static final Message MSG_INVALID_KEY = Message.raw("Invalid key. Valid keys: combattimer, cooldown, default, persist, lootprotection");
    private static final Message MSG_INVALID_VALUE = Message.raw("Invalid value. Use a number for timers, or true/false/yes/no/on/off for toggles.");
    private static final Message MSG_PERSIST_RESTART = Message.raw("Note: 'persist' changes require a server restart to take effect.");

    @Nonnull
    private final RequiredArg<String> keyArg;

    @Nonnull
    private final RequiredArg<String> valueArg;

    public PvPAdminSetCommand() {
        super("set", "Set a config value");
        this.keyArg = this.withRequiredArg("key", "Config key (combattimer, cooldown, default, persist, lootprotection)", ArgTypes.STRING);
        this.valueArg = this.withRequiredArg("value", "New value", ArgTypes.STRING);
    }

    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        String key = this.keyArg.get(context).toLowerCase();
        String value = this.valueArg.get(context).toLowerCase();

        PvPToggleConfig config = PvPTogglePlugin.CONFIG.get();

        switch (key) {
            case "combattimer" -> {
                Long seconds = parseLong(value);
                if (seconds == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setCombatTimerSeconds(seconds);
                context.sendMessage(Message.raw("Combat timer set to " + seconds + "s" + (seconds == 0 ? " (disabled)" : "")));
            }
            case "cooldown" -> {
                Long seconds = parseLong(value);
                if (seconds == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setToggleCooldownSeconds(seconds);
                context.sendMessage(Message.raw("Toggle cooldown set to " + seconds + "s" + (seconds == 0 ? " (disabled)" : "")));
            }
            case "default" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setDefaultPvPEnabled(enabled);
                context.sendMessage(Message.raw("Default PvP state set to " + (enabled ? "enabled" : "disabled")));
            }
            case "persist" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setPersistPvPState(enabled);
                context.sendMessage(Message.raw("Persist PvP state set to " + (enabled ? "enabled" : "disabled")));
                context.sendMessage(MSG_PERSIST_RESTART);
            }
            case "lootprotection" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setItemProtectionEnabled(enabled);
                context.sendMessage(Message.raw("Loot protection set to " + (enabled ? "enabled" : "disabled")));
            }
            default -> context.sendMessage(MSG_INVALID_KEY);
        }

        PvPTogglePlugin.CONFIG.save();

        return CompletableFuture.completedFuture(null);
    }

    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {
        return switch (value) {
            case "true", "yes", "on", "1" -> true;
            case "false", "no", "off", "0" -> false;
            default -> null;
        };
    }
}
