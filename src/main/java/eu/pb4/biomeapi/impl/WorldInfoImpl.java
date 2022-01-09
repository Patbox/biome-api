package eu.pb4.biomeapi.impl;

import com.mojang.datafixers.util.Pair;
import eu.pb4.biomeapi.api.BiomeNoiseBuilder;
import eu.pb4.biomeapi.mixin.VanillaBiomeParametersAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
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
    private final Object2IntMap<String> biomeWeights = new Object2IntOpenHashMap<>();
    private final Int2ObjectMap<String> id2Region = new Int2ObjectOpenHashMap<>();
    private final Object2IntMap<String> regionIds = new Object2IntOpenHashMap<>();

    private int currentId = 1;

    private WorldInfoImpl() {
        this.biomeWeights.put("vanilla", 10);
        this.regionIds.put("vanilla", 0);
        this.id2Region.put(0, "vanilla");
    }

    public void registerNoise(String namespace, int weight, Consumer<BiomeNoiseBuilder> callback) {
        this.biomeAdders.computeIfAbsent(namespace, (n) -> new ArrayList<>()).add(callback);
        this.biomeWeights.put(namespace, Math.max(0, Math.max(this.biomeWeights.getInt(namespace), weight)));
        var id = currentId++;
        this.regionIds.put(namespace, id);
        this.id2Region.put(id, namespace);
    }

    public String getById(int id) {
        return this.id2Region.getOrDefault(id, "<undefined>");
    }

    public int getBound() {
        var val = 0;
        for (var i : this.biomeWeights.values()) {
            val += i;
        }
        return val;
    }

    public int[] getWeights() {
        var list = new ArrayList<>(this.id2Region.int2ObjectEntrySet());
        list.sort(Comparator.comparing((entry) -> entry.getIntKey()));
        var weights = new IntArrayList();

        for (var entry : list) {
            for (int i = 0; i < this.biomeWeights.getInt(entry.getValue()); i++) {
                weights.add(entry.getIntKey());
            }
        }

        return weights.toIntArray();
    }

    public void invokeNoise(Consumer<Pair<MultiNoiseUtil.NoiseHypercube, RegistryKey<Biome>>> parameters) {
        if (lock) {
            return;
        }

        var list = new ArrayList<>(this.biomeAdders.entrySet());
        list.sort(Comparator.comparing((entry) -> entry.getKey()));

        for (var pair : list) {
            var biome = new BiomeNoiseBuilderImpl(parameters, this.regionIds.getInt(pair.getKey()), this);
            for (var callback : pair.getValue()) {
                callback.accept(biome);
            }
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
