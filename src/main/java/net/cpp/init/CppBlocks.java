package net.cpp.init;

import static net.cpp.Craftingpp.CPP_GROUP_MACHINE;

import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.BeaconEnhancerBlock;
import net.cpp.block.ChestDropperBlock;
import net.cpp.block.CraftingMachineBlock;
import net.cpp.block.DustbinBlock;
import net.cpp.block.EmptyBookshelfBlock;
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
import net.minecraft.util.registry.Registry;

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
	public static final Block RARE_EARTH_GLASS = registerBlock("rare_earth_glass", new GlassBlock(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static final Block REINFORCED_GLASS = registerBlock("reinforced_glass", new GlassBlock(FabricBlockSettings.of(Material.STONE).requiresTool().breakByTool(FabricToolTags.PICKAXES, 3).strength(50.0F, 3600000.0F).sounds(BlockSoundGroup.METAL).nonOpaque()));
	public static final Block MOON_STONE = registerBlock("moon_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(5.0F, 6.0F).sounds(BlockSoundGroup.METAL)));
	public static final Block SUN_STONE = registerBlock("sun_stone", new Block(FabricBlockSettings.of(Material.METAL).requiresTool().breakByTool(FabricToolTags.PICKAXES, 2).strength(3.0F, 6.0F).sounds(BlockSoundGroup.METAL)));

	public static void register() {
	}

	private static Block registerMachine(String id, Block block) {
		Block regBlock = Registry.register(Registry.BLOCK, "cpp:" + id, block);
		BlockItem item = Registry.register(Registry.ITEM, Registry.BLOCK.getId(regBlock), new BlockItem(regBlock, new Item.Settings().group(CPP_GROUP_MACHINE)));
		item.appendBlocks(Item.BLOCK_ITEMS, item);
		return regBlock;
	}

	private static Block registerBlock(String id, Block block) {
		return Registry.register(Registry.BLOCK, "cpp:" + id, block);
	}
}
