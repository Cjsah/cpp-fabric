package net.cjsah.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends HandledScreen<CraftingMachineScreenHandler> {

    public static final Identifier TEXTURE = new Identifier("cpp","textures/gui/crafting_machine.png");
    public int OUT = 1;

    public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = false;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {

        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;

        if (mouseX >= i+94 && mouseX <= i+109 && mouseY > j+57 && mouseY <= j+72) {
            switch (OUT) {
                case 1: {
                    System.out.println("south");
                    OUT = 2;
                    break;
                }
                case 2: {
                    System.out.println("west");
                    OUT = 3;
                    break;
                }
                case 3: {
                    System.out.println("north");
                    OUT = 4;
                    break;
                }
                case 4: {
                    System.out.println("down");
                    OUT = 5;
                    break;
                }
                case 5: {
                    System.out.println("up");
                    OUT = 6;
                    break;
                }
                case 6: {
                    System.out.println("east");
                    OUT = 1;
                    break;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
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
        this.client.getTextureManager().bindTexture(TEXTURE);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
        switch (OUT) {
            case 1: {
                this.drawTexture(matrices, i+94, j+57, 0, 166, 16, 16);
                break;
            }
            case 2: {
                this.drawTexture(matrices, i+94, j+57, 16, 166, 16, 16);
                break;
            }
            case 3: {
                this.drawTexture(matrices, i+94, j+57, 32, 166, 16, 16);
                break;
            }
            case 4: {
                this.drawTexture(matrices, i+94, j+57, 48, 166, 16, 16);
                break;
            }
            case 5: {
                this.drawTexture(matrices, i+94, j+57, 64, 166, 16, 16);
                break;
            }
            case 6: {
                this.drawTexture(matrices, i+94, j+57, 80, 166, 16, 16);
                break;
            }
        }
    }
}
