package eu.pb4.biomeapi.impl.duck;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public interface ExtendedNoiseValue {
    long biomeApi_getModNoise();
    MultiNoiseUtil.NoiseValuePoint biomeApi_setModNoise(long modNoise);
}
