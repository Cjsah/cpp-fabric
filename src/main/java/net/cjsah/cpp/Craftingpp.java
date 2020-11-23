package net.cjsah.cpp;

import net.cjsah.cpp.init.CppBlockEntities;
import net.cjsah.cpp.init.CppBlocks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Craftingpp implements ModInitializer {

	public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(new Identifier("cpp", "title")).icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();

	@Override
	public void onInitialize() {
		CppBlocks.register();
		CppBlockEntities.register();
		CppBlockEntities.registerServerGUI();

//		Registry.register(Registry.BLOCK, new Identifier("cpp", "crafting_machine"), CRAFTING_MACHINE_BLOCK);
//		Registry.register(Registry.ITEM, new Identifier("cpp", "crafting_machine"), new BlockItem(CRAFTING_MACHINE_BLOCK, new Item.Settings().group(CPP_GROUP)));
////		CRAFTING_MACHINE_HANDLER = Registry.register(Registry.SCREEN_HANDLER, new Identifier("cpp", "crafting_machine"), CRAFTING_MACHINE_HANDLER);
//		CRAFTING_MACHINE_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("cpp", "crafting_machine"), BlockEntityType.Builder.create(CraftingMachineBlockEntity::new).build(null));
//
//		ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier("cpp", "crafting_machine"), ((syncId, identifier, player, buf) -> ((CraftingMachineBlockEntity) Objects.requireNonNull(player.world.getBlockEntity(buf.readBlockPos()))).createMenu(syncId, player.inventory, player)));


	}

//	public static final CraftingMachineBlock CRAFTING_MACHINE_BLOCK = new CraftingMachineBlock();
//	public static final Identifier INSPECT_CRAFTING_MACHINE = new Identifier("cpp","crafting_machine");
//
//	public static BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE_BLOCK_ENTITY;
//
//	public static ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE_HANDLER;



//	// 创造模式物品栏
//	public static final ItemGroup CPP_GROUP = FabricItemGroupBuilder.create(
//			new Identifier("cpp", "title"))
//			.icon(() -> new ItemStack(CRAFTING_MACHINE_BLOCK)).appendItems(stacks -> {
//				stacks.add(new ItemStack(CRAFTING_MACHINE_BLOCK));
//			}).build();
}