package net.cpp.block;

import javax.annotation.Nullable;

import net.cpp.block.entity.ChestDropperBlockEntity;
import net.cpp.block.entity.DustbinBlockEntity;
import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppStats;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ChestDropperBlock extends BarrelBlock {

	public ChestDropperBlock() {
		super(Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	}
	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new ChestDropperBlockEntity(pos, state);
	}
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ChestDropperBlockEntity) {
				player.openHandledScreen((ChestDropperBlockEntity) blockEntity);
				player.incrementStat(CppStats.INTERACT_WITH_CHEST_DROPPER);
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}

			return ActionResult.CONSUME;
		}
	}
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return checkType(world, type, CppBlockEntities.CHEST_DROPPER);
	}

	@Nullable
	protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends ChestDropperBlockEntity> expectedType) {
		return world.isClient ? null : checkType(givenType, expectedType, ChestDropperBlockEntity::tick);
	}
}
