package cpp;

import cpp.entity.AGolemEntity;
import cpp.client.render.entity.DarkChickenEntityRenderer;
import cpp.client.render.entity.DarkCowEntityRenderer;
import cpp.client.render.entity.DarkMooshroomEntityRenderer;
import cpp.client.render.entity.DarkPigEntityRenderer;
import cpp.client.render.entity.DarkSheepEntityRenderer;
import cpp.client.render.entity.GolemEntityRenderer;
import cpp.init.*;
import cpp.screen.AllInOneMachineScreen;
import cpp.screen.BeaconEnhancerScreen;
import cpp.screen.ColorPaletteScreen;
import cpp.screen.CraftingMachineScreen;
import cpp.screen.EmptyBookshelfScreen;
import cpp.screen.GoldenAnvilScreen;
import cpp.screen.ItemProcessorScreen;
import cpp.screen.MobProjectorScreen;
import cpp.screen.PortableCraftingMachineScreen;
import cpp.screen.TradeMachineScreen;
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

import static cpp.init.CppBlocks.*;

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
			EntityRendererRegistry.INSTANCE.register(type, ctx -> new GolemEntityRenderer(ctx, type));
		}
		EntityRendererRegistry.INSTANCE.register(CppEntities.DARK_COW, DarkCowEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CppEntities.DARK_MOOSHROOM, DarkMooshroomEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CppEntities.DARK_SHEEP, DarkSheepEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CppEntities.DARK_PIG, DarkPigEntityRenderer::new);
		EntityRendererRegistry.INSTANCE.register(CppEntities.DARK_CHICKEN, DarkChickenEntityRenderer::new);
		
		BlockRenderLayerMapImpl.INSTANCE.putBlocks(RenderLayer.getCutout(), FRUIT_SAPLING, ORE_SAPLING, SAKURA_SAPLING, WOOL_SAPLING, BLUE_ROSE, POINSETTIA, CHRISTMAS_TREE, RICE, BROKEN_SPAWNER);
		BlockRenderLayerMapImpl.INSTANCE.putBlocks(RenderLayer.getTranslucent(), RARE_EARTH_GLASS, REINFORCED_GLASS);
		for (Block block : CppBlocks.FLOWER_GRASSES) {
			BlockRenderLayerMapImpl.INSTANCE.putBlock(block, RenderLayer.getCutout());
		}
	}
	
}
