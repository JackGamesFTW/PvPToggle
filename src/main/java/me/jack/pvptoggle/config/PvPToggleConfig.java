package me.jack.pvptoggle.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.jack.pvptoggle.constants.PvPToggleConstants;

public class PvPToggleConfig {
    private boolean persistPvPState = PvPToggleConstants.PERSIST_PVP_STATE;
    private boolean defaultPvPEnabled = PvPToggleConstants.DEFAULT_PVP_ENABLED;

    private long combatTimerSeconds = PvPToggleConstants.COMBAT_TIMER_SECONDS;
    private long toggleCooldownSeconds = PvPToggleConstants.TOGGLE_COOLDOWN_SECONDS;

    public static final BuilderCodec<PvPToggleConfig> CODEC = BuilderCodec
            .builder(PvPToggleConfig.class, PvPToggleConfig::new)
            .append(new KeyedCodec<>("PersistPvPState", Codec.BOOLEAN),
                    (config, value) -> config.persistPvPState = value,
                    (config) -> config.persistPvPState).add()
            .append(new KeyedCodec<>("DefaultPvPEnabled", Codec.BOOLEAN),
                    (config, value) -> config.defaultPvPEnabled = value,
                    (config) -> config.defaultPvPEnabled).add()
            .append(new KeyedCodec<>("CombatTimerSeconds", Codec.LONG),
                    (config, value) -> config.combatTimerSeconds = value,
                    (config) -> config.combatTimerSeconds).add()
            .append(new KeyedCodec<>("ToggleCooldownSeconds", Codec.LONG),
                    (config, value) -> config.toggleCooldownSeconds = value,
                    (config) -> config.toggleCooldownSeconds).add()
            .build();

    public boolean isPersistPvPState() {
        return persistPvPState;
    }

    public PvPToggleConfig setPersistPvPState(boolean persistPvPState) {
        this.persistPvPState = persistPvPState;

        return this;
    }

    public boolean isDefaultPvPEnabled() {
        return defaultPvPEnabled;
    }

    public PvPToggleConfig setDefaultPvPEnabled(boolean defaultPvPEnabled) {
        this.defaultPvPEnabled = defaultPvPEnabled;

        return this;
    }

    public long getCombatTimerSeconds() {
        return combatTimerSeconds;
    }

    public PvPToggleConfig setCombatTimerSeconds(long combatTimerSeconds) {
        this.combatTimerSeconds = combatTimerSeconds;

        return this;
    }

    public long getToggleCooldownSeconds() {
        return toggleCooldownSeconds;
    }

    public PvPToggleConfig setToggleCooldownSeconds(long toggleCooldownSeconds) {
        this.toggleCooldownSeconds = toggleCooldownSeconds;

        return this;
    }
}
