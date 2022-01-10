package eu.pb4.biomeapi.impl.duck;

import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ExtendedNoiseHypercube {
    MultiNoiseUtil.ParameterRange DEFAULT_RANGE = new MultiNoiseUtil.ParameterRange(0, 0);

    MultiNoiseUtil.ParameterRange biomeApi_getModRegion();
    MultiNoiseUtil.ParameterRange biomeApi_getRegionSide();
    MultiNoiseUtil.NoiseHypercube biomeApi_setModRange(MultiNoiseUtil.ParameterRange region, MultiNoiseUtil.ParameterRange side);
}
