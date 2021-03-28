package net.cpp.init;

import static net.cpp.Craftingpp.*;
import static net.minecraft.entity.effect.StatusEffects.*;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.google.common.collect.ImmutableList;

import net.cpp.Craftingpp;
import net.cpp.api.SimpleSaplingGenerator;
import net.cpp.block.*;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractBlock.Settings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.Material;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SkullBlock.SkullType;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.AliasedBlockItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SkullItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;

@SuppressWarnings({"unused", "DuplicatedCode", "SameParameterValue"})
public final class CppBlocks {
	public static final AbstractBlock.Settings FURNACE_SETTINGS = AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(3.5F);
	public static final AbstractBlock.Settings BARREL_SETTINGS = AbstractBlock.Settings.of(Material.WOOD).strength(2.5F).sounds(BlockSoundGroup.WOOD);
	public static final Block CRAFTING_MACHINE = registerMachine("crafting_machine", new CraftingMachineBlock(BARREL_SETTINGS));
	public static final Block ALL_IN_ONE_MACHINE = registerMachine("all_in_one_machine", new AllInOneMachineBlock(FURNACE_SETTINGS));
	public static final Block TRADE_MACHINE = registerMachine("trade_machine", new TradeMachineBlock(BARREL_SETTINGS));
	public static final Block ITEM_PROCESSOR = registerMachine("item_processor", new ItemProcessorBlock(FURNACE_SETTINGS));
	public static final Block MOB_PROJECTOR = registerMachine("mob_projector", new MobProjectorBlock(FURNACE_SETTINGS));
	public static final Block BEACON_ENHANCER = registerMachine("beacon_enhancer", new BeaconEnhancerBlock(FURNACE_SETTINGS));
	public static final Block GOLDEN_ANVIL = registerMachine("golden_anvil", new GoldenAnvilBlock(FURNACE_SETTINGS));
	public static final Block DUSTBIN = registerMachine("dustbin", new DustbinBlock(FURNACE_SETTINGS));
	public static final Block CHEST_DROPPER = registerMachine("chest_dropper", new ChestDropperBlock(BARREL_SETTINGS));
	public static final Block EMPTY_BOOKSHELF = registerMachine("empty_bookshelf", new EmptyBookshelfBlock(BARREL_SETTINGS));
	public static final Block RARE_EARTH_GLASS = registerBlock("rare_earth_glass", new GlassBlock(FabricBlockSettings.of(Material.GLASS).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.GLASS).nonOpaque()), CPP_GROUP_MISC);
	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(FabricBlockSettings.of(Material.GLASS).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50.0F, 3600000.0F).sounds(BlockSoundGroup.GLASS).nonOpaque()), CPP_GROUP_MISC);
	public static final Block MOON_STONE = registerBlock("moon_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)), CPP_GROUP_MISC);
	public static final Block SUN_STONE = registerBlock("sun_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL)), CPP_GROUP_MISC);
	public static final Block BROKEN_SPAWNER = registerBlock("broken_spawner", new BrokenSpawnerBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F).sounds(BlockSoundGroup.METAL).nonOpaque()), CPP_GROUP_MISC);

	public static final Block BLUEBERRY_CAKE = registerBlock("blueberry_cake", new CppCakeBlock(RESISTANCE, 600, 0, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)), CPP_GROUP_FOOD);
	public static final Block STRAWBERRY_CAKE = registerBlock("strawberry_cake", new CppCakeBlock(JUMP_BOOST, 600, 0, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)), CPP_GROUP_FOOD);
	public static final Block CHEESE_CAKE = registerBlock("cheese_cake", new CppCakeBlock(STRENGTH, 600, 0, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)), CPP_GROUP_FOOD);
	public static final Block CHOCOLATE_CAKE = registerBlock("chocolate_cake", new CppCakeBlock(HASTE, 600, 0, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)), CPP_GROUP_FOOD);
	public static final Block ROTTEN_FRESH_CAKE = registerBlock("rotten_flesh_cake", new CppCakeBlock(HUNGER, 100, 254, FabricBlockSettings.of(Material.CAKE).strength(0.5F).sounds(BlockSoundGroup.WOOL)), CPP_GROUP_FOOD);

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
	public static final Block CROCUS = registerFlowerGrass("crocus");
	public static final Block PANSY = registerFlowerGrass("pansy");
	public static final Block ARABIA_SPEEDWELL = registerFlowerGrass("arabia_speedwell");
	public static final Block SILENE_PENDULA = registerFlowerGrass("silene_pendula");
	public static final Block ARTEMISIA_ARGYI = registerFlowerGrass("artemisia_argyi");
	public static final Block BLUE_ROSE = registerBlock("blue_rose", CustomHeightPlantBlock.of(2, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), CPP_GROUP_PLANT);
	public static final Block POINSETTIA = registerPlant("poinsettia");
	public static final Block CHRISTMAS_TREE = registerBlock("christmas_tree", CustomHeightPlantBlock.of(2, FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)), CPP_GROUP_PLANT);
	public static final Block RICE = Registry.register(Registry.BLOCK, new Identifier(MOD_ID3, "rice"), new RiceBlock(FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
	public static final Block FLOWER_PORTAL = Registry.register(Registry.BLOCK, new Identifier(MOD_ID3, "flower_portal"), new FlowerPortalBlock(FabricBlockSettings.of(Material.PORTAL).noCollision().ticksRandomly().strength(-1.0F).sounds(BlockSoundGroup.GLASS).luminance(15)));
	public static final Block STRUCTURE_GENERATOR = Registry.register(Registry.BLOCK, new Identifier(MOD_ID3, "structure_generator"), new StructureGenerationBlock(FabricBlockSettings.of(Material.AIR).noCollision().dropsNothing().air().nonOpaque()));
	public static final Block BLOCK_BREAKER = registerBlock(BlockBreakerBlock.ID, new BlockBreakerBlock(FabricBlockSettings.of(Material.STONE).requiresTool().strength(3.5F)), CPP_GROUP_MACHINE);
	public static final Block FERMENTER = registerBlock("fermenter", new FermenterBlock(FabricBlockSettings.of(Material.WOOD).strength(0.6F).sounds(BlockSoundGroup.WOOD)), CPP_GROUP_MACHINE);
	public static final Block VARIANT_SKULL = registerBlock("variant_skull", new VariantSkullBlock(FabricBlockSettings.of(Material.WOOD).strength(0.6F).sounds(BlockSoundGroup.WOOD)), CPP_GROUP_MISC);

	public static final List<Block> FLOWER_GRASSES = ImmutableList.of(LYCORIS_RADIATA, TRIFOLIUM, BLACKTHORN, CATTAIL, MARIGOLD, HIBISCUS, HYACINTH, CALAMUS, WILD_LILIUM, BAUHINIA, FLUFFY_GRASS, GERBERA, ESPARTO, GLOW_FORSYTHIA, GLAZED_SHADE, STELERA, FORAGE_CRYSTAL, ISORCHID, BURNING_CHRYSANTHE, OXALIS, CALLIOPSIS, CYCLAMEN, IRIS, LILIUM_PUMILUM, SNOWDROP, NARCISSUS, COLE_FLOWER, LUPINE, CROCUS, PANSY, ARABIA_SPEEDWELL, SILENE_PENDULA, ARTEMISIA_ARGYI);

	public static void loadClass() {}
	
	
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
		BlockItem item = Registry.register(Registry.ITEM, identifier, new BlockItem(regBlock, new Item.Settings().group(CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerSapling(String id, BiFunction<SaplingGenerator, Settings, SaplingBlock> constructor, SaplingGenerator saplingGenerator) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, constructor.apply(saplingGenerator, FabricBlockSettings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new BlockItem(regBlock, new Item.Settings().group(CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerFlowerGrass(String id, StatusEffect suspiciousStewEffect, int effectDuration) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, new FlowerGrass1Block(suspiciousStewEffect, effectDuration, FabricBlockSettings.of(Material.PLANT).ticksRandomly().noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerFlowerGrass(String id) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, new FlowerGrass2Block(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerPlant(String id) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block regBlock = Registry.register(Registry.BLOCK, identifier, new PublicPlantBlock(FabricBlockSettings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new AliasedBlockItem(regBlock, new Item.Settings().group(CPP_GROUP_PLANT)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	public static Block registerSkull(String id, SkullType type) {
		Identifier identifier = new Identifier(Craftingpp.MOD_ID3, id);
		Block block = Registry.register(Registry.BLOCK, identifier, new PublicSkullBlock(type, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0F)));
		Block wallBlock = Registry.register(Registry.BLOCK, identifier, new PublicWallSkullBlock(type, AbstractBlock.Settings.of(Material.DECORATION).strength(1.0F)));
		BlockItem item = Registry.register(Registry.ITEM, identifier, new SkullItem(block, wallBlock, new Item.Settings().group(CPP_GROUP_MISC).rarity(Rarity.UNCOMMON)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return block;
	}

	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	private static boolean never(BlockState state, BlockView world, BlockPos pos) {
		return false;
	}

}
