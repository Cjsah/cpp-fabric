package cpp.screen;

import java.awt.Color;

import com.mojang.blaze3d.systems.RenderSystem;

import cpp.api.Utils;
import cpp.init.CppItems;
import cpp.screen.handler.ColorPaletteScreenHandler;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import static cpp.screen.ColorPaletteScreen.FinalInts.*;

public class ColorPaletteScreen extends AMachineScreen<ColorPaletteScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.ITEM.getId(CppItems.COLOR_PALETTE).getPath());
	public static final String TROPICAL_BUCKET_NBT_KEY = "BucketVariantTag";
	private float[] hsb = new float[]{0, 255f / 256, 255f / 256};
	private TextFieldWidget rgbTextField;
	
	public ColorPaletteScreen(ColorPaletteScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
		handler.colorPaletteScreen = this;
	}
	
	@Override
	protected void init() {
		super.init();
		{
			rgbTextField = new TextFieldWidget(textRenderer, x + TFX, x + TFY, TFW, TFH, LiteralText.EMPTY);
			rgbTextField.setTextPredicate(text -> {
				String textString = text.toUpperCase();
				for (int j = 0; j < textString.length(); j++)
					if ("0123456789ABCDEF".indexOf(textString.charAt(j)) == -1)
						return false;
				return true;
			});
			rgbTextField.setEditableColor(-1);
			rgbTextField.setUneditableColor(-1);
			rgbTextField.setDrawsBackground(false);
			rgbTextField.setX(x + TFX);
			rgbTextField.setChangedListener(s -> {
				int rgb = Integer.valueOf(s.length() == 0 ? "0" : s, 16);
				setHsb(rgb);
				setRgb(rgb);
			});
			rgbTextField.setMaxLength(6);
			this.addSelectableChild(rgbTextField);
		}
	}
	
	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}
	
	@Override
	public void tick() {
		super.tick();
	}
	
	@Override
	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		{
			//绘制共同背景
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			client.getTextureManager().bindTexture(getBackground());
			int posX = this.x;
			int posY = (this.height - this.backgroundHeight) / 2;
			drawTexture(matrices, posX, posY, 0, 0, this.backgroundWidth, this.backgroundHeight, TW, TH);
			//把文本框设为不可见
			rgbTextField.visible = false;
			ItemStack inputStack = handler.getMaterialStack();
			if (!inputStack.isEmpty()) {
				//根据原料槽放入的物品来渲染对应的背景
				int mode = handler.getCurrentMode();
				switch (mode) {
					case 1: {//RGB色
						drawTexture(matrices, posX, posY, 0, backgroundHeight, backgroundWidth, SH, TW, TH);
						//按钮和文本框设为可见
						rgbTextField.visible = true;
						//绘制3个彩色滑动条
						for (int k = 0; k < B1W; k++) {
							drawVerticalLine(matrices, posX + B1X + k, posY + B1Y - 1, posY + B1Y + B1H, Color.HSBtoRGB(1f * k / B1W, 1, 1));
							drawVerticalLine(matrices, posX + B1X + k, posY + B1Y + B1G - 1, posY + B1Y + B1G + B1H, Color.HSBtoRGB(hsb[0], 1f * k / B1W, 1));
							drawVerticalLine(matrices, posX + B1X + k, posY + B1Y + B1G * 2 - 1, posY + B1Y + B1G * 2 + B1H, Color.HSBtoRGB(hsb[0], hsb[1], 1f * k / B1W));
						}
						//绘制箭头
						for (int k = 0; k < 3; k++)
							drawTexture(matrices, posX + B1X - A1W / 2 + (int) (hsb[k] * (B1W)), posY + B1Y - 1 - A1Y + B1G * k, A1X, A1Y, A1W, A1H, TW, TH);
						//显示当前选中的颜色色块
						DrawableHelper.fill(matrices, x + CBX, y + CBY, x + CBX + CBW, y + CBY + CBH, handler.getRgb() | 0xff000000);
						//显示染料名字
						Item dye = handler.getNeededDye(0);
						textRenderer.draw(matrices, dye.getName(),x+ CBX + CBW + 4, y + CBY, 0);
						break;
					}
					case 2: {//热带鱼染色
						drawTexture(matrices, posX, posY, 0, backgroundHeight + SH, backgroundWidth, SH, TW, TH);
						{
							//渲染色条
							for (int iy = 0; iy < 2; iy++) {
								for (int ix = 0; ix < DyeColor.values().length; ix++) {
									int argb = 0xff;
									for (float fl : DyeColor.byId(ix).getColorComponents()) {
										argb <<= 8;
										argb += fl * 0xff;
									}
									DrawableHelper.fill(matrices, posX + B2X + ix * 4, posY + B2Y + B2G * iy, posX + B2X + (ix + 1) * B23SW, posY + B2Y + B2G * iy + B23H, argb);
								}
							}
						}
						{
							//渲染箭头
							for (int i = 0; i < 2; i++) {
								drawTexture(matrices, posX + B2X - A23W / 2 + B23SW / 2 + handler.getSelectedColorId(i) * B23SW, posY + B2Y - A23H - 1 + i * B2G, A23X, A23Y, A23W, A23H, TW, TH);
							}
						}
						{
							//渲染热带鱼
							TropicalFishEntity fish = new TropicalFishEntity(EntityType.TROPICAL_FISH, client.world) {
								//鱼只有接触水才会直立，否则是横躺的，所以重写该方法让鱼直立
								@Override
								public boolean isTouchingWater() {
									return true;
								}
							};
							int variant = 0;
							if (inputStack.getTag() != null && inputStack.getTag().contains(TROPICAL_BUCKET_NBT_KEY)) {
								variant = inputStack.getTag().getInt(TROPICAL_BUCKET_NBT_KEY) & 0x0000ffff;
							}
							variant |= handler.getSelectedColorId(0) << 16;
							variant |= handler.getSelectedColorId(1) << 24;
							fish.setVariant(variant);
							int size = 52;
							int x1 = posX + 8 + 27;
							int y1 = posY + 18 + 40;
							InventoryScreen.drawEntity(x1, y1, size, x1 - mouseX, y1 - mouseY, fish);
							//显示热带鱼名称
							textRenderer.draw(matrices, getTropicalFishName(variant), posX + 64, posY + 22, 0);
							
						}
						break;
					}
					case 3: {//原版16色
						drawTexture(matrices, posX, posY, 0, backgroundHeight + SH * 2, backgroundWidth, SH, TW, TH);
						{
							//渲染色条
							for (int ix = 0; ix < DyeColor.values().length; ix++) {
								int argb = 0xff;
								for (float fl : DyeColor.byId(ix).getColorComponents()) {
									argb <<= 8;
									argb += fl * 0xff;
								}
								DrawableHelper.fill(matrices, posX + B3X + ix * B23SW, posY + B3Y, posX + B3X + (ix + 1) * B23SW, posY + B3Y + B23H, argb);
							}
						}
						{
							//渲染箭头
							drawTexture(matrices, posX + B3X - A23W / 2 + B23SW / 2 + handler.getSelectedColorId(2) * B23SW, posY + B3Y - A23H - 1, A23X, A23Y, A23W, A23H, TW, TH);
						}
					}
				}
				if (mode > 0) {
					//渲染需要的染料
					renderNeededDye(matrices, 0);
					if (mode == 2) {
						renderNeededDye(matrices, 1);
					}
				}
			}
		}
		{//绘制标题
		
		}
		
		
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean clicked = false;
		switch (handler.getCurrentMode()) {
			case 1: {
				int i = locateScrollMode1(mouseX, mouseY);
				if (i != -1) {
					hsb[i] = (float) ((mouseX - x - B1X) / B1W);
					setRgb(hsb);
					setRgbTextField(handler.getRgb());
					clicked = true;
				}
				break;
			}
			case 2: {
				int i = locateScrollMode2(mouseX, mouseY);
				if (i != -1) {
					setSelectedColor(i, (int) ((mouseX - x - B2X) / B23SW));
					clicked = true;
				}
				break;
			}
			case 3: {
				double mx = mouseX - x - B3X;
				double my = mouseY - y - B3Y;
				if (mx >= 0 && mx < B23SW * DyeColor.values().length && my >= 0 && my < B23H) {
					setSelectedColor(2, (int) (mx / B23SW));
					clicked = true;
				} else if (focusedSlot != null && focusedSlot.id >= 39 && focusedSlot.id < 39 + 16) {
					int id = focusedSlot.id - 39;
					setSelectedColor(2, id);
				}
			}
		}
		return super.mouseClicked(mouseX, mouseY, button) || clicked;
	}
	
	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {//TODO
		boolean scrolled = false;
		switch (handler.getCurrentMode()) {
			case 1: {
				int i = locateScrollMode1(mouseX, mouseY);
				if (i != -1) {
					hsb[i] -= (float) (amount * 3 / B1W / (hasShiftDown() ? 15 : 1));
					if (hsb[i] < 0)
						hsb[i] = 0;
					if (hsb[i] > 255f / 256)
						hsb[i] = 255f / 256;
					setRgb(hsb);
					setRgbTextField(handler.getRgb());
					scrolled = true;
				} else if (mouseX >= x + TFX && mouseX < x + TFX + TFW && mouseY >= y + TFY && mouseY < y + TFY + TFH) {
					int irgb = Integer.valueOf(rgbTextField.getText(), 16);
					int index = (int) ((TFW - (mouseX - x - TFX)) / TFSW);
					int digit = ((irgb >> (index * 4) & 0xf) + (int) amount) & 0xf;
					irgb = irgb & ~(-1 >> (index * 4) & 0xf << (index * 4)) | (digit << (index * 4)) & 0xffffff;
					setRgb(irgb);
					setRgbTextField(handler.getRgb());
					setHsb(handler.getRgb());
					scrolled = true;
				}
				break;
			}
			case 2: {
				
				break;
			}
		}
		
		return super.mouseScrolled(mouseX, mouseY, amount) || scrolled;
	}
	
	public int locateScrollMode1(double mouseX, double mouseY) {
		double mx = mouseX - x - B1X, my = mouseY - y - B1Y + A1H + 1;
		if (mx >= 0 && mx < B1W && my > 0 && my < A1H + 1 + B1H + 1 + B1G * 2 && my % B1G < B1H + 1 + A1H + 1) {
			return (int) (my / B1G);
		}
		return -1;
	}
	
	public int locateScrollMode2(double mouseX, double mouseY) {
		double mx = mouseX - x - B2X, my = mouseY - y - B2Y + A23H + 1;
		if (mx >= 0 && mx < B23SW * DyeColor.values().length && my >= 0 && my < A23H + 1 + B23H + 1 + B2G && my % B2G < B23H + 1 + A23H + 1) {
			return (int) (my / B2G);
		}
		return -1;
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {//TODO
		if (rgbTextField.isActive()) {
			return rgbTextField.keyPressed(keyCode, scanCode, modifiers);
		} else if (keyCode == 256) {
			client.player.closeHandledScreen();
		}
		
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	public Text getTropicalFishName(int variant) {
		Formatting[] formattings = new Formatting[]{Formatting.BLACK};
		String string = "color.minecraft." + TropicalFishEntity.getBaseDyeColor(variant);
		String string2 = "color.minecraft." + TropicalFishEntity.getPatternDyeColor(variant);
		for (int j = 0; j < TropicalFishEntity.COMMON_VARIANTS.length; ++j) {
			if (variant == TropicalFishEntity.COMMON_VARIANTS[j]) {
				return new TranslatableText(TropicalFishEntity.getToolTipForVariant(j)).formatted(formattings);
			}
		}
		MutableText text = new TranslatableText(TropicalFishEntity.getTranslationKey(variant)).formatted(formattings);
		MutableText mutableText = new TranslatableText(string);
		if (!string.equals(string2)) {
			mutableText.append(",").append(new TranslatableText(string2));
		}
		mutableText.formatted(formattings);
		text.append(" ").append(mutableText);
		return text;
	}
	
	public void appliedNewMaterial() {
		ItemStack stack = handler.getMaterialStack();
		switch (handler.getCurrentMode()) {
			case 1: {
				if (stack.getItem() instanceof DyeableItem && stack.hasTag() && stack.getTag().contains("display") && stack.getSubTag("display").contains("color")) {
					setRgb(stack.getTag().getCompound("display").getInt("color"));
					setHsb(handler.getRgb());
					setRgbTextField(handler.getRgb());
				}
				break;
			}
			case 2: {
				if (stack.hasTag() && stack.getTag().contains(TROPICAL_BUCKET_NBT_KEY)) {
					int variantColor = stack.getTag().getInt(TROPICAL_BUCKET_NBT_KEY);
					setSelectedColor(0, (variantColor >> 16) & 0xff);
					setSelectedColor(1, (variantColor >> 24) & 0xff);
				}
				break;
			}
			case 3:
				DyeColor dyeColor = ColorPaletteScreenHandler.getDyeColor(stack.getItem());
				if (dyeColor != null) {
					setSelectedColor(2, dyeColor.getId());
				}
				break;
		}
		
		
	}
	
	public void setRgbTextField(int rgb) {
		rgbTextField.setText(String.format("%06X", rgb));
	}
	
	public void setHsb(int rgb) {
		Utils.hsbFromRGB(rgb, hsb);
	}
	
	public void setRgb(int rgb) {
		handler.setRgb(rgb);
	}
	
	public void setRgb(float[] hsb) {
		handler.setRgb(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]) & 0xffffff);
	}
	
	public void setSelectedColor(int i, int value) {
		handler.setSelectedColorId(i, value);
	}
	
	public void renderNeededDye(MatrixStack matrices, int i) {
		int sx = x + handler.getDyeSlot(i).x, sy = y + handler.getDyeSlot(0).y;
		if (!handler.getDyeSlot(i).getStack().isOf(handler.getNeededDye(i))) {
			//绘制需要的染料
			itemRenderer.renderInGui(handler.getNeededDye(i).getDefaultStack(), sx, sy);
			//如果没放入染料或放入染料不对，渲染红色背景
			DrawableHelper.fill(matrices, sx, sy, sx + 16, sy + 16, Color.RED.getRGB() & 0x00ffffff | 0x7f000000);
		}
	}
	
	public static class FinalInts {
		/** 整张贴图的宽 */
		public static final int TW = 256;
		/** 整张贴图的高 */
		public static final int TH = 512;
		/** 三个贴图分页的高 */
		public static final int SH = 83;
		/** 第一分页的第一个色条的横坐标 */
		public static final int B1X = 8;
		/** 第一分页的第一个色条的纵坐标 */
		public static final int B1Y = 36;
		/** 第一分页的色条的宽 */
		public static final int B1W = 140;
		/** 第一分页的色条的高 */
		public static final int B1H = 6;
		/** 第一分页的相邻色条的纵坐标之差 */
		public static final int B1G = 14;
		/** 第二分页的第一个色条的横坐标 */
		public static final int B2X = 65;
		/** 第二分页的第一个色条的纵坐标 */
		public static final int B2Y = 42;
		/** 第二、三分页的色条的单个颜色色块的宽 */
		public static final int B23SW = 4;
		/** 第二、三分页的色条的高 */
		public static final int B23H = 10;
		/** 第二分页的相邻色条的纵坐标之差 */
		public static final int B2G = 18;
		/** 第三分页的色条的横坐标 */
		public static final int B3X = 8;
		/** 第三分页的色条的纵坐标 */
		public static final int B3Y = 23;
		/** 第一分页的箭头在贴图文件中的横坐标 */
		public static final int A1X = 190;
		/** 第一分页的箭头在贴图文件中的纵坐标 */
		public static final int A1Y = 5;
		/** 第一分页的箭头的宽 */
		public static final int A1W = 7;
		/** 第一分页的箭头的高 */
		public static final int A1H = 5;
		/** 第二、三分页的箭头在贴图文件中的横坐标 */
		public static final int A23X = 190;
		/** 第二、三分页的箭头在贴图文件中的纵坐标 */
		public static final int A23Y = 0;
		/** 第二、三分页的箭头的宽 */
		public static final int A23W = 8;
		/** 第二、三分页的箭头的高 */
		public static final int A23H = 5;
		public static final int TFX = 9;
		public static final int TFY = 19;
		public static final int TFSW = 6;
		public static final int TFW = 36;
		public static final int TFH = 9;
		public static final int CBX = TFX + TFW + 4;
		public static final int CBY = TFY - 1;
		public static final int CBW = 10;
		public static final int CBH = CBW;
	}
}
