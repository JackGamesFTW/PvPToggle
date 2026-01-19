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

    private static final Message MSG_INVALID_KEY = Message.translation("pvptoggle.errors.admin.set.invalid_key");
    private static final Message MSG_INVALID_VALUE = Message.translation("pvptoggle.errors.admin.set.invalid_value");
    private static final Message MSG_PERSIST_RESTART = Message.translation("pvptoggle.errors.admin.set.persist_restart");

    @Nonnull
    private final RequiredArg<String> keyArg;

    @Nonnull
    private final RequiredArg<String> valueArg;

    public PvPAdminSetCommand() {
        super("set", "Set a config value");
        this.keyArg = this.withRequiredArg("key", "Config key (combattimer, cooldown, default, persist, itemprotection)", ArgTypes.STRING);
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
                Message timerMsg = Message.translation(seconds == 0 ? "pvptoggle.common.seconds_disabled" : "pvptoggle.common.seconds").param("count", seconds);
                context.sendMessage(Message.translation("pvptoggle.admin.set.combat_timer").param("value", timerMsg));
            }
            case "cooldown" -> {
                Long seconds = parseLong(value);
                if (seconds == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setToggleCooldownSeconds(seconds);
                Message timerMsg = Message.translation(seconds == 0 ? "pvptoggle.common.seconds_disabled" : "pvptoggle.common.seconds").param("count", seconds);
                context.sendMessage(Message.translation("pvptoggle.admin.set.toggle_cooldown").param("value", timerMsg));
            }
            case "default" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setDefaultPvPEnabled(enabled);
                context.sendMessage(Message.translation("pvptoggle.admin.set.default_state")
                        .param("state", Message.translation(enabled ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));
            }
            case "persist" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setPersistPvPState(enabled);
                context.sendMessage(Message.translation("pvptoggle.admin.set.persist_data")
                        .param("state", Message.translation(enabled ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));
                context.sendMessage(MSG_PERSIST_RESTART);
            }
            case "itemprotection" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setItemProtectionEnabled(enabled);
                context.sendMessage(Message.translation("pvptoggle.admin.set.item_protection")
                        .param("state", Message.translation(enabled ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));
            }
            case "knockback" -> {
                Boolean enabled = parseBoolean(value);
                if (enabled == null) {
                    context.sendMessage(MSG_INVALID_VALUE);
                    return CompletableFuture.completedFuture(null);
                }
                config.setKnockbackEnabled(enabled);
                context.sendMessage(Message.translation("pvptoggle.admin.set.knockback")
                        .param("state", Message.translation(enabled ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));
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
