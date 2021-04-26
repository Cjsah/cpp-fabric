package cpp.config;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.ModMenu;
import cpp.api.ICppConfig;
import cpp.api.TexturedButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ScreenTexts;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

import java.util.Objects;

public class OptionsScreen extends Screen {
    private static final Identifier SETTING_BUTTON = new Identifier(ModMenu.MOD_ID, "textures/gui/configure_button.png");
    private static final Text SETTING_TEXT = new TranslatableText("modmenu.cpp.setting");
    private final Screen screen;
    private TextFieldWidget searchBox;
    private int searchBoxX;
    private DescriptionListWidget descriptionListWidget;
    private ConfigListWidget configList;
    private ConfigListEntry selected;
    private double scrollPercent = 0.0D;
    private int paneY;
    private int rightPaneX;

    public OptionsScreen(Screen screen) {
        super(new TranslatableText("modmenu.cpp.title"));
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

    @Override
    public void tick() {
        this.searchBox.tick();
    }

    @Override
    protected void init() {
        Objects.requireNonNull(this.client).keyboard.setRepeatEvents(true);
        this.paneY = 48;
        int paneWidth = this.width / 2 - 8;
        this.rightPaneX = this.width - paneWidth;
        int searchBoxWidth = paneWidth - 32 - 22;
        this.searchBoxX = paneWidth / 2 - searchBoxWidth / 2 - 11;
        this.searchBox = new TextFieldWidget(this.textRenderer, searchBoxX, 22, searchBoxWidth, 20, this.searchBox, new TranslatableText("modmenu.search"));
        this.configList = new ConfigListWidget(this.client, paneWidth, this.height, this.paneY + 19, this.height - 36, 20, this.searchBox.getText(), this);

        this.configList.setLeftPos(0);
        this.descriptionListWidget = new DescriptionListWidget(this.client, paneWidth, this.height, this.paneY + 19, this.height - 36, 9 + 1, this);
        this.descriptionListWidget.setLeftPos(this.rightPaneX);
        this.children.add(this.searchBox);
        this.children.add(this.configList);
        this.children.add(this.descriptionListWidget);
        // 设置
        this.addButton(new TexturedButtonWidget(width - 40, paneY - 20, 20, 20, 0, 0, SETTING_TEXT, SETTING_BUTTON, 32, 64, button -> {
            //screen
            if (this.selected != null) {
                this.client.openScreen(new ConfigScreen(this, this.selected.getKey(), ((ICppConfig)selected.getConfigItem()).getDefaultConfig()));
            }
        }, (buttonWidget, matrices, mouseX, mouseY) -> {
            TexturedButtonWidget button = (TexturedButtonWidget) buttonWidget;
            if (button.isJustHovered()) {
                this.renderTooltip(matrices, SETTING_TEXT, mouseX, mouseY);
            } else if (button.isFocusedButNotHovered()) {
                this.renderTooltip(matrices, SETTING_TEXT, button.x, button.y);
            }
        }) {
            @Override
            public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

                this.active = selected != null;

                super.render(matrices, mouseX, mouseY, delta);
            }

            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderColor(1, 1, 1, 1f);
                super.renderButton(matrices, mouseX, mouseY, delta);
            }
        });
        this.addButton(new ButtonWidget(this.width / 2 -75, this.height - 28, 150, 20, ScreenTexts.DONE, (button) -> this.client.openScreen(this.screen)));
        this.setInitialFocus(this.searchBox);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        ConfigListEntry selectedEntry = this.selected;
        if (selectedEntry != null) {
            this.descriptionListWidget.render(matrices, mouseX, mouseY, delta);
        }

        this.configList.render(matrices, mouseX, mouseY, delta);
        this.searchBox.render(matrices, mouseX, mouseY, delta);
        RenderSystem.disableBlend();
        drawTextWithShadow(matrices, this.textRenderer, this.title, this.configList.getWidth() / 2 - 40, 8, 16777215);
        super.render(matrices, mouseX, mouseY, delta);
//        this.configList.reloadFilters();

        this.textRenderer.draw(matrices, getConfigCountText(), (float)this.searchBoxX, 52.0F, 16777215);

        if (selectedEntry != null) {
            String key = selectedEntry.getKey();
            int x = this.rightPaneX;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
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

            // 说明
            this.textRenderer.draw(matrices, key, (float)(x + imageOffset), (float)(this.paneY - 18 + lineSpacing), 8421504);
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers) || this.searchBox.keyPressed(keyCode, scanCode, modifiers);
    }

    ConfigListEntry getSelectedEntry() {
        return this.selected;
    }

    private Text getConfigCountText() {
        return new TranslatableText("modmenu.cpp.showing", this.configList.getDisplayedCount());
    }

    double getScrollPercent() {
        return this.scrollPercent;
    }

    void updateScrollPercent(double scrollPercent) {
        this.scrollPercent = scrollPercent;
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void renderBackground(MatrixStack matrices) {
        int x1 = 0, y1 = 0, x2 = this.width, y2 = this.height;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        MinecraftClient.getInstance().getTextureManager().bindTexture(DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        buffer.vertex(x1, y2, 0.0D).texture((float)x1 / 32.0F, (float)y2 / 32.0F).color(64, 64, 64, 255).next();
        buffer.vertex(x2, y2, 0.0D).texture((float)x2 / 32.0F, (float)y2 / 32.0F).color(64, 64, 64, 255).next();
        buffer.vertex(x2, y1, 0.0D).texture((float)x2 / 32.0F, (float)y1 / 32.0F).color(64, 64, 64, 255).next();
        buffer.vertex(x1, y1, 0.0D).texture((float)x1 / 32.0F, (float)y1 / 32.0F).color(64, 64, 64, 255).next();
        tessellator.draw();
    }

    @Override
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
