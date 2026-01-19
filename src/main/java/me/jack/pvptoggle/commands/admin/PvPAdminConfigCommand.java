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

        context.sendMessage(Message.translation("pvptoggle.config.title"));

        long combatTimer = config.getCombatTimerSeconds();
        Message combatValue = combatTimer > 0 
                ? Message.translation("pvptoggle.common.seconds").param("count", combatTimer) 
                : Message.translation("pvptoggle.common.disabled");
        context.sendMessage(Message.translation("pvptoggle.config.combat_timer").param("value", combatValue));

        long cooldown = config.getToggleCooldownSeconds();
        Message cooldownValue = cooldown > 0 
                ? Message.translation("pvptoggle.common.seconds").param("count", cooldown) 
                : Message.translation("pvptoggle.common.disabled");
        context.sendMessage(Message.translation("pvptoggle.config.toggle_cooldown").param("value", cooldownValue));

        context.sendMessage(Message.translation("pvptoggle.config.default_state")
                .param("state", Message.translation(config.isDefaultPvPEnabled() ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));

        context.sendMessage(Message.translation("pvptoggle.config.persist_data")
                .param("state", Message.translation(config.isPersistPvPState() ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));

        context.sendMessage(Message.translation("pvptoggle.config.item_protection")
                .param("state", Message.translation(config.isItemProtectionEnabled() ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));

        context.sendMessage(Message.translation("pvptoggle.config.knockback")
                .param("state", Message.translation(config.isKnockbackEnabled() ? "pvptoggle.common.enabled" : "pvptoggle.common.disabled")));

        context.sendMessage(Message.raw(""));
        context.sendMessage(Message.translation("pvptoggle.config.help.title"));
        context.sendMessage(Message.translation("pvptoggle.config.help.combat"));
        context.sendMessage(Message.translation("pvptoggle.config.help.cooldown"));
        context.sendMessage(Message.translation("pvptoggle.config.help.default"));
        context.sendMessage(Message.translation("pvptoggle.config.help.persist"));
        context.sendMessage(Message.translation("pvptoggle.config.help.itemprotection"));
        context.sendMessage(Message.translation("pvptoggle.config.help.knockback"));

        return CompletableFuture.completedFuture(null);
    }
}