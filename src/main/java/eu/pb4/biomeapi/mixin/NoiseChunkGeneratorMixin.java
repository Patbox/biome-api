package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.WorldInfoImpl;
import eu.pb4.biomeapi.impl.duck.ExtendedColumnSampler;
import eu.pb4.biomeapi.impl.duck.ExtendedMultiNoiseBiomeSource;
import eu.pb4.biomeapi.impl.sampler.VoronoiNoiseSampler;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.NoiseChunkGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(NoiseChunkGenerator.class)
public abstract class NoiseChunkGeneratorMixin {
    @Shadow @Final private NoiseColumnSampler noiseColumnSampler;

    @Inject(method = "<init>(Lnet/minecraft/util/registry/Registry;Lnet/minecraft/world/biome/source/BiomeSource;Lnet/minecraft/world/biome/source/BiomeSource;JLjava/util/function/Supplier;)V", at = @At("TAIL"))
    private void biomeApi_setType(Registry noiseRegistry, BiomeSource populationSource, BiomeSource biomeSource, long seed, Supplier settings, CallbackInfo ci) {
        if (biomeSource instanceof ExtendedMultiNoiseBiomeSource source && source.biomeApi_getType() != null) {
            ((ExtendedColumnSampler) this.noiseColumnSampler).biomeApi_setSampler(new VoronoiNoiseSampler(seed, WorldInfoImpl.FREQUENCY, source.biomeApi_getType().getBound(), source.biomeApi_getType().getWeights()));
        }
    }
}
