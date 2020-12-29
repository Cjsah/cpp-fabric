package net.cpp.block.entity;

import static net.minecraft.item.Items.*;
import static net.cpp.init.CppItems.*;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.cpp.block.AMachineBlock;
import net.cpp.block.EmptyBookshelfBlock;
import net.cpp.gui.handler.EmptyBookshelfScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
/**
 * 空书架方块实体
 * @author Ph-苯
 *
 */
public class EmptyBookshelfBlockEntity extends AMachineBlockEntity implements SidedInventory {
	/**
	 * 可储存的物品
	 */
	public static final Set<Item> STORABLE = ImmutableSet.of(BOOK, PAPER, ANCIENT_SCROLL, COMPASS, ENCHANTED_BOOK, MAP, FILLED_MAP, WRITTEN_BOOK, WRITABLE_BOOK);
	private int viewerCount = 0;

	public EmptyBookshelfBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.EMPTY_BOOKSHELF, pos, state);
		setCapacity(3);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EmptyBookshelfBlockEntity blockEntity) {
		if (!world.isClient) {
			boolean bookshelf = true;
			int bookState = 0;
			for (int i = 0; i < 3; i++) {
				bookState <<= 1;
				if (!blockEntity.getStack(i).isEmpty()) {
					bookState |= 1;
				}
				if (!ItemStack.areEqual(blockEntity.getStack(i), BOOK.getDefaultStack())) {
					bookshelf = false;
				}
			}
			if (bookshelf && blockEntity.viewerCount <= 0) {
				blockEntity.clear();
				world.setBlockState(pos, Blocks.BOOKSHELF.getDefaultState());
				blockEntity.markRemoved();
				return;
			}
			world.setBlockState(pos, CppBlocks.EMPTY_BOOKSHELF.getDefaultState().with(EmptyBookshelfBlock.BOOK_STATE, bookState));
		}
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return new int[] { 0, 1, 2 };
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return STORABLE.contains(stack.getItem());
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return true;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new EmptyBookshelfScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public void onOpen(PlayerEntity player) {
		if (viewerCount < 0)
			viewerCount = 0;
		viewerCount++;
		super.onOpen(player);
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (viewerCount < 0)
			viewerCount = 0;
		viewerCount--;
		super.onClose(player);
	}
}
