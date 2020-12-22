package net.cpp.block.entity;

import static net.cpp.api.CodingTool.of;
import static net.cpp.api.CodingTool.setOf;
import static net.minecraft.entity.EntityType.BAT;
import static net.minecraft.entity.EntityType.BEE;
import static net.minecraft.entity.EntityType.BLAZE;
import static net.minecraft.entity.EntityType.CAT;
import static net.minecraft.entity.EntityType.CAVE_SPIDER;
import static net.minecraft.entity.EntityType.COW;
import static net.minecraft.entity.EntityType.CREEPER;
import static net.minecraft.entity.EntityType.DOLPHIN;
import static net.minecraft.entity.EntityType.DONKEY;
import static net.minecraft.entity.EntityType.DROWNED;
import static net.minecraft.entity.EntityType.ELDER_GUARDIAN;
import static net.minecraft.entity.EntityType.ENDERMAN;
import static net.minecraft.entity.EntityType.ENDERMITE;
import static net.minecraft.entity.EntityType.EVOKER;
import static net.minecraft.entity.EntityType.FOX;
import static net.minecraft.entity.EntityType.GHAST;
import static net.minecraft.entity.EntityType.GUARDIAN;
import static net.minecraft.entity.EntityType.HOGLIN;
import static net.minecraft.entity.EntityType.HORSE;
import static net.minecraft.entity.EntityType.HUSK;
import static net.minecraft.entity.EntityType.LLAMA;
import static net.minecraft.entity.EntityType.MAGMA_CUBE;
import static net.minecraft.entity.EntityType.MOOSHROOM;
import static net.minecraft.entity.EntityType.OCELOT;
import static net.minecraft.entity.EntityType.PANDA;
import static net.minecraft.entity.EntityType.PARROT;
import static net.minecraft.entity.EntityType.PHANTOM;
import static net.minecraft.entity.EntityType.PIG;
import static net.minecraft.entity.EntityType.PIGLIN;
import static net.minecraft.entity.EntityType.PIGLIN_BRUTE;
import static net.minecraft.entity.EntityType.PILLAGER;
import static net.minecraft.entity.EntityType.POLAR_BEAR;
import static net.minecraft.entity.EntityType.RAVAGER;
import static net.minecraft.entity.EntityType.SHEEP;
import static net.minecraft.entity.EntityType.SHULKER;
import static net.minecraft.entity.EntityType.SILVERFISH;
import static net.minecraft.entity.EntityType.SKELETON;
import static net.minecraft.entity.EntityType.SKELETON_HORSE;
import static net.minecraft.entity.EntityType.SLIME;
import static net.minecraft.entity.EntityType.SPIDER;
import static net.minecraft.entity.EntityType.SQUID;
import static net.minecraft.entity.EntityType.STRAY;
import static net.minecraft.entity.EntityType.STRIDER;
import static net.minecraft.entity.EntityType.TRADER_LLAMA;
import static net.minecraft.entity.EntityType.TURTLE;
import static net.minecraft.entity.EntityType.VILLAGER;
import static net.minecraft.entity.EntityType.VINDICATOR;
import static net.minecraft.entity.EntityType.WANDERING_TRADER;
import static net.minecraft.entity.EntityType.WITCH;
import static net.minecraft.entity.EntityType.WITHER_SKELETON;
import static net.minecraft.entity.EntityType.WOLF;
import static net.minecraft.entity.EntityType.ZOMBIE;
import static net.minecraft.entity.EntityType.ZOMBIE_HORSE;
import static net.minecraft.entity.EntityType.ZOMBIE_VILLAGER;
import static net.minecraft.item.Items.BAMBOO;
import static net.minecraft.item.Items.BEETROOT_SEEDS;
import static net.minecraft.item.Items.BLAZE_POWDER;
import static net.minecraft.item.Items.BONE;
import static net.minecraft.item.Items.BOOK;
import static net.minecraft.item.Items.CARROT;
import static net.minecraft.item.Items.CHARCOAL;
import static net.minecraft.item.Items.CHEST;
import static net.minecraft.item.Items.CHORUS_FRUIT;
import static net.minecraft.item.Items.CLAY_BALL;
import static net.minecraft.item.Items.COAL_BLOCK;
import static net.minecraft.item.Items.DANDELION;
import static net.minecraft.item.Items.EMERALD;
import static net.minecraft.item.Items.END_STONE;
import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;
import static net.minecraft.item.Items.FEATHER;
import static net.minecraft.item.Items.GHAST_TEAR;
import static net.minecraft.item.Items.GLOWSTONE_DUST;
import static net.minecraft.item.Items.GOLD_BLOCK;
import static net.minecraft.item.Items.GOLD_NUGGET;
import static net.minecraft.item.Items.GUNPOWDER;
import static net.minecraft.item.Items.ICE;
import static net.minecraft.item.Items.INK_SAC;
import static net.minecraft.item.Items.IRON_NUGGET;
import static net.minecraft.item.Items.KELP;
import static net.minecraft.item.Items.LEATHER;
import static net.minecraft.item.Items.MAGMA_CREAM;
import static net.minecraft.item.Items.MELON_SEEDS;
import static net.minecraft.item.Items.NETHER_BRICK;
import static net.minecraft.item.Items.PHANTOM_MEMBRANE;
import static net.minecraft.item.Items.POPPY;
import static net.minecraft.item.Items.PORKCHOP;
import static net.minecraft.item.Items.POTATO;
import static net.minecraft.item.Items.PUMPKIN_SEEDS;
import static net.minecraft.item.Items.RABBIT_HIDE;
import static net.minecraft.item.Items.REDSTONE;
import static net.minecraft.item.Items.RED_SAND;
import static net.minecraft.item.Items.ROTTEN_FLESH;
import static net.minecraft.item.Items.SAND;
import static net.minecraft.item.Items.SEAGRASS;
import static net.minecraft.item.Items.SKELETON_SKULL;
import static net.minecraft.item.Items.SLIME_BALL;
import static net.minecraft.item.Items.SPIDER_EYE;
import static net.minecraft.item.Items.STONE_BRICKS;
import static net.minecraft.item.Items.STRING;
import static net.minecraft.item.Items.SWEET_BERRIES;
import static net.minecraft.item.Items.WARPED_FUNGUS;
import static net.minecraft.item.Items.WHEAT;
import static net.minecraft.item.Items.WHEAT_SEEDS;
import static net.minecraft.item.Items.WHITE_CARPET;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.cpp.gui.handler.MobProjectorScreenHandler;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MobProjectorBlockEntity extends AExpMachineBlockEntity {
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1, 2, 3 };
	private static final Map<Set<Item>, Recipe> RECIPES = new HashMap<>();
	public static final int WORK_TIME_TOTAL = 200;
	private DefaultedList<ItemStack> inventory = DefaultedList.ofSize(4, ItemStack.EMPTY);
	private int currentRecipeCode;
	public final PropertyDelegate propertyDelegate = new ExpPropertyDelegate() {
		@Override
		public int size() {
			return 5;
		}

		@Override
		public void set(int index, int value) {
			switch (index) {
			case 4:
				currentRecipeCode = value;
				break;
			default:
				super.set(index, value);
			}
		}

		@Override
		public int get(int index) {
			switch (index) {
			case 4:
				return currentRecipeCode;
			default:
				return super.get(index);
			}
		}
	};

	public MobProjectorBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.MOB_PROJECTOR.getDefaultState());
	}

	public MobProjectorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.MOB_PROJECTOR, blockPos, blockState);
		workTimeTotal = WORK_TIME_TOTAL;
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
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
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		Inventories.fromTag(tag, inventory);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		super.toTag(tag);
		Inventories.toTag(tag, inventory);
		return tag;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new MobProjectorScreenHandler(syncId, playerInventory, this);
	}

	/*
	 * 以下是Tickable的方法
	 */

	public static void tick(World world, BlockPos pos, BlockState state, MobProjectorBlockEntity blockEntity) {
		if (!world.isClient) {
			blockEntity.expBottle(blockEntity.getStack(0));
			boolean reciped = true;// 有配方吗？
			if (blockEntity.getStack(1).getItem() == Items.EGG) {
				Recipe recipe = RECIPES.get(setOf(blockEntity.getStack(2).getItem(), blockEntity.getStack(3).getItem()));
				if (recipe != null) {
					reciped = false;
					blockEntity.currentRecipeCode = recipe.code;
					if (blockEntity.expStorage >= recipe.experience)
						if (blockEntity.workTime >= WORK_TIME_TOTAL) {
							EntityType<? extends MobEntity> entityType = recipe.output();
							int dx = (int) (blockEntity.getOutputDir().getOffsetX() * (1 + entityType.getWidth())) + pos.getX();
							int dy = pos.getY();
							if (blockEntity.getOutputDir().getOffsetY() < 0)
								dy -= entityType.getHeight();
							else if (blockEntity.getOutputDir().getOffsetY() > 0)
								dy += 1;
							int dz = (int) (blockEntity.getOutputDir().getOffsetZ() * (1 + entityType.getWidth())) + pos.getZ();
							entityType.spawn((ServerWorld) world, null, null, null, new BlockPos(dx, dy, dz), SpawnReason.SPAWNER, false, false);
							blockEntity.workTime = 0;
							blockEntity.expStorage -= recipe.experience;
							for (int i = 0; i < 3; i++)
								blockEntity.getStack(i + 1).decrement(1);
						} else {
							blockEntity.workTime++;
//							workTime += 9;
						}
				} else {

				}
			}
			if (reciped) {
				blockEntity.currentRecipeCode = -1;
				blockEntity.workTime = 0;
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
		if (stack.getItem() == Items.EGG)
			return slot == 1;
		if (stack.getItem() == EXPERIENCE_BOTTLE)
			return slot == 0;
		return slot == 2 && !ItemStack.areItemsEqual(stack, getStack(3)) || slot == 3 && !ItemStack.areItemsEqual(stack, getStack(2));
	}

	/*
	 * 以下是自定义方法
	 */

	public int getCurrentRecipeCode() {
		return currentRecipeCode;
	}

	public static void addRecipe(Item input1, Item input2, int exp, EntityType<? extends MobEntity> entityType, int code) {
		addRecipe(input1, input2, exp, of(entityType, 1.), code);
	}

	public static void addRecipe(Item input1, Item input2, int exp, Map<EntityType<? extends MobEntity>, Double> map, int code) {
		Set<Item> set = setOf(input1, input2);
		RECIPES.put(set, new Recipe(set, map, exp, code));
	}

	static {
		for (Item item : ItemTags.WOOL.values())
			addRecipe(item, WHEAT, 4, SHEEP, 1);
		addRecipe(LEATHER, WHEAT, 4, of(COW, .9, MOOSHROOM, .1), 2);
		addRecipe(PORKCHOP, CARROT, 4, PIG, 3);
		addRecipe(PORKCHOP, POTATO, 4, PIG, 4);
		addRecipe(FEATHER, WHEAT_SEEDS, 4, EntityType.CHICKEN, 4);
		addRecipe(FEATHER, BEETROOT_SEEDS, 4, EntityType.CHICKEN, 5);
		addRecipe(FEATHER, PUMPKIN_SEEDS, 4, EntityType.CHICKEN, 5);
		addRecipe(FEATHER, MELON_SEEDS, 4, EntityType.CHICKEN, 5);
		addRecipe(RABBIT_HIDE, CARROT, 4, EntityType.RABBIT, 5);
		addRecipe(RABBIT_HIDE, DANDELION, 4, EntityType.RABBIT, 6);
		addRecipe(FEATHER, CHARCOAL, 4, BAT, 6);
		addRecipe(INK_SAC, Items.COD, 4, SQUID, 7);
		addRecipe(INK_SAC, Items.SALMON, 4, SQUID, 8);
		addRecipe(GUNPOWDER, SAND, 4, CREEPER, 8);
		addRecipe(GUNPOWDER, RED_SAND, 4, CREEPER, 9);
		addRecipe(ROTTEN_FLESH, IRON_NUGGET, 4, of(ZOMBIE, .5, ZOMBIE_VILLAGER, .2, HUSK, .2, DROWNED, .1), 9);
		addRecipe(BONE, Items.ARROW, 4, of(SKELETON, .9, STRAY, .1), 10);
		addRecipe(STRING, SPIDER_EYE, 4, of(SPIDER, .9, CAVE_SPIDER, .1), 11);
		addRecipe(STONE_BRICKS, IRON_NUGGET, 4, SILVERFISH, 12);
		addRecipe(ICE, Items.COD, 4, POLAR_BEAR, 13);
		addRecipe(ICE, Items.SALMON, 4, POLAR_BEAR, 14);
		addRecipe(REDSTONE, GLOWSTONE_DUST, 8, WITCH, 14);
		addRecipe(SLIME_BALL, CLAY_BALL, 8, SLIME, 15);
		addRecipe(PHANTOM_MEMBRANE, FEATHER, 8, PHANTOM, 16);
		addRecipe(GOLD_NUGGET, ROTTEN_FLESH, 8, of(PIGLIN, .8, PIGLIN_BRUTE, .1, HOGLIN, .1), 17);
		addRecipe(GHAST_TEAR, GUNPOWDER, 8, GHAST, 18);
		addRecipe(MAGMA_CREAM, NETHER_BRICK, 8, MAGMA_CUBE, 19);
		addRecipe(BLAZE_POWDER, NETHER_BRICK, 8, BLAZE, 20);
		addRecipe(Items.ENDER_PEARL, END_STONE, 8, ENDERMAN, 21);
		addRecipe(END_STONE, GOLD_NUGGET, 8, ENDERMITE, 22);
		addRecipe(WARPED_FUNGUS, STRING, 8, STRIDER, 23);
		addRecipe(EMERALD, BOOK, 8, of(VILLAGER, .9, WANDERING_TRADER, .1), 24);
		addRecipe(EMERALD, GOLD_BLOCK, 32, of(VINDICATOR, .4, PILLAGER, .4, EVOKER, .1, RAVAGER, .4), 25);
		addRecipe(Items.POTION, Items.COD, 32, of(GUARDIAN, .9, ELDER_GUARDIAN, .1), 26);
		addRecipe(Items.POTION, Items.SALMON, 32, of(GUARDIAN, .9, ELDER_GUARDIAN, .1), 27);
		addRecipe(CHORUS_FRUIT, Items.ENDER_PEARL, 32, SHULKER, 27);
		addRecipe(BONE, COAL_BLOCK, 32, WITHER_SKELETON, 28);
		addRecipe(BONE, SKELETON_SKULL, 32, WOLF, 29);
		addRecipe(Items.COD, SKELETON_SKULL, 32, of(CAT, .9, OCELOT, .1), 30);
		addRecipe(Items.SALMON, SKELETON_SKULL, 32, of(CAT, .9, OCELOT, .1), 31);
		addRecipe(LEATHER, SKELETON_SKULL, 32, of(HORSE, .8, ZOMBIE_HORSE, .1, SKELETON_HORSE, .1), 31);
		addRecipe(CHEST, SKELETON_SKULL, 32, DONKEY, 32);
		addRecipe(WHITE_CARPET, SKELETON_SKULL, 32, of(LLAMA, .9, TRADER_LLAMA, .1), 33);
		addRecipe(FEATHER, SKELETON_SKULL, 32, PARROT, 34);
		addRecipe(SEAGRASS, SKELETON_SKULL, 32, TURTLE, 35);
		addRecipe(SWEET_BERRIES, SKELETON_SKULL, 32, FOX, 36);
		addRecipe(BAMBOO, SKELETON_SKULL, 32, PANDA, 37);
		addRecipe(POPPY, SKELETON_SKULL, 32, BEE, 38);
		addRecipe(KELP, SKELETON_SKULL, 32, DOLPHIN, 39);
		addRecipe(Items.COD, BONE, 32, EntityType.COD, 40);
		addRecipe(Items.SALMON, BONE, 32, EntityType.SALMON, 41);
		addRecipe(Items.TROPICAL_FISH, BONE, 32, EntityType.TROPICAL_FISH, 42);
		addRecipe(Items.PUFFERFISH, BONE, 32, EntityType.PUFFERFISH, 43);
	}

	public static class Recipe {
		public static final Recipe EMPTY = new Recipe(Collections.emptySet(), Collections.emptyMap(), 0, -1);
		public final Set<Item> inputs;
		public final Map<EntityType<? extends MobEntity>, Double> spawnMap;
		public final int experience;
		public final int code;

		public Recipe(Set<Item> inputs, Map<EntityType<? extends MobEntity>, Double> spawnMap, int experience, int code) {
			this.inputs = inputs;
			this.spawnMap = spawnMap;
			this.experience = experience;
			this.code = code;
		}

		public EntityType<? extends MobEntity> output() {
			double r = Math.random();
			for (Entry<EntityType<? extends MobEntity>, Double> entry : spawnMap.entrySet()) {
				if (r < entry.getValue()) {
					return entry.getKey();
				}
				r -= entry.getValue();
			}
			return EntityType.BAT;
		}
	}
}