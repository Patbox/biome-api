package eu.pb4.biomeapi.mixin;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public class VanillaBiomeParametersMixin {
    @Inject(method = "writeVanillaBiomeParameters", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/util/VanillaBiomeParameters;writeOceanBiomes(Ljava/util/function/Consumer;)V", shift = At.Shift.BEFORE))
    private void biomeApi_injectBiomes(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters, CallbackInfo ci) {
        WorldInfoImpl.OVERWORLD.invokeNoise(parameters);
    }
}
