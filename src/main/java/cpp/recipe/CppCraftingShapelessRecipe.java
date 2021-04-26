package cpp.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import cpp.init.CppRecipes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CppCraftingShapelessRecipe implements ICppCraftingRecipe {
	private final Identifier id;
	private final String group;
	private final ItemStack output;
	private final DefaultedList<Ingredient> input;

	public CppCraftingShapelessRecipe(Identifier id, String group, ItemStack output, DefaultedList<Ingredient> input) {
		this.id = id;
		this.group = group;
		this.output = output;
		this.input = input;
	}

	public boolean matches(CraftingInventory craftingInventory, World world) {
		RecipeMatcher recipeFinder = new RecipeMatcher();
		int i = 0;

		for (int j = 0; j < craftingInventory.size(); ++j) {
			ItemStack itemStack = craftingInventory.getStack(j);
			if (!itemStack.isEmpty()) {
				++i;
				recipeFinder.addInput(itemStack, 1);
			}
		}

		return i == this.input.size() && recipeFinder.match(this, null);
	}

	@Override
	public ItemStack craft(CraftingInventory craftingInventory) {
		return this.output.copy();
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean fits(int width, int height) {
		return width * height >= this.input.size();
	}


	@Override
	public ItemStack getOutput() {
		 return this.output;
	 }
	@Override
	public DefaultedList<Ingredient> getIngredients() {
		 return this.input;
	 }

	@Override
	@Environment(EnvType.CLIENT)
	public String getGroup() {
		 return this.group;
	 }

	@Override
	public Identifier getId() {
		 return this.id;
	 }

	@Override
	public RecipeSerializer<?> getSerializer() {
		 return CppRecipes.CRAFTING_SHAPELESS_SERIALIZER;
	 }
	public static class Serializer implements RecipeSerializer<CppCraftingShapelessRecipe> {
		@Override
		public CppCraftingShapelessRecipe read(Identifier identifier, JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "group", "");
			DefaultedList<Ingredient> defaultedList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
			if (defaultedList.isEmpty()) {
				throw new JsonParseException("No ingredients for shapeless recipe");
			} else if (defaultedList.size() > 9) {
				throw new JsonParseException("Too many ingredients for shapeless recipe");
			} else {
				ItemStack itemStack = ICppCraftingRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
				return new CppCraftingShapelessRecipe(identifier, string, itemStack, defaultedList);
			}
		}

		private static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for (int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					defaultedList.add(ingredient);
				}
			}

			return defaultedList;
		}

		@Override
		public CppCraftingShapelessRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			String string = packetByteBuf.readString(32767);
			int i = packetByteBuf.readVarInt();
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i, Ingredient.EMPTY);

			for (int j = 0; j < defaultedList.size(); ++j) {
				defaultedList.set(j, Ingredient.fromPacket(packetByteBuf));
			}

			ItemStack itemStack = packetByteBuf.readItemStack();
			return new CppCraftingShapelessRecipe(identifier, string, itemStack, defaultedList);
		}

		public void write(PacketByteBuf packetByteBuf, CppCraftingShapelessRecipe shapelessRecipe) {
			packetByteBuf.writeString(shapelessRecipe.group);
			packetByteBuf.writeVarInt(shapelessRecipe.input.size());

            for (Ingredient ingredient : shapelessRecipe.input) {
                ingredient.write(packetByteBuf);
            }

			packetByteBuf.writeItemStack(shapelessRecipe.output);
		}
	}
}
