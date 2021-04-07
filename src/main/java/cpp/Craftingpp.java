package cpp;

import static cpp.api.CppChat.say;

import cpp.init.CppGeneratorType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpp.config.CppConfig;
import cpp.ducktyping.IPlayerJoinCallback;
import cpp.init.CppBlockEntities;
import cpp.init.CppBlocks;
import cpp.init.CppEntities;
import cpp.init.CppFeatures;
import cpp.init.CppItems;
import cpp.init.CppLootTableFunctions;
import cpp.init.CppRecipes;
import cpp.init.CppScreenHandler;
import cpp.init.CppStats;
import cpp.misc.AttachAttributesLootFunction;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Craftingpp implements ModInitializer {

	public static final String MOD_ID1 = "Crafting++";
	public static final String MOD_ID2 = "Craftingpp";
	public static final String MOD_ID3 = "cpp";

	public static final Logger logger = LogManager.getLogger("Craftingpp");

	public static final CppConfig CONFIG = new CppConfig();

	public static final ItemGroup CPP_GROUP_MACHINE = FabricItemGroupBuilder.create(new Identifier("cpp:title.machine")).icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();
	public static final ItemGroup CPP_GROUP_MISC = FabricItemGroupBuilder.create(new Identifier("cpp:title.misc")).icon(() -> new ItemStack(CppItems.ENCHANTED_IRON)).build();
	public static final ItemGroup CPP_GROUP_TOOL = FabricItemGroupBuilder.create(new Identifier("cpp:title.tool")).icon(() -> new ItemStack(CppItems.BROOM)).build();
	public static final ItemGroup CPP_GROUP_FOOD = FabricItemGroupBuilder.create(new Identifier("cpp:title.food")).icon(() -> new ItemStack(CppItems.CITRUS)).build();
	public static final ItemGroup CPP_GROUP_PLANT = FabricItemGroupBuilder.create(new Identifier("cpp:title.plant")).icon(() -> new ItemStack(CppBlocks.ORE_SAPLING)).build();
	public static final ItemGroup CPP_GROUP_DECORATE = FabricItemGroupBuilder.create(new Identifier("cpp:title.decorate")).icon(() -> new ItemStack(CppItems.CLASSICAL_PAINTING)).build();



	@Override
	public void onInitialize() {

		logger.info("welcome to use cpp");

		CppBlocks.loadClass();
		CppItems.loadClass();
		CppBlockEntities.loadClass();
		CppScreenHandler.loadClass();
		CppRecipes.loadClass();
		CppStats.loadClass();
		CppLootTableFunctions.loadClass();
		CppEntities.loadClass();
		CppFeatures.loadClass();
		CppGeneratorType.loadClass();
		AttachAttributesLootFunction.loadClass();
		IPlayerJoinCallback.EVENT.register((player, server) -> {
			if (!player.world.isClient)
				say(player, new TranslatableText("misc.cpp1", new TranslatableText("chat.cpp.load1"), new TranslatableText("chat.cpp.load2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.cjsah.net/ruhuasiyu/"))).formatted(Formatting.GOLD)));
			return ActionResult.PASS;
		});

	}
}
