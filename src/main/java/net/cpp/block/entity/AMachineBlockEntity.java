package net.cpp.block.entity;

import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class AMachineBlockEntity extends LootableContainerBlockEntity
		implements Tickable, NamedScreenHandlerFactory, IOutputDiractionalBlockEntity {
	protected Direction outputDir = Direction.EAST;

	protected AMachineBlockEntity(BlockEntityType<?> blockEntityType) {
		super(blockEntityType);
	}

	/*
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		outputDir = IOutputDiractionalBlockEntity.byteToDir(tag.getByte("outputDir"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		tag.putByte("outputDir", IOutputDiractionalBlockEntity.dirToByte(outputDir));
		return tag;
	}

	@Override
	public Text getContainerName() {
		return getCustomName() != null ? getCustomName() : getTitle();
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

	/*
	 * 以下是自定义方法
	 */
	/**
	 * 改编自漏斗的代码，获取输出方向的物品栏
	 * 
	 * @return
	 */
	@Nullable
	public Inventory getOutputInventory() {
		return getInventoryAt(this.getWorld(), this.pos.offset(outputDir));
	}

	/**
	 * 从漏斗抄来的代码，判断指定方向的物品栏是否已满
	 * 
	 * @param inv
	 * @param direction
	 * @return
	 */
	public boolean isInventoryFull(Inventory inv, Direction direction) {
		return getAvailableSlots(inv, direction).allMatch((i) -> {
			ItemStack itemStack = inv.getStack(i);
			return itemStack.getCount() >= itemStack.getMaxCount();
		});
	}

	/**
	 * 输出一叠物品
	 * 
	 * @param outputStack 要被输出的物品
	 * @return 因输出不下而剩下的物品
	 */
	public ItemStack output(ItemStack outputStack) {
		Inventory inventory = getOutputInventory();
		ItemStack restStack = outputStack;
		if (inventory != null) {
			Direction direction = getOutputDir();
			if (!this.isInventoryFull(inventory, direction)) {
				restStack = transfer(this, inventory, outputStack.copy(), direction);
				if (restStack.isEmpty()) {
					inventory.markDirty();
				}

			}
		}
		return restStack;
	}

	public abstract Text getTitle();

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
		} else if (block.hasBlockEntity()) {
			BlockEntity blockEntity = world.getBlockEntity(blockPos);
			if (blockEntity instanceof Inventory) {
				inventory = (Inventory) blockEntity;
				if (inventory instanceof ChestBlockEntity && block instanceof ChestBlock) {
					inventory = ChestBlock.getInventory((ChestBlock) block, blockState, world, blockPos, true);
				}
			}
		}

		return (Inventory) inventory;
	}

	/**
	 * 从漏斗抄来的代码，获取指定方向物品栏可以输入物品的槽位
	 * 
	 * @param inventory
	 * @param side
	 * @return
	 */
	public static IntStream getAvailableSlots(Inventory inventory, Direction side) {
		return inventory instanceof SidedInventory ? IntStream.of(((SidedInventory) inventory).getAvailableSlots(side))
				: IntStream.range(0, inventory.size());
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
			ItemStack itemStack = (ItemStack) inventory.getStack(i);
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
	public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack,
			@Nullable Direction side) {
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
	public static ItemStack transfer(@Nullable Inventory from, Inventory to, ItemStack stack, int slot,
			@Nullable Direction direction) {
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

	/**
	 * 将UUID转化为包含4个元素的int型数组</br>
	 * （本类未使用）
	 * 
	 * @see #intArrayToUUID(IntArrayTag)
	 * @param uuid
	 * @return 转化的数组
	 */
	public static int[] uuidToIntArray(UUID uuid) {
		int[] arr = new int[4];
		arr[0] = (int) (uuid.getMostSignificantBits() >> 32);
		arr[1] = (int) uuid.getMostSignificantBits();
		arr[2] = (int) (uuid.getLeastSignificantBits() >> 32);
		arr[3] = (int) uuid.getLeastSignificantBits();
		return arr;
	}

	/**
	 * 将长为4的int数组标签转化为UUID</br>
	 * （本类未使用）
	 * 
	 * @see #uuidToIntArray(UUID)
	 * @param uuidListTag
	 * @return 转化的UUID
	 */
	public static UUID intArrayToUUID(IntArrayTag uuidListTag) {
		long mostSigBits = uuidListTag.get(0).getLong() << 32;
		mostSigBits += uuidListTag.get(1).getLong();
		long leastSigBits = uuidListTag.get(2).getLong() << 32;
		leastSigBits += uuidListTag.get(3).getLong();
		return new UUID(mostSigBits, leastSigBits);
	}
}
