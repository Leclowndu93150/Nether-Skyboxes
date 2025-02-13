package com.leclowndu93150.nether_skyboxes;

import com.aetherteam.aether.data.resources.registries.AetherDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class FogRenderer {
    @SubscribeEvent
    public void onFogColors(ViewportEvent.ComputeFogColor event) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (level.dimension() == Level.NETHER && !ModConfig.USE_VANILLA_NETHER_FOG.get()) {
            if (ModConfig.USE_NETHER_SKY_FOG.get()) {
                event.setRed(ModConfig.getRedComponent(ModConfig.NETHER_FOG_COLOR));
                event.setGreen(ModConfig.getGreenComponent(ModConfig.NETHER_FOG_COLOR));
                event.setBlue(ModConfig.getBlueComponent(ModConfig.NETHER_FOG_COLOR));
            }
        }

        if (level.dimension() == AetherDimensions.AETHER_LEVEL && ModConfig.USE_CUSTOM_AETHER_FOG.get()) {
            event.setRed(ModConfig.getRedComponent(ModConfig.AETHER_FOG_COLOR));
            event.setGreen(ModConfig.getGreenComponent(ModConfig.AETHER_FOG_COLOR));
            event.setBlue(ModConfig.getBlueComponent(ModConfig.AETHER_FOG_COLOR));
        }
    }

    @SubscribeEvent
    public void onFogRender(ViewportEvent.RenderFog event) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;

        if (level.dimension() == Level.NETHER && !ModConfig.USE_VANILLA_NETHER_FOG.get()) {
            if (ModConfig.DISABLE_NETHER_FOG.get() && !ModConfig.USE_NETHER_SKY_FOG.get()) {
                event.setCanceled(true);
            } else {
                event.setFarPlaneDistance(ModConfig.NETHER_FOG_FAR_PLANE.get().floatValue());
                event.setNearPlaneDistance(ModConfig.NETHER_FOG_NEAR_PLANE.get().floatValue());
            }
        } else if (level.dimension() == AetherDimensions.AETHER_LEVEL && ModConfig.USE_CUSTOM_AETHER_FOG.get()) {
            if (ModConfig.DISABLE_AETHER_FOG.get()) {
                event.setCanceled(true);
            } else {
                event.setFarPlaneDistance(ModConfig.AETHER_FOG_FAR_PLANE.get().floatValue());
                event.setNearPlaneDistance(ModConfig.AETHER_FOG_NEAR_PLANE.get().floatValue());
            }
        }
    }
}