package cpp.config;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import cpp.Craftingpp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Language;

public class DescriptionListWidget extends EntryListWidget<DescriptionListWidget.DescriptionEntry> {
    private final OptionsScreen parent;
    private ConfigListEntry lastSelected = null;
//    private boolean scrolling;

    public DescriptionListWidget(MinecraftClient client, int width, int height, int top, int bottom, int entryHeight, OptionsScreen parent) {
        super(client, width, height, top, bottom, entryHeight);
        this.parent = parent;

        this.filter();
        this.setScrollAmount(parent.getScrollPercent() * (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
    }

    private void filter() {
        if (this.getScrollAmount() > (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4))) {
            this.setScrollAmount(Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
        }
    }

    @Override
    @SuppressWarnings("DuplicatedCode")
    public void setScrollAmount(double amount) {
        super.setScrollAmount(amount);
        int denominator = Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4));
        if (denominator <= 0) {
            this.parent.updateScrollPercent(0.0D);
        } else {
            this.parent.updateScrollPercent(this.getScrollAmount() / (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
        }
    }

    @Override
    protected void updateScrollingState(double mouseX, double mouseY, int button) {
        super.updateScrollingState(mouseX, mouseY, button);
//        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPositionX() && mouseX < (double)(this.getScrollbarPositionX() + 6);
    }

    @Override
    public DescriptionListWidget.DescriptionEntry getSelected() {
        return null;
    }

    @Override
    public int getRowWidth() {
        return this.width - 10;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.width - 6 + this.left;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        ConfigListEntry selectedEntry = this.parent.getSelectedEntry();
        if (selectedEntry != this.lastSelected) {
            this.lastSelected = selectedEntry;
            this.clearEntries();
            this.setScrollAmount(-1.7976931348623157E308D);

            for (String line : Craftingpp.CONFIG.formatJson(Craftingpp.CONFIG.getConfig(selectedEntry.getKey())).split("\n")) {
                this.children().add(new DescriptionListWidget.DescriptionEntry(Text.of(line), this));
            }
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.disableTexture();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(this.left, (this.top + 4), 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.right, (this.top + 4), 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.right, this.top, 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.left, this.top, 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.left, this.bottom, 0.0D).texture(0.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.right, this.bottom, 0.0D).texture(1.0F, 1.0F).color(0, 0, 0, 255).next();
        bufferBuilder.vertex(this.right, (this.bottom - 4), 0.0D).texture(1.0F, 0.0F).color(0, 0, 0, 0).next();
        bufferBuilder.vertex(this.left, (this.bottom - 4), 0.0D).texture(0.0F, 0.0F).color(0, 0, 0, 0).next();
        tessellator.draw();


        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(this.left, this.bottom, 0.0D).color(0, 0, 0, 128).next();
        bufferBuilder.vertex(this.right, this.bottom, 0.0D).color(0, 0, 0, 128).next();
        bufferBuilder.vertex(this.right, this.top, 0.0D).color(0, 0, 0, 128).next();
        bufferBuilder.vertex(this.left, this.top, 0.0D).color(0, 0, 0, 128).next();
        tessellator.draw();

        int k = this.getRowLeft();
        int l = this.top + 4 - (int)this.getScrollAmount();
        this.renderList(matrices, k, l, mouseX, mouseY, delta);

        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    static class DescriptionEntry extends EntryListWidget.Entry<DescriptionListWidget.DescriptionEntry>{

        private final Text text;
        private final DescriptionListWidget parent;

        public DescriptionEntry(Text text, DescriptionListWidget parent) {
            this.text = text;
            this.parent = parent;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            if (y > 68 && y < parent.height - 45) MinecraftClient.getInstance().textRenderer.draw(matrices, Language.getInstance().reorder(this.text), (float)x, (float)y, 11184810);
        }
    }
}


