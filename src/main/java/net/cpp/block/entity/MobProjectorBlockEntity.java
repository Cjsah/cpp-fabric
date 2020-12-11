package net.cpp.block.entity;

import static net.minecraft.entity.EntityType.*;
import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.cpp.gui.handler.MobProjectorScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import static net.minecraft.item.Items.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Direction.Axis;

public class MobProjectorBlockEntity extends AMachineBlockEntity implements SidedInventory {
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2, 3 };
	private static final Map<Set<Item>, Recipe> RECIPES = new HashMap<>();
	public static final int WORK_TIME_TOTAL = 200;
	private int workTime = 0;
	private int expStorage = 0;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {

		@Override
		public int size() {
			return 3;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 0:
				setOutputDir(IOutputDiractionalBlockEntity.byteToDir((byte) value));
				break;
			case 1:
				workTime = value;
				break;
			case 2:
				expStorage = value;
				break;
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 0:
				return dirToByte();
			case 1:
				return workTime;
			case 2:
				return expStorage;
			default:
				return -1;
			}
		}
	};

	public MobProjectorBlockEntity() {
		super(CppBlockEntities.MOB_PROJECTOR);
	}

	/*
	 * 以下是AMachineBlock的方法
	 */
	@Override
	public Text getTitle() {
		return CppBlocks.MOB_PROJECTOR.getName();
	}

	/*
	 * 以下是LootableContainerBlockEntity的方法
	 */
	@Override
	public DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;

	}

	/*
	 * 以下是LockableContainerBlockEntity的方法
	 */
	@Override
	public void fromTag(BlockState state, CompoundTag tag) {
		super.fromTag(state, tag);
		Inventories.fromTag(tag, inventory);
		workTime = tag.getInt("workTime");
		expStorage = tag.getInt("expStorage");
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		tag.putInt("workTime", workTime);
		tag.putInt("expStorage", expStorage);
		return tag;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new MobProjectorScreenHandler(syncId, playerInventory, this);
	}

	/*
	 * 以下是IOutputDiractionalBlockEntity的方法
	 */
	@Override
	public void shiftOutputDir() {
		propertyDelegate.set(0, dirToByte() + 1);
	}

	/*
	 * 以下是Tickable的方法
	 */
	@Override
	public void tick() {
		if (!world.isClient) {
			if (getStack(3).getItem() == EXPERIENCE_BOTTLE && expStorage <= 91) {
				getStack(3).decrement(1);
				expStorage += 9;
			}
			if (getStack(0).getItem() == Items.EGG) {
				Recipe recipe = RECIPES.get(Set.of(getStack(1).getItem(), getStack(2).getItem()));
				if (recipe != null) {
					if (workTime >= WORK_TIME_TOTAL) {
						recipe.output().spawn((ServerWorld) world, null, null, null, pos.offset(Axis.Y, -2),
								SpawnReason.SPAWNER, false, false);
						workTime = 0;
					} else {
						workTime++;
					}
				} else {
					workTime = 0;
				}
			}
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
		return slot == 3 && stack.getItem() == EXPERIENCE_BOTTLE;
	}

	@Override
	public boolean canExtract(int slot, ItemStack stack, Direction dir) {
		return false;
	}

	/*
	 * 以下是自定义方法
	 */

	public boolean isWorking() {
		return workTime > 0;
	}

	public int getExpStorage() {
		return expStorage;
	}

	public int getWorkTime() {
		return workTime;
	}

	public int getWorkTimeTotal() {
		return WORK_TIME_TOTAL;
	}

	public static void addRecipe(Item input1, Item input2, int exp, EntityType<? extends MobEntity> entityType) {
		addRecipe(input1, input2, exp, Map.of(1D, entityType));
	}

	public static void addRecipe(Item input1, Item input2, int exp, Map<Double, EntityType<? extends MobEntity>> map) {
		Set<Item> set = Set.of(input1, input2);
		RECIPES.put(set, new Recipe(set, map, exp));
	}

	static {
		addRecipe(BLUE_WOOL, WHEAT, 4, SHEEP);
		addRecipe(LEATHER, WHEAT, 4, Map.of(0.9D, COW, 0.1D, MOOSHROOM));
		addRecipe(PORKCHOP, CARROT, 4, PIG);
		addRecipe(PORKCHOP, POTATO, 4, PIG);
		addRecipe(FEATHER, WHEAT_SEEDS, 4, EntityType.CHICKEN);
		addRecipe(FEATHER, BEETROOT_SEEDS, 4, EntityType.CHICKEN);
		addRecipe(FEATHER, PUMPKIN_SEEDS, 4, EntityType.CHICKEN);
		addRecipe(FEATHER, MELON_SEEDS, 4, EntityType.CHICKEN);
		addRecipe(RABBIT_HIDE, CARROT, 4, EntityType.RABBIT);
		addRecipe(RABBIT_HIDE, DANDELION, 4, EntityType.RABBIT);
		addRecipe(FEATHER, CHARCOAL, 4, BAT);
		addRecipe(INK_SAC, Items.COD, 4, SQUID);
		addRecipe(INK_SAC, Items.SALMON, 4, SQUID);
		addRecipe(GUNPOWDER, SAND, 4, CREEPER);
		addRecipe(GUNPOWDER, RED_SAND, 4, CREEPER);
		addRecipe(ROTTEN_FLESH, IRON_NUGGET, 4, Map.of(.5, ZOMBIE, .2, ZOMBIE_VILLAGER, .2, HUSK, .1, DROWNED));
		addRecipe(BONE, Items.ARROW, 4, Map.of(.9, SKELETON, .1, STRAY));
		addRecipe(STRING, SPIDER_EYE, 4, Map.of(.9, SPIDER, .1, CAVE_SPIDER));
		addRecipe(STONE_BRICKS, IRON_NUGGET, 4, SILVERFISH);
		addRecipe(ICE, Items.COD, 4, POLAR_BEAR);
		addRecipe(ICE, Items.SALMON, 4, POLAR_BEAR);
		addRecipe(REDSTONE, GLOWSTONE_DUST, 8, WITCH);
		addRecipe(SLIME_BALL, CLAY_BALL, 8, SLIME);
		addRecipe(PHANTOM_MEMBRANE, FEATHER, 8, PHANTOM);
		addRecipe(GOLD_NUGGET, ROTTEN_FLESH, 8, Map.of(.8, PIGLIN, .1, PIGLIN_BRUTE, .1, HOGLIN));
		addRecipe(GHAST_TEAR, GUNPOWDER, 8, GHAST);
		addRecipe(MAGMA_CREAM, NETHER_BRICK, 8, MAGMA_CUBE);
		addRecipe(BLAZE_POWDER, NETHER_BRICK, 8, BLAZE);
		addRecipe(Items.ENDER_PEARL, END_STONE, 8, ENDERMAN);
		addRecipe(END_STONE, GOLD_NUGGET, 8, ENDERMITE);
		addRecipe(WARPED_FUNGUS, STRING, 8, STRIDER);
		addRecipe(EMERALD, BOOK, 8, Map.of(.9, VILLAGER, .1, WANDERING_TRADER));
		addRecipe(EMERALD, GOLD_BLOCK, 32, Map.of(.4, VINDICATOR, .4, PILLAGER, .1, EVOKER, .1, RAVAGER));
		addRecipe(Items.POTION, Items.COD, 32, Map.of(.9, GUARDIAN, .1, ELDER_GUARDIAN));
		addRecipe(Items.POTION, Items.SALMON, 32, Map.of(.9, GUARDIAN, .1, ELDER_GUARDIAN));
		addRecipe(CHORUS_FRUIT, Items.ENDER_PEARL, 32, SHULKER);
		addRecipe(BONE, COAL_BLOCK, 32, WITHER_SKELETON);
		addRecipe(BONE, SKELETON_SKULL, 32, WOLF);
		addRecipe(Items.COD, SKELETON_SKULL, 32, Map.of(.9, CAT, .1, OCELOT));
		addRecipe(Items.SALMON, SKELETON_SKULL, 32, Map.of(.9, CAT, .1, OCELOT));
		addRecipe(LEATHER, SKELETON_SKULL, 32, Map.of(.8, HORSE, .1, ZOMBIE_HORSE, .1, SKELETON_HORSE));
		addRecipe(CHEST, SKELETON_SKULL, 32, DONKEY);
		addRecipe(WHITE_CARPET, SKELETON_SKULL, 32, Map.of(.9, LLAMA, .1, TRADER_LLAMA));
		addRecipe(FEATHER, SKELETON_SKULL, 32, PARROT);
		addRecipe(SEAGRASS, SKELETON_SKULL, 32, TURTLE);
		addRecipe(SWEET_BERRIES, SKELETON_SKULL, 32, FOX);
		addRecipe(BAMBOO, SKELETON_SKULL, 32, PANDA);
		addRecipe(POPPY, SKELETON_SKULL, 32, BEE);
		addRecipe(KELP, SKELETON_SKULL, 32, DOLPHIN);
		addRecipe(Items.COD, BONE, 32, EntityType.COD);
		addRecipe(Items.SALMON, BONE, 32, EntityType.SALMON);
		addRecipe(Items.TROPICAL_FISH, BONE, 32, EntityType.TROPICAL_FISH);
		addRecipe(Items.PUFFERFISH, BONE, 32, EntityType.PUFFERFISH);
	}

	public static class Recipe {
		public static final Recipe EMPTY = new Recipe(Collections.emptySet(), Collections.emptyMap(), 0);
		public final Set<Item> inputs;
		public final Map<Double, EntityType<? extends MobEntity>> spawnMap;
		public final int experience;

		public Recipe(Set<Item> inputs, Map<Double, EntityType<? extends MobEntity>> spawnMap, int experience) {
			this.inputs = inputs;
			this.spawnMap = spawnMap;
			this.experience = experience;
		}

		public EntityType<? extends MobEntity> output() {
			double r = Math.random();
			for (Entry<Double, EntityType<? extends MobEntity>> entry : spawnMap.entrySet()) {
				if (r < entry.getKey())
					return entry.getValue();
				r -= entry.getKey();
			}
			return EntityType.BAT;
		}
	}
}
