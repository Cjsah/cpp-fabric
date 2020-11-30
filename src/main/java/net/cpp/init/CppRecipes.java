package net.cpp.init;

import net.cpp.other.CppCraftingShapedRecipe;
import net.cpp.other.CppCraftingShapelessRecipe;
import net.cpp.other.ICppCraftingRecipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;

public final class CppRecipes {

    public static final RecipeType<ICppCraftingRecipe> CRAFTING = RecipeType.register("cpp:crafting");

    public static final RecipeSerializer<CppCraftingShapedRecipe> CPP_CRAFTING_SHAPED = RecipeSerializer
            .register("cpp:crafting_shaped", new CppCraftingShapedRecipe.Serializer());


    public static final RecipeSerializer<CppCraftingShapelessRecipe> CPP_CRAFTING_SHAPELESS = RecipeSerializer
            .register("cpp:crafting_shapeless", new CppCraftingShapelessRecipe.Serializer());


    public static void register() {}

}
