package me.jack.pvptoggle.commands;

import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractPlayerCommand;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.components.PvPToggleComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import java.time.Instant;

/**
 * This is an example command that will simply print the name of the plugin in chat when used.
 */
public class PvPToggleCommand extends AbstractPlayerCommand {
    public PvPToggleCommand() {
        super("toggle", "Toggle PvP");

        this.setPermissionGroup(GameMode.Adventure);
    }

    @Override
    protected void execute(
        @NonNullDecl CommandContext commandContext,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PlayerRef playerRef,
        @NonNullDecl World world
    ) {
        PvPToggleComponent pvp = (PvPToggleComponent) store.getComponent(ref, PvPToggleComponent.getComponentType());

        if (pvp == null) {
            pvp = new PvPToggleComponent();
            store.putComponent(ref, PvPToggleComponent.getComponentType(), pvp);
        }

        if (pvp.isInCombat()) {
            commandContext.sendMessage(Message.translation("pvptoggle.combat_cooldown").param("timeLeft", pvp.getRemainingCombatTime()));
            return;
        }

        if (pvp.isOnCooldown()) {
            commandContext.sendMessage(Message.translation("pvptoggle.toggle_cooldown").param("timeLeft", pvp.getRemainingCooldown()));
            return;
        }

        String messageKey = pvp.isPvPEnabled() ? "pvptoggle.off" : "pvptoggle.on";

        pvp.setPvPEnabled(!pvp.isPvPEnabled());
        pvp.setLastToggleTime(Instant.now());
        commandContext.sendMessage(Message.translation(messageKey));
    }
}