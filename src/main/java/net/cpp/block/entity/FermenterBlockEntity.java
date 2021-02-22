package net.cpp.block.entity;

import net.cpp.block.FermenterBlock;
import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Objects;

import static net.minecraft.block.ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE;
import static net.minecraft.block.ComposterBlock.LEVEL;
@Deprecated
public class FermenterBlockEntity extends BlockEntity implements SidedInventory {
	private ItemStack stack = ItemStack.EMPTY;
	
	public FermenterBlockEntity(BlockPos pos, BlockState state) {
		super(CppBlockEntities.FERMENTER, pos, state);
	}
	
	@Override
	public int[] getAvailableSlots(Direction side) {
		return side == Direction.DOWN ? new int[]{0} : new int[0];
	}
	
	@Override
	public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
		return false;
	}
	
	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return dir == Direction.DOWN;
	}
	
	@Override
	public int size() {
		return 1;
	}
	
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	@Override
	public ItemStack getStack(int slot) {
		return stack;
	}
	
	@Override
	public ItemStack removeStack(int slot, int amount) {
		ItemStack removedStack;
		if (amount >= stack.getCount()) {
			removedStack = stack;
			stack = ItemStack.EMPTY;
		} else {
			removedStack = stack.copy();
			stack.decrement(amount);
			removedStack.setCount(amount);
		}
		return removedStack;
	}
	
	@Override
	public ItemStack removeStack(int slot) {
		return removeStack(slot, getStack(slot).getCount());
	}
	
	@Override
	public void setStack(int slot, ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return false;
	}
	
	@Override
	public void clear() {
		stack = ItemStack.EMPTY;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		stack = ItemStack.fromTag(tag.getCompound("stack"));
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("stack", stack.toTag(new CompoundTag()));
		return super.toTag(tag);
	}
	
	@Override
	public void markDirty() {
		
		tick(getWorld(), getPos(),getCachedState(),this);
		super.markDirty();
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, FermenterBlockEntity blockEntity) {

	}
}
