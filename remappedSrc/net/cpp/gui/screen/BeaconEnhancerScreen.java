package net.cpp.gui.screen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.api.CodingTool;
import net.cpp.gui.handler.BeaconEnhancerScreenHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class BeaconEnhancerScreen extends HandledScreen<BeaconEnhancerScreenHandler> {
	public static final int PLAYER_EFFECT_BUTTON_SYNC_ID = CodingTool.nextSyncId(), MOB_EFFECT_BUTTON_SYNC_ID = CodingTool.nextSyncId(), ONLY_ADVERSE_BUTTON_SYNC_ID = CodingTool.nextSyncId();
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/beacon_enhancer.png");
	public static final List<Identifier> PLAYER_EFFECT_TEXTURES, MOB_EFFECT_TEXTURES;
	public static final List<Text> ONLY_ADVERSE_TEXTS = ImmutableList.of(new TranslatableText("gui.all_living"), new TranslatableText("gui.only_adverse"));
	public final TexturedButtonWidget playerEffectButton = new TexturedButtonWidget(0, 0, 24, 24, 0, 0, 0, BACKGROUND, buttonWidget -> {
//		System.out.println(1);
		client.interactionManager.clickButton(handler.syncId, PLAYER_EFFECT_BUTTON_SYNC_ID);
	}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(PLAYER_EFFECT_TEXTURES.get(handler.blockEntity.propertyDelegate.get(0)));
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, 0, 0, 24, 24, 24, 24);
		}
	};
	public final TexturedButtonWidget mobEffectButton = new TexturedButtonWidget(0, 0, 24, 24, 0, 0, 0, BACKGROUND, buttonWidget -> {
		client.interactionManager.clickButton(handler.syncId, MOB_EFFECT_BUTTON_SYNC_ID);
	}) {
		@Override
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(MOB_EFFECT_TEXTURES.get(handler.blockEntity.propertyDelegate.get(1)));
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, 0, 0, 24, 24, 24, 24);
		}
	};
	public final TexturedButtonWidget onlyAdverseButton = new TexturedButtonWidget(0, 0, 20, 20, 0, 0, 0, BACKGROUND, buttonWidget -> {
		client.interactionManager.clickButton(handler.syncId, ONLY_ADVERSE_BUTTON_SYNC_ID);
	}) {
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
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}

	@Override
	protected void init() {
		super.init();
		playerEffectButton.setPos(x + 40, y + 31);
		addButton(playerEffectButton);
		mobEffectButton.setPos(x + 112, y + 31);
		addButton(mobEffectButton);
		onlyAdverseButton.setPos(x + 150, y + 33);
		addButton(onlyAdverseButton);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (playerEffectButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(handler.blockEntity.getPlayerEffect().getName(), AMachineScreen.CLICK_TO_SHIFT), x, y);
		} else if (mobEffectButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(handler.blockEntity.getMobEffect().getName(), AMachineScreen.CLICK_TO_SHIFT), x, y);
		} else if (onlyAdverseButton.isHovered()) {
			renderTooltip(matrices, ImmutableList.of(ONLY_ADVERSE_TEXTS.get(handler.blockEntity.propertyDelegate.get(2)), AMachineScreen.CLICK_TO_SHIFT), x, y);
		} else
			super.drawMouseoverTooltip(matrices, x, y);
	}

	static {
		{
			List<Identifier> tempList = new ArrayList<Identifier>();
			for (String s : new String[] { "fire_resistance", "night_vision", "water_breathing", "invisibility", "saturation" })
				tempList.add(new Identifier(String.format("textures/mob_effect/%s.png", s)));
			tempList.add(new Identifier("cpp:textures/mob_effect/chain.png"));
			PLAYER_EFFECT_TEXTURES = Collections.unmodifiableList(tempList);

			tempList = new ArrayList<Identifier>();
			for (String s : new String[] { "weakness", "slowness", "glowing", "poison", "wither" })
				tempList.add(new Identifier(String.format("textures/mob_effect/%s.png", s)));
			tempList.add(new Identifier("cpp:textures/gui/attract.png"));
			MOB_EFFECT_TEXTURES = Collections.unmodifiableList(tempList);
		}
	}
}