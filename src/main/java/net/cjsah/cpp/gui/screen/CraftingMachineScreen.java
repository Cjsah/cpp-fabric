package net.cjsah.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.cjsah.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends HandledScreen<CraftingMachineScreenHandler> {

    public static final Identifier TEXTURE = new Identifier("cpp","textures/gui/crafting_machine.png");

    public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.passEvents = false;
        this.backgroundHeight = 166;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
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
    }




//
//        // Upper-left corner
//        this.drawTexture(matrices, 0, 0, 0, 0, 7, 17);
//
//        // Top
//        drawTexture(matrices, 7, 0, handler.inventory().width() * 18, 17, 7, 0, 1, 17, 256, 256);
//
//        // Upper-right corner
//        this.drawTexture(matrices, 7 + handler.inventory().width() * 18, 0, 169, 0, 7, 17);
//
//        // Left
//        drawTexture(matrices, 0, 17, 7, (handler.inventory().height() + 4) * 18 + 22, 0, 17, 7, 1, 256, 256);
//
//        // Lower-left corner
//        this.drawTexture(matrices, 0, (handler.inventory().height() + 4) * 18 + 34, 0, 215, 7, 7);
//
//        // Lower
//        drawTexture(matrices, 7, (handler.inventory().height() + 4) * 18 + 34, handler.inventory().width() * 18, 7, 7, 215, 1, 7, 256, 256);
//
//        // Lower-right corner
//        this.drawTexture(matrices, (handler.inventory().width() * 18 + 7), (handler.inventory().height() + 4) * 18 + 34, 169, 215, 7, 7);
//
//        // Right
//        drawTexture(matrices, (handler.inventory().width() * 18 + 7), 17, 7, (handler.inventory().height() + 4) * 18 + 17, 169, 17, 7, 1, 256, 256);
//
//        // Background fill
//        fill(matrices, 7, 17, this.backgroundWidth - 10, this.backgroundHeight - 7, 0xFFC6C6C6);
//
//        this.handler.slots.forEach(s -> {
//            this.drawTexture(matrices, s.x - 1, s.y - 1, 7, 17, 18, 18);
//        });

//        matrices.pop();

//    }
//    @Override
//    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
//        this.renderBackground(matrices);
//        super.render(matrices, mouseX, mouseY, delta);
//        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
//    }

}
