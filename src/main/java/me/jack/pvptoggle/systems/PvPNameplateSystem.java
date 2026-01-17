package me.jack.pvptoggle.systems;

import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.dependency.Dependency;
import com.hypixel.hytale.component.dependency.Order;
import com.hypixel.hytale.component.dependency.SystemDependency;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import com.hypixel.hytale.server.core.modules.entity.player.PlayerSystems;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.components.PvPToggleComponent;
import me.jack.pvptoggle.config.PvPToggleConfig;
import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import javax.annotation.Nonnull;
import java.util.Set;

/**
 * System that removes item/durability loss for PvP deaths when friendly PvP mode is enabled.
 * This allows players to duel for fun without losing items or durability.
 */
public class PvPNameplateSystem extends RefChangeSystem<EntityStore, PvPToggleComponent> {
    @Nonnull
    @Override
    public Query getQuery() {
        return PlayerRef.getComponentType();
    }

    @NonNullDecl
    @Override
    public Set<Dependency<EntityStore>> getDependencies() {
        return Set.of(
            new SystemDependency(Order.AFTER, PlayerSystems.NameplateRefChangeSystem.class)
        );
    }

    @NonNullDecl
    @Override
    public ComponentType componentType() {
        return PvPToggleComponent.getComponentType();
    }

    @Override
    public void onComponentAdded(
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PvPToggleComponent pvpToggleComponent,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        updateNameplate(ref, pvpToggleComponent, commandBuffer);
    }

    @Override
    public void onComponentSet(
        @NonNullDecl Ref<EntityStore> ref,
        @NullableDecl PvPToggleComponent oldComponent,
        @NonNullDecl PvPToggleComponent newComponent,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        if (oldComponent == null || oldComponent.isPvPEnabled() != newComponent.isPvPEnabled()) {
            updateNameplate(ref, newComponent, commandBuffer);
        }
    }

    @Override
    public void onComponentRemoved(
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PvPToggleComponent pvpToggleComponent,
        @NonNullDecl Store<EntityStore> store,
        @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        if (!PvPTogglePlugin.CONFIG.get().isPvPIndicatorEnabled()) {
            return;
        }

        Nameplate nameplate = commandBuffer.getComponent(ref, Nameplate.getComponentType());

        if (nameplate == null) {
            return;
        }

        String currentText = nameplate.getText();
        currentText = stripIndicators(currentText);
        nameplate.setText(currentText);
    }

    private void updateNameplate(
        @NonNullDecl Ref<EntityStore> ref,
        @NonNullDecl PvPToggleComponent pvpToggleComponent,
        @NonNullDecl CommandBuffer<EntityStore> commandBuffer
    ) {
        PvPToggleConfig config = PvPTogglePlugin.CONFIG.get();

//        if (!config.isPvPIndicatorEnabled()) {
//            return;
//        }

        Nameplate nameplate = commandBuffer.getComponent(ref, Nameplate.getComponentType());

        if (nameplate == null) {
            return;
        }

        String prefix = config.getPvPIndicatorPrefix();
        String suffix = config.getPvPIndicatorSuffix();

        String currentText = nameplate.getText();

//        if (pvpToggleComponent.isPvPEnabled()) {
            String baseName = stripIndicators(currentText);
            nameplate.setText(prefix + baseName + suffix);
//        } else {
//            nameplate.setText(stripIndicators(currentText));
//        }
    }

    private String stripIndicators(@Nonnull String text) {
        PvPToggleConfig config = PvPTogglePlugin.CONFIG.get();
        String prefix = config.getPvPIndicatorPrefix();
        String suffix = config.getPvPIndicatorSuffix();

        String result = text;

        if (!prefix.isEmpty() && result.startsWith(prefix)) {
            result = result.substring(prefix.length());
        }

        if (!suffix.isEmpty() && result.endsWith(suffix)) {
            result = result.substring(0, result.length() - suffix.length());
        }

        return result;
    }
}
