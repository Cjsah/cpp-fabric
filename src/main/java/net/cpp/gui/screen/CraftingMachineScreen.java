package net.cpp.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;

import net.cpp.gui.handler.CraftingMachineScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CraftingMachineScreen extends HandledScreen<CraftingMachineScreenHandler> {
    private static final Identifier TEXTURE = new Identifier("cpp:textures/gui/crafting_machine_gui.png");
    //	private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier("minecraft:textures/gui/recipe_button.png");
//	private final RecipeBookWidget recipeBook = new RecipeBookWidget();
    private boolean narrow;
    //	private CraftingMachineBlockEntity blockEntity;
    public final OutputDirectionButton oButton;

    public CraftingMachineScreen(CraftingMachineScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
//		blockEntity = handler.getBlockEntity();
        oButton = new OutputDirectionButton(buttonWidget -> {
            handler.propertyDelegate.set(0, handler.propertyDelegate.get(0) + 1);
            this.client.interactionManager.clickButton(this.handler.syncId, 1010);
        }, handler.propertyDelegate);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
//		if (this.recipeBook.isOpen() && this.narrow) {
//			this.drawBackground(matrices, delta, mouseX, mouseY);
//			this.recipeBook.render(matrices, mouseX, mouseY, delta);
//		} else {
//			this.recipeBook.render(matrices, mouseX, mouseY, delta);
        super.render(matrices, mouseX, mouseY, delta);
//		this.recipeBook.drawGhostSlots(matrices, this.x, this.y, true, delta);
//		}
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
//		this.recipeBook.drawTooltip(matrices, this.x, this.y, mouseX, mouseY);
    }

    protected void init() {
        super.init();
        this.narrow = this.width < 379;
//		this.recipeBook.initialize(this.width, this.height, this.client, this.narrow,
//				(AbstractRecipeScreenHandler<?>) this.handler);
//		this.x = this.recipeBook.findLeftEdge(this.narrow, this.width, this.backgroundWidth);
//		this.children.add(this.recipeBook);
//		this.setInitialFocus(this.recipeBook);
        oButton.setPos(x + 94, y + 57);
        this.addButton(oButton);
//		this.addButton(new TexturedButtonWidget(this.x + 5, this.height / 2 - 49, 20, 18, 0, 0, 19,
//				RECIPE_BUTTON_TEXTURE, (buttonWidget) -> {
//					this.recipeBook.reset(this.narrow);
//					this.recipeBook.toggleOpen();
//					this.x = this.recipeBook.findLeftEdge(this.narrow, this.width, this.backgroundWidth);
//					((TexturedButtonWidget) buttonWidget).setPos(this.x + 5, this.height / 2 - 49);
//					oButton.setPos(x + 94, y + 57);
//				}));
        this.titleX = 29;
    }

    public void tick() {
        super.tick();
//		this.recipeBook.update();
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(TEXTURE);
        int i = this.x;
        int j = (this.height - this.backgroundHeight) / 2;
        this.drawTexture(matrices, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }

    protected boolean isPointWithinBounds(int xPosition, int yPosition, int width, int height, double pointX,
                                          double pointY) {
        return (!this.narrow /* || !this.recipeBook.isOpen() */)
                && super.isPointWithinBounds(xPosition, yPosition, width, height, pointX, pointY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
//		if (this.recipeBook.mouseClicked(mouseX, mouseY, button)) {
//			this.setFocused(this.recipeBook);
//			clicked = true;
//		} else {
//			clicked = this.narrow && this.recipeBook.isOpen() ? true :
//		}

        return super.mouseClicked(mouseX, mouseY, button);
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double) left || mouseY < (double) top || mouseX >= (double) (left + this.backgroundWidth)
                || mouseY >= (double) (top + this.backgroundHeight);
        return /*
         * this.recipeBook.isClickOutsideBounds(mouseX, mouseY, this.x, this.y,
         * this.backgroundWidth, this.backgroundHeight, button) &&
         */ bl;
    }

    protected void onMouseClick(Slot slot, int invSlot, int clickData, SlotActionType actionType) {
        super.onMouseClick(slot, invSlot, clickData, actionType);
//		this.recipeBook.slotClicked(slot);
    }

    public void refreshRecipeBook() {
//		this.recipeBook.refresh();
    }

    public void removed() {
//		this.recipeBook.close();
        super.removed();
    }

//	public RecipeBookWidget getRecipeBookWidget() {
//		return this.recipeBook;
//	}
}
