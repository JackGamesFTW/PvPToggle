package me.jack.pvptoggle.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.server.core.asset.type.gameplay.DeathConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.damage.*;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.PvPTogglePlugin;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * System that removes item/durability loss for PvP deaths when friendly PvP mode is enabled.
 * This allows players to duel for fun without losing items or durability.
 */
public class PvPItemProtectionSystem extends DeathSystems.OnDeathSystem {
    @Nonnull
    @Override
    public Query getQuery() {
        return PlayerRef.getComponentType();
    }

    @NonNullDecl
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return Set.of(
            new SystemDependency(Order.AFTER, DeathSystems.PlayerDropItemsConfig.class),
            new SystemDependency(Order.BEFORE, DeathSystems.DropPlayerDeathItems.class)
        );
    }

    @Override
    public void onComponentAdded(
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl DeathComponent deathComponent,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        if (!PvPTogglePlugin.CONFIG.get().isItemProtectionEnabled()) {
            return;
        }

        Damage deathInfo = deathComponent.getDeathInfo();

        if (deathInfo == null) {
            return;
        }

        Damage.Source source = deathInfo.getSource();

        if (!(source instanceof Damage.EntitySource entitySource)) {
            return;
        }

        Ref<EntityStore> attackerRef = entitySource.getRef();

        if (!attackerRef.isValid()) {
            return;
        }


        Player attackerPlayer = store.getComponent(attackerRef, Player.getComponentType());

        if (attackerPlayer == null) {
            return;
        }

        deathComponent.setItemsLossMode(DeathConfig.ItemsLossMode.NONE);
        deathComponent.setItemsAmountLossPercentage(0);
        deathComponent.setItemsDurabilityLossPercentage(0);
    }
}
