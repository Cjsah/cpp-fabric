package cpp.block.entity;

import static cpp.init.CppBlocks.FRUIT_LEAVES;
import static cpp.init.CppBlocks.FRUIT_SAPLING;
import static cpp.init.CppBlocks.ORE_LEAVES;
import static cpp.init.CppBlocks.ORE_SAPLING;
import static cpp.init.CppBlocks.SAKURA_LEAVES;
import static cpp.init.CppBlocks.SAKURA_SAPLING;
import static cpp.init.CppBlocks.WOOL_LEAVES;
import static cpp.init.CppBlocks.WOOL_SAPLING;
import static cpp.init.CppItems.GRAFTER;
import static cpp.init.CppItems.GREEN_FORCE_OF_WATER;
import static cpp.init.CppItems.RED_FORCE_OF_FIRE;
import static cpp.init.CppItems.SPLINT;
import static net.minecraft.item.Items.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cpp.block.FlowerGrass1Block;
import cpp.init.CppBlockEntities;
import cpp.init.CppBlockTags;
import cpp.init.CppBlocks;
import cpp.init.CppItems;
import cpp.item.Compressor;
import cpp.screen.handler.ItemProcessorScreenHandler;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.SmeltingRecipe;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

/**
 * 物品处理机方块实体
 * 
 * @author Ph-苯
 *
 */
public class ItemProcessorBlockEntity extends AOutputMachineBlockEntity {
	/**
	 * 皮革制品
	 */
	public static final Set<Item> LEATHERS = new HashSet<>(Arrays.asList(LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS));
	/**
	 * 配方
	 */
	public static final Map<Item, Map<Item, ItemStackAndCount>> RECIPES = new HashMap<>();
	/**
	 * 所有矿石
	 */
	public static final Set<BlockItem> ORES;
	private static final int[] AVAILABLE_SLOTS = new int[] { 0, 1 };
	private int lastTickCount = -1;
	public final PropertyDelegate propertyDelegate = new OutputDirectionPropertyDelegate();

	public ItemProcessorBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.ITEM_PROCESSOR.getDefaultState());
	}

	public ItemProcessorBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.ITEM_PROCESSER, blockPos, blockState);
		setCapacity(4);
	}

	@Override
	public PropertyDelegate getPropertyDelegate() {
		return propertyDelegate;
	}

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

	public static void tick(World world, BlockPos pos, BlockState state, ItemProcessorBlockEntity blockEntity) {
		// XXX 代码结构需要优化
		if (!world.isClient) {
			if (!blockEntity.getStack(0).isEmpty() && !blockEntity.getStack(1).isEmpty()) {
				Item tool = blockEntity.getStack(0).getItem();
				ItemStack input1 = blockEntity.getStack(1), output1 = blockEntity.getStack(2);
				if (tool == RED_FORCE_OF_FIRE) {
// 红色火之力
					SmeltingRecipe recipe = world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(input1), blockEntity.world).orElse(null);
					if (recipe != null) {
						ItemStack result = recipe.getOutput();
						boolean processed;
						if (processed = output1.isEmpty())
							blockEntity.setStack(2, result.copy());
						else if (processed = output1.isItemEqual(result) && output1.getCount() + result.getCount() <= output1.getMaxCount())
							output1.increment(result.getCount());
						if (processed)
							input1.decrement(1);
					}
				} else if (tool == CppItems.COMPRESSOR) {
// 压缩器
					if (input1.getCount() >= input1.getMaxCount() && (output1.isEmpty() || output1.getCount() < output1.getMaxCount())) {
						ItemStack output = Compressor.compress(input1);
						if (input1 != output && blockEntity.tryInsert(2, output))
							input1.decrement(input1.getMaxCount());
					}
				} else if (tool == BONE_MEAL) {
//骨粉
					if (input1.getItem() == NETHERRACK) {
						Block block = world.getBlockState(pos.up()).getBlock();
						Set<Block> tmpSet = new HashSet<>(Arrays.asList(Blocks.CRIMSON_NYLIUM, Blocks.WARPED_NYLIUM));
						if (tmpSet.contains(block) && blockEntity.canInsert(2, new ItemStack(block))) {
							blockEntity.insert(2, new ItemStack(block));
							blockEntity.getStack(1).decrement(1);
							blockEntity.getStack(0).decrement(1);
						}
					}
				} else if (tool instanceof MiningToolItem && FabricToolTags.PICKAXES.contains(tool) && ORES.contains(blockEntity.getStack(1).getItem())) {
//镐可以挖掘矿石，受附魔影响
					BlockState blockState = ((BlockItem) blockEntity.getStack(1).getItem()).getBlock().getDefaultState();
					if (tool.isSuitableFor(blockState)) {
						List<ItemStack> list = blockState.getDroppedStacks(new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.ORIGIN, Vec3d.ofCenter(pos)).parameter(LootContextParameters.TOOL, blockEntity.getStack(0)));
						boolean able = true;
						if (list.size() > 2)
							able = false;
						else
							for (int i = 0; i < list.size(); i++) {
								if (!blockEntity.getStack(i + 2).isEmpty() && (!ItemStack.areItemsEqual(blockEntity.getStack(i + 2), list.get(i)) || blockEntity.getStack(i + 2).getCount() + list.get(i).getCount() > blockEntity.getStack(i + 2).getMaxCount())) {
									able = false;
									break;
								}
							}
						if (able) {
							blockEntity.getStack(0).damage(1, world.random, null);
							blockEntity.getStack(1).decrement(1);
							for (int i = 0; i < list.size(); i++) {
								if (blockEntity.getStack(i + 2).isEmpty())
									blockEntity.setStack(i + 2, list.get(i));
								else
									blockEntity.getStack(i + 2).increment(list.get(i).getCount());
							}
						}
					}
				} else {
					ItemStackAndCount itemStackAndCount = RECIPES.getOrDefault(tool, Collections.emptyMap()).get(input1.getItem());
					boolean greenForceOfWater = true;
					if (tool == GREEN_FORCE_OF_WATER) {
						String mode = blockEntity.getStack(0).getOrCreateTag().getString("mode");
//					System.out.println(mode);
						greenForceOfWater = "water".equals(mode);
					}
					if (greenForceOfWater && itemStackAndCount != null && input1.getCount() >= itemStackAndCount.count) {

						boolean used = false;
						if (input1.getItem() == GILDED_BLACKSTONE) {
							if (blockEntity.lastTickCount == -1)
								blockEntity.lastTickCount = 2 + (int) (4 * Math.random());
							if ((output1.isEmpty() || output1.getItem() == GOLD_NUGGET && output1.getCount() + blockEntity.lastTickCount <= output1.getMaxCount())) {
								input1.decrement(1);
								if (output1.isEmpty())
									blockEntity.setStack(2, new ItemStack(GOLD_NUGGET, blockEntity.lastTickCount));
								else
									output1.increment(blockEntity.lastTickCount);
								used = true;
								blockEntity.lastTickCount = -1;
							}
						} else if ((output1.isEmpty() || ItemStack.areItemsEqual(itemStackAndCount.itemStack, output1) && output1.getCount() + itemStackAndCount.itemStack.getCount() <= output1.getMaxCount())) {
							Item result1 = itemStackAndCount.itemStack.getItem();
							ItemStack output2 = blockEntity.getStack(3);
							boolean able = true;
							if (LEATHERS.contains(input1.getItem())) {
								if (output1.isEmpty()) {
									output1 = input1.copy();
									output1.getOrCreateSubTag("display").remove("color");
									blockEntity.setStack(2, output1);
									input1.decrement(1);
								}
							} else {
								if (result1 == CARVED_PUMPKIN) {
									if (output2.isEmpty() || output2.getItem() == PUMPKIN_SEEDS && output2.getCount() + 4 <= output2.getMaxCount()) {
										if (output2.isEmpty())
											blockEntity.setStack(3, new ItemStack(PUMPKIN_SEEDS, 4));
										else
											output2.increment(4);
									} else
										able = false;
								} else if (result1 == OBSIDIAN) {
									if (input1.getItem() == GREEN_FORCE_OF_WATER) {
										able = "lava".equals(input1.getOrCreateTag().getString("mode")) && input1.getTag().getInt("lava") > 0;
									} else if ((output2.isEmpty() || output2.getItem() == BUCKET && output2.getCount() + 1 <= output2.getMaxCount())) {
										if (output2.isEmpty())
											blockEntity.setStack(3, new ItemStack(BUCKET, 1));
										else
											output2.increment(1);
									} else
										able = false;
								}
								if (able) {
									if (input1.getItem() == GREEN_FORCE_OF_WATER) {
										input1.getOrCreateTag().putInt("lava", input1.getOrCreateTag().getInt("lava") - 1);
									} else
										input1.decrement(itemStackAndCount.count);
									if (output1.isEmpty())
										blockEntity.setStack(2, itemStackAndCount.itemStack.copy());
									else
										output1.increment(itemStackAndCount.itemStack.getCount());
									used = true;
								}
							}
						}
						if (used && blockEntity.getStack(0).damage(1, world.random, null)) {
							blockEntity.getStack(0).decrement(1);
						}
					}
				}
			}
			blockEntity.output(2);
			blockEntity.output(3);
		}
	}

	@Override
	protected DefaultedList<ItemStack> getInvStackList() {
		return inventory;
	}

	@Override
	protected void setInvStackList(DefaultedList<ItemStack> list) {
		inventory = list;
	}

	@Override
	protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
		return new ItemProcessorScreenHandler(syncId, playerInventory, this);
	}

	@Override
	public int[] getAvailableSlots(Direction side) {
		return AVAILABLE_SLOTS;
	}

	@Override
	public boolean canInsert(int slot, ItemStack stack, Direction dir) {
		return slot == 0 && stack != null && RECIPES.containsKey(stack.getItem()) || slot == 1;
	}

	/**
	 * 查询物品是否可烧炼
	 * 
	 * @param itemStack 要被查询的物品叠
	 * @return 可烧炼
	 */
	public boolean isSmeltable(ItemStack itemStack) {
		return this.world.getRecipeManager().getFirstMatch(RecipeType.SMELTING, new SimpleInventory(itemStack), this.world).isPresent();
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, Item item2) {
		put(map, item1, item2, 1);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, Item item2, int count2) {
		put(map, item1, 1, item2, count2);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, int count1, Item item2) {
		put(map, item1, count1, item2, 1);
	}

	public static void put(Map<Item, ItemStackAndCount> map, Item item1, int count1, Item item2, int count2) {
		map.put(item1, new ItemStackAndCount(new ItemStack(item2, count2), count1));
	}

	static {
		{
			Map<Item, ItemStackAndCount> /* 临时映射 */ map, /* 占位符 */marker = Collections.emptyMap();

			// 锹
			map = new HashMap<>();
			put(map, GRASS_BLOCK, DIRT);
			put(map, MYCELIUM, DIRT);
			put(map, PODZOL, DIRT);
			put(map, GRAVEL, FLINT);
			put(map, GILDED_BLACKSTONE, GOLD_NUGGET, 2);
			put(map, CLAY, CLAY_BALL, 4);
			put(map, SNOW_BLOCK, SNOWBALL, 4);
			for (Item item : FabricToolTags.SHOVELS.values()) {
				RECIPES.put(item, map);
			}

			// 锄
			map = new HashMap<>();
			put(map, COARSE_DIRT, DIRT);
			for (Item item : FabricToolTags.HOES.values()) {
				RECIPES.put(item, map);
			}

			// 镐
			map = new HashMap<>();
			put(map, STONE, COBBLESTONE);
			put(map, CRIMSON_NYLIUM, NETHERRACK);
			put(map, WARPED_NYLIUM, NETHERRACK);
			put(map, GLOWSTONE, GLOWSTONE_DUST, 4);
			for (Item item : FabricToolTags.PICKAXES.values()) {
				RECIPES.put(item, map);
			}

			// 剪刀
			map = new HashMap<>();
			put(map, PUMPKIN, CARVED_PUMPKIN);
			for (Item item : FabricToolTags.SHEARS.values()) {
				RECIPES.put(item, map);
			}

			// 斧
			map = new HashMap<>();
			put(map, MELON, MELON_SLICE, 9);
			put(map, ACACIA_LOG, STRIPPED_ACACIA_LOG);
			put(map, ACACIA_WOOD, STRIPPED_ACACIA_WOOD);
			put(map, BIRCH_LOG, STRIPPED_BIRCH_LOG);
			put(map, BIRCH_WOOD, STRIPPED_BIRCH_WOOD);
			put(map, CRIMSON_STEM, STRIPPED_CRIMSON_STEM);
			put(map, CRIMSON_HYPHAE, STRIPPED_CRIMSON_HYPHAE);
			put(map, DARK_OAK_LOG, STRIPPED_DARK_OAK_LOG);
			put(map, DARK_OAK_WOOD, STRIPPED_DARK_OAK_WOOD);
			put(map, JUNGLE_LOG, STRIPPED_JUNGLE_LOG);
			put(map, JUNGLE_WOOD, STRIPPED_JUNGLE_WOOD);
			put(map, OAK_LOG, STRIPPED_OAK_LOG);
			put(map, OAK_WOOD, STRIPPED_OAK_WOOD);
			put(map, SPRUCE_LOG, STRIPPED_SPRUCE_LOG);
			put(map, SPRUCE_WOOD, STRIPPED_SPRUCE_WOOD);
			put(map, WARPED_STEM, STRIPPED_WARPED_STEM);
			put(map, WARPED_HYPHAE, STRIPPED_WARPED_HYPHAE);
			for (Item item : FabricToolTags.AXES.values()) {
				RECIPES.put(item, map);
			}

			// 剪枝器
			map = new HashMap<>();
			put(map, ACACIA_LEAVES, ACACIA_SAPLING);
			put(map, BIRCH_LEAVES, BIRCH_SAPLING);
			put(map, DARK_OAK_LEAVES, DARK_OAK_SAPLING);
			put(map, JUNGLE_LEAVES, JUNGLE_SAPLING);
			put(map, OAK_LEAVES, OAK_SAPLING);
			put(map, SPRUCE_LEAVES, SPRUCE_SAPLING);
			put(map, FRUIT_LEAVES.asItem(), FRUIT_SAPLING.asItem());
			put(map, ORE_LEAVES.asItem(), ORE_SAPLING.asItem());
			put(map, SAKURA_LEAVES.asItem(), SAKURA_SAPLING.asItem());
			put(map, WOOL_LEAVES.asItem(), WOOL_SAPLING.asItem());
			put(map, NETHER_WART_BLOCK, CRIMSON_FUNGUS);
			put(map, WARPED_WART_BLOCK, WARPED_FUNGUS);
			put(map, BROWN_MUSHROOM_BLOCK, BROWN_MUSHROOM);
			put(map, RED_MUSHROOM_BLOCK, RED_MUSHROOM);
			RECIPES.put(GRAFTER, map);

			// 活塞
			map = new HashMap<>();
			put(map, WHEAT, 9, HAY_BLOCK);
			put(map, MELON_SLICE, 9, MELON);
			put(map, DRIED_KELP, 9, DRIED_KELP_BLOCK);
			put(map, NETHER_WART, 9, NETHER_WART_BLOCK);
			put(map, SLIME_BALL, 9, SLIME_BLOCK);
			put(map, BONE_MEAL, 9, BONE_BLOCK);
			put(map, SPLINT, 9, DIAMOND);
			put(map, COAL, 9, COAL_BLOCK);
			put(map, IRON_INGOT, 9, IRON_BLOCK);
			put(map, GOLD_INGOT, 9, GOLD_BLOCK);
			put(map, REDSTONE, 9, REDSTONE_BLOCK);
			put(map, LAPIS_LAZULI, 9, LAPIS_BLOCK);
			put(map, DIAMOND, 9, DIAMOND_BLOCK);
			put(map, EMERALD, 9, EMERALD_BLOCK);
			put(map, IRON_NUGGET, 9, IRON_INGOT);
			put(map, GOLD_NUGGET, 9, GOLD_INGOT);
			put(map, NETHERITE_INGOT, 9, NETHERITE_BLOCK);
			put(map, RABBIT_HIDE, 4, LEATHER);
			put(map, SNOWBALL, 4, SNOW_BLOCK);
			put(map, GLOWSTONE_DUST, 4, GLOWSTONE);
			put(map, CLAY_BALL, 4, CLAY);
			put(map, QUARTZ, 4, QUARTZ_BLOCK);
			put(map, COPPER_INGOT, 4, COPPER_BLOCK);
			RECIPES.put(PISTON, map);

			// 合成器
			map = new HashMap<>();
			for (Block plant : CppBlockTags.FLOWER_GRASSES_1.values())
				put(map, plant.asItem(), ((FlowerGrass1Block) plant).getSeed(), 3);
			RECIPES.put(CppBlocks.CRAFTING_MACHINE.asItem(), map);

			// 给红色火之力添加占位符
			RECIPES.put(RED_FORCE_OF_FIRE, marker);

			// 水桶
			map = new HashMap<>();
			for (Item item : new Item[] { WHITE_STAINED_GLASS, ORANGE_STAINED_GLASS, MAGENTA_STAINED_GLASS, LIGHT_BLUE_STAINED_GLASS, YELLOW_STAINED_GLASS, LIME_STAINED_GLASS, PINK_STAINED_GLASS, GRAY_STAINED_GLASS, LIGHT_GRAY_STAINED_GLASS, CYAN_STAINED_GLASS, PURPLE_STAINED_GLASS, BLUE_STAINED_GLASS, BROWN_STAINED_GLASS, GREEN_STAINED_GLASS, RED_STAINED_GLASS, BLACK_STAINED_GLASS })
				put(map, item, GLASS);
			for (Item item : new Item[] { WHITE_TERRACOTTA, ORANGE_TERRACOTTA, MAGENTA_TERRACOTTA, LIGHT_BLUE_TERRACOTTA, YELLOW_TERRACOTTA, LIME_TERRACOTTA, PINK_TERRACOTTA, GRAY_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, CYAN_TERRACOTTA, PURPLE_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, GREEN_TERRACOTTA, RED_TERRACOTTA, BLACK_TERRACOTTA })
				put(map, item, TERRACOTTA);
			for (Item item : new Item[] { WHITE_WOOL, ORANGE_WOOL, MAGENTA_WOOL, LIGHT_BLUE_WOOL, YELLOW_WOOL, LIME_WOOL, PINK_WOOL, GRAY_WOOL, LIGHT_GRAY_WOOL, CYAN_WOOL, PURPLE_WOOL, BLUE_WOOL, BROWN_WOOL, GREEN_WOOL, RED_WOOL, BLACK_WOOL })
				put(map, item, WHITE_WOOL);
			for (Item item : new Item[] { WHITE_BED, ORANGE_BED, MAGENTA_BED, LIGHT_BLUE_BED, YELLOW_BED, LIME_BED, PINK_BED, GRAY_BED, LIGHT_GRAY_BED, CYAN_BED, PURPLE_BED, BLUE_BED, BROWN_BED, GREEN_BED, RED_BED, BLACK_BED })
				put(map, item, WHITE_BED);
			put(map, SPONGE, WET_SPONGE);
			put(map, LAVA_BUCKET, OBSIDIAN);
			put(map, GREEN_FORCE_OF_WATER, OBSIDIAN);
			put(map, STICKY_PISTON, PISTON);
			put(map, FILLED_MAP, MAP);
			put(map, GLASS_BOTTLE, POTION);
			{
				Item[] powders = { WHITE_CONCRETE_POWDER, ORANGE_CONCRETE_POWDER, MAGENTA_CONCRETE_POWDER, LIGHT_BLUE_CONCRETE_POWDER, YELLOW_CONCRETE_POWDER, LIME_CONCRETE_POWDER, PINK_CONCRETE_POWDER, GRAY_CONCRETE_POWDER, LIGHT_GRAY_CONCRETE_POWDER, CYAN_CONCRETE_POWDER, PURPLE_CONCRETE_POWDER, BLUE_CONCRETE_POWDER, BROWN_CONCRETE_POWDER, GREEN_CONCRETE_POWDER, RED_CONCRETE_POWDER, BLACK_CONCRETE_POWDER }, concretes = { WHITE_CONCRETE, ORANGE_CONCRETE, MAGENTA_CONCRETE, LIGHT_BLUE_CONCRETE, YELLOW_CONCRETE, LIME_CONCRETE, PINK_CONCRETE, GRAY_CONCRETE, LIGHT_GRAY_CONCRETE, CYAN_CONCRETE, PURPLE_CONCRETE, BLUE_CONCRETE, BROWN_CONCRETE, GREEN_CONCRETE, RED_CONCRETE, BLACK_CONCRETE };
				for (int i = 0; i < powders.length; i++)
					put(map, powders[i], concretes[i]);
			}
			for (Item item : new Item[] { LEATHER_HELMET, LEATHER_CHESTPLATE, LEATHER_LEGGINGS, LEATHER_BOOTS })
				put(map, item, item);
			for (Item item : new Item[] { WHITE_BANNER, ORANGE_BANNER, MAGENTA_BANNER, LIGHT_BLUE_BANNER, YELLOW_BANNER, LIME_BANNER, PINK_BANNER, GRAY_BANNER, LIGHT_GRAY_BANNER, CYAN_BANNER, PURPLE_BANNER, BLUE_BANNER, BROWN_BANNER, GREEN_BANNER, RED_BANNER, BLACK_BANNER })
				put(map, item, item);
			RECIPES.put(WATER_BUCKET, map);

			// 绿色水之力可替代水桶，所以拥有水桶全部配方，此外还可以填充水桶
			put(map, BUCKET, WATER_BUCKET);
			RECIPES.put(GREEN_FORCE_OF_WATER, map);

			map = new HashMap<>();
			put(map, DIRT, GRASS_BLOCK);
			RECIPES.put(GRASS_BLOCK, map);

			map = new HashMap<>();
			put(map, DIRT, MYCELIUM);
			RECIPES.put(MYCELIUM, map);

			RECIPES.put(BONE_MEAL, marker);

			RECIPES.put(CppItems.COMPRESSOR, marker);
		}
		{
			Set<BlockItem> set = new HashSet<>();
			for (Entry<RegistryKey<Item>, Item> entry : Registry.ITEM.getEntries())
				if (entry.getKey().getValue().getPath().contains("_ore"))
					if (entry.getValue() instanceof BlockItem)
						set.add((BlockItem) entry.getValue());
			set.add((BlockItem) ANCIENT_DEBRIS);
			ORES = Collections.unmodifiableSet(set);
		}
	}

	public static class ItemStackAndCount {
		public final ItemStack itemStack;
		public final int count;

		public ItemStackAndCount(ItemStack itemStack) {
			this(itemStack, 1);
		}

		public ItemStackAndCount(ItemStack itemStack, int count) {
			this.itemStack = itemStack;
			this.count = count;
		}

//		@Override
//		public boolean equals(Object obj) {
//			if (obj == null)
//				return false;
//			if (obj == this)
//				return true;
//			if (obj instanceof ItemStackAndCount) {
//				ItemStackAndCount o = (ItemStackAndCount) obj;
//				return count <= o.count && itemStack.equals(o.itemStack);
//			}
//			return false;
//		}
//
//		@Override
//		public int hashCode() {
//			return itemStack.hashCode();
//		}
	}
}
