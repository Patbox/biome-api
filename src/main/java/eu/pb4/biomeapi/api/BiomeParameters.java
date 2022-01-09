package eu.pb4.biomeapi.api;

import eu.pb4.biomeapi.impl.WorldInfoImpl;

import java.util.function.Consumer;

public final class BiomeParameters {
    private BiomeParameters() {}

    public static void registerOverworld(String regionId, int weight, Consumer<BiomeNoiseBuilder> callback) {
        WorldInfoImpl.OVERWORLD.registerNoise(regionId, weight, callback);
    }

    public static void registerNether(String regionId, int weight, Consumer<BiomeNoiseBuilder> callback) {
        WorldInfoImpl.NETHER.registerNoise(regionId, weight, callback);
    }
}
