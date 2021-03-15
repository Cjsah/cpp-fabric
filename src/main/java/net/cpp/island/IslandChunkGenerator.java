package net.cpp.island;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkRegion;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructureConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;

import javax.annotation.Nonnull;
import java.util.Optional;

public class IslandChunkGenerator extends ChunkGenerator {
    public static final Codec<IslandChunkGenerator> CODEC = RecordCodecBuilder.create((instance) ->
            instance.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.populationSource
            ), Codec.LONG.fieldOf("seed").stable().forGetter((islandChunkGenerator) ->
                    islandChunkGenerator.seed
            )).apply(instance, instance.stable(IslandChunkGenerator::new)));
    private static final ImmutableMap<StructureFeature<?>, StructureConfig> structures;
    protected static final int islandInterval = 1000;
    private static final CompoundTag defaultTag = new CompoundTag();
    private final long seed;
    private final StructuresConfig structuresConfig;

    public IslandChunkGenerator(BiomeSource biomeSource, long seed) {
        this(biomeSource, biomeSource, seed, new StructuresConfig(Optional.empty(), structures));
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
    public void buildSurface(ChunkRegion region, @Nonnull Chunk chunk) {
        int startX = getCorner(chunk.getPos().getStartX());
        int startZ = getCorner(chunk.getPos().getStartZ());
        if (Math.abs(startX % 1000) < 16 && Math.abs(startZ % 1000) < 16) {
            BlockPos pos = new BlockPos(startX - (startX % 1000), 63, startZ - (startZ % 1000));
            region.setBlockState(pos.add(0, -1, 0), Blocks.BEDROCK.getDefaultState(), 2);
            region.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
            BlockEntity blockEntity = region.getBlockEntity(pos);
            CompoundTag tag = defaultTag.copy();
            tag.putInt("x", pos.getX());
            tag.putInt("y", pos.getY());
            tag.putInt("z", pos.getZ());
            blockEntity.fromTag(tag);
        }
    }
    private int getCorner(int pos) {
        return pos >= 0 ? pos + 15 : pos;
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
        return this.structuresConfig;
    }

    static {
        ListTag list = new ListTag();
        list.add(newItem(0, Items.OAK_SAPLING, 4));
        list.add(newItem(1, Items.DIRT, 1));
        list.add(newItem(2, Items.BONE_MEAL, 16));
        defaultTag.put("Items", list);

        structures = new ImmutableMap.Builder<StructureFeature<?>, StructureConfig>()
                .put(StructureFeature.END_CITY, new StructureConfig(20, 11, 10387313))
                // 破损的传送门
                .put(StructureFeature.RUINED_PORTAL, new StructureConfig(40, 15, 34222645))
                .put(StructureFeature.BASTION_REMNANT, new StructureConfig(27, 4, 30084232))
                .put(StructureFeature.FORTRESS, new StructureConfig(27, 4, 30084232))
                .put(StructureFeature.NETHER_FOSSIL, new StructureConfig(2, 1, 14357921))
                .build();

    }

    @Nonnull
    private static CompoundTag newItem(int slot, @Nonnull Item item, int count) {
        CompoundTag tag = new CompoundTag();
        tag.putByte("Slot", (byte) slot);
        tag.putString("id", item.toString());
        tag.putByte("Count", (byte) count);
        return tag;
    }
}
