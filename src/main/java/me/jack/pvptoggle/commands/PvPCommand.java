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
import me.jack.pvptoggle.commands.admin.PvPAdminCommand;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

/**
 * This is an example command that will simply print the name of the plugin in chat when used.
 */
public class PvPCommand extends AbstractPlayerCommand {
    public PvPCommand() {
        super("pvp", "Toggle PvP off/on for yourself");
        this.setPermissionGroup(GameMode.Adventure);

        this.addSubCommand(new PvPOnCommand());
        this.addSubCommand(new PvPOffCommand());
        this.addSubCommand(new PvPStatusCommand());
        this.addSubCommand(new PvPAdminCommand());
    }

    @Override
    protected void execute(
        @NonNullDecl CommandContext commandContext,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PlayerRef playerRef,
        @NonNullDecl World world
    ) {
        commandContext.sendMessage(Message.raw("Hello"));
    }
}