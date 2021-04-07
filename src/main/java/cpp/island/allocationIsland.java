package cpp.island;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 回字形分配岛屿
 */
public class allocationIsland {
    private static int islandID = 1;
    // 圈数
    private static int around = 1;


    public static void allocation(World world, PlayerEntity player) {
        BlockPos pos = getPos();

        // player设为已拥有岛屿

        player.teleport(pos.getX() + .5D, pos.getY() + 3D, pos.getZ() + .5D);

    }

    private static BlockPos getPos() {
        if (islandID > getCount(around)) around++;

        int aroundId = islandID - getCount(around - 1);
        int length = around * 2 + 1;

        islandID++;

        if (aroundId <= length) {
            return createPos(-around, aroundId - around - 1);
        }else if (aroundId <= length * 2 - 2) {
            return createPos(aroundId - length - around, around);
        }else if (aroundId <= length * 3 - 2) {
            return createPos(around, length - aroundId + around * 3);
        }else {
            return createPos(length - aroundId + around * 5, -around);
        }
    }

    private static int getCount(int around) {
        return around * (around + 1) / 2 * 8;
    }

    private static BlockPos createPos(int x, int z) {
        return new BlockPos(x * IslandChunkGenerator.islandInterval, 62, z * IslandChunkGenerator.islandInterval);
    }

}
