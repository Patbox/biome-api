package eu.pb4.biomeapi.impl;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.api.BiomeNoiseBuilder;
import eu.pb4.biomeapi.mixin.VanillaBiomeParametersAccessor;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.biome.source.util.VanillaBiomeParameters;

import java.util.*;
import java.util.function.Consumer;

public final class WorldInfoImpl {
    public static final int NOISE_COUNT = 8;
    public static final float FREQUENCY = 0.01f;

    public static final WorldInfoImpl OVERWORLD = new WorldInfoImpl();
    public static final WorldInfoImpl NETHER = new WorldInfoImpl();
    public static final WorldInfoImpl THE_END = new WorldInfoImpl();

    private final Map<String, List<Consumer<BiomeNoiseBuilder>>> biomeAdders = new HashMap<>();
    private final Map<RegistryKey<Biome>, List<MultiNoiseUtil.NoiseHypercube>> vanillaBiomeRef = new HashMap<>();
    private static boolean lock;

    public void registerNoise(String namespace, Consumer<BiomeNoiseBuilder> callback) {
        this.biomeAdders.computeIfAbsent(namespace, (n) -> new ArrayList<>()).add(callback);
    }

    public int getBound() {
        return this.biomeAdders.size() + 1;
    }

    public void invokeNoise(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        if (lock) {
            return;
        }

        var list = new ArrayList<>(this.biomeAdders.entrySet());
        list.sort(Comparator.comparing((entry) -> entry.getKey()));

        int offset = 1;
        for (var pair : list) {
            var biome = new BiomeNoiseBuilderImpl(parameters, offset, this);
            for (var callback : pair.getValue()) {
                callback.accept(biome);
            }
            offset++;
        }
    }

    public void cloneLayout(BiomeNoiseBuilder builder, RegistryKey<Biome> vanilla, RegistryKey<Biome> custom) {
        var list = vanillaBiomeRef.get(vanilla);

        if (list != null) {
            for (var entry : list) {
                builder.addBiome(entry.temperature(), entry.humidity(), entry.continentalness(), entry.erosion(), entry.depth(), entry.weirdness(), custom);
            }
        }
    }

    public void fillLayout(BiomeNoiseBuilder builder) {
        for (var entry : this.vanillaBiomeRef.entrySet()) {
            for (var list : entry.getValue()) {
                builder.addBiome(list.temperature(), list.humidity(), list.continentalness(), list.erosion(), list.depth(), list.weirdness(), entry.getKey());
            }
        }
    }

    static {
        lock = true;

        ((VanillaBiomeParametersAccessor) (Object) new VanillaBiomeParameters()).callWriteVanillaBiomeParameters((pair) -> {
            OVERWORLD.vanillaBiomeRef.computeIfAbsent(pair.getSecond(), (k) -> new ArrayList<>()).add(pair.getFirst());
        });

        NETHER.vanillaBiomeRef.put(
                BiomeKeys.NETHER_WASTES,
                List.of(
                        MultiNoiseUtil.createNoiseHypercube(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                )
        );

        NETHER.vanillaBiomeRef.put(
                BiomeKeys.SOUL_SAND_VALLEY,
                List.of(
                        MultiNoiseUtil.createNoiseHypercube(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                )
        );

        NETHER.vanillaBiomeRef.put(
                BiomeKeys.CRIMSON_FOREST,
                List.of(
                        MultiNoiseUtil.createNoiseHypercube(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)
                )
        );

        NETHER.vanillaBiomeRef.put(
                BiomeKeys.WARPED_FOREST,
                List.of(
                        MultiNoiseUtil.createNoiseHypercube(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F)
                )
        );

        NETHER.vanillaBiomeRef.put(
                BiomeKeys.BASALT_DELTAS,
                List.of(
                        MultiNoiseUtil.createNoiseHypercube(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F)
                )
        );

        lock = false;
    }
}
