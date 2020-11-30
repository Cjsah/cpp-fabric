package net.cpp.block;

import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
/**
 * 机器模板，子类只需要实现两个方法{@link BlockEntityProvider#createBlockEntity(BlockView)}和{@link #getStatIdentifier()}
 * @author Ph-苯
 *
 */
public abstract class AMachineBlock extends BlockWithEntity {

	public AMachineBlock() {
		super(Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	}

	/*
	 * 以下是BlockWithEntity的方法
	 */
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	/*
	 * 以下是Block的方法
	 */
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (itemStack.hasCustomName()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof LockableContainerBlockEntity) {
				((LockableContainerBlockEntity) blockEntity).setCustomName(itemStack.getName());
			}
		}
	}

	/*
	 * 以下是AbstractBlock的方法
	 */
	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand,
			BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			player.openHandledScreen((NamedScreenHandlerFactory) world.getBlockEntity(pos));
			player.incrementStat(getStatIdentifier());
			return ActionResult.CONSUME;
		}
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory) blockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}

	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	/*
	 * 以下是自定义的方法
	 */
	/**
	 * @return 右键方块时需要增加的统计
	 */
	public abstract Identifier getStatIdentifier();
}
