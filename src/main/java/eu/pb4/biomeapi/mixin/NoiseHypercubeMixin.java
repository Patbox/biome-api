package eu.pb4.biomeapi.mixin;

import com.google.common.collect.ImmutableList;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseHypercube;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseValue;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.*;

import java.util.List;

@Mixin(MultiNoiseUtil.NoiseHypercube.class)
public class NoiseHypercubeMixin implements ExtendedNoiseHypercube {

    @Shadow @Final private MultiNoiseUtil.ParameterRange temperature;

    @Shadow @Final private MultiNoiseUtil.ParameterRange humidity;

    @Shadow @Final private MultiNoiseUtil.ParameterRange continentalness;

    @Shadow @Final private MultiNoiseUtil.ParameterRange erosion;

    @Shadow @Final private MultiNoiseUtil.ParameterRange depth;

    @Shadow @Final private MultiNoiseUtil.ParameterRange weirdness;

    @Shadow @Final private long offset;

    @Unique
    private MultiNoiseUtil.ParameterRange biomeApi_modRange = ExtendedNoiseHypercube.DEFAULT_RANGE;


    /**
     * @author Patbox
     * @reason Performance and allowing to add more biomes
     */
    @Overwrite
    public long getSquaredDistance(MultiNoiseUtil.NoiseValuePoint point) {
        return (this.biomeApi_modRange.getDistance(((ExtendedNoiseValue) (Object) point).biomeApi_getModNoise()) == 0 ?
                MathHelper.square(this.temperature.getDistance(point.temperatureNoise()))
                + MathHelper.square(this.humidity.getDistance(point.humidityNoise()))
                + MathHelper.square(this.continentalness.getDistance(point.continentalnessNoise()))
                + MathHelper.square(this.erosion.getDistance(point.erosionNoise()))
                + MathHelper.square(this.depth.getDistance(point.depth()))
                + MathHelper.square(this.weirdness.getDistance(point.weirdnessNoise()))
                + MathHelper.square(this.offset)
                : Long.MAX_VALUE);
    }

    /**
     * @author Patbox
     * @reason Performance and allowing to add more biomes
     */
    @Overwrite
    public List<MultiNoiseUtil.ParameterRange> getParameters() {
        return ImmutableList.of(
                this.temperature,
                this.humidity,
                this.continentalness,
                this.erosion,
                this.depth,
                this.weirdness,
                new MultiNoiseUtil.ParameterRange(this.offset, this.offset),
                this.biomeApi_modRange
        );
    }

    @Override
    public MultiNoiseUtil.ParameterRange biomeApi_getModRange() {
        return this.biomeApi_modRange;
    }

    @Override
    public MultiNoiseUtil.NoiseHypercube biomeApi_setModRange(MultiNoiseUtil.ParameterRange value) {
        this.biomeApi_modRange = value;
        return (MultiNoiseUtil.NoiseHypercube) (Object) this;
    }
}
