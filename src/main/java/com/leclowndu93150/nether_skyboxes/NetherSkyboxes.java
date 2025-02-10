package com.leclowndu93150.nether_skyboxes;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

@Mod(NetherSkyboxes.MODID)
public class NetherSkyboxes {

    public static final String MODID = "nether_skyboxes";
    private static final Logger LOGGER = LogUtils.getLogger();

    public NetherSkyboxes() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new CustomSkyboxRenderer());
    }


    public static class CustomSkyboxRenderer {
        private static final ResourceLocation CUSTOM_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/nether_sky.png");

        @SubscribeEvent
        public void renderSky(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            PoseStack poseStack = event.getPoseStack();

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.depthMask(false);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bindForSetup(CUSTOM_SKY);
            RenderSystem.setShaderTexture(0, CUSTOM_SKY);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);

            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuilder();
            bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

            for (int face = 0; face < 6; face++) {
                poseStack.pushPose();
                if (face == 1) {
                    poseStack.mulPose(new Quaternionf().rotateX((float)Math.toRadians(90.0F)));
                } else if (face == 2) {
                    poseStack.mulPose(new Quaternionf().rotateX((float)Math.toRadians(-90.0F)));
                    poseStack.mulPose(new Quaternionf().rotateY((float)Math.toRadians(180.0F)));
                } else if (face == 3) {
                    poseStack.mulPose(new Quaternionf().rotateX((float)Math.toRadians(180.0F)));
                } else if (face == 4) {
                    poseStack.mulPose(new Quaternionf().rotateZ((float)Math.toRadians(90.0F)));
                    poseStack.mulPose(new Quaternionf().rotateY((float)Math.toRadians(-90.0F)));
                } else if (face == 5) {
                    poseStack.mulPose(new Quaternionf().rotateZ((float)Math.toRadians(-90.0F)));
                    poseStack.mulPose(new Quaternionf().rotateY((float)Math.toRadians(90.0F)));
                }

                Matrix4f matrix4f = poseStack.last().pose();
                UVRange tex = Utils.TEXTURE_FACES[face];
                bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, -100.0F).uv(tex.getMinU(), tex.getMinV()).endVertex();
                bufferBuilder.vertex(matrix4f, -100.0F, -100.0F, 100.0F).uv(tex.getMinU(), tex.getMaxV()).endVertex();
                bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, 100.0F).uv(tex.getMaxU(), tex.getMaxV()).endVertex();
                bufferBuilder.vertex(matrix4f, 100.0F, -100.0F, -100.0F).uv(tex.getMaxU(), tex.getMinV()).endVertex();

                poseStack.popPose();
            }

            BufferUploader.drawWithShader(bufferBuilder.end());
            RenderSystem.depthMask(true);
            RenderSystem.disableBlend();
        }
    }

}
