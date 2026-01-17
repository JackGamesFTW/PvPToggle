package me.jack.pvptoggle.systems;

import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.jack.pvptoggle.PvPTogglePlugin;
import me.jack.pvptoggle.components.PvPToggleComponent;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.ComponentType;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.RefChangeSystem;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.nameplate.Nameplate;
import me.jack.pvptoggle.config.PvPToggleConfig;

import javax.annotation.Nonnull;
import java.util.logging.Level;

/**
 * System that updates the nameplate when a player's PvP toggle state changes.
 * Works with PvPNameplateSystem which handles the actual nameplate formatting.
 */
public class PvPToggleNameplateUpdateSystem extends RefChangeSystem<EntityStore, PvPToggleComponent> {

    @Nonnull
    @Override
    public Query getQuery() {
        return Player.getComponentType();
    }

    @Nonnull
    @Override
    public ComponentType componentType() {
        return PvPToggleComponent.getComponentType();
    }

    @Override
    public void onComponentAdded(
            @Nonnull Ref ref,
            @Nonnull PvPToggleComponent component,
            @Nonnull Store store,
            @Nonnull CommandBuffer commandBuffer
    ) {
        triggerNameplateUpdate(ref, component, store, commandBuffer);
    }

    @Override
    public void onComponentSet(
            @Nonnull Ref ref,
            PvPToggleComponent oldComponent,
            @Nonnull PvPToggleComponent newComponent,
            @Nonnull Store store,
            @Nonnull CommandBuffer commandBuffer
    ) {
        if (oldComponent == null || oldComponent.isPvPEnabled() != newComponent.isPvPEnabled()) {
            triggerNameplateUpdate(ref, newComponent, store, commandBuffer);
        }
    }

    @Override
    public void onComponentRemoved(
            @Nonnull Ref ref,
            @Nonnull PvPToggleComponent component,
            @Nonnull Store store,
            @Nonnull CommandBuffer commandBuffer
    ) {
        triggerNameplateUpdate(ref, null, store, commandBuffer);
    }

    private void triggerNameplateUpdate(
            @Nonnull Ref ref,
            PvPToggleComponent pvpComponent,
            @Nonnull Store store,
            @Nonnull CommandBuffer commandBuffer
    ) {
        if (!PvPTogglePlugin.CONFIG.get().isPvPIndicatorEnabled()) {
            return;
        }

        Nameplate nameplate = (Nameplate) commandBuffer.getComponent(ref, Nameplate.getComponentType());
        if (nameplate == null) {
            return;
        }

        boolean pvpEnabled = pvpComponent != null ? pvpComponent.isPvPEnabled() : PvPTogglePlugin.CONFIG.get().isDefaultPvPEnabled();

        PvPToggleConfig config = PvPTogglePlugin.CONFIG.get();
        String prefix = config.getPvPIndicatorPrefix();
        String suffix = config.getPvPIndicatorSuffix();
        String currentText = nameplate.getText();

        // Strip existing indicators to get base name
        String baseName = currentText;
        if (!prefix.isEmpty() && baseName.startsWith(prefix)) {
            baseName = baseName.substring(prefix.length());
        }
        if (!suffix.isEmpty() && baseName.endsWith(suffix)) {
            baseName = baseName.substring(0, baseName.length() - suffix.length());
        }

        // Apply or remove indicators
        String expectedText = pvpEnabled ? (prefix + baseName + suffix) : baseName;

        if (!currentText.equals(expectedText)) {
            nameplate.setText(expectedText);
        }
    }
}
