package me.jack.pvptoggle.systems;

import com.hypixel.hytale.component.*;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageEventSystem;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageModule;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.components.PvPToggleComponent;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.time.Instant;

/**
 * System that tracks combat state for PvP damage.
 * Runs in the inspect group (after filtering) to only track damage that goes through.
 * Updates both attacker and target's last combat time when PvP damage occurs.
 */
public class CombatTrackingSystem extends DamageEventSystem {
    @Nonnull
    @Override
    public Query getQuery() {
        return PlayerRef.getComponentType();
    }

    @NullableDecl
    @Override
    public SystemGroup<EntityStore> getGroup() {
        return DamageModule.get().getInspectDamageGroup();
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

        World world = ((EntityStore) store.getExternalData()).getWorld();

        if (!world.getWorldConfig().isPvpEnabled()) {
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

        PvPToggleComponent targetPvP = (PvPToggleComponent) commandBuffer.getComponent(
                targetRef,
                PvPToggleComponent.getComponentType()
        );

        if (targetPvP == null) {
            commandBuffer.putComponent(targetRef, PvPToggleComponent.getComponentType(), new PvPToggleComponent());
        }

        PvPToggleComponent attackerPvP = (PvPToggleComponent) commandBuffer.getComponent(
                attackerRef,
                PvPToggleComponent.getComponentType()
        );

        if (targetPvP == null || attackerPvP == null) {
            return;
        }

        // Only track combat if BOTH players have PvP enabled (actual PvP occurred)
        if (!targetPvP.isPvPEnabled() || !attackerPvP.isPvPEnabled()) {
            return;
        }

        Instant now = Instant.now();

        targetPvP.setLastCombatTime(now);
        attackerPvP.setLastCombatTime(now);
    }
}
