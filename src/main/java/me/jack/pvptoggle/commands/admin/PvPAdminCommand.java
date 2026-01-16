package me.jack.pvptoggle.commands.admin;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.command.system.basecommands.AbstractCommandCollection;

public class PvPAdminCommand extends AbstractCommandCollection {
    public PvPAdminCommand() {
        super("admin", "Manage PvPToggle Configuration");
        this.setPermissionGroup(GameMode.Creative);

        this.addSubCommand(new PvPAdminConfigCommand());
        this.addSubCommand(new PvPAdminSetCommand());
    }
}