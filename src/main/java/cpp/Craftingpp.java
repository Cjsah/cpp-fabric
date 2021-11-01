package cpp;

import static cpp.api.CppChat.say;

import cpp.config.CppConfig;
import cpp.init.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpp.ducktyping.IPlayerJoinCallback;
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
	
	public static final Logger logger = LogManager.getLogger(MOD_ID1);

	public static final ItemGroup CPP_GROUP_MACHINE = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.machine")).icon(() -> new ItemStack(CppBlocks.CRAFTING_MACHINE)).build();
	public static final ItemGroup CPP_GROUP_TOOL = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.tool")).icon(() -> new ItemStack(CppItems.BROOM)).build();
	public static final ItemGroup CPP_GROUP_FOOD = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.food")).icon(() -> new ItemStack(CppItems.CITRUS)).build();
	public static final ItemGroup CPP_GROUP_MISC = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.misc")).icon(() -> new ItemStack(CppItems.ENCHANTED_IRON)).build();
	public static final ItemGroup CPP_GROUP_PLANT = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.plant")).icon(() -> new ItemStack(CppBlocks.ORE_SAPLING)).build();
	public static final ItemGroup CPP_GROUP_DECORATE = FabricItemGroupBuilder.create(new Identifier(MOD_ID3, "title.decorate")).icon(() -> new ItemStack(CppItems.CLASSICAL_PAINTING)).build();
	
	@Override
	public void onInitialize() {

		CppConfig.load();
		CppBlockEntities.init();
		CppBlocks.init();
		CppBlockTags.init();
		CppCommands.init();
		CppEffects.init();
		CppEntities.init();
		CppFeatures.init();
		CppGeneratorType.init();
		CppItems.init();
		CppItemTags.init();
		CppLootTableFunctions.init();
		CppRecipes.init();
		CppScreenHandler.init();
		CppStats.init();
		CppWorlds.init();

		AttachAttributesLootFunction.init();

		IPlayerJoinCallback.EVENT.register((player, server) -> {
			if (!player.world.isClient)
				say(player, new TranslatableText("misc.cpp1", new TranslatableText("chat.cpp.load1"), new TranslatableText("chat.cpp.load2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.cjsah.net/ruhuasiyu/"))).formatted(Formatting.GOLD)));
			return ActionResult.PASS;
		});
	}
}
