package net.cpp.gui.screen;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.cpp.api.CodingTool;
import net.cpp.gui.handler.MobProjectorScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MobProjectorScreen extends AExpMachineScreen<MobProjectorScreenHandler> {
	public static final Identifier BACKGROUND = new Identifier("cpp:textures/gui/mob_projector.png");
	public static final List<Identifier> PICTURES;

	public MobProjectorScreen(MobProjectorScreenHandler handler, PlayerInventory inventory, Text title) {
		super(handler, inventory, title);
	}

	@Override
	protected Identifier getBackground() {
		return BACKGROUND;
	}

	protected void init() {
		super.init();
		oButton.setPos(x + CodingTool.x(5), y + CodingTool.y(0));
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

//		System.out.println(handler.blockEntity.getCurrentRecipeCode());
		if (handler.blockEntity.getCurrentRecipeCode() > 0) {
			client.getTextureManager().bindTexture(PICTURES.get(handler.blockEntity.getCurrentRecipeCode()));
			drawTexture(matrices, x + 19, y + 18, 0, 0, 52, 52, 52, 52);
		}

		client.getTextureManager().bindTexture(BACKGROUND);
		drawTexture(matrices, x + 106, y + 40, 88, 0, 38, 26, 128, 128);
		int t = (int) (System.currentTimeMillis() % (12 * 50) / 50) / 2;
		drawTexture(matrices, x + 107, y + 50, 88, 26 + t * 7, 36, 7, 128, 128);

		int dy = 52 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal();
		drawTexture(matrices, x + 19, y + 18 + dy, 19, 18 + dy, 52, 52 - dy);
	}

	public boolean workTimeIsHovered(int mx, int my) {
		return mx >= x + 19 && mx <= x + 19 + 52 && my >= y + 18 && my <= y + 18 + 52;
	}

	static {
		List<Identifier> list = new LinkedList<>();
		list.add(null);
		for (String s : new String[] { "sheep", "cow", "pig", "chicken", "rabbit", "bat", "squid", "creeper", "zombie", "skeleton", "spider", "silverfish", "polar_bear", "witch", "slime", "phantom", "piglin", "ghast", "magma_cube", "blaze", "enderman", "endermite", "strider", "villager", "vindicator", "guardian", "shulker", "wither_skeleton", "wolf", "cat", "horse", "donkey", "llama", "parrot", "turtle", "fox", "panda", "bee", "dolphin", "cod", "salmon", "tropical_fish", "pufferfish" })
			list.add(new Identifier(String.format("cpp:textures/gui/mob_projector_pictures/%s.png", s)));
		PICTURES = Collections.unmodifiableList(list);
	}
}
