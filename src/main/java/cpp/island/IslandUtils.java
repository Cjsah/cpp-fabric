package cpp.island;

import cpp.mixin.AccessorProtoChunk;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;

public class IslandUtils {

    private static final NbtCompound defaultNbt = new NbtCompound();
    private static final Block chest = Blocks.CHEST;

    @SuppressWarnings("ConstantConditions")
    public static void generator(ProtoChunk chunk, WorldAccess world) {
        ChunkSection[] sections = chunk.getSectionArray();
        Arrays.fill(sections, WorldChunk.EMPTY_SECTION);
        for (BlockPos bePos : chunk.getBlockEntityPositions()) {
            chunk.removeBlockEntity(bePos);
        }
        ((AccessorProtoChunk) chunk).getLightSources().clear();
        long[] emptyHeightmap = new PackedIntegerArray(9, 256).getStorage();
        for (Map.Entry<Heightmap.Type, Heightmap> heightmapEntry : chunk.getHeightmaps()) {
            heightmapEntry.getValue().setTo(chunk, Heightmap.Type.WORLD_SURFACE_WG, emptyHeightmap);
        }

        // 基岩箱子
        int chunkX = chunk.getPos().getStartX();
        int chunkZ = chunk.getPos().getStartZ();
        if ((chunkX == 0 || chunkX == -16) && (chunkZ == 0 || chunkZ == -16)) {
            for (int i = -3; i <= 3; i++) {
                for (int j = -3; j <= 3; j++) {
                    BlockPos pos = new BlockPos(i, 64, j);
                    if (posInChunk(pos, chunk)) {
                        chunk.setBlockState(pos, Blocks.BEDROCK.getDefaultState(), false);
                    }
                }
            }
        }
        int startX = getCorner(chunkX);
        int startZ = getCorner(chunkZ);
        if (Math.abs(startX % 1000) < 16 && Math.abs(startZ % 1000) < 16) {
            BlockPos pos = new BlockPos(startX - (startX % 1000), 63, startZ - (startZ % 1000));
            if (pos.getX() != 0 || pos.getZ() != 0) {
                chunk.setBlockState(pos.down(), Blocks.BEDROCK.getDefaultState(), false);
                chunk.setBlockState(pos, chest.getDefaultState(), false);
                BlockEntity blockEntity = ((BlockEntityProvider) chest).createBlockEntity(pos, chest.getDefaultState());
                NbtCompound nbt = defaultNbt.copy();
                nbt.putInt("x", pos.getX());
                nbt.putInt("y", pos.getY());
                nbt.putInt("z", pos.getZ());
                blockEntity.readNbt(nbt);
                chunk.setBlockEntity(blockEntity);
            }
        }
    }

    private static boolean posInChunk(BlockPos pos, Chunk chunk) {
        int chunkX = chunk.getPos().getStartX();
        int chunkZ = chunk.getPos().getStartZ();
        return pos.getX() >= chunkX && pos.getX() <= chunkX + 15 && pos.getZ() >= chunkZ && pos.getZ() <= chunkZ + 15;
    }

    private static int getCorner(int pos) {
        return pos >= 0 ? pos + 15 : pos;
    }

    static {
        NbtList list = new NbtList();
        list.add(newItem(0, Items.OAK_SAPLING, 4));
        list.add(newItem(1, Items.DIRT, 1));
        list.add(newItem(2, Items.BONE_MEAL, 16));
        defaultNbt.put("Items", list);
    }

    @Nonnull
    private static NbtCompound newItem(int slot, @Nonnull Item item, int count) {
        NbtCompound nbt = new NbtCompound();
        nbt.putByte("Slot", (byte) slot);
        nbt.putString("id", item.toString());
        nbt.putByte("Count", (byte) count);
        return nbt;
    }

}
