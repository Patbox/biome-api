package eu.pb4.biomeapi.mixin;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.impl.duck.ExtendedMultiNoiseBiomeSource;
import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(MultiNoiseBiomeSource.Preset.class)
public abstract class PresetMixin {
    @Unique
    private static final ThreadLocal<Registry<Biome>> biomeApi_tempRegistry = new ThreadLocal<>();

    @Inject(method = "method_31088", at = @At("HEAD"))
    private static void biomeApi_catchRegistry(Registry<Biome> registry, CallbackInfoReturnable<MultiNoiseUtil.Entries> cir) {
        biomeApi_tempRegistry.set(registry);
    }

    @ModifyArg(method = "method_31088", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/MultiNoiseUtil$Entries;<init>(Ljava/util/List;)V"))
    private static List<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> biomeApi_addNetherBiomes(List<Pair<MultiNoiseUtil.NoiseHypercube, Supplier<Biome>>> vanillaBiomes) {
        var list = new ArrayList<>(vanillaBiomes);

        var registry = biomeApi_tempRegistry.get();
        biomeApi_tempRegistry.set(null);

        WorldInfoImpl.NETHER.invokeNoise((c) -> list.add(c.mapSecond(key -> (Supplier) () -> registry.get(key))));

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