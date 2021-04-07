package cpp.item;

import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;

import static cpp.init.CppBlocks.CRAFTING_MACHINE;
import static net.minecraft.item.Items.AIR;
import static net.minecraft.item.Items.CRAFTING_TABLE;

public class RecipeCreator extends Item {
	private final Function<Item, FileWriter> pather = item -> {
		try {
			Identifier id = Registry.ITEM.getId(item);
			FileWriter fw = new FileWriter(String.format("D:\\CCC\\Documents\\编程\\Minecraft Mod\\更多的合成 FabricMod 开发中\\src\\main\\resources\\data\\cpp\\recipes\\%s.json", id.getPath()));
			return fw;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	};
	public RecipeCreator() {
		super(new Settings());
	}

	public RecipeCreator(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			try {
				ServerWorld world = (ServerWorld) context.getWorld();
				BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
				if (blockEntity != null && blockEntity instanceof BarrelBlockEntity) {
					BarrelBlockEntity barrel = (BarrelBlockEntity) blockEntity;
					ItemStack typeStack = barrel.getStack(0);
					if (typeStack.isOf(CRAFTING_TABLE) || typeStack.isOf(CRAFTING_MACHINE.asItem())) {
						if (typeStack.getCount() == 1) {
							int height = 3, width = 3;
							Item[][] ingredients = new Item[3][3];
							Map<Item, Character> map = new HashMap<>();
							map.put(AIR, ' ');
							for (int i = 0; i < 3; i++) {
								for (int j = 0; j < 3; j++) {
									ItemStack ingrStack = barrel.getStack(j + 1 + i * 9);
									ingredients[i][j] = ingrStack.getItem();
									map.computeIfAbsent(ingrStack.getItem(), item -> (char) (map.size() + 'A' - 1));
								}
							}
							for1: for (int i = 2; i >= 0; i--) {
								for (int j = 0; j < 3; j++) {
									if (ingredients[i][j] != AIR) {
										break for1;
									}
								}
								height = i;
							}
							for1: for (int i = 2; i >= 0; i--) {
								for (int j = 0; j < 3; j++) {
									if (ingredients[j][i] != AIR) {
										break for1;
									}
								}
								width = i;
							}
							String[] pattern = new String[height];
							for (int i = 0; i < height; i++) {
								pattern[i] = "";
								for (int j = 0; j < width; j++) {
									pattern[i] += map.get(ingredients[i][j]);
								}
							}
							ItemStack resultStack = barrel.getStack(8);
							FileWriter fw = pather.apply(resultStack.getItem());
							fw.write("{\r\n" + "	\"type\": \"" + (typeStack.isOf(CRAFTING_TABLE) ? "" : "cpp:") + "crafting_shaped\",\r\n" + "	\"pattern\": [\r\n" + "");
							for (int i = 0; i < pattern.length; i++) {
								fw.write(String.format("		\"%s\"%s\r\n", pattern[i], i < pattern.length - 1 ? "," : ""));
							}
							fw.write("	],\r\n" + "	\"key\": {\r\n" + "");
							List<Entry<Item, Character>> list = new ArrayList<>(map.entrySet());
							Collections.sort(list, (a, b) -> Character.compare(a.getValue(), b.getValue()));
							for (Iterator<Entry<Item, Character>> iterator = list.iterator(); iterator.hasNext();) {
								Entry<Item, Character> entry = iterator.next();
								if (entry.getKey() != AIR) {
									fw.write(String.format("		\"%c\": {\r\n" + "			\"item\": \"%s\"\r\n" + "		}%s\r\n" + "", entry.getValue(), Registry.ITEM.getId(entry.getKey()), iterator.hasNext() ? "," : ""));
								}
							}
							fw.write(String.format("	},\r\n" + "	\"result\": {\r\n" + "		\"item\": \"%s\"", Registry.ITEM.getId(resultStack.getItem())));
							if (resultStack.getCount() > 1) {
								fw.write(",\r\n" + "		\"count\": " + resultStack.getCount());
							}
							fw.write("\r\n	}\r\n" + "}");
							fw.close();
							return ActionResult.SUCCESS;
						} else {
							List<Item> ingredients = new ArrayList<>(9);
							for (int i = 0; i < 3; i++) {
								for (int j = 0; j < 3; j++) {
									ItemStack ingrStack = barrel.getStack(i + 1 + j * 9);
									if (!ingrStack.isEmpty())
										ingredients.add(barrel.getStack(i + 1 + j * 9).getItem());
								}
							}
							ItemStack resultStack = barrel.getStack(8);
							FileWriter fw = pather.apply(resultStack.getItem());
							fw.write(String.format("{\r\n" + "	\"type\": \"%scrafting_shapeless\",\r\n" + "	\"ingredients\": [\r\n" + "", typeStack.isOf(CRAFTING_TABLE) ? "" : "cpp:"));
							for (Iterator<Item> iterator = ingredients.iterator(); iterator.hasNext();) {
								Item item = iterator.next();
								fw.write(String.format("		{\r\n" + "			\"item\": \"%s\"\r\n" + "		}%s\r\n" + "", Registry.ITEM.getId(item), iterator.hasNext() ? "," : ""));
							}
							fw.write(String.format("	],\r\n" + "	\"result\": {\r\n" + "		\"item\": \"%s\"", Registry.ITEM.getId(resultStack.getItem())));
							if (resultStack.getCount() > 1) {
								fw.write(",\r\n" + "		\"count\": " + resultStack.getCount());
							}
							fw.write("\r\n	}\r\n" + "}");
							fw.close();
							return ActionResult.SUCCESS;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ActionResult.PASS;
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}

}
