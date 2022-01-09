package eu.pb4.biomeapi.mixin;

import eu.pb4.biomeapi.impl.duck.ExtendedColumnSampler;
import eu.pb4.biomeapi.impl.duck.ExtendedMultiNoiseBiomeSource;
import eu.pb4.biomeapi.impl.duck.ExtendedNoiseValue;
import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;

@Mixin(MultiNoiseBiomeSource.class)
public abstract class MultiNoiseBiomeSourceMixin implements ExtendedMultiNoiseBiomeSource {
    @Unique
    private WorldInfoImpl biomeApi_worldInfo = null;

    @Inject(method = "<init>(Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$Entries;Ljava/util/Optional;)V", at = @At("TAIL"))
    private void biomeApi_setType(MultiNoiseUtil.Entries biomeEntries, Optional<MultiNoiseBiomeSource.Instance> instance, CallbackInfo ci) {
        if (instance.isPresent()) {
            var preset = instance.get().preset();
            if (preset == MultiNoiseBiomeSource.Preset.OVERWORLD) {
                this.biomeApi_worldInfo = WorldInfoImpl.OVERWORLD;
            } else if (preset == MultiNoiseBiomeSource.Preset.NETHER) {
                this.biomeApi_worldInfo = WorldInfoImpl.NETHER;
            }
        }
    }

    @Inject(method = "addDebugInfo", at = @At(value = "TAIL"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void biomeApi_catchSeed(List<String> info, BlockPos pos, MultiNoiseUtil.MultiNoiseSampler noiseSampler, CallbackInfo ci, int x, int y, int z, MultiNoiseUtil.NoiseValuePoint noiseValuePoint) {
        if (noiseSampler instanceof ExtendedColumnSampler ex) {
            var id = (int) MultiNoiseUtil.method_38666(((ExtendedNoiseValue) (Object) noiseValuePoint).biomeApi_getModNoise());
            info.add("[Biome API] Mod Noise: "
                    + id
                    + " ("
                    + (this.biomeApi_worldInfo != null ? this.biomeApi_worldInfo.getById(id) : "<none>")
                    + ") | "
                    + ex.biomeApi_getSampler().sampleUnfiltered(x, y, z));
        }
    }

    @Inject(method = "withSeed", at = @At("RETURN"), cancellable = true)
    private void biomeApi_setInfo(long seed, CallbackInfoReturnable<BiomeSource> cir) {
        ((ExtendedMultiNoiseBiomeSource) cir.getReturnValue()).biomeApi_setType(this.biomeApi_worldInfo);
    }

    @Override
    public void biomeApi_setType(WorldInfoImpl dimension) {
        this.biomeApi_worldInfo = dimension;
    }

    @Override
    public WorldInfoImpl biomeApi_getType() {
        return this.biomeApi_worldInfo;
    }
}
