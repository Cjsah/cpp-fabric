package net.cpp.config;

import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;

public class ConfigEntry extends EntryListWidget.Entry<ConfigEntry>{

    protected OrderedText text;

    public ConfigEntry(OrderedText text) {
        this.text = text;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

    }
}
