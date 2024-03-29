package cpp.screen;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.mojang.blaze3d.systems.RenderSystem;
import cpp.api.Utils;
import cpp.init.CppBlocks;
import cpp.screen.handler.MobProjectorScreenHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class MobProjectorScreen extends AExpMachineScreen<MobProjectorScreenHandler> {
	public static final Identifier BACKGROUND = getBackgroundByName(Registry.BLOCK.getId(CppBlocks.MOB_PROJECTOR).getPath());
	public static final Identifier FLASH = getBackgroundByName("mob_projector_flash");
	/**
	 * 生成过程中显示的图片
	 */
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
		oButton.setPos(x + Utils.x(7), y + Utils.y(2));
	}

	protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
		super.drawBackground(matrices, delta, mouseX, mouseY);

		if (handler.blockEntity.getCurrentRecipeCode() > 0) {
			RenderSystem.setShaderTexture(0, PICTURES.get(handler.blockEntity.getCurrentRecipeCode()));
			drawTexture(matrices, x + 19, y + 18, 0, 0, 52, 52, 52, 52);
		}

		RenderSystem.setShaderTexture(0, FLASH);
		int t = (int) (System.currentTimeMillis() % (12 * 50) / 50) / 2;
		drawTexture(matrices, x + 102, y + 40, 0, 26 * t, 26, 26, 26, 156);

		RenderSystem.setShaderTexture(0, BACKGROUND);
		int dy = 52 * handler.blockEntity.getWorkTime() / handler.blockEntity.getWorkTimeTotal();
		drawTexture(matrices, x + 19, y + 18 + dy, 19, 18 + dy, 52, 52 - dy);
	}

	public boolean workTimeIsHovered(int mx, int my) {
		return mx >= x + 19 && mx <= x + 19 + 52 && my >= y + 18 && my <= y + 18 + 52;
	}

	static {
		List<Identifier> list = new LinkedList<>();
		list.add(null);
		for (String s : new String[] { "sheep", "cow", "pig", "chicken", "rabbit", "bat", "squid", "creeper", "zombie", "skeleton", "spider", "silverfish", "polar_bear", "witch", "slime", "phantom", "piglin", "ghast", "magma_cube", "blaze", "enderman", "endermite", "strider", "villager", "vindicator", "guardian", "shulker", "wither_skeleton", "wolf", "cat", "horse", "donkey", "llama", "parrot", "turtle", "fox", "panda", "bee", "dolphin", "cod", "salmon", "tropical_fish", "pufferfish" ,"axolot"})
			list.add(new Identifier(String.format("cpp:textures/gui/mob_projector_pictures/%s.png", s)));
		PICTURES = Collections.unmodifiableList(list);
	}
}
