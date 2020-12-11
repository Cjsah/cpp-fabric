package net.cpp.gui.screen;

import static net.cpp.gui.handler.SlotTool.x;
import static net.cpp.gui.handler.SlotTool.y;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.block.entity.MobProjectorBlockEntity;
import net.cpp.gui.handler.MobProjectorScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MobProjectorScreen extends HandledScreen<MobProjectorScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/mob_projector.png");
	public static final List<Identifier> PICTURES;
	public final OutputDirectionButton oButton = new OutputDirectionButton(buttonWidget -> {
		this.client.interactionManager.clickButton(this.handler.syncId, OutputDirectionButton.SYNCHRONIZED_ID);
	}, handler.blockEntity.propertyDelegate);

	public MobProjectorScreen(MobProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.drawMouseoverTooltip(matrices, mouseX, mouseY);
	}

	protected void init() {
		super.init();
		oButton.setPos(x + x(5), y + y(0));
		addButton(oButton);
		titleX = 29;
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.client.getTextureManager().bindTexture(BACKGROUND);
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

//		if (handler.blockEntity.isWorking()) {
//			this.drawTexture(matrices, x + 74, y + 36, 176, 0, 11,
//					handler.blockEntity.getWorkTimeTotal() > 0
//							? 1 + 15 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal()
//							: 0);
//		}
		int exp = handler.blockEntity.getExpStorage();
		if (exp > 0) {
			client.getTextureManager().bindTexture(AllInOneMachineScreen.XP);
			int t = (int) (System.currentTimeMillis() % (16 * 50) / 50);
			drawTexture(matrices, x + 152, y + 68 - (exp + 1) / 2, 0, t * 50, 16, (exp + 1) / 2, 16, 800);
		}
		client.getTextureManager().bindTexture(AllInOneMachineScreen.FRAME);
		drawTexture(matrices, x + 151, y + 17, 0, 0, 18, 52, 18, 52);

		if (handler.blockEntity.getCurrentRecipeCode() > 0) {
			client.getTextureManager().bindTexture(PICTURES.get(handler.blockEntity.getCurrentRecipeCode()));
			drawTexture(matrices, x + 19, y + 18, 0, 0, 52, 52, 52, 52);
		}
		client.getTextureManager().bindTexture(BACKGROUND);
		drawTexture(matrices, x + 106, y + 40, 88, 0, 38, 26, 128, 128);
		int t = (int) (System.currentTimeMillis() % (12 * 50) / 50) / 2;
		drawTexture(matrices, x + 107, y + 50, 88, 26 + t * 7, 36, 7, 128, 128);

		int dy = 52 * handler.blockEntity.getWorkTime() / MobProjectorBlockEntity.WORK_TIME_TOTAL;
		drawTexture(matrices, x + 19, y + 18 + dy, 19, 18 + dy, 52, 52 - dy);
	}

	@Override
	protected void drawMouseoverTooltip(MatrixStack matrices, int x, int y) {
		if (xpIsHovered(x, y)) {
			renderTooltip(matrices, new LiteralText(String.format("%d/100", handler.blockEntity.getExpStorage())), x,
					y);
		} else if (workTimeIsHovered(x, y)) {
			renderTooltip(matrices, new LiteralText(String.format("%.1fs/%.1fs", handler.blockEntity.getWorkTime() / 20.,
					handler.blockEntity.getWorkTimeTotal() / 20.)), x, y);
		}
		super.drawMouseoverTooltip(matrices, x, y);
	}

	public boolean xpIsHovered(int mx, int my) {
		return mx >= x + 151 && mx <= x + 151 + 18 && my >= y + 17 && my <= y + 17 + 52;
	}

	public boolean workTimeIsHovered(int mx, int my) {
		return mx >= x + 19 && mx <= x + 19 + 52 && my >= y + 18 && my <= y + 18 + 52;
	}

	static {
		String[] strs = { "sheep", "cow", "pig", "chicken", "rabbit", "bat", "squid", "creeper", "zombie", "skeleton",
				"spider", "silverfish", "polar_bear", "witch", "slime", "phantom", "piglin", "ghast", "magma_cube",
				"blaze", "enderman", "endermite", "strider", "villager", "vindicator", "guardian", "shulker",
				"wither_skeleton", "wolf", "cat", "horse", "donkey", "llama", "parrot", "turtle", "fox", "panda", "bee",
				"dolphin", "cod", "salmon", "tropical_fish", "pufferfish" };
		List<Identifier> list = new LinkedList<>();
		list.add(null);
		for (String s: strs)
			list.add(new Identifier(String.format("cpp:textures/gui/mob_projector_pictures/%s.png", s)));
		PICTURES = Collections.unmodifiableList(list);
	}
}
