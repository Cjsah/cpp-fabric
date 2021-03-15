package net.cpp.island;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
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
            )).apply(instance, instance.stable(IslandChunkGenerator::new)));
    protected static final int islandInterval = 1000;
    private final long seed;
    private final StructuresConfig structuresConfig;

    public IslandChunkGenerator(BiomeSource biomeSource, long seed) {
        this(biomeSource, biomeSource, seed, new StructuresConfig(false));
    }

    private IslandChunkGenerator(BiomeSource populationSource, BiomeSource biomeSource, long seed, StructuresConfig structuresConfig) {
        super(populationSource, biomeSource, structuresConfig, seed);
        this.structuresConfig = structuresConfig;
        this.seed = seed;
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
    @SuppressWarnings("ConstantConditions")
    public void buildSurface(ChunkRegion region, Chunk chunk) {
        int startX = chunk.getPos().getStartX();
        int startZ = chunk.getPos().getStartZ();
        if (Math.abs(startX % 1000) < 16 && Math.abs(startZ % 1000) < 16) {
            System.out.println(startX%1000);
            BlockPos pos = new BlockPos(startX - (startX % 1000), 62, startZ - (startZ % 1000));
            chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
            chunk.setBlockState(pos.add(0, 1, 0), Blocks.CHEST.getDefaultState(), false);
//            LootableContainerBlockEntity blockEntity = (LootableContainerBlockEntity) chunk.getBlockEntity(pos.add(0, 1, 0));
//            blockEntity.setStack(0, new ItemStack(Items.OAK_SAPLING, 4));
//            blockEntity.setStack(1, new ItemStack(Items.DIRT, 1));
//            blockEntity.setStack(2, new ItemStack(Items.BONE_MEAL, 16));
        }
    }

    @Override
    public int getSpawnHeight() {
        return 64;
    }

    @Override
    public void populateNoise(@Nonnull WorldAccess world, StructureAccessor accessor, @Nonnull Chunk chunk) {
//        BlockState blockState = Blocks.AIR.getDefaultState();
//        Heightmap heightmap = chunk.getHeightmap(Heightmap.Type.OCEAN_FLOOR_WG);
//        Heightmap heightmap2 = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG);
//
//        int y = world.getBottomY();
//        for(int x = 0; x < 16; ++x) {
//            for(int z = 0; z < 16; ++z) {
//                heightmap.trackUpdate(x, y, z, blockState);
//                heightmap2.trackUpdate(x, y, z, blockState);
//            }
//        }
    }

    @Override
    public int getHeight(int x, int z, @Nonnull Heightmap.Type heightmap, HeightLimitView world) {
        return world.getBottomY();
    }

    @Override
    public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
        return new VerticalBlockSample(0, new BlockState[]{});
    }

    @Override
    public StructuresConfig getStructuresConfig() {
        return this.structuresConfig;
    }

}
