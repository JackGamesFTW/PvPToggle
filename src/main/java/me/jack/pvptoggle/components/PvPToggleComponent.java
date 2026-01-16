package me.jack.pvptoggle.components;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.component.Component;
import com.hypixel.hytale.component.ComponentType;
import me.jack.pvptoggle.PvPTogglePlugin;

import java.time.Instant;

public class PvPToggleComponent implements Component {
    private boolean pvpEnabled;
    private Instant lastToggleTime = Instant.EPOCH;
    private Instant lastCombatTime = Instant.EPOCH;

    public PvPToggleComponent() {
        this.pvpEnabled = PvPTogglePlugin.CONFIG.get().isDefaultPvPEnabled();
    }

    public PvPToggleComponent(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public static ComponentType getComponentType() {
        return PvPTogglePlugin.getInstance().getPvPToggleComponentType();
    }

    public static final BuilderCodec<PvPToggleComponent> CODEC = BuilderCodec
        .builder(PvPToggleComponent.class, PvPToggleComponent::new)
        .append(new KeyedCodec<>("PvPEnabled", Codec.BOOLEAN),
                (component, enabled) -> component.pvpEnabled = enabled,
                (component) -> component.pvpEnabled).add()
        .build();

    public boolean isPvPEnabled() {
        return pvpEnabled;
    }

    public void setPvPEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public Instant getLastToggleTime() {
        return lastToggleTime;
    }

    public void setLastToggleTime(Instant lastToggleTime) {
        this.lastToggleTime = lastToggleTime;
    }

    public Instant getLastCombatTime() {
        return lastCombatTime;
    }

    public void setLastCombatTime(Instant lastCombatTime) {
        this.lastCombatTime = lastCombatTime;
    }

    public boolean isInCombat() {
        long duration = PvPTogglePlugin.CONFIG.get().getCombatTimerSeconds();
        if (duration <= 0) return false;
        return Instant.now().isBefore(this.lastCombatTime.plusSeconds(duration));
    }

    public boolean isOnCooldown() {
        long duration = PvPTogglePlugin.CONFIG.get().getToggleCooldownSeconds();
        if (duration <= 0) return false;
        return Instant.now().isBefore(this.lastToggleTime.plusSeconds(duration));
    }

    public long getRemainingCombatTime() {
        if (!isInCombat()) return 0;
        long duration = PvPTogglePlugin.CONFIG.get().getCombatTimerSeconds();
        Instant combatEnds = this.lastCombatTime.plusSeconds(duration);
        return Math.max(0, combatEnds.getEpochSecond() - Instant.now().getEpochSecond());
    }

    public long getRemainingCooldown() {
        if (!isOnCooldown()) return 0;
        long cooldown = PvPTogglePlugin.CONFIG.get().getToggleCooldownSeconds();
        Instant cooldownEnds = this.lastToggleTime.plusSeconds(cooldown);
        return Math.max(0, cooldownEnds.getEpochSecond() - Instant.now().getEpochSecond());
    }

    @Override
    public Component clone() {
        PvPToggleComponent clone = new PvPToggleComponent(this.pvpEnabled);

        clone.lastCombatTime = this.lastCombatTime;
        clone.lastToggleTime = this.lastToggleTime;

        return clone;
    }
}
