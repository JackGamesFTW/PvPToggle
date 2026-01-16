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
public class PvPOnCommand extends AbstractPlayerCommand {
    public PvPOnCommand() {
        super("on", "Enable PvP");

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

        if (pvp.isPvPEnabled()) {
            commandContext.sendMessage(Message.raw("PvP is already enabled."));
            return;
        }

        if (pvp.isInCombat()) {
            commandContext.sendMessage(Message.raw("You cannot toggle PvP while in combat! Wait " + pvp.getRemainingCombatTime() + " seconds."));
            return;
        }

        if (pvp.isOnCooldown()) {
            commandContext.sendMessage(Message.raw("You are toggling too quick! Wait " + pvp.getRemainingCooldown() + " seconds."));
            return;
        }

        pvp.setPvPEnabled(true);
        pvp.setLastToggleTime(Instant.now());
        commandContext.sendMessage(Message.raw("PvP is now enabled. You can attack and be attacked by other players."));
    }
}