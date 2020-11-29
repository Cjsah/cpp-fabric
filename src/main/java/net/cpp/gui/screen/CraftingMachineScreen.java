package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends HandledScreen<CraftingMachineScreenHandler> {

    public static final Identifier TEXTURE = new Identifier("cpp","textures/gui/crafting_machine.png");
//    public byte out;
    public CraftingMachineScreenHandler handler;

    public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = false;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.handler = handler;
//        this.out = this.handler.block.getFacing();
    }

//    @Override
//    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//
//        int i = (this.width - this.backgroundWidth) / 2;
//        int j = (this.height - this.backgroundHeight) / 2;
//
//        if (mouseX >= i+94 && mouseX <= i+109 && mouseY > j+57 && mouseY <= j+72) {
//            switch (handler.block.changeFacing()) {
//                case 1: {
//                    out = 1;
//                    break;
//                }
//                case 2: {
//                    out = 2;
//                    break;
//                }
//                case 3: {
//                    out = 3;
//                    break;
//                }
//                case 4: {
//                    out = 4;
//                    break;
//                }
//                case 5: {
//                    out = 5;
//                    break;
//                }
//                case 6: {
//                    out = 6;
//                    break;
//                }
//            }
//            handler.block.registerFacing();
//        }
//
//        return super.mouseClicked(mouseX, mouseY, button);
//    }

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
//        switch (out) {
//            case 1: {
//                this.drawTexture(matrices, i+94, j+57, 0, 166, 16, 16);
//                break;
//            }
//            case 2: {
//                this.drawTexture(matrices, i+94, j+57, 16, 166, 16, 16);
//                break;
//            }
//            case 3: {
//                this.drawTexture(matrices, i+94, j+57, 32, 166, 16, 16);
//                break;
//            }
//            case 4: {
//                this.drawTexture(matrices, i+94, j+57, 48, 166, 16, 16);
//                break;
//            }
//            case 5: {
//                this.drawTexture(matrices, i+94, j+57, 64, 166, 16, 16);
//                break;
//            }
//            case 6: {
//                this.drawTexture(matrices, i+94, j+57, 80, 166, 16, 16);
//                break;
//            }
//        }
    }
}
