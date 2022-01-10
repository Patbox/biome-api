package eu.pb4.biomeapi.impl;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.api.BiomeNoiseBuilder;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseHypercube;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Consumer;

@ApiStatus.Internal
public class BiomeNoiseBuilderImpl implements BiomeNoiseBuilder {
    private final int offset;
    private final Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters;
    private final WorldInfoImpl worldInfo;

    public BiomeNoiseBuilderImpl(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, int offset, WorldInfoImpl worldInfo) {
        this.offset = offset;
        this.parameters = parameters;
        this.worldInfo = worldInfo;
    }

    @Override
    public void addBiome(MultiNoiseUtil.ParameterRange temperature, MultiNoiseUtil.ParameterRange humidity, MultiNoiseUtil.ParameterRange continentalness, MultiNoiseUtil.ParameterRange erosion, MultiNoiseUtil.ParameterRange depth, MultiNoiseUtil.ParameterRange weirdness, MultiNoiseUtil.ParameterRange regionSide, float offset, RegistryKey<Biome> biome) {
        this.parameters.accept(Pair.of(
                ((ExtendedNoiseHypercube) (Object) MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, depth, weirdness, offset))
                        .biomeApi_setModRange(MultiNoiseUtil.ParameterRange.of(this.offset), MultiNoiseUtil.ParameterRange.of(this.offset)),
                biome
        ));
    }

    @Override
    public void addBiome(float temperature, float humidity, float continentalness, float erosion, float depth, float weirdness, float regionSide, float offset, RegistryKey<Biome> biome) {
        this.parameters.accept(Pair.of(
                ((ExtendedNoiseHypercube) (Object) MultiNoiseUtil.createNoiseHypercube(temperature, humidity, continentalness, erosion, depth, weirdness, offset))
                        .biomeApi_setModRange(MultiNoiseUtil.ParameterRange.of(this.offset), MultiNoiseUtil.ParameterRange.of(regionSide)),
                biome
        ));
    }

    @Override
    public void addSimilar(RegistryKey<Biome> vanillaBiome, RegistryKey<Biome> biome) {
        this.worldInfo.cloneLayout(this, vanillaBiome, biome);
    }

    @Override
    public void fillVanilla() {
        this.worldInfo.fillLayout(this);
    }
}
