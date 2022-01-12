package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.duck.ExtendedColumnSampler;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseValue;
import eu.pb4.biomeapi.impl.sampler.ModdedNoiseSampler;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.gen.NoiseColumnSampler;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.random.ChunkRandom;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(NoiseColumnSampler.class)
public class NoiseColumnSamplerMixin implements ExtendedColumnSampler {
    @Unique
    private ModdedNoiseSampler biomeApi_modNoise;

    @Unique
    private final Long2ObjectLinkedOpenHashMap<double[]> biomeApi_cache = new Long2ObjectLinkedOpenHashMap<>(16, 0.25f);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void patboxBiomes_init(GenerationShapeConfig config, boolean hasNoiseCaves, long seed, Registry noiseRegistry, ChunkRandom.RandomProvider randomProvider, CallbackInfo ci) {
        this.biomeApi_modNoise = ModdedNoiseSampler.noop();
    }

    @Inject(method = "method_39329", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void patboxBiomes_addNoise(int i, int j, int k, NoiseColumnSampler.class_6747 arg, CallbackInfoReturnable<MultiNoiseUtil.NoiseValuePoint> cir, double x, double y, double z) {
        var modNoise = this.biomeApi_sampleCached(ChunkPos.toLong(i, k), x, y, z);
        cir.setReturnValue(((ExtendedNoiseValue) (Object) cir.getReturnValue()).biomeApi_setModNoise((long) (modNoise[0] * 10000), (long) (modNoise[1] * 10000)));
    }


    private double[] biomeApi_sampleCached(long realPos, double x, double y, double z) {
        synchronized(this.biomeApi_cache) {
            var value = this.biomeApi_cache.get(realPos);
            if (value != null) {
                return value;
            } else {
                var newValue = this.biomeApi_modNoise.sample(x, y, z);
                this.biomeApi_cache.put(realPos, newValue);
                if (this.biomeApi_cache.size() > 1024) {
                    for(int k = 0; k < 1024 / 16; ++k) {
                        this.biomeApi_cache.removeFirst();
                    }
                }

                return newValue;
            }
        }
    }

    @Override
    public double[] biomeApi_sampleModNoise(double x, double y, double z) {
        return this.biomeApi_modNoise.sample(x, y, z);
    }

    @Override
    public ModdedNoiseSampler biomeApi_getSampler() {
        return this.biomeApi_modNoise;
    }

    @Override
    public void biomeApi_setSampler(ModdedNoiseSampler sampler) {
        this.biomeApi_modNoise = sampler;
    }
}
