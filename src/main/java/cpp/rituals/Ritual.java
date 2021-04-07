package cpp.rituals;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class Ritual {

//    private static final Collection<RitualRecipe> recipes = Collections.emptyList();

    /**
     *
     * @param dispenserPos  发射器坐标
     * @param center        基座中心方块
     * @param corner        基座角落方块
     * @param border        基座四周方块
     * @return              是否符合多方块结构
     */
    public static boolean valid(@Nonnull World world, BlockPos dispenserPos, Block center, Block corner, Block border) {
        if (world.getBlockState(dispenserPos).getBlock() != Blocks.DISPENSER) {
            return false;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                BlockPos pos = dispenserPos.add(i, -1, j);
                if (
                        (i == 0 && j == 0 && world.getBlockState(pos).getBlock() != center) ||
                        (world.getBlockState(pos).getBlock() != ((i + j) % 2 == 0 ? corner : border))
                ) {
                    return false;
                }
            }
        }
        return true;
    }

//    @SuppressWarnings("ConstantConditions")
//    public static boolean isComposite(World world, BlockPos pos) {
//        CompoundTag tag = world.getBlockEntity(pos).toTag(new CompoundTag());
//        world.getEntitiesByType()
//    }

    enum RitualType {
        MAGIC(Blocks.LAPIS_BLOCK, Blocks.EMERALD_BLOCK, Blocks.IRON_BLOCK),
        ANCIENT(Blocks.BEDROCK, Blocks.OBSIDIAN, Blocks.MAGMA_BLOCK);

        private final Block center;
        private final Block corner;
        private final Block border;

        RitualType(Block center, Block corner, Block border) {
            this.center = center;
            this.corner = corner;
            this.border = border;
        }

        public Block getCenter() {
            return center;
        }

        public Block getCorner() {
            return corner;
        }

        public Block getBorder() {
            return border;
        }
    }


    static {
    }

}
