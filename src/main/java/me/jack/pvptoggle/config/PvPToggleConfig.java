package me.jack.pvptoggle.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import me.jack.pvptoggle.constants.PvPToggleConstants;

public class PvPToggleConfig {
    private boolean persistPvPState = PvPToggleConstants.PERSIST_PVP_STATE;
    private boolean defaultPvPEnabled = PvPToggleConstants.DEFAULT_PVP_ENABLED;
    private boolean itemProtectionEnabled = PvPToggleConstants.LOOT_PROTECTION_ENABLED;
    private boolean knockbackEnabled = PvPToggleConstants.KNOCKBACK_ENABLED;

    private long combatTimerSeconds = PvPToggleConstants.COMBAT_TIMER_SECONDS;
    private long toggleCooldownSeconds = PvPToggleConstants.TOGGLE_COOLDOWN_SECONDS;

    private String pvpIndicatorPrefix = PvPToggleConstants.PVP_INDICATOR_PREFIX;
    private String pvpIndicatorSuffix = PvPToggleConstants.PVP_INDICATOR_SUFFIX;

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
            .append(new KeyedCodec<>("ItemProtectionEnabled", Codec.BOOLEAN),
                    (config, value) -> config.itemProtectionEnabled = value,
                    (config) -> config.itemProtectionEnabled).add()
            .append(new KeyedCodec<>("KnockbackEnabled", Codec.BOOLEAN),
                    (config, value) -> config.knockbackEnabled = value,
                    (config) -> config.knockbackEnabled).add()
            .append(new KeyedCodec<>("PvPIndicatorPrefix", Codec.STRING),
                    (config, value) -> config.pvpIndicatorPrefix = value,
                    (config) -> config.pvpIndicatorPrefix).add()
            .append(new KeyedCodec<>("PvPIndicatorSuffix", Codec.STRING),
                    (config, value) -> config.pvpIndicatorSuffix = value,
                    (config) -> config.pvpIndicatorSuffix).add()
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

    public boolean isItemProtectionEnabled() {
        return itemProtectionEnabled;
    }

    public PvPToggleConfig setItemProtectionEnabled(boolean itemProtectionEnabled) {
        this.itemProtectionEnabled = itemProtectionEnabled;

        return this;
    }

    public boolean isKnockbackEnabled() {
        return knockbackEnabled;
    }

    public PvPToggleConfig setKnockbackEnabled(boolean knockbackEnabled) {
        this.knockbackEnabled = knockbackEnabled;

        return this;
    }

    public String getPvPIndicatorPrefix() {
        return pvpIndicatorPrefix;
    }

    public PvPToggleConfig setPvPIndicatorPrefix(String pvpIndicatorPrefix) {
        this.pvpIndicatorPrefix = pvpIndicatorPrefix;

        return this;
    }

    public String getPvPIndicatorSuffix() {
        return pvpIndicatorSuffix;
    }

    public PvPToggleConfig setPvPIndicatorSuffix(String pvpIndicatorSuffix) {
        this.pvpIndicatorSuffix = pvpIndicatorSuffix;

        return this;
    }

    public boolean isPvPIndicatorEnabled() {
        return !pvpIndicatorPrefix.isEmpty() || !pvpIndicatorSuffix.isEmpty();
    }
}
