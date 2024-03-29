package cpp.recipe;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import cpp.init.CppRecipes;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class CppCraftingShapedRecipe implements ICppCraftingRecipe {
	private final int width;
	private final int height;
	private final DefaultedList<Ingredient> inputs;
	private final ItemStack output;
	private final Identifier id;
	private final String group;

	public CppCraftingShapedRecipe(Identifier id, String group, int width, int height,
			DefaultedList<Ingredient> ingredients, ItemStack output) {
		this.id = id;
		this.group = group;
		this.width = width;
		this.height = height;
		this.inputs = ingredients;
		this.output = output;
	}

	@Override
	public boolean matches(CraftingInventory inv, World world) {
		for (int i = 0; i <= inv.getWidth() - this.width; ++i) {
			for (int j = 0; j <= inv.getHeight() - this.height; ++j) {
				if (this.matchesSmall(inv, i, j, true)) {
					return true;
				}

				if (this.matchesSmall(inv, i, j, false)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public ItemStack craft(CraftingInventory inv) {
		return this.getOutput().copy();
	}

	@Override
	public boolean fits(int width, int height) {
		return width >= this.width && height >= this.height;
	}

	@Override
	public ItemStack getOutput() {
		return this.output;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CppRecipes.CRAFTING_SHAPED_SERIALIZER;
	}

	private boolean matchesSmall(CraftingInventory inv, int offsetX, int offsetY, boolean bl) {
		for (int i = 0; i < inv.getWidth(); ++i) {
			for (int j = 0; j < inv.getHeight(); ++j) {
				int k = i - offsetX;
				int l = j - offsetY;
				Ingredient ingredient = Ingredient.EMPTY;
				if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
					if (bl) {
						ingredient = this.inputs.get(this.width - k - 1 + l * this.width);
					} else {
						ingredient = this.inputs.get(k + l * this.width);
					}
				}

				if (!ingredient.test(inv.getStack(i + j * inv.getWidth()))) {
					return false;
				}
			}
		}

		return true;
	}

	private static Map<String, Ingredient> getComponents(JsonObject json) {
		Map<String, Ingredient> map = Maps.newHashMap();

		for (Entry<String, JsonElement> entry : json.entrySet()) {
			if (entry.getKey().length() != 1) {
				throw new JsonSyntaxException("Invalid key entry: '" + entry.getKey()
						+ "' is an invalid symbol (must be 1 character only).");
			}

			if (" ".equals(entry.getKey())) {
				throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
			}

			map.put(entry.getKey(), Ingredient.fromJson(entry.getValue()));
		}

		map.put(" ", Ingredient.EMPTY);
		return map;
	}

	private static String[] getPattern(JsonArray json) {
		String[] strings = new String[json.size()];
		if (strings.length > 3) {
			throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
		} else if (strings.length == 0) {
			throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
		} else {
			for (int i = 0; i < strings.length; ++i) {
				String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
				if (string.length() > 3) {
					throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
				}

				if (i > 0 && strings[0].length() != string.length()) {
					throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
				}

				strings[i] = string;
			}

			return strings;
		}
	}

	@VisibleForTesting
	static String[] combinePattern(String... lines) {
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for (int m = 0; m < lines.length; ++m) {
			String string = lines[m];
			i = Math.min(i, findNextIngredient(string));
			int n = findNextIngredientReverse(string);
			j = Math.max(j, n);
			if (n < 0) {
				if (k == m) {
					++k;
				}

				++l;
			} else {
				l = 0;
			}
		}

		if (lines.length == l) {
			return new String[0];
		} else {
			String[] strings = new String[lines.length - l - k];

			for (int o = 0; o < strings.length; ++o) {
				strings[o] = lines[o + k].substring(i, j + 1);
			}

			return strings;
		}
	}

	private static int findNextIngredient(String pattern) {
		int i;
		for (i = 0; i < pattern.length() && pattern.charAt(i) == ' '; ++i) {
		}

		return i;
	}

	private static int findNextIngredientReverse(String pattern) {
		int i;
		for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
		}

		return i;
	}

	private static DefaultedList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> key, int width,
			int height) {
		DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
		Set<String> set = Sets.newHashSet(key.keySet());
		set.remove(" ");

		for (int i = 0; i < pattern.length; ++i) {
			for (int j = 0; j < pattern[i].length(); ++j) {
				String string = pattern[i].substring(j, j + 1);
				Ingredient ingredient = key.get(string);
				if (ingredient == null) {
					throw new JsonSyntaxException(
							"Pattern references symbol '" + string + "' but it's not defined in the key");
				}

				set.remove(string);
				defaultedList.set(j + width * i, ingredient);
			}
		}

		if (!set.isEmpty()) {
			throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
		} else {
			return defaultedList;
		}
	}

	public static class Serializer implements RecipeSerializer<CppCraftingShapedRecipe> {
		public CppCraftingShapedRecipe read(Identifier identifier, JsonObject jsonObject) {
			String string = JsonHelper.getString(jsonObject, "group", "");
			Map<String, Ingredient> map = CppCraftingShapedRecipe
					.getComponents(JsonHelper.getObject(jsonObject, "key"));
			String[] strings = CppCraftingShapedRecipe
					.combinePattern(CppCraftingShapedRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
			int i = strings[0].length();
			int j = strings.length;
			DefaultedList<Ingredient> defaultedList = CppCraftingShapedRecipe.getIngredients(strings, map, i, j);
			ItemStack itemStack = ICppCraftingRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
			return new CppCraftingShapedRecipe(identifier, string, i, j, defaultedList, itemStack);
		}

		public CppCraftingShapedRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			int i = packetByteBuf.readVarInt();
			int j = packetByteBuf.readVarInt();
			String string = packetByteBuf.readString(32767);
			DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

			for (int k = 0; k < defaultedList.size(); ++k) {
				defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
			}

			ItemStack itemStack = packetByteBuf.readItemStack();
			return new CppCraftingShapedRecipe(identifier, string, i, j, defaultedList, itemStack);
		}

		public void write(PacketByteBuf packetByteBuf, CppCraftingShapedRecipe shapedRecipe) {
			packetByteBuf.writeVarInt(shapedRecipe.width);
			packetByteBuf.writeVarInt(shapedRecipe.height);
			packetByteBuf.writeString(shapedRecipe.group);

			for (Ingredient ingredient : shapedRecipe.inputs) {
				ingredient.write(packetByteBuf);
			}

			packetByteBuf.writeItemStack(shapedRecipe.output);
		}
	}
}
