package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.duck.ExtendedColumnSampler;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseValue;
import eu.pb4.biomeapi.impl.sampler.ModdedNoiseSampler;
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

    @Inject(method = "<init>", at = @At("TAIL"))
    private void patboxBiomes_init(GenerationShapeConfig config, boolean hasNoiseCaves, long seed, Registry noiseRegistry, ChunkRandom.RandomProvider randomProvider, CallbackInfo ci) {
        this.biomeApi_modNoise = (x, y, z) -> 0;
    }

    @Inject(method = "method_39329", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void patboxBiomes_addNoise(int i, int j, int k, NoiseColumnSampler.class_6747 arg, CallbackInfoReturnable<MultiNoiseUtil.NoiseValuePoint> cir, double x, double y, double z) {
        cir.setReturnValue(((ExtendedNoiseValue) (Object) cir.getReturnValue()).biomeApi_setModNoise((long) (this.biomeApi_sampleModNoise(x, y, z) * 10000)));
    }

    @Override
    public double biomeApi_sampleModNoise(double x, double y, double z) {
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
