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
    private MultiNoiseUtil.ParameterRange biomeApi_modRegion = ExtendedNoiseHypercube.DEFAULT_RANGE;
    @Unique
    private MultiNoiseUtil.ParameterRange biomeApi_regionSide = ExtendedNoiseHypercube.DEFAULT_RANGE;


    /**
     * @author Patbox
     * @reason Performance and allowing to add more biomes
     */
    @Overwrite
    public long getSquaredDistance(MultiNoiseUtil.NoiseValuePoint point) {
        return (this.biomeApi_modRegion.getDistance(((ExtendedNoiseValue) (Object) point).biomeApi_getModRegion()) == 0 ?
                MathHelper.square(this.temperature.getDistance(point.temperatureNoise()))
                + MathHelper.square(this.humidity.getDistance(point.humidityNoise()))
                + MathHelper.square(this.continentalness.getDistance(point.continentalnessNoise()))
                + MathHelper.square(this.erosion.getDistance(point.erosionNoise()))
                + MathHelper.square(this.depth.getDistance(point.depth()))
                + MathHelper.square(this.weirdness.getDistance(point.weirdnessNoise()))
                + MathHelper.square(this.offset)
                + MathHelper.square(this.biomeApi_regionSide.getDistance(((ExtendedNoiseValue) (Object) point).biomeApi_getRegionSide()))
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
                this.biomeApi_modRegion,
                this.biomeApi_regionSide
        );
    }

    @Override
    public MultiNoiseUtil.ParameterRange biomeApi_getModRegion() {
        return this.biomeApi_modRegion;
    }

    @Override
    public MultiNoiseUtil.ParameterRange biomeApi_getRegionSide() {
        return this.biomeApi_regionSide;
    }

    @Override
    public MultiNoiseUtil.NoiseHypercube biomeApi_setModRange(MultiNoiseUtil.ParameterRange region, MultiNoiseUtil.ParameterRange side) {
        this.biomeApi_modRegion = region;
        this.biomeApi_regionSide = side;
        return (MultiNoiseUtil.NoiseHypercube) (Object) this;
    }
}
