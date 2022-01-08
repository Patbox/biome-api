package eu.pb4.biomeapi.api;

import eu.pb4.biomeapi.impl.WorldInfoImpl;

import java.util.function.Consumer;

public final class BiomeParameters {
    private BiomeParameters() {}

    public static void registerOverworld(String namespace, Consumer<BiomeNoiseBuilder> callback) {
        WorldInfoImpl.OVERWORLD.registerNoise(namespace, callback);
    }

    public static void registerNether(String namespace, Consumer<BiomeNoiseBuilder> callback) {
        WorldInfoImpl.NETHER.registerNoise(namespace, callback);
    }
}
