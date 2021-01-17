package net.cpp.recipe;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.function.Function;
import java.util.function.Predicate;
import static net.minecraft.item.Items.*;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.AllInOneMachineBlockEntity.Degree;
import net.cpp.init.CppItemTags;
import net.cpp.init.CppRecipes;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.data.server.ItemTagsProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AllInOneMachineRecipe implements Recipe<AllInOneMachineBlockEntity> {
	public final Identifier id;
	public final Degree temperature;
	public final Degree pressure;
	public final List<Pair<Item, Predicate<CompoundTag>>> ingredient;
	public final int experience;
	public final int time;
	public final Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> outputer;

	public AllInOneMachineRecipe(Identifier id, Degree temperature, Degree pressure, List<Pair<Item, Predicate<CompoundTag>>> ingredient, int experience, int time, Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> outputer) {
		this.id = id;
		this.temperature = temperature;
		this.pressure = pressure;
		this.ingredient = ingredient;
		this.experience = experience;
		this.time = time;
		this.outputer = outputer;
	}

	/*
	 * 以下是Recipe的方法
	 */
	@Override
	public boolean matches(AllInOneMachineBlockEntity blockEntity, World world) {
		if (temperature != blockEntity.getTemperature() || pressure != blockEntity.getPressure())
			return false;
		List<Pair<Item, CompoundTag>> input = Lists.newLinkedList();
		for (int i = 1; i < 3; i++) {
			ItemStack stack = blockEntity.getStack(i);
			if (!stack.isEmpty()) {
				input.add(Pair.of(stack.getItem(), stack.getTag()));
			}
		}
		if (ingredient.size() != input.size())
			return false;
		Collections.sort(ingredient, (a, b) -> Integer.compare(Item.getRawId(a.getLeft()), Item.getRawId(b.getLeft())));
		Collections.sort(input, (a, b) -> Integer.compare(Item.getRawId(a.getLeft()), Item.getRawId(b.getLeft())));
		Iterator<Pair<Item, Predicate<CompoundTag>>> ite1 = ingredient.iterator();
		Iterator<Pair<Item, CompoundTag>> ite2 = input.iterator();
		while (ite1.hasNext()) {
			Predicate<CompoundTag> predicate = ite1.next().getRight();
			CompoundTag tag = ite2.next().getRight();
			if (predicate != null && !predicate.test(tag))
				return false;
		}
		return true;
	}

	/**
	 * @see #crafts(AllInOneMachineBlockEntity)
	 */
	@Deprecated
	@Override
	public ItemStack craft(AllInOneMachineBlockEntity blockEntity) {
		return outputer.apply(blockEntity).getLeft();
	}

	@Override
	public boolean fits(int width, int height) {
		return height == 1 && width == ingredient.size();
	}

	/**
	 * @see #crafts(AllInOneMachineBlockEntity)
	 */
	@Deprecated
	@Override
	public ItemStack getOutput() {
		return outputer.apply(new AllInOneMachineBlockEntity()).getLeft();
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
	public Pair<ItemStack, ItemStack> crafts(AllInOneMachineBlockEntity blockEntity) {
		return outputer.apply(blockEntity);
	}

	public static Predicate<CompoundTag> test(String potion) {
		return tag -> tag.getString("Potion").equals(potion);
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1) {
		return out(item1, 1);
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, Item item2) {
		return out(item1, 1, item2, 1);
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, int count1) {
		return out(item1, count1, Items.AIR, 0);
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, int count1, Item item2, int count2) {
		return blockEntity -> Pair.of(new ItemStack(item1, count1), new ItemStack(item2, count2));
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, Item item2, double count2) {
		return out(item1, 1, item2, count2);
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, int count1, Item item2, double count2) {
		return blockEntity -> Pair.of(new ItemStack(item1, count1), new ItemStack(item2, floats(count2)));
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(Item item1, double count1, Item item2, double count2) {
		return blockEntity -> Pair.of(new ItemStack(item1, floats(count1)), new ItemStack(item2, floats(count2)));
	}

	public static Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> out(List<Item> items1, int count1, Item item2, double count2) {
		return blockEntity -> Pair.of(new ItemStack(items1.get(blockEntity.getWorld().random.nextInt(items1.size())), count1), new ItemStack(item2, floats(count2)));
	}

	public static int floats(double d) {
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
			Degree temperature = Degree.ORDINARY;
			if (jsonObject.has("temperature")) {
				temperature = Degree.valueOf(jsonObject.get("temperature").getAsString());
			}
			Degree pressure = Degree.ORDINARY;
			if (jsonObject.has("pressure")) {
				pressure = Degree.valueOf(jsonObject.get("pressure").getAsString());
			}
			List<Pair<Item, Predicate<CompoundTag>>> ingredient = Lists.newLinkedList();
			for (JsonElement json : JsonHelper.getArray(jsonObject, "ingredients")) {
				if (json.isJsonObject()) {
					JsonObject obj = json.getAsJsonObject();
					JsonElement itemId = obj.get("item");
					Item item = Registry.ITEM.get(new Identifier(itemId.getAsString()));
					if (item != Items.AIR) {
						Predicate<CompoundTag> predicate = null;
						if (obj.has("potion")) {
							JsonElement potion = obj.get("potion");
							predicate = test(potion.getAsString());
						}
						ingredient.add(Pair.of(item, predicate));
					}
				}
			}
			int experience = 0;
			if (jsonObject.has("experience")) {
				experience = jsonObject.get("experience").getAsInt();
			}
			int time = 0;
			if (jsonObject.has("time")) {
				time = jsonObject.get("time").getAsInt();
			}
			Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> outputer;
			Item item1 = null, item2 = null;
			List<Item> items1 = null;
			Double count1 = null, count2 = null;
			for (JsonElement json : JsonHelper.getArray(jsonObject, "results")) {
				if (json.isJsonObject()) {
					JsonObject obj = json.getAsJsonObject();
					if (obj.has("count")) {
						double d = obj.get("count").getAsDouble();
						if (count1 == null) {
							count1 = d;
						} else {
							count2 = d;
						}
					}
					if (obj.has("item")) {
						Item item = Registry.ITEM.get(new Identifier(obj.get("item").getAsString()));
						if (item != AIR) {
							if (item1 == null) {
								item1 = item;
								if (count1 == null)
									count1 = 1.;
							} else {
								item2 = item;
								if (count2 == null)
									count2 = 1.;
							}
						}
					} else if (obj.has("tag")) {
						Tag<Item> tag = ServerTagManagerHolder.getTagManager().method_33166(Registry.ITEM_KEY, new Identifier(obj.get("tag").getAsString()), id -> new JsonSyntaxException("Unknown item tag '" + id + "'"));
						items1 = tag.values();
					} else if (obj.has("items")) {
						JsonArray jsonIds = obj.get("items").getAsJsonArray();
						items1 = Lists.newArrayListWithCapacity(jsonIds.size());
						for (JsonElement jsonId : jsonIds) {
							items1.add(Registry.ITEM.get(new Identifier(jsonId.getAsString())));
						}
					}
				}
			}
			if (items1 == null) {
				outputer = out(item1, count1, item2, count2);
			} else {
				outputer = out(items1, count1.intValue(), item2, count2);
			}
			return new AllInOneMachineRecipe(identifier, temperature, pressure, ingredient, experience, time, outputer);
		}

		public AllInOneMachineRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {// TODO
			Degree temperature = Degree.valueOf(packetByteBuf.readString());
			Degree pressure = Degree.valueOf(packetByteBuf.readString());
			List<Pair<Item, Predicate<CompoundTag>>> ingredient = Lists.newLinkedList();
			for (int i = 0; i < 2; i++) {
				int i0 = packetByteBuf.readInt();
				String s1 = packetByteBuf.readString();
				if (i0 != 0)
					ingredient.add(Pair.of(Item.byRawId(i0), s1.isEmpty() ? null : test(s1)));
			}
			int experience = packetByteBuf.readInt();
			int time = packetByteBuf.readInt();
			Function<AllInOneMachineBlockEntity, Pair<ItemStack, ItemStack>> outputer;
			Item item1 = Item.byRawId(packetByteBuf.readInt()), item2 = Item.byRawId(packetByteBuf.readInt());
			List<Item> items1 = null;
			if (packetByteBuf.readBoolean()) {
				int[] ia1 = packetByteBuf.readIntArray();
				items1 = Lists.newArrayListWithCapacity(ia1.length);
				for (int ia1i : ia1) {
					items1.add(Item.byRawId(ia1i));
				}
			}
			Double count1 = packetByteBuf.readDouble(), count2 = packetByteBuf.readDouble();
			if (items1 == null) {
				outputer = out(item1, count1, item2, count2);
			} else {
				outputer = out(items1, count1.intValue(), item2, count2);
			}
			return new AllInOneMachineRecipe(identifier, temperature, pressure, ingredient, experience, time, outputer);
		}

		public void write(PacketByteBuf packetByteBuf, AllInOneMachineRecipe recipe) {
			// TODO
//			packetByteBuf.writeVarInt(recipe.input.size());
//			Iterator<Ingredient> var3 = recipe.input.iterator();
//
//			while (var3.hasNext()) {
//				Ingredient ingredient = (Ingredient) var3.next();
//				ingredient.write(packetByteBuf);
//			}
//			for (ResultItemStack resultItemStack : recipe.output) {
//				packetByteBuf.writeVarInt(Item.getRawId(resultItemStack.item));
//				packetByteBuf.writeFloat(resultItemStack.count);
//			}
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

		private static DefaultedList<ResultItemStack> getResults(JsonArray json) {
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
			ResultItemStack resultItemStack = new ResultItemStack(Item.byRawId(packetByteBuf.readVarInt()), packetByteBuf.readFloat(), null);
			return resultItemStack;
		}
	}
}
