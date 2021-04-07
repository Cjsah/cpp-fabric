package cpp.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import cpp.Craftingpp;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public abstract class AMachineScreen<T extends ScreenHandler> extends HandledScreen<T> {
	public static final String CONTAINER_GUI_PATH = Craftingpp.MOD_ID3 + ":textures/gui/container/";
	public AMachineScreen(T handler, PlayerInventory inventory, Text title) {
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
		this.client.getTextureManager().bindTexture(getBackground());
		int i = this.x;
		int j = (this.height - this.backgroundHeight) / 2;
		this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
	}
	
	protected abstract Identifier getBackground() ;
	public static Identifier getBackgroundByName(String name){
		return new Identifier(CONTAINER_GUI_PATH + name + ".png");
	}
}
