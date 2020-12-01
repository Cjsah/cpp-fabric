package net.cpp.block.entity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppRecipes;
import net.cpp.recipe.ICppCraftingRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;


/**
 * 合成器方块实体
 * 
 * @author Ph-苯
 *
 */
public class CraftingMachineBlockEntity extends AMachineBlockEntity {
	public static final Text TITLE = CppBlocks.CRAFTING_MACHINE.getName();
	private CppCraftingInventory inputInventory = new CppCraftingInventory();
	private int viewerCnt = 0;
//	private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<Identifier>();
	/**
	 * 试图输出到指定方向的容器，但是没输出完就塞满了，剩下的物品
	 */
	private ItemStack leftover = ItemStack.EMPTY;

	public CraftingMachineBlockEntity() {
		super(CppBlockEntities.CRAFTING_MACHINE);
	}
	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Text getTitle() {
		return TITLE;
	}
	/*
	 * 以下是LootableContainerBlockEntity的方法
	 */
	@Override
	public ItemStack getStack(int slot) {
		if (slot < 9)
			return inputInventory.getStack(slot);
		if (slot == 9)
			return leftover;
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot, int amount) {
		if (slot < 9) {
			return inputInventory.removeStack(slot, amount);
		}
		if (slot == 9) {
			ItemStack itemStack;
			if (amount >= leftover.getCount()) {
				itemStack = leftover;
				leftover = ItemStack.EMPTY;
			} else {
				itemStack = leftover.copy();
				itemStack.setCount(amount);
				leftover.setCount(leftover.getCount() - amount);
			}
			return itemStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public ItemStack removeStack(int slot) {
		if (slot < 9) {
			return inputInventory.removeStack(slot);
		}
		if (slot == 9) {
			ItemStack itemStack = leftover;
			leftover = ItemStack.EMPTY;
			return itemStack;
		}
		return ItemStack.EMPTY;
	}

	@Override
	public void setStack(int slot, ItemStack stack) {
		if (slot < 9) {
			inputInventory.setStack(slot, stack);
		}
		if (slot == 9) {
			leftover = stack.copy();
		}
	}

	@Override
	public void clear() {
		inputInventory.clear();
		leftover = ItemStack.EMPTY;
	}

	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		DefaultedList<ItemStack> list = DefaultedList.ofSize(10, ItemStack.EMPTY);
		for (int i = 0; i < size(); i++)
			list.set(i, getStack(i));
		return list;
	}

	@Override
	public void setInvStackList(DefaultedList<ItemStack> list) {
		for (int i = 0; i < size(); i++)
			setStack(i, list.get(i));
	}

	/*
	 * 以下是LockableContainerBlockEntity的方法（非 Javadoc）
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		inventoryFromTag(tag, inputInventory);
		leftover = ItemStack.fromTag(tag.getCompound("leftover"));
		outputDir = IOutputDiractionalBlockEntity.byteToDir(tag.getByte("outputDir"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		inventoryToTag(tag, inputInventory);
		tag.put("leftover", itemStackToTag(leftover));
		tag.putByte("outputDir", IOutputDiractionalBlockEntity.dirToByte(outputDir));
		return tag;
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		CraftingMachineScreenHandler handler = new CraftingMachineScreenHandler(syncId, playerInventory, this,
				super.propertyDelegate, ScreenHandlerContext.create(world, pos));
		inputInventory.setHandler(handler);
		return handler;
	}

	/*
	 * 以下是SidedInventory的方法（非 Javadoc）
	 */
	@Override
	public int[] getAvailableSlots(Direction side) {
		int[] slots = new int[9];
		int size = 0;
		for (int i = 0; i < 9; i++)
			if (!inputInventory.getStack(i).isEmpty()) {
				slots[size++] = i;
			}
		slots = Arrays.copyOf(slots, size);
		return slots;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot >= 0 && slot < 9 && getStack(slot).getItem().equals(stack.getItem());
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

//	/*
//	 * 以下是RecipeUnlocker的方法（非 Javadoc）
//	 */
//	@Override
//	public void setLastRecipe(Recipe<?> recipe) {
//		if (recipe != null) {
//			Identifier identifier = recipe.getId();
//			this.recipesUsed.addTo(identifier, 1);
//		}
//	}
//
//	@Override
//	public Recipe<?> getLastRecipe() {
//		return null;
//	}
//
//	/*
//	 * 以下是RecipeInputProvider的方法（非 Javadoc）
//	 */
//	@Override
//	public void provideRecipeInputs(RecipeFinder finder) {
//		inputInventory.provideRecipeInputs(finder);
//	}

	/*
	 * 以下是Tickable的方法（非 Javadoc）
	 */
	@Override
	public void tick() {
		if (!getWorld().isClient) {
			world.updateComparators(pos, getCachedState().getBlock());
			if (getOutputInventory() != null) {
				if (!getLeftover().isEmpty()) {
					setLeftover(output(getLeftover()));
				}
				if (!isEmpty() && viewerCnt <= 0) {
					ItemStack outputStack = getResult();
					if (!getResult().isEmpty()) {
						boolean everyNotSingle = true;
						for (int i = 0; i < 9; i++) {
							if (inputInventory.getStack(i).getCount() == 1) {
								everyNotSingle = false;
							}
						}
						if (everyNotSingle) {
							ItemStack restStack = output(getResult());
							if (!outputStack.equals(restStack)) {
								setLeftover(restStack);
								for (int i = 0; i < 9; i++) {
									inputInventory.removeStack(i, 1);
								}
							}
						}
					}
				}
			}
		}
	}

	/*
	 * 以下是Inventory的方法（非 Javadoc）
	 */
	@Override
	public int size() {
		return 10;
	}

	@Override
	public void onOpen(PlayerEntity player) {
		if (!player.isSpectator()) {
			if (this.viewerCnt < 0) {
				this.viewerCnt = 0;
			}
			++this.viewerCnt;
		}
	}

	@Override
	public void onClose(PlayerEntity player) {
		if (!player.isSpectator()) {
			--this.viewerCnt;
		}
	}

	/*
	 * 以下是自定义方法
	 */
	/**
	 * @see #leftover
	 */
	public ItemStack getLeftover() {
		return getStack(9);
	}

	/**
	 * @see #leftover
	 */
	public void setLeftover(ItemStack i) {
		setStack(9, i);
	}

	/**
	 * 判断一个配方能否被使用。当且仅当任意打开过该合成器的玩家都没有解锁该配方且游戏规则“限制合成”开启时才禁止被使用</br>
	 * （未完成，目前永远返回true）
	 * 
	 * @param recipe
	 * @return
	 * @see RecipeUnlocker#shouldCraftRecipe(World, ServerPlayerEntity, Recipe)
	 */
	public boolean shouldCraftRecipe(Recipe<?> recipe) {
		return true;
	}

	public void setScreenHandler(CraftingMachineScreenHandler screenHandler) {
		inputInventory.setHandler(screenHandler);
	}

	public CppCraftingInventory getInputInventory() {
		return inputInventory;
	}

	public ItemStack getResult() {
		ItemStack itemStack = ItemStack.EMPTY;
		if (!getWorld().isClient) {
			Optional<ICppCraftingRecipe> optional = getWorld().getServer().getRecipeManager()
					.getFirstMatch(CppRecipes.CRAFTING, inputInventory, getWorld());
			if (optional.isPresent()) {
				ICppCraftingRecipe craftingRecipe = optional.get();
				if (shouldCraftRecipe(craftingRecipe)) {
					itemStack = craftingRecipe.craft(inputInventory).copy();
				}
			}
		}
		return itemStack;
	}

	/**
	 * 输出一叠物品
	 * 
	 * @param outputStack 要被输出的物品
	 * @return 因输出不下而剩下的物品
	 */
	public ItemStack output(ItemStack outputStack) {
		Inventory inventory = getOutputInventory();
		ItemStack restStack = ItemStack.EMPTY;
		if (inventory != null) {
			Direction direction = getOutputDir();
			if (!this.isInventoryFull(inventory, direction)) {
				restStack = transfer(this, inventory, outputStack, direction);
				if (restStack.isEmpty()) {
					inventory.markDirty();
				}

			}
		}
		return restStack;
	}

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
