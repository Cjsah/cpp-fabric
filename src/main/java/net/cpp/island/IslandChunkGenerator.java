package net.cpp.island;

import com.mojang.serialization.Codec;
import net.cpp.Craftingpp;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import javax.annotation.Nonnull;

public class IslandChunkGenerator extends ChunkGenerator {
    public static final Codec<IslandChunkGenerator> CODEC = IslandChunkGeneratorConfig.CODEC.fieldOf("settings").xmap(IslandChunkGenerator::new, IslandChunkGenerator::getConfig).codec();
    private final IslandChunkGeneratorConfig config;

    protected static final int islandInterval = 1000;

    public IslandChunkGenerator(IslandChunkGeneratorConfig config) {
        super(config.getBiomeSource(), config.getBiomeSource(), config.getStructuresConfig(), config.getSeed());
        this.config = config;
    }

    public IslandChunkGeneratorConfig getConfig() {
        return this.config;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return this;
    }

    @Override
    public void buildSurface(ChunkRegion region, @Nonnull Chunk chunk) {
    }

    @Override
    public int getSpawnHeight() {
        return 64;
    }

    @Override
    public void populateNoise(@Nonnull WorldAccess world, StructureAccessor accessor, @Nonnull Chunk chunk) {
    }

    @Override
    public int getHeight(int x, int z, @Nonnull Heightmap.Type heightmap, @Nonnull HeightLimitView world) {
        return world.getBottomY();
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return new VerticalBlockSample(0, new BlockState[]{});
    }

    @Override
    public StructuresConfig getStructuresConfig() {
        return this.config.getStructuresConfig();
    }

    static {
        Registry.register(Registry.CHUNK_GENERATOR, Craftingpp.MOD_ID3 + "cpp_island", IslandChunkGenerator.CODEC);
    }
}
