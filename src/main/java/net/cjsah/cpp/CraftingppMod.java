package net.cjsah.cpp;

import net.cjsah.cpp.block.CraftingMachineBlock;
import net.cjsah.cpp.blockentity.CraftingMachineBlockEntity;
import net.cjsah.cpp.inventory.CraftingMachineScreenHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CraftingppMod implements ModInitializer {

	@Override
	public void onInitialize() {

		// 注册物品
//		Registry.register(Registry.BLOCK, new Identifier("cpp", "crafting_machine"), CRAFTING_MACHINE);
//		Registry.register(Registry.ITEM, new Identifier("cpp", "crafting_machine"), new BlockItem(CRAFTING_MACHINE, new Item.Settings().group(CPP_GROUP)));
//		CRAFTING_MACHINE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, "cpp:crafting_machine", BlockEntityType.Builder.create(CraftingMachineEntity::new, CRAFTING_MACHINE).build(null));
		Registry.register(Registry.BLOCK, new Identifier("cpp", "crafting_machine"), CRAFTING_MACHINE_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("cpp", "crafting_machine"), new BlockItem(CRAFTING_MACHINE_BLOCK, new Item.Settings().group(CPP_GROUP)));
	}



	// 新物品
//	public static final Block CRAFTING_MACHINE = new Block(FabricBlockSettings.of(Material.METAL).build());

	public static final Block CRAFTING_MACHINE_BLOCK = new CraftingMachineBlock(AbstractBlock.Settings.of(Material.WOOD).strength(3.0F, 4.8F).sounds(BlockSoundGroup.WOOD));

	// 方块实体
	public static BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE_BLOCK_ENTITY;

	public static ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE_SCREEN_HANDLER;

	public static Identifier CRAFTING_MACHINE;

	// 创造模式物品栏
	public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(
			new Identifier("cpp", "title"))
			.icon(() -> new ItemStack(CRAFTING_MACHINE_BLOCK)).appendItems(stacks -> {
				stacks.add(new ItemStack(CRAFTING_MACHINE_BLOCK));
			}).build();
}
