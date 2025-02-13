package com.leclowndu93150.nether_skyboxes;

import com.aetherteam.aether.data.resources.registries.AetherDimensions;
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
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.slf4j.Logger;

@Mod(NetherSkyboxes.MODID)
public class NetherSkyboxes {
    public static final String MODID = "nether_skyboxes";
    private static final Logger LOGGER = LogUtils.getLogger();


    public NetherSkyboxes() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, com.leclowndu93150.nether_skyboxes.ModConfig.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new CustomSkyboxRenderer());
        MinecraftForge.EVENT_BUS.register(new FogRenderer());
        MinecraftForge.EVENT_BUS.register(new CustomSkyboxRenderer());
    }

    public static class CustomSkyboxRenderer {
        private static final ResourceLocation NETHER_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/nether_sky.png");
        private static final ResourceLocation AETHER_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/aether_sky.png");


        @SubscribeEvent
        public void renderNetherSky(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            PoseStack poseStack = event.getPoseStack();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, com.leclowndu93150.nether_skyboxes.ModConfig.NETHER_SKYBOX_OPACITY.get().floatValue());

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bindForSetup(NETHER_SKY);
            RenderSystem.setShaderTexture(0, NETHER_SKY);
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
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }

        @SubscribeEvent
        public void renderAetherSky(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != AetherDimensions.AETHER_LEVEL) return;

            PoseStack poseStack = event.getPoseStack();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, com.leclowndu93150.nether_skyboxes.ModConfig.AETHER_SKYBOX_OPACITY.get().floatValue());

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bindForSetup(AETHER_SKY);
            RenderSystem.setShaderTexture(0, AETHER_SKY);
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
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    }
}