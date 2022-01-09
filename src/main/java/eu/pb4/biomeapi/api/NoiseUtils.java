package eu.pb4.biomeapi.api;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;


public final class NoiseUtils {
    public static final MultiNoiseUtil.ParameterRange DEFAULT_PARAMETER = MultiNoiseUtil.ParameterRange.of(-1.0F, 1.0F);
    public static final MultiNoiseUtil.ParameterRange[] TEMPERATURE_PARAMETERS = new MultiNoiseUtil.ParameterRange[]{MultiNoiseUtil.ParameterRange.of(-1.0F, -0.45F), MultiNoiseUtil.ParameterRange.of(-0.45F, -0.15F), MultiNoiseUtil.ParameterRange.of(-0.15F, 0.2F), MultiNoiseUtil.ParameterRange.of(0.2F, 0.55F), MultiNoiseUtil.ParameterRange.of(0.55F, 1.0F)};
    public static final MultiNoiseUtil.ParameterRange[] HUMIDITY_PARAMETERS = new MultiNoiseUtil.ParameterRange[]{MultiNoiseUtil.ParameterRange.of(-1.0F, -0.35F), MultiNoiseUtil.ParameterRange.of(-0.35F, -0.1F), MultiNoiseUtil.ParameterRange.of(-0.1F, 0.1F), MultiNoiseUtil.ParameterRange.of(0.1F, 0.3F), MultiNoiseUtil.ParameterRange.of(0.3F, 1.0F)};
    public static final MultiNoiseUtil.ParameterRange[] EROSION_PARAMETERS = new MultiNoiseUtil.ParameterRange[]{MultiNoiseUtil.ParameterRange.of(-1.0F, -0.78F), MultiNoiseUtil.ParameterRange.of(-0.78F, -0.375F), MultiNoiseUtil.ParameterRange.of(-0.375F, -0.2225F), MultiNoiseUtil.ParameterRange.of(-0.2225F, 0.05F), MultiNoiseUtil.ParameterRange.of(0.05F, 0.45F), MultiNoiseUtil.ParameterRange.of(0.45F, 0.55F), MultiNoiseUtil.ParameterRange.of(0.55F, 1.0F)};
    public static final MultiNoiseUtil.ParameterRange FROZEN_TEMPERATURE = TEMPERATURE_PARAMETERS[0];
    public static final MultiNoiseUtil.ParameterRange NON_FROZEN_TEMPERATURE_PARAMETERS = MultiNoiseUtil.ParameterRange.combine(TEMPERATURE_PARAMETERS[1], TEMPERATURE_PARAMETERS[4]);
    public static final MultiNoiseUtil.ParameterRange MUSHROOM_FIELDS_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-1.2F, -1.05F);
    public static final MultiNoiseUtil.ParameterRange DEEP_OCEAN_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-1.05F, -0.455F);
    public static final MultiNoiseUtil.ParameterRange OCEAN_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-0.455F, -0.19F);
    public static final MultiNoiseUtil.ParameterRange SHORE_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-0.19F, -0.11F);
    public static final MultiNoiseUtil.ParameterRange RIVER_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-0.11F, 0.55F);
    public static final MultiNoiseUtil.ParameterRange NEAR_INLAND_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(-0.11F, 0.03F);
    public static final MultiNoiseUtil.ParameterRange MID_INLAND_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(0.03F, 0.3F);
    public static final MultiNoiseUtil.ParameterRange FAR_INLAND_CONTINENTALNESS = MultiNoiseUtil.ParameterRange.of(0.3F, 1.0F);

    private NoiseUtils() {}

    public static MultiNoiseUtil.ParameterRange range(float min, float max) {
        return MultiNoiseUtil.ParameterRange.of(Math.min(min, max), Math.max(min, max));
    }

    public static MultiNoiseUtil.ParameterRange point(float value) {
        return MultiNoiseUtil.ParameterRange.of(value);
    }
}
