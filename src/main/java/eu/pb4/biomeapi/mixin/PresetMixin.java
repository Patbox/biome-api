package eu.pb4.biomeapi.mixin;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.impl.duck.ExtendedMultiNoiseBiomeSource;
import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(MultiNoiseBiomeSource.Preset.class)
public abstract class PresetMixin {
    @ModifyArg(method = "method_31088", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$Entries;<init>(Ljava/util/List;)V"))
    private static List<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> biomeApi_addNetherBiomes(List<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> vanillaBiomes) {
        var list = new ArrayList<>(vanillaBiomes);

        WorldInfoImpl.NETHER.invokeNoise(list::add);

        return list;
    }

    @Inject(method = "getBiomeSource(Lnet/minecraft/util/registry/Registry;Z)Lnet/minecraft/world/biome/source/MultiNoiseBiomeSource;", at = @At("RETURN"), cancellable = true)
    private void biomeApi_setType(Registry<Biome> biomeRegistry, boolean useInstance, CallbackInfoReturnable<MultiNoiseBiomeSource> cir) {
        if ((Object) this == MultiNoiseBiomeSource.Preset.OVERWORLD) {
            ((ExtendedMultiNoiseBiomeSource) cir.getReturnValue()).biomeApi_setType(WorldInfoImpl.OVERWORLD);
        } else if ((Object) this == MultiNoiseBiomeSource.Preset.NETHER) {
            ((ExtendedMultiNoiseBiomeSource) cir.getReturnValue()).biomeApi_setType(WorldInfoImpl.NETHER);
        }
    }
}