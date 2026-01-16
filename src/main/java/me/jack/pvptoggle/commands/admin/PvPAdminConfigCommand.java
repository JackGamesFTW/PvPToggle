package me.jack.pvptoggle.commands.admin;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.config.PvPToggleConfig;

import javax.annotation.Nonnull;
import java.util.concurrent.CompletableFuture;

public class PvPAdminConfigCommand extends AbstractCommand {
    public PvPAdminConfigCommand() {
        super("config", "Show current PvPToggle configuration");
    }


    @Override
    protected CompletableFuture<Void> execute(@Nonnull CommandContext context) {
        PvPToggleConfig config = PvPTogglePlugin.CONFIG.get();

        context.sendMessage(Message.raw("PvP Toggle Config:"));

        long combatTimer = config.getCombatTimerSeconds();
        String combatText = combatTimer > 0 ? combatTimer + " seconds" : "disabled";
        context.sendMessage(Message.raw("Combat timer: " + combatText));

        long cooldown = config.getToggleCooldownSeconds();
        String cooldownText = cooldown > 0 ? cooldown + " seconds" : "disabled";
        context.sendMessage(Message.raw("Toggle cooldown: " + cooldownText));

        context.sendMessage(Message.raw("Default PvP state: " + (config.isDefaultPvPEnabled() ? "enabled" : "disabled")));
        context.sendMessage(Message.raw("Persist data across restarts: " + (config.isPersistPvPState() ? "enabled" : "disabled")));

        context.sendMessage(Message.raw(""));
        context.sendMessage(Message.raw("To change these settings:"));
        context.sendMessage(Message.raw("  /pvp admin set combattimer <seconds>"));
        context.sendMessage(Message.raw("  /pvp admin set cooldown <seconds>"));
        context.sendMessage(Message.raw("  /pvp admin set default <true|false|yes|no|on|off>"));
        context.sendMessage(Message.raw("  /pvp admin set persist <true|false|yes|no|on|off>"));

        return CompletableFuture.completedFuture(null);
    }
}