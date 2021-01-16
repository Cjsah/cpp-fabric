package net.cpp;

import static net.cpp.init.CppBlocks.*;
import static net.cpp.init.CppBlocks.ORE_SAPLING;
import static net.cpp.init.CppBlocks.SAKURA_SAPLING;
import static net.cpp.init.CppBlocks.WOOL_SAPLING;

import net.cpp.entity.AGolemEntity;
import net.cpp.entity.render.GolemRenderer;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppEntities;
import net.cpp.init.CppPredicates;
import net.cpp.init.CppScreenHandler;
import net.cpp.screen.AllInOneMachineScreen;
import net.cpp.screen.BeaconEnhancerScreen;
import net.cpp.screen.ColorPaletteScreen;
import net.cpp.screen.CraftingMachineScreen;
import net.cpp.screen.EmptyBookshelfScreen;
import net.cpp.screen.GoldenAnvilScreen;
import net.cpp.screen.ItemProcessorScreen;
import net.cpp.screen.MobProjectorScreen;
import net.cpp.screen.PortableCraftingMachineScreen;
import net.cpp.screen.TradeMachineScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.impl.blockrenderlayer.BlockRenderLayerMapImpl;
import net.minecraft.block.Block;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.EntityType;

@Environment(EnvType.CLIENT)
public class CraftingppClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		CppPredicates.register();

		ScreenRegistry.register(CppScreenHandler.CRAFTING_MACHINE, CraftingMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.ALL_IN_ONE_MACHINE, AllInOneMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.ITEM_PROCESSOR, ItemProcessorScreen::new);
		ScreenRegistry.register(CppScreenHandler.MOB_PROJECTOR, MobProjectorScreen::new);
		ScreenRegistry.register(CppScreenHandler.BEACON_ENHANCER, BeaconEnhancerScreen::new);
		ScreenRegistry.register(CppScreenHandler.TRADE_MACHINE, TradeMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.PORTABLE_CRAFTING_TABLE, CraftingScreen::new);
		ScreenRegistry.register(CppScreenHandler.PORTABLE_CRAFTING_MACHINE, PortableCraftingMachineScreen::new);
		ScreenRegistry.register(CppScreenHandler.GOLDEN_ANVIL, GoldenAnvilScreen::new);
		ScreenRegistry.register(CppScreenHandler.EMPTY_BOOKSHELF, EmptyBookshelfScreen::new);
		ScreenRegistry.register(CppScreenHandler.COLOR_PALETTE, ColorPaletteScreen::new);

		for (EntityType<? extends AGolemEntity> type : CppEntities.GOLEMS) {
			EntityRendererRegistry.INSTANCE.register(type, ctx -> new GolemRenderer(ctx, type));
		}

		BlockRenderLayerMapImpl.INSTANCE.putBlocks(RenderLayer.getCutout(), FRUIT_SAPLING, ORE_SAPLING, SAKURA_SAPLING, WOOL_SAPLING, BLUE_ROSE, POINSETTIA, CHRISTMAS_TREE,RICE);
		for (Block block : CppBlocks.FLOWER_GRASSES)
			BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getCutout());
	}

}
