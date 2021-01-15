package net.cpp.config;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.prospector.modmenu.util.BadgeRenderer;
import io.github.prospector.modmenu.util.HardcodedUtil;
import io.github.prospector.modmenu.util.RenderUtils;
import net.cpp.Craftingpp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget.Entry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.registry.Registry;
import org.apache.logging.log4j.Logger;


public class ConfigListEntry extends Entry<ConfigListEntry> {

    protected Identifier iconLocation;
    protected final MinecraftClient client = MinecraftClient.getInstance();
    public static final Identifier UNKNOWN_ICON = new Identifier("textures/misc/unknown_pack.png");
    private static final Logger LOGGER = Craftingpp.logger;
    private final String key;
    private final JsonObject value;
    private final ConfigListWidget list;

    public ConfigListEntry(String key, JsonObject value, ConfigListWidget list) {
        this.key = key;
        this.value = value;
        this.list = list;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        x += getXOffset();
        entryWidth -= getXOffset();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindIconTexture();
        RenderSystem.enableBlend();
        DrawableHelper.drawTexture(matrices, x, y, 0.0F, 0.0F, 32, 32, 32, 32);
        RenderSystem.disableBlend();
        Text name = Registry.ITEM.get(new Identifier(Craftingpp.MOD_ID3, this.key)).getName();
        StringVisitable trimmedName = name;
        int maxNameWidth = entryWidth - 32 - 3;
        TextRenderer font = this.client.textRenderer;
        if (font.getWidth(name) > maxNameWidth) {
            StringVisitable ellipsis = StringVisitable.plain("...");
            trimmedName = StringVisitable.concat(font.trimToWidth(name, maxNameWidth - font.getWidth(ellipsis)), ellipsis);
        }
        font.draw(matrices, Language.getInstance().reorder(trimmedName), x + 32 + 3, y + 1, 0xFFFFFF);

        RenderUtils.drawWrappedString(matrices, this.getDescription(), x + 32 + 3 + 4, y + 9 + 2, entryWidth - 32 - 7, 2, 8421504);

    }

    public String getDescription() {
        return this.key;
    }

    public void bindIconTexture() {
        if (this.iconLocation == null) {
            this.iconLocation = new Identifier(Craftingpp.MOD_ID3, "textures/item/" + this.key + ".png");
//            NativeImageBackedTexture icon = this.createIcon();
//            if (icon != null) {
//                this.client.getTextureManager().registerTexture(this.iconLocation, icon);
//            } else {
//                this.iconLocation = UNKNOWN_ICON;
//            }
        }

        this.client.getTextureManager().bindTexture(this.iconLocation);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.list.select(this);
        return true;
    }

    public String getKey() {
        return key;
    }

    public JsonObject getValue() {
        return value;
    }

    public int getXOffset() {
        return 0;
    }

}
