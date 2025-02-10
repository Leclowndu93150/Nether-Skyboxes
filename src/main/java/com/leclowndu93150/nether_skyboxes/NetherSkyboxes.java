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
import org.joml.Vector3f;
import org.slf4j.Logger;

@Mod(NetherSkyboxes.MODID)
public class NetherSkyboxes {

    public static final String MODID = "nether_skyboxes";
    private static final Logger LOGGER = LogUtils.getLogger();


    public static class Config {
        public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
        public static final ForgeConfigSpec SPEC;

        public static final ForgeConfigSpec.BooleanValue USE_VANILLA_FOG;
        public static final ForgeConfigSpec.BooleanValue USE_SKY_FOG;
        public static final ForgeConfigSpec.DoubleValue FOG_OPACITY;
        public static final ForgeConfigSpec.DoubleValue FOG_RED;
        public static final ForgeConfigSpec.DoubleValue FOG_GREEN;
        public static final ForgeConfigSpec.DoubleValue FOG_BLUE;
        public static final ForgeConfigSpec.DoubleValue SKYBOX_OPACITY;

        static {
            BUILDER.push("Nether Skybox Settings");

            SKYBOX_OPACITY = BUILDER.comment("Skybox's Alpha value (0.0-1.0)")
                    .defineInRange("skyboxOpacity",0.3D,0.0D,1.0D);

            USE_VANILLA_FOG = BUILDER.comment("Use vanilla nether fog")
                    .define("useVanillaFog", false);

            USE_SKY_FOG = BUILDER.comment("Use sky texture color for fog")
                    .define("useSkyFog", true);

            FOG_OPACITY = BUILDER.comment("Fog opacity (0.0-1.0)")
                    .defineInRange("fogOpacity", 0.2D, 0.0D, 1.0D);

            FOG_RED = BUILDER.comment("Fog red component (0.0-1.0)")
                    .defineInRange("fogRed", 1.0D, 0.0D, 1.0D);

            FOG_GREEN = BUILDER.comment("Fog green component (0.0-1.0)")
                    .defineInRange("fogGreen", 0.3D, 0.0D, 1.0D);

            FOG_BLUE = BUILDER.comment("Fog blue component (0.0-1.0)")
                    .defineInRange("fogBlue", 0.3D, 0.0D, 1.0D);

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
        private static final ResourceLocation CUSTOM_SKY = new ResourceLocation(NetherSkyboxes.MODID, "textures/environment/nether_sky.png");

        @SubscribeEvent
        public void onFogColors(ViewportEvent.ComputeFogColor event) {
            if (!Config.USE_SKY_FOG.get() || Config.USE_VANILLA_FOG.get()) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            float opacity = Config.FOG_OPACITY.get().floatValue();
            event.setRed(Config.FOG_RED.get().floatValue() * opacity);
            event.setGreen(Config.FOG_GREEN.get().floatValue() * opacity);
            event.setBlue(Config.FOG_BLUE.get().floatValue() * opacity);
        }

        @SubscribeEvent
        public void onFogRender(ViewportEvent.RenderFog event) {
            if (Config.USE_VANILLA_FOG.get()) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            float opacity = Config.FOG_OPACITY.get().floatValue();
            event.setFarPlaneDistance(80.0F * (2.0F - opacity));
            event.setNearPlaneDistance(8.0F * (2.0F - opacity));
        }

        @SubscribeEvent
        public void renderSky(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

            Level level = Minecraft.getInstance().level;
            if (level == null || level.dimension() != Level.NETHER) return;

            PoseStack poseStack = event.getPoseStack();

            RenderSystem.depthMask(false);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, Config.SKYBOX_OPACITY.get().floatValue());

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
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
            RenderSystem.depthMask(true);
        }
    }

}
