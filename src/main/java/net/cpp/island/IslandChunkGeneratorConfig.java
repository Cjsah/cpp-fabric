package net.cpp.island;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.FixedBiomeSource;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.feature.StructureFeature;

import java.util.Optional;

public class IslandChunkGeneratorConfig {
    public static final Codec<IslandChunkGeneratorConfig> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((islandChunkGeneratorConfig) ->
                    islandChunkGeneratorConfig.biomeRegistry
            ), Codec.LONG.fieldOf("seed").stable().forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.seed
            )).apply(instance, IslandChunkGeneratorConfig::new)
    );

    private final Registry<Biome> biomeRegistry;
    private final StructuresConfig structuresConfig;
    private final BiomeSource biomeSource;
    private final long seed;

    public IslandChunkGeneratorConfig(Registry<Biome> biomeRegistry, long seed) {
        this.biomeRegistry = biomeRegistry;
        // 多群系
        this.biomeSource = new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry);
        // 单群系
//        this.biomeSource = new FixedBiomeSource(biomeRegistry.getOrThrow(BiomeKeys.PLAINS));
        this.structuresConfig = this.initStructuresConfig();
        this.seed = seed;
    }

    private StructuresConfig initStructuresConfig() {
        return new StructuresConfig(Optional.empty(), new ImmutableMap.Builder<StructureFeature<?>, StructureConfig>()
                .put(StructureFeature.END_CITY, new StructureConfig(20, 11, 10387313))
                // 破损的传送门
                .put(StructureFeature.RUINED_PORTAL, new StructureConfig(40, 15, 34222645))
                .put(StructureFeature.BASTION_REMNANT, new StructureConfig(27, 4, 30084232))
                .put(StructureFeature.FORTRESS, new StructureConfig(27, 4, 30084232))
                .put(StructureFeature.NETHER_FOSSIL, new StructureConfig(2, 1, 14357921))
                .build()
        );
    }

    public BiomeSource getBiome() {
        return biomeSource;
    }

    public StructuresConfig getStructuresConfig() {
        return structuresConfig;
    }

    public long getSeed() {
        return seed;
    }
}
