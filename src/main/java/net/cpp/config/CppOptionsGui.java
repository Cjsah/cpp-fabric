package net.cpp.config;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.logging.log4j.Logger;

import net.cpp.Craftingpp;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CppOptionsGui extends Screen {
    private static final Logger LOGGER = Craftingpp.logger;
    private static final CppConfig config = Craftingpp.CONFIG;
    private final Screen screen;
    private TextFieldWidget searchBox;
    private DescriptionListWidget descriptionListWidget;
    private ConfigListWidget configList;
    private Text tooltip;
    private ConfigListEntry selected;
    private double scrollPercent = 0.0D;
    private boolean init = false;
    private boolean filterOptionsShown = false;
    private int paneY;
    private int paneWidth;
    private int rightPaneX;
    private int searchBoxX;
    private int filtersX;
    private int filtersWidth;
    private int searchRowWidth;
    public final Set<String> showModChildren = new HashSet();

    public CppOptionsGui(Screen screen) {
        super(new TranslatableText("modmenu.title"));
        this.screen = screen;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.configList.isMouseOver(mouseX, mouseY)) {
            return this.configList.mouseScrolled(mouseX, mouseY, amount);
        } else {
            return this.descriptionListWidget.isMouseOver(mouseX, mouseY) && this.descriptionListWidget.mouseScrolled(mouseX, mouseY, amount);
        }
    }

    public void tick() {
        this.searchBox.tick();
    }

    protected void init() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(true);
        this.paneY = 48;
        this.paneWidth = this.width / 2 - 8;
        this.rightPaneX = this.width - this.paneWidth;
        int searchBoxWidth = this.paneWidth - 32 - 22;
        this.searchBoxX = this.paneWidth / 2 - searchBoxWidth / 2 - 11;
        this.searchBox = new TextFieldWidget(this.textRenderer, this.searchBoxX, 22, searchBoxWidth, 20, this.searchBox, new TranslatableText("modmenu.search"));
        this.configList = new ConfigListWidget(this.client, this.paneWidth, this.height, this.paneY + 19, this.height - 36, 36, this.searchBox.getText(), config.JSON, this);

        this.configList.setLeftPos(0);
        this.descriptionListWidget = new DescriptionListWidget(this.client, this.paneWidth, this.height, this.paneY + 60, this.height - 36, 9 + 1, this);
        this.descriptionListWidget.setLeftPos(this.rightPaneX);
        this.children.add(this.searchBox);
        this.searchRowWidth = this.searchBoxX + searchBoxWidth + 22;
        this.updateFiltersX();
        this.children.add(this.configList);
        this.children.add(this.descriptionListWidget);
        this.addButton(new ButtonWidget(this.width / 2 -75, this.height - 28, 150, 20, ScreenTexts.DONE, (button) -> this.client.openScreen(this.screen)));
        this.setInitialFocus(this.searchBox);
        this.init = true;

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || this.searchBox.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        this.tooltip = null;
        ConfigListEntry selectedEntry = this.selected;
        if (selectedEntry != null) {
            this.descriptionListWidget.render(matrices, mouseX, mouseY, delta);
        }

        this.configList.render(matrices, mouseX, mouseY, delta);
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        drawTextWithShadow(matrices, this.textRenderer, this.title, this.configList.getWidth() / 2, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
    }

    private boolean updateFiltersX() {
        if (this.filtersWidth + this.textRenderer.getWidth(this.getConfigCountText()) + 20 >= this.searchRowWidth && (this.filtersWidth + this.textRenderer.getWidth(this.getConfigCountText()) + 20 >= this.searchRowWidth || this.filtersWidth + this.textRenderer.getWidth(this.getConfigCountText()) + 20 >= this.searchRowWidth)) {
            this.filtersX = this.paneWidth / 2 - this.filtersWidth / 2;
            return !this.filterOptionsShown;
        } else {
            this.filtersX = this.searchRowWidth - this.filtersWidth + 1;
            return true;
        }
    }

    ConfigListEntry getSelectedEntry() {
        return this.selected;
    }

    private Text getConfigCountText() {
        return new TranslatableText("modmenu.showingMods.a", this.configList.getDisplayedCount());
    }

    private void setTooltip(Text tooltip) {
        this.tooltip = tooltip;
    }

    double getScrollPercent() {
        return this.scrollPercent;
    }

    void updateScrollPercent(double scrollPercent) {
        this.scrollPercent = scrollPercent;
    }

    public void renderBackground(MatrixStack matrices) {
        overlayBackground(0, 0, this.width, this.height, 64, 64, 64, 255, 255);
    }

    static void overlayBackground(int x1, int y1, int x2, int y2, int red, int green, int blue, int startAlpha, int endAlpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(x1, y2, 0.0D).texture((float)x1 / 32.0F, (float)y2 / 32.0F).color(red, green, blue, endAlpha).next();
        buffer.vertex(x2, y2, 0.0D).texture((float)x2 / 32.0F, (float)y2 / 32.0F).color(red, green, blue, endAlpha).next();
        buffer.vertex(x2, y1, 0.0D).texture((float)x2 / 32.0F, (float)y1 / 32.0F).color(red, green, blue, startAlpha).next();
        buffer.vertex(x1, y1, 0.0D).texture((float)x1 / 32.0F, (float)y1 / 32.0F).color(red, green, blue, startAlpha).next();
        tessellator.draw();
    }

    public void onClose() {
        super.onClose();
        this.configList.close();
        this.client.openScreen(this.screen);
    }

}
