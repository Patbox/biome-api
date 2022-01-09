package eu.pb4.biomeapi.testmod;

import eu.pb4.biomeapi.api.NoiseUtils;
import eu.pb4.biomeapi.api.BiomeParameters;
import eu.pb4.biomeapi.api.MaterialRulesInitializer;
import net.minecraft.block.Blocks;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.OverworldBiomeCreator;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.MultiNoiseBiomeSource;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;
import net.minecraft.world.gen.surfacebuilder.VanillaSurfaceRules;

import java.util.List;
import java.util.function.Consumer;

public class ModInitializer implements net.fabricmc.api.ModInitializer, MaterialRulesInitializer {
    public static final RegistryKey<Biome> key = RegistryKey.of(Registry.BIOME_KEY, new Identifier("test_mod", "biome"));
    public static final RegistryKey<Biome> key2 = RegistryKey.of(Registry.BIOME_KEY, new Identifier("test_mod", "biome2"));
    @Override
    public void onInitialize() {


        // Adds custom biome, fill rest with vanilla
        BiomeParameters.registerOverworld("testmod:vanila_filled", 2, (builder) -> {
            builder.addSimilar(BiomeKeys.OCEAN, key);

            builder.addSimilar(BiomeKeys.FOREST, key);

            builder.addBiome(0.3f, 0f, 0.6f, 0f, 0f, 0.2f, key);

            builder.addBiome(
                    NoiseUtils.range(0f, 0.3f),
                    NoiseUtils.point(0f),
                    NoiseUtils.range(0.3f, 0.6f),
                    NoiseUtils.range(0f, 0.1f),
                    NoiseUtils.point(0f),
                    NoiseUtils.point(0.2f),
                    key
            );

            builder.fillVanilla();
        });

        // Entire region uses custom biome
        BiomeParameters.registerOverworld("testmod:single_biome", 1, (builder) -> {
            builder.addBiome(0.0f, 0f, 0.0f, 0f, 0f, 0.0f, key2);
        });

        // Entire region uses custom biome
        BiomeParameters.registerOverworld("testmod:multi_biome", 3, (builder) -> {
            builder.addBiome(0.0f, 0f, 0.0f, 0f, 0f, 0.0f, BiomeKeys.NETHER_WASTES);
            builder.addBiome(0.0F, -0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, BiomeKeys.SOUL_SAND_VALLEY);
            builder.addBiome(0.4F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, BiomeKeys.CRIMSON_FOREST);
            builder.addBiome(0.0F, 0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.375F, BiomeKeys.WARPED_FOREST);
            builder.addBiome(-0.5F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.175F, BiomeKeys.BASALT_DELTAS);
        });

        Registry.register(BuiltinRegistries.BIOME, key, OverworldBiomeCreator.createTheVoid());
        Registry.register(BuiltinRegistries.BIOME, key2, OverworldBiomeCreator.createFrozenOcean(true));
    }


    @Override
    public void addOverworldRules(boolean surface, boolean bedrockRoof, boolean bedrockFloor, Consumer<MaterialRules.MaterialRule> consumer) {
        consumer.accept(
                MaterialRules.condition(
                        MaterialRules.biome(key),
                        MaterialRules.condition(
                                MaterialRules.STONE_DEPTH_FLOOR,
                                MaterialRules.condition(
                                        MaterialRules.surface(),
                                        MaterialRules.block(Blocks.OBSIDIAN.getDefaultState())
                                )
                        )
                )
        );
        consumer.accept(
                MaterialRules.condition(
                        MaterialRules.biome(key2),
                        MaterialRules.condition(
                                MaterialRules.STONE_DEPTH_FLOOR,
                                MaterialRules.condition(
                                        MaterialRules.surface(),
                                        MaterialRules.block(Blocks.CYAN_WOOL.getDefaultState())
                                )
                        )
                )
        );

        consumer.accept(MaterialRules.condition(
                MaterialRules.biome(BiomeKeys.NETHER_WASTES, BiomeKeys.SOUL_SAND_VALLEY, BiomeKeys.CRIMSON_FOREST, BiomeKeys.WARPED_FOREST, BiomeKeys.BASALT_DELTAS),
                VanillaSurfaceRules.createNetherSurfaceRule()
                )
        );
    }
}
