package cpp.recipe;

import static cpp.init.CppItems.BASALT_PLUGIN;
import static cpp.init.CppItems.BLACKSTONE_PLUGIN;
import static cpp.init.CppItems.COBBLESTONE_PLUGIN;
import static cpp.init.CppItems.END_STONE_PLUGIN;
import static cpp.init.CppItems.GREEN_FORCE_OF_WATER;
import static cpp.init.CppItems.NETHERRACK_PLUGIN;
import static cpp.init.CppItems.STONE_PLUGIN;
import static net.minecraft.item.Items.LAVA_BUCKET;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import cpp.api.Utils;
import cpp.block.OreLeavesBlock;
import cpp.block.entity.AllInOneMachineBlockEntity;
import cpp.block.entity.AllInOneMachineBlockEntity.Degree;
import cpp.init.CppBlocks;
import cpp.init.CppRecipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class AllInOneMachineRecipe implements Recipe<AllInOneMachineBlockEntity> {
	public final Identifier id;
	public final Degree temperature;
	public final Degree pressure;
	public final List<Pair<List<Item>, String>> ingredient;
	public final int experience;
	public final int time;
	public final List<Pair<List<Item>, Double>> products;
	/**
	 * 不被消耗的原料
	 */
	public static final Set<Item> UNCONSUMABLE = new HashSet<>(Arrays.asList(LAVA_BUCKET, COBBLESTONE_PLUGIN, STONE_PLUGIN, BLACKSTONE_PLUGIN, NETHERRACK_PLUGIN, END_STONE_PLUGIN, BASALT_PLUGIN, GREEN_FORCE_OF_WATER));

	public AllInOneMachineRecipe(Identifier id, Degree temperature, Degree pressure, List<Pair<List<Item>, String>> ingredient, int experience, int time, List<Pair<List<Item>, Double>> products) {
		this.id = id;
		this.temperature = temperature;
		this.pressure = pressure;
		this.ingredient = ingredient;
		this.experience = experience;
		this.time = time;
		this.products = products;
	}

	/*
	 * 以下是Recipe的方法
	 */
	@Override
	public boolean matches(AllInOneMachineBlockEntity blockEntity, World world) {
		if (temperature != blockEntity.getTemperature() || pressure != blockEntity.getPressure() || experience > blockEntity.getExpStorage())
			return false;
		List<Pair<List<Item>, String>> ingredient = Lists.newLinkedList(this.ingredient);
		List<Pair<Item, CompoundTag>> input = Lists.newLinkedList();
		for (int i = 1; i < 3; i++) {
			ItemStack stack = blockEntity.getStack(i);
			if (!stack.isEmpty()) {
				input.add(Pair.of(stack.getItem(), stack.getTag()));
			}
		}
		if (ingredient.size() != input.size())
			return false;
		for1: for (Pair<Item, CompoundTag> pair1 : input) {
			Iterator<Pair<List<Item>, String>> ite = ingredient.iterator();
			while (ite.hasNext()) {
				Pair<List<Item>, String> pair2 = ite.next();
				if (pair2.getLeft().contains(pair1.getLeft())) {
					String potion = pair2.getRight();
					if (potion == null || pair1.getRight().getString("Potion").equals(potion)) {
						ite.remove();
						continue for1;
					}
				}
			}
		}
		return ingredient.isEmpty();
	}

	/**
	 * @see #produce(AllInOneMachineBlockEntity)
	 */
	@Deprecated
	@Override
	public ItemStack craft(AllInOneMachineBlockEntity blockEntity) {
		return products.isEmpty() ? ItemStack.EMPTY : produce(blockEntity).get(0);
	}

	@Override
	public boolean fits(int width, int height) {
		return height == 1 && width == ingredient.size();
	}

	/**
	 * @see #produce(AllInOneMachineBlockEntity)
	 */
	@Deprecated
	@Override
	public ItemStack getOutput() {
		return products.isEmpty() ? ItemStack.EMPTY : maximize(null).get(0);
	}

	@Override
	public Identifier getId() {
		return id;
	}

	@Override
	public RecipeType<?> getType() {
		return CppRecipes.ALL_IN_ONE_MACHINE_TYPE;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return CppRecipes.ALL_IN_ONE_MACHINE_SERIALIZER;
	}

	@Override
	public ItemStack getRecipeKindIcon() {
		return CppBlocks.ALL_IN_ONE_MACHINE.asItem().getDefaultStack();
	}

	@Override
	public String getGroup() {
		return "all_in_one_machine";
	}

	@Override
	public DefaultedList<ItemStack> getRemainingStacks(AllInOneMachineBlockEntity inventory) {
		DefaultedList<ItemStack> list = DefaultedList.ofSize(2, ItemStack.EMPTY);
		for (int i = 1; i < 3; i++) {
			if (UNCONSUMABLE.contains(inventory.getStack(i).getItem())) {
				list.set(i - 1, inventory.getStack(i));
			}
		}
		return list;
	}

	/*
	 * 以下是自定义方法
	 */
	public List<ItemStack> produce(@Nullable AllInOneMachineBlockEntity blockEntity) {
		List<ItemStack> list = Lists.newLinkedList();
		if ("cpp:ores_and_ore_sapling".equals(id.toString())) {
			list.add(new ItemStack(OreLeavesBlock.randomOre(), 2));
			list.add(new ItemStack(CppBlocks.ORE_SAPLING, floats(1.5)));
		} else {
			for (Pair<List<Item>, Double> pair : products) {
				list.add(new ItemStack(random(pair.getLeft()), floats(pair.getRight())));
			}
		}
		return list;
	}

	public List<ItemStack> maximize(@Nullable AllInOneMachineBlockEntity blockEntity) {
		List<ItemStack> list = Lists.newLinkedList();
		if ("cpp:ores_and_ore_sapling".equals(id.toString())) {
			list.add(new ItemStack(OreLeavesBlock.randomOre(), 2));
			list.add(new ItemStack(CppBlocks.ORE_SAPLING, 2));
		} else {
			for (Pair<List<Item>, Double> pair : products) {
				list.add(new ItemStack(random(pair.getLeft()), (int) Math.ceil(pair.getRight())));
			}
		}
		return list;
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

	public static Item random(List<Item> items) {
		return items.get((int) (Math.random() * items.size()));
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
				temperature = Degree.valueOf(jsonObject.get("temperature").getAsString().toUpperCase());
			}
			Degree pressure = Degree.ORDINARY;
			if (jsonObject.has("pressure")) {
				pressure = Degree.valueOf(jsonObject.get("pressure").getAsString().toUpperCase());
			}
			ImmutableList.Builder<Pair<List<Item>, String>> ingredientBuilder = ImmutableList.builder();
			for (JsonElement jse : JsonHelper.getArray(jsonObject, "ingredients")) {
				ImmutableList.Builder<Item> itemsBuilder = ImmutableList.builder();
				String potion = null;
				if (jse.isJsonObject()) {
					JsonObject obj = jse.getAsJsonObject();
					if (obj.has("tag")) {
						itemsBuilder.addAll(Utils.getTag(obj.get("tag").getAsString(), Registry.ITEM_KEY).values());
					} else {
						JsonElement jsonItemId = obj.get("item");
						if (jsonItemId.isJsonArray()) {
							for (JsonElement jse2 : jsonItemId.getAsJsonArray()) {
								itemsBuilder.add(Registry.ITEM.get(new Identifier(jse2.getAsString())));
							}
						} else {
							itemsBuilder.add(Registry.ITEM.get(new Identifier(jsonItemId.getAsString())));
						}
					}
					if (obj.has("potion")) {
						potion = obj.get("potion").getAsString();
					}
				} else {
					itemsBuilder.add(Registry.ITEM.get(new Identifier(jse.getAsString())));
				}
				ingredientBuilder.add(Pair.of(itemsBuilder.build(), potion));
			}
			int experience = 1;
			if (jsonObject.has("experience")) {
				experience = jsonObject.get("experience").getAsInt();
			}
			int time = 1;
			if (jsonObject.has("time")) {
				time = jsonObject.get("time").getAsInt();
			}
			ImmutableList.Builder<Pair<List<Item>, Double>> productsBuilder = ImmutableList.builder();
			for (JsonElement jse : jsonObject.get("products").getAsJsonArray()) {
				List<Item> list;
				double count = 1;
				if (jse.isJsonObject()) {
					JsonObject itemAndCount = jse.getAsJsonObject();
					if (itemAndCount.has("tag")) {
						list = Utils.getTag(itemAndCount.get("tag").getAsString(), Registry.ITEM_KEY).values();
					} else {
						ImmutableList.Builder<Item> itemsBuilder = ImmutableList.builder();
						JsonElement jsonItem = itemAndCount.get("item");
						if (jsonItem.isJsonArray()) {
							for (JsonElement jse2 : jsonItem.getAsJsonArray()) {
								itemsBuilder.add(Registry.ITEM.get(new Identifier(jse2.getAsString())));
							}
						} else {
							itemsBuilder.add(Registry.ITEM.get(new Identifier(jsonItem.getAsString())));
						}
						list = itemsBuilder.build();
					}
					if (itemAndCount.has("count")) {
						count = itemAndCount.get("count").getAsDouble();
					}
				} else {
					list = ImmutableList.of(Registry.ITEM.get(new Identifier(jse.getAsString())));
				}
				productsBuilder.add(Pair.of(list, count));
			}
			return new AllInOneMachineRecipe(identifier, temperature, pressure, ingredientBuilder.build(), experience, time, productsBuilder.build());
		}

		public void write(PacketByteBuf packetByteBuf, AllInOneMachineRecipe recipe) {
			packetByteBuf.writeByte(recipe.temperature.ordinal());
			packetByteBuf.writeByte(recipe.pressure.ordinal());
			packetByteBuf.writeInt(recipe.ingredient.size());
			for (Pair<List<Item>, String> pair : recipe.ingredient) {
				writeItems(pair.getLeft(), packetByteBuf);
				if (pair.getRight() == null) {
					packetByteBuf.writeBoolean(false);
				} else {
					packetByteBuf.writeBoolean(true);
					packetByteBuf.writeString(pair.getRight());
				}
			}
			packetByteBuf.writeVarInt(recipe.experience);
			packetByteBuf.writeVarInt(recipe.time);
			packetByteBuf.writeInt(recipe.products.size());
			for (Pair<List<Item>, Double> pair : recipe.products) {
				writeItems(pair.getLeft(), packetByteBuf);
				packetByteBuf.writeDouble(pair.getRight());
			}
		}

		public AllInOneMachineRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
			Degree temperature = Degree.values()[packetByteBuf.readByte()];
			Degree pressure = Degree.values()[packetByteBuf.readByte()];
			int ingredientSize = packetByteBuf.readInt();
			ImmutableList.Builder<Pair<List<Item>, String>> ingredientBuilder = ImmutableList.builder();
			for (int i = 0; i < ingredientSize; i++) {
				List<Item> items = readItems(packetByteBuf);
				String potion;
				if (packetByteBuf.readBoolean()) {
					potion = packetByteBuf.readString();
				} else {
					potion = null;
				}
				ingredientBuilder.add(Pair.of(items, potion));
			}
			int experience = packetByteBuf.readVarInt();
			int time = packetByteBuf.readVarInt();
			int productsSize = packetByteBuf.readInt();
			ImmutableList.Builder<Pair<List<Item>, Double>> productsBuilder = ImmutableList.builder();
			for (int i = 0; i < productsSize; i++) {
				int itemSize = packetByteBuf.readInt();
				ImmutableList.Builder<Item> itemBuilder = ImmutableList.builder();
				for (int j = 0; j < itemSize; j++) {
					itemBuilder.add(Item.byRawId(packetByteBuf.readInt()));
				}
				List<Item> itemList = itemBuilder.build();
				double count = packetByteBuf.readDouble();
				productsBuilder.add(Pair.of(itemList, count));
			}
			return new AllInOneMachineRecipe(identifier, temperature, pressure, ingredientBuilder.build(), experience, time, productsBuilder.build());
		}

		public static void writeItems(List<Item> items, PacketByteBuf packetByteBuf) {
			packetByteBuf.writeInt(items.size());
			for (Item item : items) {
				packetByteBuf.writeInt(Item.getRawId(item));
			}
		}

		public static List<Item> readItems(PacketByteBuf packetByteBuf) {
			int size = packetByteBuf.readInt();
			List<Item> items = Lists.newArrayListWithCapacity(size);
			for (int i = 0; i < size; i++) {
				items.add(Item.byRawId(packetByteBuf.readInt()));
			}
			return items;
		}
	}

}
