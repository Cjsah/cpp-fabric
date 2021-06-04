package cpp.config;

import com.mojang.blaze3d.systems.RenderSystem;
import cpp.Craftingpp;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget.Entry;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Language;
import net.minecraft.util.registry.Registry;


public class ConfigListEntry extends Entry<ConfigListEntry> {

    protected Identifier iconLocation;
    protected final MinecraftClient client = MinecraftClient.getInstance();
//    public static final Identifier UNKNOWN_ICON = new Identifier("textures/misc/unknown_pack.png");
    private final String key;
    private final ConfigListWidget list;

    public ConfigListEntry(String key, ConfigListWidget list) {
        this.key = key;
        this.list = list;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
        x += getXOffset();
        entryWidth -= getXOffset();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        this.bindIconTexture();
        RenderSystem.enableBlend();
        // 图标
        DrawableHelper.drawTexture(matrices, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
        RenderSystem.disableBlend();
        Text name = Text.of(key);
        StringVisitable trimmedName = name;
        int maxNameWidth = entryWidth - 32 - 3;
        TextRenderer font = this.client.textRenderer;
        if (font.getWidth(name) > maxNameWidth) {
            StringVisitable ellipsis = StringVisitable.plain("...");
            trimmedName = StringVisitable.concat(font.trimToWidth(name, maxNameWidth - font.getWidth(ellipsis)), ellipsis);
        }
        // 名
        font.draw(matrices, Language.getInstance().reorder(trimmedName), x + 16 + 3, y + 3, 0xFFFFFF);
    }

    public Text getConfigName() {
        return getConfigItem().getName();
    }

    public Item getConfigItem() {
        return Registry.ITEM.get(new Identifier(Craftingpp.MOD_ID3, this.key));
    }

    public void bindIconTexture() {
        if (this.iconLocation == null) {
            this.iconLocation = new Identifier(Craftingpp.MOD_ID3, "textures/item/" + this.key + ".png");
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

    public int getXOffset() {
        return 0;
    }

    @Override
    public Text method_37006() {
        return null;
    }
}
