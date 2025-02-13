package com.leclowndu93150.nether_skyboxes.mixin;

import com.aetherteam.aether.client.event.listeners.DimensionClientListener;
import net.minecraftforge.client.event.ViewportEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimensionClientListener.class)
public class DimensionClientListenerMixin {
    @Inject(method = "onRenderFog", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cancelFogRender(ViewportEvent.RenderFog event, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "onRenderFogColor", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cancelFogColor(ViewportEvent.ComputeFogColor event, CallbackInfo ci) {
        ci.cancel();
    }
}
