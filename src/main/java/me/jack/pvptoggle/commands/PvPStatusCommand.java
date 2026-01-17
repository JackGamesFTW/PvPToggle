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
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.components.PvPToggleComponent;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

public class PvPStatusCommand extends AbstractPlayerCommand {
    public PvPStatusCommand() {
        super("status", "Check your PvP status");

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
        PvPToggleComponent pvpComponent = (PvPToggleComponent) store.getComponent(
                ref,
                PvPToggleComponent.getComponentType()
        );

        if (pvpComponent == null) {
            pvpComponent = new PvPToggleComponent();
            store.putComponent(ref, PvPToggleComponent.getComponentType(), pvpComponent);
        }

        String itemProtection = PvPTogglePlugin.CONFIG.get().isItemProtectionEnabled() ? "enabled" : "disabled";

        if (pvpComponent.isPvPEnabled()) {
            commandContext.sendMessage(Message.translation("pvptoggle.status.enabled").param("itemProtection", itemProtection));
        } else {
            commandContext.sendMessage(Message.translation("pvptoggle.status.disabled").param("itemProtection", itemProtection));
        }
    }
}