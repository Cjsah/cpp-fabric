package cpp.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import cpp.init.CppBlocks;
import cpp.init.CppRecipes;
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
		return CppRecipes.CRAFTING_TYPE;
	}
	
	@Override
	default ItemStack createIcon() {
		return CppBlocks.CRAFTING_MACHINE.asItem().getDefaultStack();
	}

	static ItemStack getItemStack(JsonObject json) {
		String string = JsonHelper.getString(json, "item");
		Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> new JsonSyntaxException("Unknown item '" + string + "'"));
		if (json.has("data")) {
			throw new JsonParseException("Disallowed data tag found");
		} else {
			int i = JsonHelper.getInt(json, "count", 1);
			return new ItemStack(item, i);
		}
	}

}
