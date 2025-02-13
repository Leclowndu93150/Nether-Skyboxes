package com.leclowndu93150.nether_skyboxes;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    // Nether
    public static final ForgeConfigSpec.DoubleValue NETHER_SKYBOX_OPACITY;
    public static final ForgeConfigSpec.BooleanValue USE_VANILLA_NETHER_FOG;
    public static final ForgeConfigSpec.BooleanValue USE_NETHER_SKY_FOG;
    public static final ForgeConfigSpec.BooleanValue DISABLE_NETHER_FOG;
    public static final ForgeConfigSpec.DoubleValue NETHER_FOG_OPACITY;
    public static final ForgeConfigSpec.DoubleValue NETHER_FOG_NEAR_PLANE;
    public static final ForgeConfigSpec.DoubleValue NETHER_FOG_FAR_PLANE;
    public static final ForgeConfigSpec.ConfigValue<String> NETHER_FOG_COLOR;

    // Aether
    public static final ForgeConfigSpec.DoubleValue AETHER_SKYBOX_OPACITY;
    public static final ForgeConfigSpec.BooleanValue DISABLE_AETHER_SKY;
    public static final ForgeConfigSpec.BooleanValue DISABLE_AETHER_FOG;
    public static final ForgeConfigSpec.BooleanValue USE_CUSTOM_AETHER_FOG;
    public static final ForgeConfigSpec.DoubleValue AETHER_FOG_OPACITY;
    public static final ForgeConfigSpec.DoubleValue AETHER_FOG_NEAR_PLANE;
    public static final ForgeConfigSpec.DoubleValue AETHER_FOG_FAR_PLANE;
    public static final ForgeConfigSpec.ConfigValue<String> AETHER_FOG_COLOR;

    static {
        BUILDER.push("Nether");
        NETHER_SKYBOX_OPACITY = BUILDER.defineInRange("skyboxOpacity", 0.3D, 0.0D, 1.0D);
        USE_VANILLA_NETHER_FOG = BUILDER.define("useVanillaFog", false);
        USE_NETHER_SKY_FOG = BUILDER.define("useSkyFog", true);
        DISABLE_NETHER_FOG = BUILDER.define("disableFog", false);
        NETHER_FOG_OPACITY = BUILDER.defineInRange("fogOpacity", 0.2D, 0.0D, 1.0D);
        NETHER_FOG_NEAR_PLANE = BUILDER.defineInRange("fogNearPlane", 8.0D, 0.0D, 160.0D);
        NETHER_FOG_FAR_PLANE = BUILDER.defineInRange("fogFarPlane", 80.0D, 0.0D, 160.0D);
        NETHER_FOG_COLOR = BUILDER.define("fogColor", "#6AC7DE");
        BUILDER.pop();

        BUILDER.push("Aether");
        AETHER_SKYBOX_OPACITY = BUILDER.defineInRange("skyboxOpacity", 0.3D, 0.0D, 1.0D);
        DISABLE_AETHER_SKY = BUILDER.define("disableSky", false);
        DISABLE_AETHER_FOG = BUILDER.define("disableFog", false);
        USE_CUSTOM_AETHER_FOG = BUILDER.define("useCustomFog", false);
        AETHER_FOG_OPACITY = BUILDER.defineInRange("fogOpacity", 0.2D, 0.0D, 1.0D);
        AETHER_FOG_NEAR_PLANE = BUILDER.defineInRange("fogNearPlane", 8.0D, 0.0D, 160.0D);
        AETHER_FOG_FAR_PLANE = BUILDER.defineInRange("fogFarPlane", 80.0D, 0.0D, 160.0D);
        AETHER_FOG_COLOR = BUILDER.define("fogColor", "#CCCCFF");
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static float getRedComponent(ForgeConfigSpec.ConfigValue<String> color) {
        return Integer.parseInt(color.get().substring(1, 3), 16) / 255.0F;
    }

    public static float getGreenComponent(ForgeConfigSpec.ConfigValue<String> color) {
        return Integer.parseInt(color.get().substring(3, 5), 16) / 255.0F;
    }

    public static float getBlueComponent(ForgeConfigSpec.ConfigValue<String> color) {
        return Integer.parseInt(color.get().substring(5, 7), 16) / 255.0F;
    }
}