package net.cpp.island;

import com.mojang.serialization.Codec;
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
    private static final CompoundTag defaultTag = new CompoundTag();

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
    @SuppressWarnings("ConstantConditions")
    public void buildSurface(ChunkRegion region, @Nonnull Chunk chunk) {
        int startX = this.getCorner(chunk.getPos().getStartX());
        int startZ = this.getCorner(chunk.getPos().getStartZ());
        if (Math.abs(startX % 1000) < 16 && Math.abs(startZ % 1000) < 16) {
            BlockPos pos = new BlockPos(startX - (startX % 1000), 63, startZ - (startZ % 1000));
            if (pos.getX() != 0 || pos.getZ() != 0) {
                region.setBlockState(pos.down(), Blocks.BEDROCK.getDefaultState(), 2);
                region.setBlockState(pos, Blocks.CHEST.getDefaultState(), 2);
                BlockEntity blockEntity = region.getBlockEntity(pos);
                CompoundTag tag = defaultTag.copy();
                tag.putInt("x", pos.getX());
                tag.putInt("y", pos.getY());
                tag.putInt("z", pos.getZ());
                blockEntity.fromTag(tag);

            }
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
        return this.config.getStructuresConfig();
    }

    static {
        Registry.register(Registry.CHUNK_GENERATOR, "cpp_island", IslandChunkGenerator.CODEC);

        ListTag list = new ListTag();
        list.add(newItem(0, Items.OAK_SAPLING, 4));
        list.add(newItem(1, Items.DIRT, 1));
        list.add(newItem(2, Items.BONE_MEAL, 16));
        defaultTag.put("Items", list);
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
