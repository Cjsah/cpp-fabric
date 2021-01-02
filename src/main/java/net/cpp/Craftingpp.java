package net.cpp;

import static net.cpp.api.CppChat.say;

import net.cpp.init.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.cpp.api.IPlayerJoinCallback;
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

	public static final Logger logger = LogManager.getLogger("Craftingpp");

	public static final ItemGroup CPP_GROUP_MACHINE = FabricItemGroupBuilder.create(new Identifier("cpp:title.machine")).icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();
	public static final ItemGroup CPP_GROUP_MISC = FabricItemGroupBuilder.create(new Identifier("cpp:title.misc")).icon(() -> new ItemStack(CppItems.ENCHANTED_IRON)).build();
	public static final ItemGroup CPP_GROUP_TOOL = FabricItemGroupBuilder.create(new Identifier("cpp:title.tool")).icon(() -> new ItemStack(CppItems.BROOM)).build();
	public static final ItemGroup CPP_GROUP_FOOD = FabricItemGroupBuilder.create(new Identifier("cpp:title.food")).icon(() -> new ItemStack(CppItems.CITRUS)).build();
	public static final ItemGroup CPP_GROUP_PLANT = FabricItemGroupBuilder.create(new Identifier("cpp:title.plant")).icon(() -> new ItemStack(CppItems.ORE_SAPLING)).build();
	public static final ItemGroup CPP_GROUP_DECORATE = FabricItemGroupBuilder.create(new Identifier("cpp:title.decorate")).icon(() -> new ItemStack(CppItems.CLASSICAL_PAINTING)).build();

	@Override
	public void onInitialize() {

		logger.info("welcome to use cpp");

		CppBlocks.register();
		CppItems.register();
		CppBlockEntities.register();
		CppScreenHandler.register();
		CppRecipes.register();
		CppStats.register();
		CppEffects.register();
		CppLootTableFunctions.register();

		IPlayerJoinCallback.EVENT.register((player, server) -> {
			if (!player.world.isClient)
				say(player, new TranslatableText("misc.cpp1", new TranslatableText("chat.cpp.load1"), new TranslatableText("chat.cpp.load2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.cjsah.net/ruhuasiyu/"))).formatted(Formatting.GOLD)));
			return ActionResult.PASS;
		});
	}
}
