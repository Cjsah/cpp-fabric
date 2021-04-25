package cpp.block.entity;

import cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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
	public void readNbt(NbtCompound tag) {
		super.readNbt(tag);
		stack = ItemStack.fromNbt(tag.getCompound("stack"));
	}
	
	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.put("stack", stack.writeNbt(new NbtCompound()));
		return super.writeNbt(tag);
	}
	
	@Override
	public void markDirty() {
		
		tick(getWorld(), getPos(),getCachedState(),this);
		super.markDirty();
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, FermenterBlockEntity blockEntity) {

	}
}
