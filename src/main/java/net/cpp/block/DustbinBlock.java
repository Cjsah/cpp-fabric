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
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DustbinBlock extends BarrelBlock {

	public DustbinBlock() {
		super(Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof DustbinBlockEntity) {
				player.openHandledScreen((DustbinBlockEntity) blockEntity);
				player.incrementStat(CppStats.INTERACT_WITH_DUSTBIN);
				PiglinBrain.onGuardedBlockInteracted(player, true);
			}

			return ActionResult.CONSUME;
		}
	}

	@Nullable
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new DustbinBlockEntity(pos, state);
	}

	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if (!world.isClient) {
			if (world.isReceivingRedstonePower(pos)) {
				DustbinBlockEntity blockEntity = (DustbinBlockEntity) world.getBlockEntity(pos);
				blockEntity.clear();
			}
		}
	}
}
