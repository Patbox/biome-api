package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.duck.ExtendedNoiseValue;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.*;

@Mixin(MultiNoiseUtil.NoiseValuePoint.class)
public class NoiseValuePointMixin implements ExtendedNoiseValue {

    @Shadow @Final private long temperatureNoise;
    @Shadow @Final private long humidityNoise;
    @Shadow @Final private long continentalnessNoise;
    @Shadow @Final private long erosionNoise;
    @Shadow @Final private long depth;
    @Shadow @Final private long weirdnessNoise;
    @Unique
    private long biomeApi_modNoise;

    @Override
    public long biomeApi_getModNoise() {
        return this.biomeApi_modNoise;
    }

    @Override
    public MultiNoiseUtil.NoiseValuePoint biomeApi_setModNoise(long modNoise) {
        this.biomeApi_modNoise = modNoise;
        return (MultiNoiseUtil.NoiseValuePoint) (Object) this;
    }

    /**
     * @author Patbox
     * @reason Performance and allowing to add more biomes
     */
    @Overwrite
    public long[] getNoiseValueList() {
        return new long[]{this.temperatureNoise, this.humidityNoise, this.continentalnessNoise, this.erosionNoise, this.depth, this.weirdnessNoise, 0L, this.biomeApi_modNoise};
    }
}
