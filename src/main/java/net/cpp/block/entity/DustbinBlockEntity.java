package net.cpp.block.entity;

import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DustbinBlockEntity extends ABarrelLikeBlockEntity {

	public DustbinBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.DUSTBIN, pos, state);
	}
	public static void tick(World world, BlockPos pos, BlockState state, DustbinBlockEntity blockEntity) {
		System.out.println(4);
		if (!world.isClient && world.isReceivingRedstonePower(pos)) {
			blockEntity.clear();
			System.out.println(14);
		}
	}
}
