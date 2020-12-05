package net.cpp.block.entity;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
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
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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
public class CraftingMachineBlockEntity extends AMachineBlockEntity implements RecipeUnlocker, RecipeInputProvider {
	public static final Text TITLE = CppBlocks.CRAFTING_MACHINE.getName();
	private CppCraftingInventory inputInventory = new CppCraftingInventory();
	private int viewerCnt = 0;
	private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<Identifier>();
	/**
	 * 试图输出到指定方向的容器，但是没输出完就塞满了，剩下的物品
	 */
	private ItemStack leftover = ItemStack.EMPTY;
	private final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int size() {
			return 1;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				setOutputDir(IOutputDiractionalBlockEntity.byteToDir((byte) value));
			default:
				break;
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return dirToByte();
			default:
				return -1;
			}
		}
	};
	
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
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		inventoryFromTag(tag, inputInventory);
		leftover = ItemStack.fromTag(tag.getCompound("leftover"));
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		inventoryToTag(tag, inputInventory);
		tag.put("leftover", itemStackToTag(leftover));
		return tag;
	}

	@Override
	public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		CraftingMachineScreenHandler handler = new CraftingMachineScreenHandler(syncId, playerInventory, this,
				propertyDelegate, ScreenHandlerContext.create(world, pos));
		inputInventory.setHandler(handler);
		return handler;
	}

	/*
	 * 以下是SidedInventory的方法
	 */
//	@Override
//	public int[] getAvailableSlots(Direction side) {
//		int[] slots = new int[9];
//		int size = 0;
//		for (int i = 0; i < 9; i++)
//			if (!inputInventory.getStack(i).isEmpty()) {
//				slots[size++] = i;
//			}
//		slots = Arrays.copyOf(slots, size);
//		return slots;
//	}
//
//	@Override
//	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
//		return slot >= 0 && slot < 9 && getStack(slot).getItem().equals(stack.getItem());
//	}
//
//	@Override
//	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
//		return false;
//	}

	/*
	 * 以下是RecipeUnlocker的方法
	 */
	@Override
	public void setLastRecipe(Recipe<?> recipe) {
		if (recipe != null) {
			Identifier identifier = recipe.getId();
			recipesUsed.addTo(identifier, 1);
		}
	}

	@Override
	public Recipe<?> getLastRecipe() {
		return null;
	}

	/*
	 * 以下是RecipeInputProvider的方法
	 */
	@Override
	public void provideRecipeInputs(RecipeFinder finder) {
		inputInventory.provideRecipeInputs(finder);
	}

	/*
	 * 以下是Tickable的方法
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
	 * 以下是Inventory的方法
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


	
}
