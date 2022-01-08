package eu.pb4.biomeapi.impl.duck;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;

public interface ExtendedNoiseHypercube {
    MultiNoiseUtil.ParameterRange DEFAULT_RANGE = new MultiNoiseUtil.ParameterRange(0, 0);

    MultiNoiseUtil.ParameterRange biomeApi_getModRange();
    MultiNoiseUtil.NoiseHypercube biomeApi_setModRange(MultiNoiseUtil.ParameterRange value);
}
