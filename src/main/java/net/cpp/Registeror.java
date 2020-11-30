package net.cpp;

import net.cpp.block.AllInOneMachineBlock;
import net.cpp.block.CraftingMachineBlock;
import net.cpp.block.entity.CraftingMachineBlockEntity;
import net.cpp.other.CppCraftingShapedRecipe;
import net.cpp.other.CppCraftingShapelessRecipe;
import net.cpp.other.ICppCraftingRecipe;
import net.cpp.screen.CraftingMachineScreenHandler;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class Registeror {
	public static final String MOD_ID = "cpp";
	public static final Item ICON_ITEM = Registry.register(Registry.ITEM,
			new Identifier(Registeror.MOD_ID, "icon_item"), new Item(new Item.Settings()));
	public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.create(new Identifier(MOD_ID, "item_group"))
			.icon(() -> new ItemStack(ICON_ITEM)).build();

	public static final Block CRAFTING_MACHINE_BLOCK = Registry.register(Registry.BLOCK,
			new Identifier(Registeror.MOD_ID, "crafting_machine"), new CraftingMachineBlock());
	public static final Block ALL_IN_ONE_MACHINE_BLOCK = Registry.register(Registry.BLOCK,
			new Identifier(Registeror.MOD_ID, "all_in_one_machine"), new AllInOneMachineBlock());
	public static final BlockEntityType<CraftingMachineBlockEntity> CRAFTING_MACHINE_BLOCK_ENTITY = Registry.register(
			Registry.BLOCK_ENTITY_TYPE, MOD_ID + ":crafting_machine",
			BlockEntityType.Builder.create(CraftingMachineBlockEntity::new, CRAFTING_MACHINE_BLOCK).build(null));
	public static final Identifier INTERACT_WITH_CRAFTING_MACHINE = Registeror
			.registerStats("interact_with_crafting_machine");
	public static final Identifier INTERACT_WITH_ALL_IN_ONE_MACHINE = Registeror
			.registerStats("interact_with_all_in_one_machine");
	public static final ScreenHandlerType<CraftingMachineScreenHandler> CRAFTING_MACHINE_HANDLER_TYPE = ScreenHandlerRegistry
			.registerSimple(Registry.BLOCK.getId(CRAFTING_MACHINE_BLOCK), CraftingMachineScreenHandler::new);
	public static final RecipeType<ICppCraftingRecipe> CPP_CRAFTING = RecipeType.register("cpp:crafting");
	public static final RecipeSerializer<CppCraftingShapedRecipe> CPP_CRAFTING_SHAPED = RecipeSerializer
			.register("cpp:crafting_shaped", new CppCraftingShapedRecipe.Serializer());
	public static final RecipeSerializer<CppCraftingShapelessRecipe> CPP_CRAFTING_SHAPELESS = RecipeSerializer
			.register("cpp:crafting_shapeless", new CppCraftingShapelessRecipe.Serializer());

	static {
		registerItem(CRAFTING_MACHINE_BLOCK);
		registerItem(ALL_IN_ONE_MACHINE_BLOCK);
	}

	static void register() {

	}

	private static Item registerItem(Block block) {
		return Registry.register(Registry.ITEM, Registry.BLOCK.getId(block),
				new BlockItem(block, new Item.Settings().group(ITEM_GROUP)));
	}

	private static Identifier registerStats(String name) {
		Identifier identifier = new Identifier(MOD_ID, name);
		Registry.register(Registry.CUSTOM_STAT, MOD_ID + ":" + name, identifier);
		return identifier;
	}

}
