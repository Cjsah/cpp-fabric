package net.cpp.init;

import net.cpp.recipe.AllInOneMachineRecipe;
import net.cpp.recipe.CppCraftingShapedRecipe;
import net.cpp.recipe.CppCraftingShapelessRecipe;
import net.cpp.recipe.ICppCraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public final class CppRecipes {

	public static final RecipeType<ICppCraftingRecipe> CRAFTING = RecipeType.register("cpp:crafting");
	public static final RecipeSerializer<CppCraftingShapedRecipe> CRAFTING_SHAPED = RecipeSerializer.register("cpp:crafting_shaped", new CppCraftingShapedRecipe.Serializer());
	public static final RecipeSerializer<CppCraftingShapelessRecipe> CRAFTING_SHAPELESS = RecipeSerializer.register("cpp:crafting_shapeless", new CppCraftingShapelessRecipe.Serializer());
	public static final RecipeType<AllInOneMachineRecipe> ALL_IN_ONE_MACHINE_RECIPE_TYPE = RecipeType.register("cpp:all_in_one_mahcine_processing");
	public static final RecipeSerializer<AllInOneMachineRecipe> ALL_IN_ONE_MACHINE_SERIALIZER = RecipeSerializer.register("cpp:all_in_one_mahcine_processing", new AllInOneMachineRecipe.Serializer());

	public static void register() {
	}

}
