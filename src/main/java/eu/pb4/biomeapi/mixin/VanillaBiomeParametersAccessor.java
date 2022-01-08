package eu.pb4.biomeapi.mixin;

import com.mojang.datafixers.util.Pair;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(VanillaBiomeParameters.class)
public interface VanillaBiomeParametersAccessor {
    @Invoker("writeVanillaBiomeParameters")
    void callWriteVanillaBiomeParameters(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters);
}
