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
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.ForgeConfigSpec;
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

    public static class Config {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        // Nether Settings
        public static final ForgeConfigSpec.DoubleValue NETHER_SKYBOX_OPACITY;
        public static final ForgeConfigSpec.BooleanValue USE_VANILLA_NETHER_FOG;
        public static final ForgeConfigSpec.BooleanValue USE_NETHER_SKY_FOG;
        public static final ForgeConfigSpec.BooleanValue DISABLE_NETHER_FOG;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_OPACITY;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_RED;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_GREEN;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_BLUE;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_NEAR_PLANE;
        public static final ForgeConfigSpec.DoubleValue NETHER_FOG_FAR_PLANE;

        // Aether Settings
        public static final ForgeConfigSpec.DoubleValue AETHER_SKYBOX_OPACITY;
        public static final ForgeConfigSpec.BooleanValue DISABLE_AETHER_SKY;
        public static final ForgeConfigSpec.BooleanValue DISABLE_AETHER_FOG;
        public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_AETHER_FOG;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_OPACITY;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_RED;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_GREEN;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_BLUE;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_NEAR_PLANE;
        public static final ForgeConfigSpec.DoubleValue AETHER_FOG_FAR_PLANE;

        static {
            BUILDER.push("Nether Settings");
            NETHER_SKYBOX_OPACITY = BUILDER.comment("Nether skybox opacity (0.0-1.0)")
                    .defineInRange("skyboxOpacity", 0.3D, 0.0D, 1.0D);
            USE_VANILLA_NETHER_FOG = BUILDER.comment("Use vanilla nether fog")
                    .define("useVanillaFog", false);
            USE_NETHER_SKY_FOG = BUILDER.comment("Use sky texture color for nether fog")
                    .define("useSkyFog", true);
            DISABLE_NETHER_FOG = BUILDER.comment("Disable nether fog completely")
                    .define("disableFog", false);
            NETHER_FOG_OPACITY = BUILDER.comment("Nether fog opacity (0.0-1.0)")
                    .defineInRange("fogOpacity", 0.2D, 0.0D, 1.0D);
            NETHER_FOG_RED = BUILDER.comment("Nether fog red (0.0-1.0)")
                    .defineInRange("fogRed", 1.0D, 0.0D, 1.0D);
            NETHER_FOG_GREEN = BUILDER.comment("Nether fog green (0.0-1.0)")
                    .defineInRange("fogGreen", 0.3D, 0.0D, 1.0D);
            NETHER_FOG_BLUE = BUILDER.comment("Nether fog blue (0.0-1.0)")
                    .defineInRange("fogBlue", 0.3D, 0.0D, 1.0D);
            NETHER_FOG_NEAR_PLANE = BUILDER.comment("Nether fog near plane distance")
                    .defineInRange("fogNearPlane", 8.0D, 0.0D, 160.0D);
            NETHER_FOG_FAR_PLANE = BUILDER.comment("Nether fog far plane distance")
                    .defineInRange("fogFarPlane", 80.0D, 0.0D, 160.0D);
            BUILDER.pop();

            BUILDER.push("Aether Settings");
            AETHER_SKYBOX_OPACITY = BUILDER.comment("Aether skybox opacity (0.0-1.0)")
                    .defineInRange("skyboxOpacity", 0.3D, 0.0D, 1.0D);
            DISABLE_AETHER_SKY = BUILDER.comment("Disable Aether skybox")
                    .define("disableSky", false);
            DISABLE_AETHER_FOG = BUILDER.comment("Disable Aether fog")
                    .define("disableFog", false);
            USE_CUSTOM_AETHER_FOG = BUILDER.comment("Use custom Aether fog")
                    .define("useCustomFog", false);
            AETHER_FOG_OPACITY = BUILDER.comment("Aether fog opacity (0.0-1.0)")
                    .defineInRange("fogOpacity", 0.2D, 0.0D, 1.0D);
            AETHER_FOG_RED = BUILDER.comment("Aether fog red (0.0-1.0)")
                    .defineInRange("fogRed", 0.8D, 0.0D, 1.0D);
            AETHER_FOG_GREEN = BUILDER.comment("Aether fog green (0.0-1.0)")
                    .defineInRange("fogGreen", 0.8D, 0.0D, 1.0D);
            AETHER_FOG_BLUE = BUILDER.comment("Aether fog blue (0.0-1.0)")
                    .defineInRange("fogBlue", 1.0D, 0.0D, 1.0D);
            AETHER_FOG_NEAR_PLANE = BUILDER.comment("Aether fog near plane distance")
                    .defineInRange("fogNearPlane", 8.0D, 0.0D, 160.0D);
            AETHER_FOG_FAR_PLANE = BUILDER.comment("Aether fog far plane distance")
                    .defineInRange("fogFarPlane", 80.0D, 0.0D, 160.0D);
            BUILDER.pop();

            SPEC = BUILDER.build();
        }
    }

    public NetherSkyboxes() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(new CustomSkyboxRenderer());
    }

    public static class CustomSkyboxRenderer {
        private static final ResourceLocation NETHER_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/nether_sky.png");
        private static final ResourceLocation AETHER_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/aether_sky.png");

        @SubscribeEvent
        public void onFogColors(ViewportEvent.ComputeFogColor event) {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            if (level.dimension() == Level.NETHER) {
                if (Config.DISABLE_NETHER_FOG.get()) {
                    event.setRed(0);
                    event.setGreen(0);
                    event.setBlue(0);
                    return;
                }
                if (!Config.USE_NETHER_SKY_FOG.get() || Config.USE_VANILLA_NETHER_FOG.get()) return;
                float opacity = Config.NETHER_FOG_OPACITY.get().floatValue();
                event.setRed(Config.NETHER_FOG_RED.get().floatValue() * opacity);
                event.setGreen(Config.NETHER_FOG_GREEN.get().floatValue() * opacity);
                event.setBlue(Config.NETHER_FOG_BLUE.get().floatValue() * opacity);
            } else if (level.dimension() == AetherDimensions.AETHER_LEVEL) {
                if (Config.DISABLE_AETHER_FOG.get()) {
                    event.setRed(0);
                    event.setGreen(0);
                    event.setBlue(0);
                    return;
                }
                if (!Config.USE_CUSTOM_AETHER_FOG.get()) return;
                float opacity = Config.AETHER_FOG_OPACITY.get().floatValue();
                event.setRed(Config.AETHER_FOG_RED.get().floatValue() * opacity);
                event.setGreen(Config.AETHER_FOG_GREEN.get().floatValue() * opacity);
                event.setBlue(Config.AETHER_FOG_BLUE.get().floatValue() * opacity);
            }
        }

        @SubscribeEvent
        public void onFogRender(ViewportEvent.RenderFog event) {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;

            if (level.dimension() == Level.NETHER) {
                if (Config.DISABLE_NETHER_FOG.get()) {
                    event.setCanceled(true);
                    return;
                }
                if (Config.USE_VANILLA_NETHER_FOG.get()) return;
                float opacity = Config.NETHER_FOG_OPACITY.get().floatValue();
                event.setFarPlaneDistance(Config.NETHER_FOG_FAR_PLANE.get().floatValue() * (2.0F - opacity));
                event.setNearPlaneDistance(Config.NETHER_FOG_NEAR_PLANE.get().floatValue() * (2.0F - opacity));
            } else if (level.dimension() == AetherDimensions.AETHER_LEVEL) {
                if (Config.DISABLE_AETHER_FOG.get()) {
                    event.setCanceled(true);
                    return;
                }
                if (!Config.USE_CUSTOM_AETHER_FOG.get()) return;
                float opacity = Config.AETHER_FOG_OPACITY.get().floatValue();
                event.setFarPlaneDistance(Config.AETHER_FOG_FAR_PLANE.get().floatValue() * (2.0F - opacity));
                event.setNearPlaneDistance(Config.AETHER_FOG_NEAR_PLANE.get().floatValue() * (2.0F - opacity));
            }
        }

        @SubscribeEvent
        public void renderNetherSky(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            PoseStack poseStack = event.getPoseStack();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Config.NETHER_SKYBOX_OPACITY.get().floatValue());

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
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Config.AETHER_SKYBOX_OPACITY.get().floatValue());

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