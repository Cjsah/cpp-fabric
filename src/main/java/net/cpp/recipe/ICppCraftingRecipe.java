package net.cpp.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import net.cpp.init.CppRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.registry.Registry;


public interface ICppCraftingRecipe extends Recipe<CraftingInventory> {
	default RecipeType<?> getType() {
		return CppRecipes.CRAFTING;
	}
	static ItemStack getItemStack(JsonObject json) {
		String string = JsonHelper.getString(json, "item");
		Item item = (Item) Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
			return new JsonSyntaxException("Unknown item '" + string + "'");
		});
		if (json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int i = JsonHelper.getInt(json, "count", 1);
			return new ItemStack(item, i);
		}
	}

}
