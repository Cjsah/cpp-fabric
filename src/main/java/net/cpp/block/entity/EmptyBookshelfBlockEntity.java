package net.cpp.block.entity;

import static net.minecraft.item.Items.*;
import static net.cpp.init.CppItems.*;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.cpp.block.AMachineBlock;
import net.cpp.gui.handler.EmptyBookshelfScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class EmptyBookshelfBlockEntity extends AMachineBlockEntity implements SidedInventory {
	public static final Set<Item> STORABLE = ImmutableSet.of(BOOK, PAPER, ANCIENT_SCROLL, COMPASS, ENCHANTED_BOOK, MAP, FILLED_MAP);

	public EmptyBookshelfBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.DUSTBIN, pos, state);
		setCapacity(3);
	}

	public static void tick(World world, BlockPos pos, BlockState state, EmptyBookshelfBlockEntity blockEntity) {
		if (!world.isClient) {
			boolean bookshelf = true;
			for (int i = 0; i < 3; i++) {
				if (!ItemStack.areEqual(blockEntity.getStack(i), BOOK.getDefaultStack())) {
					bookshelf = false;
					break;
				}
			}
			if (bookshelf) {
				world.setBlockState(pos, Blocks.BOOKSHELF.getDefaultState());
				blockEntity.markRemoved();
			}
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
}
