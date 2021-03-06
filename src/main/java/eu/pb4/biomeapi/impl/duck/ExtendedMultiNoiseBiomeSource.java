package eu.pb4.biomeapi.impl.duck;

import eu.pb4.biomeapi.impl.WorldInfoImpl;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface ExtendedMultiNoiseBiomeSource {
    void biomeApi_setType(WorldInfoImpl dimension);
    WorldInfoImpl biomeApi_getType();
}
