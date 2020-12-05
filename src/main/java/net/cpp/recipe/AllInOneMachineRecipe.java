package net.cpp.recipe;

import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import it.unimi.dsi.fastutil.ints.IntList;
import net.cpp.init.CppRecipes;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeFinder;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AllInOneMachineRecipe implements Recipe<Inventory> {
	protected final Identifier id;
	public final int temperaturePressure;
	protected final DefaultedList<Ingredient> input;
	protected final DefaultedList<ResultItemStack> output;
	public final int time;

	public AllInOneMachineRecipe(Identifier id, int temperaturePressure,DefaultedList<Ingredient> input,
			DefaultedList<ResultItemStack> output, int time) {
		this.id = id;
		this.temperaturePressure = temperaturePressure;
		this.output = output;
		this.input = input;
		this.time = time;
	}

	@Override
	public boolean matches(Inventory inv, World world) {
		RecipeFinder recipeFinder = new RecipeFinder();
		int i = 0;

		for (int j = 0; j < inv.size(); ++j) {
			ItemStack itemStack = inv.getStack(j);
			if (!itemStack.isEmpty()) {
				++i;
				recipeFinder.method_20478(itemStack, 1);
			}
		}

		return i == this.input.size() && recipeFinder.findRecipe(this, (IntList) null);
	}

	@Override
	public ItemStack craft(Inventory inv) {
		throw new UnsupportedOperationException("请调用crafts(Inventory)方法");
	}

	@Override
	public boolean fits(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getOutput() {
		throw new UnsupportedOperationException("请调用getOutputs()方法");
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return CppRecipes.ALL_IN_ONE_MACHINE_RECIPE_TYPE;
	}
	@Override
	public RecipeSerializer<?> getSerializer() {
		// TODO 自动生成的方法存根
		return null;
	}
	/*
	 * 以下是自定义方法
	 */
	public DefaultedList<ItemStack> crafts(Inventory inv) {
		return getOutputs();
	}

	public DefaultedList<ItemStack> getOutputs() {
		DefaultedList<ItemStack> list = DefaultedList.ofSize(output.size(), ItemStack.EMPTY);
		for (int i = 0; i < list.size(); i++) {
			list.set(i, new ItemStack(output.get(i).item, doubleRandToInt(output.get(i).count)));
		}
		return list;
	}

	public static int doubleRandToInt(double d) {
		int z = (int) Math.floor(d);
		z += Math.random() < d - z ? 1 : 0;
		return z;
	}
	public static int getTemperaturePressure(String temperature, String pressure) {
		int rst = 0;
		switch (temperature) {
		case "ordinary":
			rst += 0;
			break;
		case "low":
			rst += 1;
			break;
		case "high":
			rst += 2;
			break;
		}
		rst *= 16;
		switch (pressure) {
		case "ordinary":
			rst += 0;
			break;
		case "low":
			rst += 1;
			break;
		case "high":
			rst += 2;
			break;
		}
		return rst;
	}
	public static class Serializer implements RecipeSerializer<AllInOneMachineRecipe> {
		public AllInOneMachineRecipe read(Identifier identifier, JsonObject jsonObject) {
			DefaultedList<Ingredient> inputList = getIngredients(JsonHelper.getArray(jsonObject, "ingredients"));
			if (inputList.isEmpty()) {
				throw new JsonParseException("无原料");
			} else if (inputList.size() > 2) {
				throw new JsonParseException("太多原料");
			} else {
				int temperaturePressure = JsonHelper.getInt(jsonObject, "temperature_pressure", 0);
				DefaultedList<ResultItemStack> resultList = getResults(JsonHelper.getArray(jsonObject, "results"));
				int time = JsonHelper.getInt(jsonObject, "time", 100);
				return new AllInOneMachineRecipe(identifier, temperaturePressure, inputList, resultList, time);
			}
		}

		public AllInOneMachineRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			int temperaturePressure = packetByteBuf.readInt();
			
			int i = packetByteBuf.readVarInt();
			DefaultedList<Ingredient> ingredients = DefaultedList.ofSize(i, Ingredient.EMPTY);
			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromPacket(packetByteBuf));
			}
			
			i = packetByteBuf.readVarInt();
			DefaultedList<ResultItemStack> results = DefaultedList.ofSize(i, ResultItemStack.EMPTY);
			for (int j = 0; j < results.size(); ++j) {
				results.set(j, ResultItemStack.valueOf(packetByteBuf));
			}
			int time = packetByteBuf.readInt();
			return new AllInOneMachineRecipe(identifier, temperaturePressure, ingredients, results, time);
		}

		public void write(PacketByteBuf packetByteBuf, AllInOneMachineRecipe recipe) {
			packetByteBuf.writeVarInt(recipe.input.size());
			Iterator<Ingredient> var3 = recipe.input.iterator();

			while (var3.hasNext()) {
				Ingredient ingredient = (Ingredient) var3.next();
				ingredient.write(packetByteBuf);
			}
			for (ResultItemStack resultItemStack: recipe.output) {
				packetByteBuf.writeVarInt(Item.getRawId(resultItemStack.item));
				packetByteBuf.writeFloat(resultItemStack.count);
			}
		}
		public static DefaultedList<Ingredient> getIngredients(JsonArray json) {
			DefaultedList<Ingredient> defaultedList = DefaultedList.of();

			for (int i = 0; i < json.size(); ++i) {
				Ingredient ingredient = Ingredient.fromJson(json.get(i));
				if (!ingredient.isEmpty()) {
					defaultedList.add(ingredient);
				}
			}

			return defaultedList;
		}

		public static DefaultedList<ResultItemStack> getResults(JsonArray json) {
			DefaultedList<ResultItemStack> defaultedList = DefaultedList.of();

			for (int i = 0; i < json.size(); ++i) {
				if (!json.get(i).isJsonObject())
					throw new ClassCastException("成品必须是JSON对象");
				ResultItemStack result = ResultItemStack.valueOf((JsonObject) json.get(i));
				if (!result.isEmpty()) {
					defaultedList.add(result);
				}
			}

			return defaultedList;
		}
	}

	public static class ResultItemStack {
		public static final ResultItemStack EMPTY = new ResultItemStack(null, 0, null);
		public final Item item;
		public final float count;
		public final CompoundTag tag;

		public ResultItemStack(Item item, float count, CompoundTag tag) {
			this.item = item;
			this.count = count;
			this.tag = tag;
		}

		public boolean isEmpty() {
			return this == EMPTY;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == null)
				return false;
			if (obj == this)
				return true;
			if (obj instanceof ResultItemStack) {
				ResultItemStack o = (ResultItemStack) obj;
				return this.count == o.count && this.item.equals(o.item) && this.tag.equals(o.tag);
			}
			return false;
		}

		public static ResultItemStack valueOf(JsonObject json) {
			String string = JsonHelper.getString(json, "item");
			Item item = (Item) Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
				return new JsonSyntaxException("Unknown item '" + string + "'");
			});
			if (json.has("data")) {
				throw new JsonParseException("Disallowed data tag found");
			} else {
				float i = JsonHelper.getFloat(json, "count", 1);
				return new ResultItemStack(item, i, null);
			}
		}
		public static ResultItemStack valueOf(PacketByteBuf packetByteBuf) {
			ResultItemStack resultItemStack = new ResultItemStack(Item.byRawId(packetByteBuf.readVarInt()),
					packetByteBuf.readFloat(), null);
			return resultItemStack;
		}
	}
}
