package net.cpp.config;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.prospector.modmenu.ModMenu;
import net.cpp.api.ICppConfig;
import net.cpp.api.TexturedButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;

import net.cpp.Craftingpp;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class CppOptionsGui extends Screen {
    private static final Identifier RESET_BUTTON = new Identifier(ModMenu.MOD_ID, "textures/gui/configure_button.png");
    private static final Text RESET_TEXT = new TranslatableText("cpp.modmenu.reset");
    private static final CppConfig config = Craftingpp.CONFIG;
    private final Screen screen;
    private TextFieldWidget searchBox;
    private int searchBoxX;
    private ConfigWidget configWidget;
    private ConfigListWidget configList;
    private Text tooltip;
    private ConfigListEntry selected;
    private double scrollPercent = 0.0D;
    private int paneY;
    private int rightPaneX;

    public CppOptionsGui(Screen screen) {
        super(new TranslatableText("cpp.modmenu.title"));
        this.screen = screen;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (this.configList.isMouseOver(mouseX, mouseY)) {
            return this.configList.mouseScrolled(mouseX, mouseY, amount);
        } else {
            return this.configWidget.isMouseOver(mouseX, mouseY) && this.configWidget.mouseScrolled(mouseX, mouseY, amount);
        }
    }

    public void tick() {
        this.searchBox.tick();
    }

    protected void init() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(true);
        this.paneY = 48;
        int paneWidth = this.width / 2 - 8;
        this.rightPaneX = this.width - paneWidth;
        int searchBoxWidth = paneWidth - 32 - 22;
        this.searchBoxX = paneWidth / 2 - searchBoxWidth / 2 - 11;
        this.searchBox = new TextFieldWidget(this.textRenderer, searchBoxX, 22, searchBoxWidth, 20, this.searchBox, new TranslatableText("modmenu.search"));
        this.configList = new ConfigListWidget(this.client, paneWidth, this.height, this.paneY + 19, this.height - 36, 20, this.searchBox.getText(), config.JSON, this);

        this.configList.setLeftPos(0);
        this.configWidget = new ConfigWidget(this.client, paneWidth, this.height, this.paneY + 19, this.height - 36, 9 + 1, this);
        this.configWidget.setLeftPos(this.rightPaneX);
        this.children.add(this.searchBox);
        this.children.add(this.configList);
        this.children.add(this.configWidget);
        this.addButton(new TexturedButtonWidget(width - 40, paneY - 20, 20, 20, 0, 0, RESET_TEXT, RESET_BUTTON, 32, 64, button -> {


        }, (buttonWidget, matrices, mouseX, mouseY) -> {
            TexturedButtonWidget button = (TexturedButtonWidget) buttonWidget;
            if (button.isJustHovered()) {
                this.renderTooltip(matrices, RESET_TEXT, mouseX, mouseY);
            } else if (button.isFocusedButNotHovered()) {
                this.renderTooltip(matrices, RESET_TEXT, button.x, button.y);
            }
        }) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

                this.active = selected != null && selected.getConfigItem() instanceof ICppConfig && !Objects.equals(selected.getValue().toString(), ((ICppConfig) selected.getConfigItem()).defaultConfig(new JsonObject()).toString());

                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                RenderSystem.color4f(1, 1, 1, 1f);
                super.renderButton(matrices, mouseX, mouseY, delta);
            }
        });
        this.addButton(new ButtonWidget(this.width / 2 -75, this.height - 28, 150, 20, ScreenTexts.DONE, (button) -> this.client.openScreen(this.screen)));
        this.setInitialFocus(this.searchBox);
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
            this.configWidget.render(matrices, mouseX, mouseY, delta);
        }

        this.configList.render(matrices, mouseX, mouseY, delta);
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        drawTextWithShadow(matrices, this.textRenderer, this.title, this.configList.getWidth() / 2 - 40, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
        this.configList.reloadFilters();

        this.textRenderer.draw(matrices, getConfigCountText(), (float)this.searchBoxX, 52.0F, 16777215);

        if (selectedEntry != null) {
            String key = selectedEntry.getKey();
            int x = this.rightPaneX;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            this.selected.bindIconTexture();
            RenderSystem.enableBlend();
            // 图标
            drawTexture(matrices, x, this.paneY - 25, 0.0F, 0.0F, 32, 32, 32, 32);
            RenderSystem.disableBlend();
            int lineSpacing = 9 + 1;
            int imageOffset = 36;
            Text name = this.selected.getConfigName();
            StringVisitable trimmedName = name;
            int maxNameWidth = this.width - (x + imageOffset);
            if (this.textRenderer.getWidth(name) > maxNameWidth) {
                StringVisitable ellipsis = StringVisitable.plain("...");
                trimmedName = StringVisitable.concat(this.textRenderer.trimToWidth(name, maxNameWidth - this.textRenderer.getWidth(ellipsis)), ellipsis);
            }
            // 名
            this.textRenderer.draw(matrices, Language.getInstance().reorder(trimmedName), (float)(x + imageOffset), (float)(this.paneY - 21), 16777215);
//            if (mouseX > x + imageOffset && mouseY > this.paneY + 1) {
//                int var10001 = this.paneY + 1;
//                if (mouseY < var10001 + 9 && mouseX < x + imageOffset + this.textRenderer.getWidth(trimmedName)) {
//                    this.setTooltip(new TranslatableText("modmenu.modIdToolTip", key));
//                }
//            }

            // 说明
            this.textRenderer.draw(matrices, key, (float)(x + imageOffset), (float)(this.paneY - 18 + lineSpacing), 8421504);


//            if (mouseX > x + imageOffset && mouseY > paneY + 1 && mouseY < paneY + 1 + textRenderer.fontHeight && mouseX < x + imageOffset + textRenderer.getWidth(trimmedName)) {
//                setTooltip(new TranslatableText("cpp.modmenu.reset"));
//            }

            if (this.tooltip != null) {
                this.renderOrderedTooltip(matrices, this.textRenderer.wrapLines(this.tooltip, 2147483647), mouseX, mouseY);
            }
        }

    }

    ConfigListEntry getSelectedEntry() {
        return this.selected;
    }

    private Text getConfigCountText() {
        return new TranslatableText("cpp.modmenu.showing", this.configList.getDisplayedCount());
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
        Objects.requireNonNull(this.client).openScreen(this.screen);
    }

    void updateSelectedEntry(ConfigListEntry entry) {
        if (entry != null) {
            this.selected = entry;
        }
    }

    public String getSearchInput() {
        return this.searchBox.getText();
    }

}
