package net.cpp.init;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;
import static net.cpp.Craftingpp.CPP_GROUP_MISC;
import static net.minecraft.entity.effect.StatusEffects.BLINDNESS;
import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;
import static net.minecraft.entity.effect.StatusEffects.GLOWING;
import static net.minecraft.entity.effect.StatusEffects.HASTE;
import static net.minecraft.entity.effect.StatusEffects.INVISIBILITY;
import static net.minecraft.entity.effect.StatusEffects.JUMP_BOOST;
import static net.minecraft.entity.effect.StatusEffects.LEVITATION;
import static net.minecraft.entity.effect.StatusEffects.MINING_FATIGUE;
import static net.minecraft.entity.effect.StatusEffects.NAUSEA;
import static net.minecraft.entity.effect.StatusEffects.POISON;
import static net.minecraft.entity.effect.StatusEffects.REGENERATION;
import static net.minecraft.entity.effect.StatusEffects.RESISTANCE;
import static net.minecraft.entity.effect.StatusEffects.SATURATION;
import static net.minecraft.entity.effect.StatusEffects.SLOWNESS;
import static net.minecraft.entity.effect.StatusEffects.SPEED;
import static net.minecraft.entity.effect.StatusEffects.STRENGTH;
import static net.minecraft.entity.effect.StatusEffects.WATER_BREATHING;
import static net.minecraft.entity.effect.StatusEffects.WEAKNESS;
import static net.minecraft.entity.effect.StatusEffects.WITHER;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.cpp.Craftingpp;
import net.cpp.api.SimpleSaplingGenerator;
import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.BeaconEnhancerBlock;
import net.cpp.block.ChestDropperBlock;
import net.cpp.block.CraftingMachineBlock;
import net.cpp.block.DustbinBlock;
import net.cpp.block.EmptyBookshelfBlock;
import net.cpp.block.FlowerGrass1Block;
import net.cpp.block.FlowerGrass2Block;
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
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.AliasedBlockItem;
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
	public static final Block LYCORIS_RADIATA = registerFlowerGrass("lycoris_radiata", STRENGTH, 1);
	public static final Block TRIFOLIUM = registerFlowerGrass("trifolium", MINING_FATIGUE, 1);
	public static final Block BLACKTHORN = registerFlowerGrass("blackthorn", WITHER, 1);
	public static final Block CATTAIL = registerFlowerGrass("cattail", INVISIBILITY, 1);
	public static final Block MARIGOLD = registerFlowerGrass("marigold", HASTE, 1);
	public static final Block HIBISCUS = registerFlowerGrass("hibiscus", WEAKNESS, 1);
	public static final Block HYACINTH = registerFlowerGrass("hyacinth", JUMP_BOOST, 1);
	public static final Block CALAMUS = registerFlowerGrass("calamus", NAUSEA, 1);
	public static final Block WILD_LILIUM = registerFlowerGrass("wild_lilium", POISON, 1);
	public static final Block BAUHINIA = registerFlowerGrass("bauhinia", SATURATION, 1);
	public static final Block FLUFFY_GRASS = registerFlowerGrass("fluffy_grass", LEVITATION, 1);
	public static final Block GERBERA = registerFlowerGrass("gerbera", BLINDNESS, 1);
	public static final Block ESPARTO = registerFlowerGrass("esparto", SPEED, 1);
	public static final Block GLOW_FORSYTHIA = registerFlowerGrass("glow_forsythia", GLOWING, 1);
	public static final Block GLAZED_SHADE = registerFlowerGrass("glazed_shade", WATER_BREATHING, 1);
	public static final Block STELERA = registerFlowerGrass("stelera", POISON, 1);
	public static final Block FORAGE_CRYSTAL = registerFlowerGrass("forage_crystal", REGENERATION, 1);
	public static final Block ISORCHID = registerFlowerGrass("isorchid", RESISTANCE, 1);
	public static final Block BURNING_CHRYSANTHE = registerFlowerGrass("burning_chrysanthe", FIRE_RESISTANCE, 1);
	public static final Block OXALIS = registerFlowerGrass("oxalis", SLOWNESS, 1);
	public static final Block CALLIOPSIS = registerFlowerGrass("calliopsis");
	public static final Block CYCLAMEN = registerFlowerGrass("cyclamen");
	public static final Block IRIS = registerFlowerGrass("iris");
	public static final Block LILIUM_PUMILUM = registerFlowerGrass("lilium_pumilum");
	public static final Block SNOWDROP = registerFlowerGrass("snowdrop");
	public static final Block NARCISSUS = registerFlowerGrass("narcissus");
	public static final Block COLE_FLOWER = registerFlowerGrass("cole_flower");
	public static final Block LUPINE = registerFlowerGrass("lupine");
	public static final Block CROCU = registerFlowerGrass("crocu");
	public static final Block PANSY = registerFlowerGrass("pansy");
	public static final Block ARABIA_SPEEDWELL = registerFlowerGrass("arabia_speedwell");
	public static final Block SILENE_PENDULA = registerFlowerGrass("silene_pendula");
	public static final Block ARTEMISIA_ARGYI = registerFlowerGrass("artemisia_argyi");
	public static final List<Block> FLOWER_GRASSES = ImmutableList.of(LYCORIS_RADIATA, TRIFOLIUM, BLACKTHORN, CATTAIL, MARIGOLD, HIBISCUS, HYACINTH, CALAMUS, WILD_LILIUM, BAUHINIA, FLUFFY_GRASS, GERBERA, ESPARTO, GLOW_FORSYTHIA, GLAZED_SHADE, STELERA, FORAGE_CRYSTAL, ISORCHID, BURNING_CHRYSANTHE, OXALIS, CALLIOPSIS, CYCLAMEN, IRIS, LILIUM_PUMILUM, SNOWDROP, NARCISSUS, COLE_FLOWER, LUPINE, CROCU, PANSY, ARABIA_SPEEDWELL, SILENE_PENDULA, ARTEMISIA_ARGYI);

	public static void register() {
	}

	private static Block registerMachine(String id, Block block) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, block);
		BlockItem item = Registry.register(Registry.ITEM, identifier, new BlockItem(regBlock, new Item.Settings().group(CPP_GROUP_MACHINE)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerBlock(String id, Block block, ItemGroup group) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, block);
		BlockItem item = Registry.register(Registry.ITEM, identifier, new BlockItem(regBlock, new Item.Settings().group(group)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerLeaves(String id, Function<Settings, LeavesBlock> constructor) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, constructor.apply(FabricBlockSettings.of(Material.LEAVES).strength(0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(CppBlocks::canSpawnOnLeaves)/* .suffocates(CppBlocks::never) *//* .blockVision(CppBlocks::never) */));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new BlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerSapling(String id, BiFunction<SaplingGenerator, Settings, SaplingBlock> constructor, SaplingGenerator saplingGenerator) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, constructor.apply(saplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerFlowerGrass(String id, StatusEffect suspiciousStewEffect, int effectDuration) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, new FlowerGrass1Block(suspiciousStewEffect, effectDuration, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerFlowerGrass(String id) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, new FlowerGrass2Block(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(Craftingpp.CPP_GROUP_PLANT)));
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
