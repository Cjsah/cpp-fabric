package net.cpp.init;

import net.cpp.island.IslandChunkGenerator;
import net.cpp.mixin.AccessorGeneratorType;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.VanillaLayeredBiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;

public final class CppGeneratorType {
    private static final GeneratorType CPP_ISLAND = new GeneratorType("cpp_island") {
        protected ChunkGenerator getChunkGenerator(Registry<Biome> biomeRegistry, Registry<ChunkGeneratorSettings> chunkGeneratorSettingsRegistry, long seed) {
            return new IslandChunkGenerator(new VanillaLayeredBiomeSource(seed, false, false, biomeRegistry), seed, () ->
                    chunkGeneratorSettingsRegistry.getOrThrow(ChunkGeneratorSettings.OVERWORLD)
            );
        }
    };

    public static void loadClass() {
        AccessorGeneratorType.getValues().add(1, CPP_ISLAND);
    }
}
