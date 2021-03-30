package net.cpp.island;

import net.cpp.mixin.AccessorProtoChunk;
import net.minecraft.util.collection.PackedIntegerArray;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Arrays;
import java.util.Map;

public class IslandUtils {
    public static void deleteBlocks(ProtoChunk chunk, WorldAccess world) {
        ChunkSection[] sections = chunk.getSectionArray();
        Arrays.fill(sections, WorldChunk.EMPTY_SECTION);
        for (BlockPos bePos : chunk.getBlockEntityPositions()) {
            chunk.removeBlockEntity(bePos);
        }
        ((AccessorProtoChunk) chunk).getLightSources().clear();
        long[] emptyHeightmap = new PackedIntegerArray(9, 256).getStorage();
        for (Map.Entry<Heightmap.Type, Heightmap> heightmapEntry : chunk.getHeightmaps()) {
            heightmapEntry.getValue().setTo(emptyHeightmap);
        }
//        Heightmap.populateHeightmaps(chunk, EnumSet.allOf(Heightmap.Type.class));
    }

}
