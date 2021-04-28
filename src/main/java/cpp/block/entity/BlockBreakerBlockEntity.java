package cpp.block.entity;

import cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class BlockBreakerBlockEntity extends BlockEntity {
	private int time;
	private String fluid = "";
	
	public BlockBreakerBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.BLOCK_BREAKER, pos, state);
	}
	
	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		time = nbt.getInt("time");
		fluid = nbt.getString("fluid");
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("time", time);
		nbt.putString("fluid", fluid);
		return super.writeNbt(nbt);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, BlockBreakerBlockEntity blockEntity) {
		if (world.getBlockState(pos.up()).isOf(Blocks.CAULDRON)) {
			if (!"".equals(blockEntity.fluid)) {
				if (blockEntity.time <= 0) {
					switch (blockEntity.fluid) {
						case "water":
							world.setBlockState(pos.up(), Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3));
							break;
						case "lava":
							world.setBlockState(pos.up(), Blocks.LAVA_CAULDRON.getDefaultState());
							break;
					}
					blockEntity.fluid = "";
				} else {
					blockEntity.time--;
				}
			} else {
				for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(pos.up()), itemEntity -> true)) {
					ItemStack stack = itemEntity.getStack();
					if (stack.getCount() >= 4) {
						if (stack.isOf(Items.SNOWBALL)) {
							stack.decrement(4);
							blockEntity.fluid = "water";
							blockEntity.time = 1200;
							break;
						} else if (stack.isOf(Items.COBBLESTONE)) {
							stack.decrement(4);
							blockEntity.fluid = "lava";
							blockEntity.time = 1200;
							break;
						}
					}
				}
			}
		}
	}
}
