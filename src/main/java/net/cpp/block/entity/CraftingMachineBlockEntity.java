package net.cpp.block.entity;

import java.util.Optional;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppRecipes;
import net.cpp.recipe.ICppCraftingRecipe;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeInputProvider;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.network.ServerPlayerEntity;
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
public class CraftingMachineBlockEntity extends AOutputMachineBlockEntity implements RecipeUnlocker, RecipeInputProvider {
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
	private CppCraftingInventory inputInventory = new CppCraftingInventory();
	private int viewerCnt = 0;
	private final Object2IntOpenHashMap<Identifier> recipesUsed = new Object2IntOpenHashMap<Identifier>();
	/**
	 * 试图输出到指定方向的容器，但是没输出完就塞满了，剩下的物品
	 */
	private ItemStack leftover = ItemStack.EMPTY;
	private final PropertyDelegate propertyDelegate = new OutputDirectionPropertyDelegate();

	public CraftingMachineBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.CRAFTING_MACHINE.getDefaultState());
	}

	public CraftingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.CRAFTING_MACHINE, blockPos, blockState);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
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
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
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
		CraftingMachineScreenHandler handler = new CraftingMachineScreenHandler(syncId, playerInventory, this, propertyDelegate, ScreenHandlerContext.create(world, pos));
		inputInventory.setHandler(handler);
		return handler;
	}

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

	public static void tick(World world, BlockPos pos, BlockState state, CraftingMachineBlockEntity blockEntity) {
		if (!blockEntity.getWorld().isClient) {
			world.updateComparators(pos, blockEntity.getCachedState().getBlock());
			if (blockEntity.getOutputInventory() != null) {
				if (!blockEntity.getLeftover().isEmpty()) {
					blockEntity.setLeftover(blockEntity.output(blockEntity.getLeftover()));
				} else if (!blockEntity.isEmpty() && blockEntity.viewerCnt <= 0) {
					ItemStack outputStack = blockEntity.getResult();
					if (!blockEntity.getResult().isEmpty()) {
						boolean everyNotSingle = true;
						for (int i = 0; i < 9; i++) {
							if (blockEntity.inputInventory.getStack(i).getCount() == 1) {
								everyNotSingle = false;
							}
						}
						if (everyNotSingle) {
							ItemStack restStack = blockEntity.output(blockEntity.getResult());
							if (!outputStack.equals(restStack)) {
								blockEntity.setLeftover(restStack);
								for (int i = 0; i < 9; i++) {
									blockEntity.inputInventory.removeStack(i, 1);
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
	 * 以下是SidedInventory的方法
	 */

	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot >= 0 && slot <= 8 && !getStack(slot).isEmpty();
	}

	/*
	 * 以下是自定义方法
	 */
	/**
	 * @see #leftover
	 */
	public ItemStack getLeftover() {
		return getStack(9).copy();
	}

	/**
	 * @see #leftover
	 */
	public void setLeftover(ItemStack i) {
		setStack(9, i);
	}

	/**
	 * 判断一个配方能否被使用。当且仅当任意打开过该合成器的玩家都没有解锁该配方且游戏规则“限制合成”开启时才禁止被使用<br>
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
			Optional<ICppCraftingRecipe> optional = getWorld().getServer().getRecipeManager().getFirstMatch(CppRecipes.CRAFTING, inputInventory, getWorld());
			if (optional.isPresent()) {
				ICppCraftingRecipe craftingRecipe = optional.get();
				if (shouldCraftRecipe(craftingRecipe)) {
					itemStack = craftingRecipe.craft(inputInventory).copy();
				}
			}
		}
		return itemStack;
	}

}
