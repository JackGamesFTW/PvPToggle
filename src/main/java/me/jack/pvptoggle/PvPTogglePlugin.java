package me.jack.pvptoggle;

import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.AddPlayerToWorldEvent;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import com.hypixel.hytale.server.core.util.Config;
import me.jack.pvptoggle.commands.PvPCommand;
import me.jack.pvptoggle.components.PvPToggleComponent;
import me.jack.pvptoggle.config.PvPToggleConfig;
import me.jack.pvptoggle.systems.CombatTrackingSystem;
import me.jack.pvptoggle.systems.PreventDamageSystem;

import javax.annotation.Nonnull;

/**
 * This class serves as the entrypoint for your plugin. Use the setup method to register into game registries or add
 * event listeners.
 */
public class PvPTogglePlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    public static Config<PvPToggleConfig> CONFIG;

    private static PvPTogglePlugin instance;

    private ComponentType pvpToggleComponentType;

    public static PvPTogglePlugin getInstance() {
        return instance;
    }

    public PvPTogglePlugin(@Nonnull JavaPluginInit init) {
        super(init);

        CONFIG = this.withConfig(PvPToggleConfig.CODEC);
    }

    @Override
    protected void setup() {
        instance = this;

        LOGGER.atInfo().log("Loading config file...");
        CONFIG.save();

        PvPToggleConfig config = CONFIG.get();

        if (config.isPersistPvPState()) {
            this.pvpToggleComponentType = this.getEntityStoreRegistry().registerComponent(
                PvPToggleComponent.class,
                "PVPToggle",
                PvPToggleComponent.CODEC
            );

            this.getLogger().atInfo().log("PvP state will be persisted across restarts.");
        } else {
            this.pvpToggleComponentType = this.getEntityStoreRegistry().registerComponent(
                PvPToggleComponent.class,
                PvPToggleComponent::new
            );

            this.getLogger().atInfo().log("PvP state will not be persisted across restarts.");
        }

        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, this::onPlayerReady);

        this.getEntityStoreRegistry().registerSystem(new CombatTrackingSystem());
        this.getEntityStoreRegistry().registerSystem(new PreventDamageSystem());

        this.getCommandRegistry().registerCommand(new PvPCommand());
    }

    public ComponentType getPvPToggleComponentType() {
        return pvpToggleComponentType;
    }

    private void onPlayerReady(@Nonnull PlayerReadyEvent event) {
        Ref<EntityStore> playerRef = event.getPlayerRef();

        if (playerRef.isValid()) {
            playerRef.getStore().ensureComponent(playerRef, this.pvpToggleComponentType);
        }
    }
}