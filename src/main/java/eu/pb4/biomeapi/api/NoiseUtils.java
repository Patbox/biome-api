package eu.pb4.biomeapi.api;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public final class NoiseUtils {
    private NoiseUtils() {}

    public static MultiNoiseUtil.ParameterRange range(float min, float max) {
        return MultiNoiseUtil.ParameterRange.of(Math.min(min, max), Math.max(min, max));
    }

    public static MultiNoiseUtil.ParameterRange point(float value) {
        return MultiNoiseUtil.ParameterRange.of(value);
    }
}
