package net.cpp.other;

import net.cpp.init.CppRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;

public interface ICppCraftingRecipe extends Recipe<CraftingInventory> {
	default RecipeType<?> getType() {
		return CppRecipes.CRAFTING;
	}
}
