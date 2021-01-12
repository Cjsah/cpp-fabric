package net.cpp.init;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;
import static net.cpp.Craftingpp.CPP_GROUP_MISC;

import java.util.function.BiFunction;
import java.util.function.Function;

import net.cpp.Craftingpp;
import net.cpp.api.SimpleSaplingGenerator;
import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.BeaconEnhancerBlock;
import net.cpp.block.ChestDropperBlock;
import net.cpp.block.CraftingMachineBlock;
import net.cpp.block.DustbinBlock;
import net.cpp.block.EmptyBookshelfBlock;
import net.cpp.block.FruitLeavesBlock;
import net.cpp.block.GoldenAnvilBlock;
import net.cpp.block.ItemProcessorBlock;
import net.cpp.block.MobProjectorBlock;
import net.cpp.block.OreLeavesBlock;
import net.cpp.block.PublicSaplingBlock;
import net.cpp.block.TradeMachineBlock;
import net.cpp.block.WoolLeavesBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

public final class CppBlocks {
	public static final Block CRAFTING_MACHINE = registerMachine("crafting_machine", new CraftingMachineBlock());
	public static final Block ALL_IN_ONE_MACHINE = registerMachine("all_in_one_machine", new AllInOneMachineBlock());
	public static final Block TRADE_MACHINE = registerMachine("trade_machine", new TradeMachineBlock());
	public static final Block ITEM_PROCESSER = registerMachine("item_processer", new ItemProcessorBlock());
	public static final Block MOB_PROJECTOR = registerMachine("mob_projector", new MobProjectorBlock());
	public static final Block BEACON_ENHANCER = registerMachine("beacon_enhancer", new BeaconEnhancerBlock());
	public static final Block GOLDEN_ANVIL = registerMachine("golden_anvil", new GoldenAnvilBlock());
	public static final Block DUSTBIN = registerMachine("dustbin", new DustbinBlock());
	public static final Block CHEST_DROPPER = registerMachine("chest_dropper", new ChestDropperBlock());
	public static final Block EMPTY_BOOKSHELF = registerMachine("empty_bookshelf", new EmptyBookshelfBlock());
	public static final Block RARE_EARTH_GLASS = registerBlock("rare_earth_glass", new GlassBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL).nonOpaque()), CPP_GROUP_MISC);
	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50.0F, 3600000.0F).sounds(BlockSoundGroup.METAL).nonOpaque()), CPP_GROUP_MISC);
	public static final Block MOON_STONE = registerBlock("moon_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)), CPP_GROUP_MISC);
	public static final Block SUN_STONE = registerBlock("sun_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL)), CPP_GROUP_MISC);
	public static final Block FRUIT_LEAVES = registerLeaves("fruit_leaves", FruitLeavesBlock::new);
	public static final Block ORE_LEAVES = registerLeaves("ore_leaves", OreLeavesBlock::new);
	public static final Block WOOL_LEAVES = registerLeaves("wool_leaves", WoolLeavesBlock::new);
	public static final Block SAKURA_LEAVES = registerLeaves("sakura_leaves", LeavesBlock::new);
	public static final Block FRUIT_SAPLING = registerSapling("fruit_sapling", PublicSaplingBlock::new, new SimpleSaplingGenerator(Blocks.OAK_LOG, FRUIT_LEAVES));
	public static final Block ORE_SAPLING = registerSapling("ore_sapling", PublicSaplingBlock::new, new SimpleSaplingGenerator(Blocks.SPRUCE_LOG, ORE_LEAVES));
	public static final Block WOOL_SAPLING = registerSapling("wool_sapling", PublicSaplingBlock::new, new SimpleSaplingGenerator(Blocks.BIRCH_LOG, WOOL_LEAVES));
	public static final Block SAKURA_SAPLING = registerSapling("sakura_sapling", PublicSaplingBlock::new, new SimpleSaplingGenerator(Blocks.JUNGLE_LOG, SAKURA_LEAVES));

	public static void register() {
	}

	private static Block registerMachine(String id, Block block) {
		Block regBlock = Registry.register(Registry.BLOCK, new Identifier(Craftingpp.MOD_ID3, id), block);
		BlockItem item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(regBlock), new BlockItem(regBlock, new Item.Settings().group(CPP_GROUP_MACHINE)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerBlock(String id, Block block, ItemGroup group) {
		return Registry.register(Registry.BLOCK, new Identifier(Craftingpp.MOD_ID3, id), block);
	}

	private static Block registerLeaves(String id, Function<Settings, LeavesBlock> constructor) {
		Block regBlock = Registry.register(Registry.BLOCK, new Identifier(Craftingpp.MOD_ID3, id), constructor.apply(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(CppBlocks::canSpawnOnLeaves)/* .suffocates(CppBlocks::never) *//* .blockVision(CppBlocks::never) */));
		BlockItem item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(regBlock), new BlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerSapling(String id, BiFunction<SaplingGenerator, Settings, SaplingBlock> constructor, SaplingGenerator saplingGenerator) {
		Block regBlock = Registry.register(Registry.BLOCK, new Identifier(Craftingpp.MOD_ID3, id), constructor.apply(saplingGenerator, FabricBlockSettings.of(Material.LEAVES).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(regBlock), new BlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	public static Boolean never(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return false;
	}
}
