package net.cpp.block;

import javax.annotation.Nullable;

import net.cpp.block.entity.DustbinBlockEntity;
import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppStats;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DustbinBlock extends AMachineBlock {

	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DustbinBlockEntity(pos, state);
	}

	@Override
	public Identifier getStatIdentifier() {
		return CppStats.INTERACT_WITH_DUSTBIN;
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.DUSTBIN);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends DustbinBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, DustbinBlockEntity::tick);
	}
}
