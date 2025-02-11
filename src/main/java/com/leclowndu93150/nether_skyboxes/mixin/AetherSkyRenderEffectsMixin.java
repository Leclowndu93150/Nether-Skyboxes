package com.leclowndu93150.nether_skyboxes.mixin;

import com.aetherteam.aether.client.renderer.level.AetherSkyRenderEffects;
import com.leclowndu93150.nether_skyboxes.NetherSkyboxes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AetherSkyRenderEffects.class)
public class AetherSkyRenderEffectsMixin {
    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelSkyRendering(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, Camera camera, Matrix4f projectionMatrix, boolean isFoggy, Runnable setupFog, CallbackInfoReturnable<Boolean> cir) {
        if (NetherSkyboxes.Config.DISABLE_AETHER_SKY.get()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelCloudRendering(ClientLevel level, int ticks, float partialTick, PoseStack poseStack, double camX, double camY, double camZ, Matrix4f projectionMatrix, CallbackInfoReturnable<Boolean> cir) {
        if (NetherSkyboxes.Config.DISABLE_AETHER_SKY.get()) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelSkyColor(ClientLevel level, Vec3 pos, float partialTick, CallbackInfoReturnable<Vec3> cir) {
        cir.setReturnValue(Vec3.ZERO);
        cir.cancel();
    }

    @Inject(method = "getCloudColor", at = @At("HEAD"), cancellable = true, remap = false)
    private void cancelCloudColor(ClientLevel level, float partialTick, CallbackInfoReturnable<Vec3> cir) {
        cir.setReturnValue(Vec3.ZERO);
        cir.cancel();
    }
}
