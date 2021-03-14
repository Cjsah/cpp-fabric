package net.cpp.island;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class IslandChunkGenerator extends ChunkGenerator {
    public static final Codec<IslandChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.populationSource
            ), Codec.LONG.fieldOf("seed").stable().forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.seed
            ), ChunkGeneratorSettings.REGISTRY_CODEC.fieldOf("settings").forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.settings
            )).apply(instance, instance.stable(IslandChunkGenerator::new)));
    private final long seed;
    private final Supplier<ChunkGeneratorSettings> settings;
    private final StructuresConfig structuresConfig;


    public IslandChunkGenerator(BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings) {
        this(biomeSource, biomeSource, seed, settings, new StructuresConfig(false));
    }

    private IslandChunkGenerator(BiomeSource populationSource, BiomeSource biomeSource, long seed, Supplier<ChunkGeneratorSettings> settings, StructuresConfig structuresConfig) {
        super(populationSource, biomeSource, structuresConfig, seed);
        this.structuresConfig = structuresConfig;
        this.seed = seed;
        this.settings =settings;
    }

    @Override
    protected Codec<? extends ChunkGenerator> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public ChunkGenerator withSeed(long seed) {
        return new IslandChunkGenerator(this.populationSource.withSeed(this.seed), this.seed, this.settings);
    }

    @Override
    public void buildSurface(ChunkRegion region, Chunk chunk) {

    }

    private void buildIsland(Chunk chunk) {
        boolean startX = chunk.getPos().getStartX() % 1000 < 16;

    }

    @Override
    public void populateNoise(@Nonnull WorldAccess world, StructureAccessor accessor, @Nonnull Chunk chunk) {
        BlockState blockState = Blocks.AIR.getDefaultState();
        Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmap2 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);

        int y = world.getBottomY();
        for(int x = 0; x < 16; ++x) {
            for(int z = 0; z < 16; ++z) {
                heightmap.trackUpdate(x, y, z, blockState);
                heightmap2.trackUpdate(x, y, z, blockState);
            }
        }
    }

    @Override
    public int getHeight(int x, int z, @Nonnull Heightmap.Type heightmap, HeightLimitView world) {
        BlockState blockState = Blocks.AIR.getDefaultState();
        if (heightmap.getBlockPredicate().test(blockState)) {
            return world.getBottomY() + 1;
        }
        return world.getBottomY();
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return new VerticalBlockSample(0, new BlockState[]{Blocks.AIR.getDefaultState()});
    }

    @Override
    public StructuresConfig getStructuresConfig() {
        return this.structuresConfig;
    }

}
