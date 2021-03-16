package net.cpp.init;

import net.cpp.island.IslandChunkGenerator;
import net.cpp.island.IslandChunkGeneratorConfig;
import net.cpp.mixin.AccessorGeneratorType;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.FlatChunkGenerator;
import net.minecraft.world.gen.chunk.FlatChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

import java.util.Collections;
import java.util.Optional;

public final class CppGeneratorType {
    private static final GeneratorType CPP_ISLAND = new GeneratorType("cpp_island") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
//            return new IslandChunkGenerator(new IslandChunkGeneratorConfig(biomeRegistry, seed));
            IslandChunkGeneratorConfig config = new IslandChunkGeneratorConfig(
                    new StructuresConfig(Optional.empty(), Collections.emptyMap()), biomeRegistry);
            config.updateLayerBlocks();
            return new IslandChunkGenerator(config);
//            FlatChunkGeneratorConfig config = new FlatChunkGeneratorConfig(
//                    new StructuresConfig(Optional.empty(), Collections.emptyMap()), biomeRegistry);
//            config.updateLayerBlocks();
//            return new FlatChunkGenerator(config);
        }
    };

    public static void loadClass() {
        AccessorGeneratorType.getValues().add(1, CPP_ISLAND);
    }
}
