package eu.pb4.biomeapi.impl.duck;

import eu.pb4.biomeapi.impl.WorldInfoImpl;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import org.jetbrains.annotations.Nullable;

public interface ExtendedBiomeSource {
    @Nullable
    WorldInfoImpl biomeApi_getWorldInfo();
}
