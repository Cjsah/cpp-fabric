package net.cpp.init;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;

import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.BeaconEnhancerBlock;
import net.cpp.block.ChestDropperBlock;
import net.cpp.block.CraftingMachineBlock;
import net.cpp.block.DustbinBlock;
import net.cpp.block.GoldenAnvilBlock;
import net.cpp.block.ItemProcessorBlock;
import net.cpp.block.MobProjectorBlock;
import net.cpp.block.TradeMachineBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.GlassBlock;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class CppBlocks {
	public static final Block CRAFTING_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:crafting_machine"), new CraftingMachineBlock());
	public static final Block ALL_IN_ONE_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:all_in_one_machine"), new AllInOneMachineBlock());
	public static final Block TRADE_MACHINE = Registry.register(Registry.BLOCK, new Identifier("cpp:trade_machine"), new TradeMachineBlock());
	public static final Block ITEM_PROCESSER = Registry.register(Registry.BLOCK, new Identifier("cpp:item_processer"), new ItemProcessorBlock());
	public static final Block MOB_PROJECTOR = Registry.register(Registry.BLOCK, new Identifier("cpp:mob_projector"), new MobProjectorBlock());
	public static final Block BEACON_ENHANCER = Registry.register(Registry.BLOCK, new Identifier("cpp:beacon_enhancer"), new BeaconEnhancerBlock());
	public static final Block GOLDEN_ANVIL = Registry.register(Registry.BLOCK, new Identifier("cpp:golden_anvil"), new GoldenAnvilBlock());
	public static final Block DUSTBIN = Registry.register(Registry.BLOCK, new Identifier("cpp:dustbin"), new DustbinBlock());
	public static final Block CHEST_DROPPER = Registry.register(Registry.BLOCK, new Identifier("cpp:chest_dropper"), new ChestDropperBlock());
	public static final Block EMPTY_BOOKSHELF = Registry.register(Registry.BLOCK, new Identifier("cpp:empty_bookshelf"), new AllInOneMachineBlock());
	public static final Block RARE_EARTH_GLASS = Registry.register(Registry.BLOCK, new Identifier("cpp:rare_earth_glass"), new GlassBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static final Block REINFORCED_GLASS = Registry.register(Registry.BLOCK, new Identifier("cpp:reinforced_glass"), new GlassBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50.0F, 3600000.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static final Block MOON_STONE = Registry.register(Registry.BLOCK, new Identifier("cpp:moon_stone"), new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)));
	public static final Block SUN_STONE = Registry.register(Registry.BLOCK, new Identifier("cpp:sun_stone"), new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL)));

	public static void register() {
		registerMachineItem(CRAFTING_MACHINE);
		registerMachineItem(ALL_IN_ONE_MACHINE);
		registerMachineItem(TRADE_MACHINE);
		registerMachineItem(ITEM_PROCESSER);
		registerMachineItem(MOB_PROJECTOR);
		registerMachineItem(BEACON_ENHANCER);
		registerMachineItem(GOLDEN_ANVIL);
		registerMachineItem(DUSTBIN);
		registerMachineItem(CHEST_DROPPER);
		registerMachineItem(EMPTY_BOOKSHELF);
	}

	private static void registerMachineItem(Block block) {
		Registry.register(Registry.ITEM, Registry.BLOCK.getId(block), new BlockItem(block, new Item.Settings().group(CPP_GROUP_MACHINE)));
	}

}
