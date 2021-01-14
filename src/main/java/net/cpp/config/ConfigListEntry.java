package net.cpp.config;

import net.cpp.Craftingpp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget.Entry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.Logger;

public class ConfigListEntry extends Entry<ConfigListEntry> {

    protected Identifier iconLocation;
    protected final MinecraftClient client = MinecraftClient.getInstance();
    public static final Identifier UNKNOWN_ICON = new Identifier("textures/misc/unknown_pack.png");
    private static final Logger LOGGER = Craftingpp.logger;


    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

    }

    public void bindIconTexture() {
        if (this.iconLocation == null) {
            this.iconLocation = new Identifier("modmenu", "icon");
//            NativeImageBackedTexture icon = this.createIcon();
//            if (icon != null) {
//                this.client.getTextureManager().registerTexture(this.iconLocation, icon);
//            } else {
//                this.iconLocation = UNKNOWN_ICON;
//            }
        }

        this.client.getTextureManager().bindTexture(this.iconLocation);
    }

}
