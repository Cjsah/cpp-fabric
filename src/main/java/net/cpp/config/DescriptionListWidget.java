package net.cpp.config;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.cpp.Craftingpp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;


public class DescriptionListWidget extends EntryListWidget<DescriptionListWidget.DescriptionEntry> {
    private final CppOptionsGui parent;
    private ConfigListEntry lastSelected = null;

    public DescriptionListWidget(MinecraftClient client, int width, int height, int top, int bottom, int entryHeight, CppOptionsGui parent) {
        super(client, width, height, top, bottom, entryHeight);
        this.parent = parent;
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

            for (OrderedText line : this.client.textRenderer.wrapLines(new LiteralText(Craftingpp.CONFIG.formatJson(Craftingpp.CONFIG.getConfig(selectedEntry.getKey()))), this.getRowWidth())) {
                this.children().add(new DescriptionListWidget.DescriptionEntry(line));
            }


//            this.children().add(new DescriptionListWidget.DescriptionEntry(
//                    new LiteralText(Craftingpp.CONFIG.formatJson(Craftingpp.CONFIG.getConfig(selectedEntry.getKey())))
//            ));

//            for (Map.Entry<String, JsonElement> kv : Craftingpp.CONFIG.getConfig(selectedEntry.getKey()).entrySet()) {
//                this.children().add(new DescriptionListWidget.DescriptionEntry(new LiteralText(kv.toString())));
//            }


//            this.children().add(new DescriptionListWidget.DescriptionEntry((OrderedText) new LiteralText(kv.toString())));
//            JsonElement value = kv.getValue();
//            if (value.isJsonArray()) {
//
//            } else if (value.isJsonObject()) {
//
//            } else if (value.isJsonPrimitive()) {
//                if (value.getAsJsonPrimitive().isBoolean()) {
//
//                } else if (value.getAsJsonPrimitive().isNumber()) {
//
//                } else if (value.getAsJsonPrimitive().isString()) {
//
//                }
//            }
        }

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ZERO, GlStateManager.DstFactor.ONE);
        RenderSystem.disableAlphaTest();
        RenderSystem.shadeModel(7425);
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
        RenderSystem.shadeModel(7424);
        RenderSystem.enableAlphaTest();
        RenderSystem.disableBlend();
    }

    static class DescriptionEntry extends EntryListWidget.Entry<DescriptionListWidget.DescriptionEntry>{

        protected OrderedText text;

        public DescriptionEntry(OrderedText text) {
            this.text = text;
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, this.text, (float)x, (float)y, 11184810);
        }
    }
}


