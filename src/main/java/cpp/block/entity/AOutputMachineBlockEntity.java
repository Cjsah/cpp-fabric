package cpp.block.entity;

import java.util.Objects;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * 拥有方向输出的机器
 * 
 * @author Ph-苯
 *
 */
public abstract class AOutputMachineBlockEntity extends AMachineBlockEntity implements NamedScreenHandlerFactory, IOutputDiractional, SidedInventory {
	/**
	 * 输出方向
	 */
	protected Direction outputDir = Direction.EAST;
	/**
	 * 命令源
	 */
	protected ServerCommandSource serverCommandSource;

	protected AOutputMachineBlockEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
		super(blockEntityType, blockPos, blockState);
	}

	/*
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		outputDir = IOutputDiractional.byteToDir(tag.getByte("outputDir"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putByte("outputDir", IOutputDiractional.dirToByte(outputDir));
		return tag;
	}

	@Override
	public int size() {
		return getInvStackList().size();
	}

	/*
	 * 以下是IOutputDiractional的方法
	 */
	@Override
	public void setOutputDir(Direction dir) {
		outputDir = dir;
	}

	@Override
	public Direction getOutputDir() {
		return outputDir;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	/*
	 * 以下是自定义方法
	 */

	/**
	 * 改编自漏斗的代码，获取输出方向的物品栏
	 * 
	 * @return 输出方向的物品栏
	 */
	@Nullable
	public Inventory getOutputInventory() {
		return getInventoryAt(this.getWorld(), this.pos.offset(outputDir));
	}

	/**
	 * 从漏斗抄来的代码，判断指定的物品栏在指定方向上是否已满
	 * 
	 * @param inv       物品栏
	 * @param direction 方向
	 * @return 已满
	 */
	public static boolean isInventoryFull(Inventory inv, Direction direction) {
		return getAvailableSlots(inv, direction).allMatch((i) -> {
			ItemStack itemStack = inv.getStack(i);
			return itemStack.getCount() >= itemStack.getMaxCount();
		});
	}

	/**
	 * 输出物品
	 * 
	 * @param index 被输出的槽位序号
	 * @return 被输出的槽位上的物品
	 */
	protected ItemStack output(int index) {
		Inventory inventory = getOutputInventory();
		if (inventory != null) {
			Direction direction = getOutputDir();
			if (!isInventoryFull(inventory, direction)) {
				setStack(index, transfer(this, inventory, getStack(index), direction));
				if (getStack(index).isEmpty()) {
					inventory.markDirty();
				}

			}
		}
		return getStack(index);
	}

	/**
	 * 输出一叠物品
	 * 
	 * @param outputStack 要被输出的物品
	 * @return 剩余的物品
	 */
	protected ItemStack output(ItemStack outputStack) {
		Inventory inventory = getOutputInventory();
		if (inventory != null) {
			Direction direction = getOutputDir();
			if (!isInventoryFull(inventory, direction)) {
				transfer(this, inventory, outputStack, direction);
				if (outputStack.isEmpty()) {
					inventory.markDirty();
				}

			}
		}
		return outputStack;
	}

	/**
	 * @param index 此机器要被输入的物品栏序号
	 * @param input 输入物品叠
	 * @return {@code input}能完全容纳{@code getStack(index)}
	 */
	protected boolean canInsert(int index, ItemStack input) {
		return getStack(index).isEmpty() || input.isEmpty() || ItemStack.areItemsEqual(input, getStack(index)) && Objects.equals(input.getTag(), getStack(index).getTag()) && getStack(index).getCount() + input.getCount() <= getStack(index).getMaxCount();
	}

	/**
	 * 将{@code input}全部放入{@code getStack(index)}中
	 * 
	 * @param index 此机器要被输入的物品栏序号
	 * @param input 输入物品叠
	 */
	protected void insert(int index, ItemStack input) {
		if (!input.isEmpty())
			if (getStack(index).isEmpty())
				setStack(index, input);
			else
				getStack(index).increment(input.getCount());
	}

	/**
	 * 当且仅当{@code input}能完全容纳{@code getStack(index)}时，才会放入
	 * 
	 * @param index 此机器要被输入的物品栏序号
	 * @param input 输入物品叠
	 * @return 成功放入
	 * @see #canInsert(int, ItemStack)
	 * @see #insert(int, ItemStack)
	 */
	protected boolean tryInsert(int index, ItemStack input) {
		if (canInsert(index, input)) {
			insert(index, input);
			return true;
		}
		return false;
	}

	/**
	 * 获得命令源，用于执行命令或目标选择器
	 * 
	 * @return 服务端命令源
	 */
	protected ServerCommandSource getServerCommandSource() {
		if (!world.isClient && serverCommandSource == null)
			serverCommandSource = new ServerCommandSource(CommandOutput.DUMMY, Vec3d.of(pos), Vec2f.ZERO, (ServerWorld) world, 4, "", LiteralText.EMPTY, world.getServer(), null);
		return serverCommandSource;
	}

	/**
	 * 改编自漏斗的代码，获取指定位置的物品栏
	 * 
	 * @param world
	 * @param pos
	 * @return
	 */
	@Nullable
	public static Inventory getInventoryAt(World world, BlockPos pos) {
		Inventory inventory = null;
		BlockPos blockPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ());
		BlockState blockState = world.getBlockState(blockPos);
		Block block = blockState.getBlock();
		if (block instanceof InventoryProvider) {
			inventory = ((InventoryProvider) block).getInventory(blockState, world, blockPos);
		} else if (blockState.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, blockPos, true);
				}
			}
		}

		return inventory;
	}

	/**
	 * 从漏斗抄来的代码，获取指定方向物品栏可以输入物品的槽位
	 * 
	 * @param inventory
	 * @param side
	 * @return
	 */
	public static IntStream getAvailableSlots(Inventory inventory, Direction side) {
		return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory) inventory).getAvailableSlots(side)) : IntStream.range(0, inventory.size());
	}

	/**
	 * 从tag读取Items并储存到inventory
	 * 
	 * @param tag
	 * @param inventory
	 */
	public static void inventoryFromTag(CompoundTag tag, Inventory inventory) {
		ListTag listTag = tag.getList("Items", 10);
		for (int i = 0; i < listTag.size(); ++i) {
			CompoundTag compoundTag = listTag.getCompound(i);
			int j = compoundTag.getByte("Slot") & 255;
			if (j >= 0 && j < inventory.size()) {
				inventory.setStack(j, ItemStack.fromTag(compoundTag));
			}
		}
	}

	/**
	 * 将inventory储存到tag的Items，即使inventory为空也储存
	 * 
	 * @see #inventoryToTag(CompoundTag, Inventory, boolean)
	 * @param tag
	 * @param inventory
	 * @return tag
	 */
	public static CompoundTag inventoryToTag(CompoundTag tag, Inventory inventory) {
		return inventoryToTag(tag, inventory, true);
	}

	/**
	 * 将inventory储存到tag的Items
	 * 
	 * @see #inventoryToTag(CompoundTag, Inventory)
	 * @param tag
	 * @param inventory
	 * @param setIfEmpty 如果为true，即使inventory为空也储存
	 * @return tag
	 */
	public static CompoundTag inventoryToTag(CompoundTag tag, Inventory inventory, boolean setIfEmpty) {
		ListTag listTag = new ListTag();

		for (int i = 0; i < inventory.size(); ++i) {
			ItemStack itemStack = inventory.getStack(i);
			if (!itemStack.isEmpty()) {
				CompoundTag compoundTag = new CompoundTag();
				compoundTag.putByte("Slot", (byte) i);
				itemStack.toTag(compoundTag);
				listTag.add(compoundTag);
			}
		}

		if (!listTag.isEmpty() || setIfEmpty) {
			tag.put("Items", listTag);
		}

		return tag;
	}

	/**
	 * 将itemStack转化为复合标签
	 * 
	 * @param itemStack
	 * @return
	 */
	public static CompoundTag itemStackToTag(ItemStack itemStack) {
		CompoundTag compoundTag = new CompoundTag();
		itemStack.toTag(compoundTag);
		return compoundTag;
	}

	/**
	 * 从漏斗抄来的代码，用于输出物品
	 * 
	 * @param from
	 * @param to
	 * @param stack
	 * @param side
	 * @return
	 */
	public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, @Nullable Direction side) {
		if (to instanceof SidedInventory && side != null) {
			SidedInventory sidedInventory = (SidedInventory) to;
			int[] is = sidedInventory.getAvailableSlots(side);

			for (int i = 0; i < is.length && !stack.isEmpty(); ++i) {
				stack = transfer(from, to, stack, is[i], side);
			}
		} else {
			int j = to.size();

			for (int k = 0; k < j && !stack.isEmpty(); ++k) {
				stack = transfer(from, to, stack, k, side);
			}
		}

		return stack;
	}

	/**
	 * 从漏斗抄来的代码，用于输出物品
	 * 
	 * @param from
	 * @param to
	 * @param stack
	 * @param slot
	 * @param direction
	 * @return
	 */
	public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot, @Nullable Direction direction) {
		ItemStack itemStack = to.getStack(slot);
		if (canInsert(to, stack, slot, direction)) {
			if (itemStack.isEmpty()) {
				to.setStack(slot, stack);
				stack = ItemStack.EMPTY;
			} else if (canMergeItems(itemStack, stack)) {
				int i = stack.getMaxCount() - itemStack.getCount();
				int j = Math.min(stack.getCount(), i);
				stack.decrement(j);
				itemStack.increment(j);
			}
		}
		return stack;
	}

	/**
	 * 从漏斗抄来的代码，用于输出物品
	 * 
	 * @param inventory
	 * @param stack
	 * @param slot
	 * @param side
	 * @return
	 */
	public static boolean canInsert(Inventory inventory, ItemStack stack, int slot, @Nullable Direction side) {
		if (!inventory.isValid(slot, stack)) {
			return false;
		} else {
			return !(inventory instanceof SidedInventory) || ((SidedInventory) inventory).canInsert(slot, stack, side);
		}
	}

	/**
	 * 从漏斗抄来的代码，用于输出物品
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static boolean canMergeItems(ItemStack first, ItemStack second) {
		if (first.getItem() != second.getItem()) {
			return false;
		} else if (first.getDamage() != second.getDamage()) {
			return false;
		} else if (first.getCount() > first.getMaxCount()) {
			return false;
		} else {
			return ItemStack.areTagsEqual(first, second);
		}
	}

	public abstract PropertyDelegate getPropertyDelegate();

	protected class OutputDirectionPropertyDelegate implements PropertyDelegate {
		@Override
		public int size() {
			return 1;
		}

		@Override
		public void set(int index, int value) {
			if (index == 0)
				setOutputDir(IOutputDiractional.byteToDir((byte) value));
		}

		@Override
		public int get(int index) {
			if (index == 0)
				return dirToByte();
			else
				return -1;
		}
	}

}
