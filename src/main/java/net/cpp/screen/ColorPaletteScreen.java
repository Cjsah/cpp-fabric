package net.cpp.screen;

import java.awt.Color;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;

import io.netty.buffer.Unpooled;
import net.cpp.init.CppItems;
import net.cpp.screen.handler.ColorPaletteScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SoundSliderWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.DyeItem;
import net.minecraft.item.DyeableItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ColorPaletteScreen extends AMachineScreen<ColorPaletteScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/color_palette.png");
	private float[] hsb = new float[]{0, 1, 1};
	private int barLength = 140, barHeight = 12, originX, originY, lineSpacing = 2, rgb;
	private TextFieldWidget[] colorFields = new TextFieldWidget[3];
	private DirectionButton[] directionButtons = new DirectionButton[6];
	private boolean scrolledOrClicked = true;
	
	public ColorPaletteScreen(ColorPaletteScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}
	
	@Override
	protected void init() {
		super.init();
		originX = x + 8;
		originY = y + 30;
		for (int i = 0; i < 3; i++) {
			colorFields[i] = new TextFieldWidget(textRenderer, originX + 8 + i * 29, originY - 10, 12, 9, LiteralText.EMPTY);
			colorFields[i].setTextPredicate(text -> {
				String textString = text.toUpperCase();
				for (int j = 0; j < textString.length(); j++)
					if ("0123456789ABCDEF".indexOf(textString.charAt(j)) == -1)
						return false;
				return true;
			});
			colorFields[i].setEditableColor(-1);
			colorFields[i].setUneditableColor(-1);
			colorFields[i].setHasBorder(false);
			colorFields[i].setX(originX + 8 + i * 29);
			final int fi = i;
			colorFields[i].setChangedListener(s -> {
				/*if (scrolledOrClicked) {
					scrolledOrClicked = false;
				} else*/
				{
					rgb = rgb & (0xff00ffff >> (fi * 8)) | (Integer.valueOf(s.length() == 0 ? "0" : s, 16) << ((2 - fi) * 8));
					hsb = Color.RGBtoHSB(rgb >> 16 & 0xff, rgb >> 8 & 0xff, rgb & 0xff, hsb);
					updateRGB();
				}
			});
			colorFields[i].setMaxLength(2);
			addButton(colorFields[i]);
		}
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 2; j++) {
				int fi = i, fj = j;
				directionButtons[i * 2 + j] = addButton(new DirectionButton(originX - 1 + 29 * (i + j) - 7 * j, originY - 12, j == 0, button -> {
					int i1 = ((rgb >> ((2 - fi) * 8)) + (fj == 0 ? -1 : 1)) & 0xff;
					rgb = rgb & (0xff00ffff >> (fi * 8)) | (i1 << ((2 - fi) * 8));
					hsb = Color.RGBtoHSB(rgb >> 16 & 0xff, rgb >> 8 & 0xff, rgb & 0xff, hsb);
					updateRGB();
				}));
			}
		}
		updateRGB();
		updateText();
	}
	
	@Override
	protected Identifier getBackground() {
		Item item = handler.getSlot(0).getStack().getItem();
		if (item instanceof DyeableItem || item == Items.FIREWORK_STAR) {
			return BACKGROUND;
		} else if (item == CppItems.DYE_STICK) {
			return BACKGROUND;
		} else if (item == Items.TROPICAL_FISH_BUCKET) {
			return BACKGROUND;
		}
		return BACKGROUND;
	}
	
	@Override
	public void tick() {
		super.tick();
		
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);
		
		//绘制3个彩色滑动条
		for (int i = 0; i < barLength; i++) {
			drawVerticalLine(matrices, originX + i, originY - 1, originY + barHeight, Color.HSBtoRGB(1f * i / barLength, 1, 1));
			drawVerticalLine(matrices, originX + i, originY + (barHeight + lineSpacing) - 1, originY + (barHeight + lineSpacing) + barHeight, Color.HSBtoRGB(hsb[0], 1f * i / barLength, 1));
			drawVerticalLine(matrices, originX + i, originY + (barHeight + lineSpacing) * 2 - 1, originY + (barHeight + lineSpacing) * 2 + barHeight, Color.HSBtoRGB(hsb[0], hsb[1], 1f * i / barLength));
		}
		//绘制滑块
		for (int i = 0; i < 3; i++)
			drawTexture(matrices, originX - 2 + (int) (hsb[i] * (barLength)), originY + (barHeight + lineSpacing) * i - 1, 176, 0, 5, 14);
		updateRGB();
		
		DyeItem dye = handler.neededDye;
		int sx = x + handler.getSlot(37).x, sy = y + handler.getSlot(37).y;
		if (!handler.items.getStack(1).isOf(dye)) {
			//绘制需要的染料
			itemRenderer.renderInGui(dye.getDefaultStack(), sx, sy);
			//如果没放入染料或放入染料不对，渲染红色背景
			DrawableHelper.fill(matrices, sx, sy, sx + 16, sy + 16, Color.RED.getRGB() & 0x00ffffff | 0x7f000000);
		}
		
		TropicalFishEntity fish = new TropicalFishEntity(EntityType.TROPICAL_FISH, client.world);
		int variant = 0;
		
		fish.setVariant(variant);
		InventoryScreen.drawEntity(x, y, 100, x - mouseX, y - mouseY, fish);
		
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean clicked = false;
		int i = locateScroll(mouseX, mouseY);
		if (i != -1) {
			hsb[i] = (float) ((mouseX - originX) / barLength);
			clicked = true;
		}
		updateRGB();
		updateText();
		return super.mouseClicked(mouseX, mouseY, button) || clicked;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		boolean scrolled = false;
		int i = locateScroll(mouseX, mouseY);
		if (i != -1) {
			hsb[i] -= (float) (amount * 3 / barLength / (hasShiftDown() ? 15 : 1));
			if (hsb[i] < 0)
				hsb[i] = 0;
			if (hsb[i] > 1)
				hsb[i] = 1;
			scrolled = true;
		}
		updateRGB();
		updateText();
		return super.mouseScrolled(mouseX, mouseY, amount) || scrolled;
	}
	
	private int locateScroll(double mouseX, double mouseY) {
		double mx = mouseX - originX, my = mouseY - originY + 1;
		if (mx >= 0 && mx < barLength && my > 0 && my < (barHeight + lineSpacing) * 3) {
			return (int) (my / (barHeight + lineSpacing));
		}
		return -1;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		if (keyCode == 256) {
			client.player.closeHandledScreen();
		}
		for (int i = 0; i < 3; i++) {
			if (colorFields[i].isActive())
				return colorFields[i].keyPressed(keyCode, scanCode, modifiers);
		}
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	/**
	 * 更新RGB文本
	 */
	private void updateText() {
		for (int i = 0; i < 3; i++) {
			colorFields[i].setText(String.format("%02X", (rgb >> ((2 - i) * 8)) & 0xff));
		}
	}
	
	/**
	 * 左右方向按钮
	 *
	 * @author Ph-苯
	 */
	public static class DirectionButton extends TexturedButtonWidget {
		public final boolean left;
		
		public DirectionButton(int x, int y, boolean left, ButtonWidget.PressAction pressAction) {
			super(x, y, 7, 12, 0, 0, 0, BACKGROUND, 16, 16, pressAction, (button, matrices, mouseX, mouseY) -> {
			
			}, LiteralText.EMPTY);
			this.left = left;
		}
		
		public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
			MinecraftClient.getInstance().getTextureManager().bindTexture(BACKGROUND);
			RenderSystem.enableDepthTest();
			drawTexture(matrices, x, y, 181 + (left ? 0 : 7), isHovered() ? 12 : 0, 7, 12);
		}
	}
	
	/**
	 * 通过HSB更新RGB
	 *
	 * @return
	 */
	public int updateRGB() {
		if (rgb != (rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]))) {
			handler.update(rgb);
			PacketByteBuf packetByteBuf = new PacketByteBuf(Unpooled.buffer());
			packetByteBuf.writeInt(rgb);
			ClientPlayNetworking.send(handler.channel, packetByteBuf);
		}
		return rgb;
	}
	
}
