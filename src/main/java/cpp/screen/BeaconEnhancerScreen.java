package cpp.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import cpp.api.Utils;
import cpp.init.CppBlocks;
import cpp.screen.handler.BeaconEnhancerScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BeaconEnhancerScreen extends HandledScreen<BeaconEnhancerScreenHandler> {
	public static final int PLAYER_EFFECT_BUTTON_SYNC_ID = Utils.nextSyncId(), MOB_EFFECT_BUTTON_SYNC_ID = Utils.nextSyncId(), ONLY_ADVERSE_BUTTON_SYNC_ID = Utils.nextSyncId();
	public static final Identifier BACKGROUND = AMachineScreen.getBackgroundByName(Registry.BLOCK.getId(CppBlocks.BEACON_ENHANCER).getPath());
	public static final List<Identifier> PLAYER_EFFECT_TEXTURES, MOB_EFFECT_TEXTURES;
	public static final List<Text> ONLY_ADVERSE_TEXTS = ImmutableList.of(new TranslatableText("gui.all_living"), new TranslatableText("gui.only_adverse"));
	/**
	 * 施加于玩家的状态效果的按钮，点击切换
	 */
	public final TexturedButtonWidget playerEffectButton = new TexturedButtonWidget(0, 0, 24, 24, 0, 0, 0, BACKGROUND, buttonWidget -> client.interactionManager.clickButton(handler.syncId, PLAYER_EFFECT_BUTTON_SYNC_ID)) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(PLAYER_EFFECT_TEXTURES.get(handler.blockEntity.propertyDelegate.get(0)));
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, 0, 0, 24, 24, 24, 24);
		}
	};
	/**
	 * 施加于生物的状态效果的按钮，点击切换
	 */
	public final TexturedButtonWidget mobEffectButton = new TexturedButtonWidget(0, 0, 24, 24, 0, 0, 0, BACKGROUND, buttonWidget -> client.interactionManager.clickButton(handler.syncId, MOB_EFFECT_BUTTON_SYNC_ID)) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(MOB_EFFECT_TEXTURES.get(handler.blockEntity.propertyDelegate.get(1)));
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, 0, 0, 24, 24, 24, 24);
		}
	};
	/**
	 * 施加于生物的状态效果是否仅施加于敌对生物的按钮，点击切换
	 */
	public final TexturedButtonWidget onlyAdverseButton = new TexturedButtonWidget(0, 0, 20, 20, 0, 0, 0, BACKGROUND, buttonWidget -> client.interactionManager.clickButton(handler.syncId, ONLY_ADVERSE_BUTTON_SYNC_ID)) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x - 24, y, 176, handler.blockEntity.isOnlyAdverse() ? 41 : 0, 44, 41);
		}
	};

	public BeaconEnhancerScreen(BeaconEnhancerScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.field_2776;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}

	@Override
	protected void init() {
		super.init();
		playerEffectButton.setPos(field_2776 + 40, field_2776 + 31);
		addButton(playerEffectButton);
		mobEffectButton.setPos(field_2776 + 112, field_2776 + 31);
		addButton(mobEffectButton);
		onlyAdverseButton.setPos(field_2776 + 150, field_2776 + 33);
		addButton(onlyAdverseButton);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (playerEffectButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(handler.blockEntity.getPlayerEffect().getName(), AOutputMachineScreen.CLICK_TO_SHIFT), x, y);
		} else if (mobEffectButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(handler.blockEntity.getMobEffect().getName(), AOutputMachineScreen.CLICK_TO_SHIFT), x, y);
		} else if (onlyAdverseButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(ONLY_ADVERSE_TEXTS.get(handler.blockEntity.propertyDelegate.get(2)), AOutputMachineScreen.CLICK_TO_SHIFT), x, y);
		} else
			super.drawMouseoverTooltip(matrices, x, y);
	}

	static {
		{
			List<Identifier> tempList = new ArrayList<>();
			for (String s : new String[] { "fire_resistance", "night_vision", "water_breathing", "invisibility", "saturation" })
				tempList.add(new Identifier(String.format("textures/mob_effect/%s.png", s)));
			tempList.add(new Identifier("cpp:textures/mob_effect/chain.png"));
			PLAYER_EFFECT_TEXTURES = Collections.unmodifiableList(tempList);

			tempList = new ArrayList<>();
			for (String s : new String[] { "weakness", "slowness", "glowing", "poison", "wither" })
				tempList.add(new Identifier(String.format("textures/mob_effect/%s.png", s)));
			tempList.add(AMachineScreen.getBackgroundByName("attract"));
			MOB_EFFECT_TEXTURES = Collections.unmodifiableList(tempList);
		}
	}
}
