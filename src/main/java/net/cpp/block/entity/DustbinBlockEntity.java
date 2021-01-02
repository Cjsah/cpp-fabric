package net.cpp.block.entity;

import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
/**
 * 垃圾桶方块实体
 * @author Ph-苯
 *
 */
public class DustbinBlockEntity extends AMachineBlockEntity {

	public DustbinBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.DUSTBIN, pos, state);
		setCapacity(27);
	}
	public static void tick(World world, BlockPos pos, BlockState state, DustbinBlockEntity blockEntity) {
		if (!world.isClient && world.isReceivingRedstonePower(pos)) {
			blockEntity.clear();
		}
	}
}
