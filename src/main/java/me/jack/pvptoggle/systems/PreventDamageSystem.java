package me.jack.pvptoggle.systems;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.SystemGroup;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.knockback.KnockbackComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.components.PvPToggleComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * System that filters player vs player damage based on PvP toggle state.
 * Damage is cancelled if either the attacker or the target has PvP disabled.
 */
public class PreventDamageSystem extends DamageEventSystem {
    @Nullable
    @Override
    public SystemGroup getGroup() {
        return DamageModule.get().getFilterDamageGroup();
    }

    @Nonnull
    @Override
    public Query getQuery() {
        return PlayerRef.getComponentType();
    }

    @Override
    public void handle(
        int index,
        @Nonnull ArchetypeChunk archetypeChunk,
        @Nonnull Store store,
        @Nonnull CommandBuffer commandBuffer,
        @Nonnull Damage damage
    ) {
        if (damage.isCancelled()) {
            return;
        }

        Damage.Source source = damage.getSource();

        if (!(source instanceof Damage.EntitySource entitySource)) {
            return;
        }

        Ref<EntityStore> attackerRef = entitySource.getRef();

        if (!attackerRef.isValid()) {
            return;
        }

        Player attackerPlayer = (Player) commandBuffer.getComponent(attackerRef, Player.getComponentType());

        if (attackerPlayer == null) {
            return;
        }

        Ref<EntityStore> targetRef = archetypeChunk.getReferenceTo(index);

        PvPToggleComponent attackerPvP = (PvPToggleComponent) commandBuffer.getComponent(
                attackerRef,
                PvPToggleComponent.getComponentType()
        );

        PvPToggleComponent targetPvP = (PvPToggleComponent) commandBuffer.getComponent(
                targetRef,
                PvPToggleComponent.getComponentType()
        );

        boolean attackerHasPvPEnabled = attackerPvP == null || attackerPvP.isPvPEnabled();
        boolean targetHasPvPEnabled = targetPvP == null || targetPvP.isPvPEnabled();

        if (!attackerHasPvPEnabled || !targetHasPvPEnabled) {
            damage.setCancelled(true);

            if (!PvPTogglePlugin.CONFIG.get().isKnockbackEnabled()) {
                commandBuffer.tryRemoveComponent(targetRef, KnockbackComponent.getComponentType());
            }
        }
    }
}
