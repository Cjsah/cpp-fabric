package net.cpp.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.prospector.modmenu.mixin.EntryListWidgetAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


public class ConfigListWidget extends AlwaysSelectedEntryListWidget<ConfigListEntry> implements AutoCloseable {

    private final CppOptionsGui parent;
    private Set<String> configKeys = new HashSet<>();
    private final Map<Path, NativeImageBackedTexture> configIconsCache = new HashMap<>();
    private List<String> ConfigContainerList = null;
    private Set<String> addedConfigs = new HashSet<>();
    private String selectedConfigId = null;
    private final JsonObject config;
    private boolean scrolling;


    protected ConfigListWidget(MinecraftClient client, int width, int height, int y1, int y2, int entryHeight, String searchTerm, JsonObject config, CppOptionsGui parent) {
        super(client, width, height, y1, y2, entryHeight);
        this.parent = parent;
        this.config = config;
        for (Map.Entry<String, JsonElement> i  : config.entrySet()) {
            configKeys.add(i.getKey());
        }

        this.filter(searchTerm, false);
        this.setScrollAmount(parent.getScrollPercent() * (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));

    }

    @Override
    protected boolean isFocused() {
        return this.parent.getFocused() == this;
    }

    @Override
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
    public void setSelected(ConfigListEntry entry) {
        super.setSelected(entry);
        this.selectedConfigId = entry.getKey();
        this.parent.updateSelectedEntry(this.getSelected());
    }

    @Override
    protected boolean isSelectedItem(int index) {
        ConfigListEntry selected = this.getSelected();
        return selected != null && selected.getKey().equals((this.getEntry(index)).getKey());
    }

    @Override
    protected boolean removeEntry(ConfigListEntry entry) {
        this.addedConfigs.remove(entry.getKey());
        return super.removeEntry(entry);
    }

    @Override
    protected ConfigListEntry remove(int index) {
        this.addedConfigs.remove((this.getEntry(index)).getKey());
        return super.remove(index);
    }

    public void reloadFilters() {
        this.filter(this.parent.getSearchInput(), true);
    }

    private List<String> search(String query, List<String> configKeys) {
        return !(query != null && !query.isEmpty()) ? configKeys : searchEach(query, configKeys);
    }

    private static List<String> searchEach(String query, List<String> configKeys) {
        List<String> result = new ArrayList<>();
        for (String key : configKeys) {
            if (key.contains(query)) result.add(key);
        }
        return result;
    }


    private void filter(String searchTerm, boolean refresh) {
        this.clearEntries();
        this.addedConfigs.clear();
        if (this.ConfigContainerList == null || refresh) {
            this.ConfigContainerList = new ArrayList<>();
            this.ConfigContainerList.addAll(this.configKeys);
            this.ConfigContainerList.sort(Comparator.comparing((configLists) -> configLists));
            ////////////////////////////////////////////////////////////////////////////////////////////
            System.out.println(this.ConfigContainerList);

        }

        List<String> matched = this.search(searchTerm, this.ConfigContainerList);
        System.out.println(matched);


        for (String key1 : matched) {
            if ((this.parent.getSelectedEntry() == null || this.children().isEmpty()) && (this.getSelected() == null || Objects.equals((this.getSelected()).getKey(), this.parent.getSelectedEntry().getKey()))) {
                if (this.getSelected() == null && !this.children().isEmpty() && this.getEntry(0) != null) {
                    this.setSelected(this.getEntry(0));
                }
            } else {
                for (ConfigListEntry key2 : this.children()) {
                    if (key2.getKey().equals(this.parent.getSelectedEntry().getKey())) {
                        this.setSelected(key2);
                    }
                }
            }

            if (this.getScrollAmount() > (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4))) {
                this.setScrollAmount(Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
            }
            this.addEntry(new ConfigListEntry(key1, config.get(key1).getAsJsonObject(), this));

        }
    }

    @Override
    protected void renderList(MatrixStack matrices, int x, int y, int mouseX, int mouseY, float delta) {
        int itemCount = this.getItemCount();
        Tessellator tessellator_1 = Tessellator.getInstance();
        BufferBuilder buffer = tessellator_1.getBuffer();

        for(int index = 0; index < itemCount; ++index) {
            int entryTop = this.getRowTop(index) + 2;
            int entryBottom = this.getRowTop(index) + this.itemHeight;
            if (entryBottom >= this.top && entryTop <= this.bottom) {
                int entryHeight = this.itemHeight - 4;
                ConfigListEntry entry = this.getEntry(index);
                int rowWidth = this.getRowWidth();
                int entryLeft;
                if (((EntryListWidgetAccessor)this).isRenderSelection() && this.isSelectedItem(index)) {
                    entryLeft = this.getRowLeft() - 2 + entry.getXOffset();
                    int selectionRight = x + rowWidth + 2;
                    RenderSystem.disableTexture();
                    float float_2 = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.color4f(float_2, float_2, float_2, 1.0F);
                    Matrix4f matrix = matrices.peek().getModel();
                    buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
                    buffer.vertex(matrix, (float)entryLeft, (float)(entryTop + entryHeight + 2), 0.0F).next();
                    buffer.vertex(matrix, (float)selectionRight, (float)(entryTop + entryHeight + 2), 0.0F).next();
                    buffer.vertex(matrix, (float)selectionRight, (float)(entryTop - 2), 0.0F).next();
                    buffer.vertex(matrix, (float)entryLeft, (float)(entryTop - 2), 0.0F).next();
                    tessellator_1.draw();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
                    buffer.vertex(matrix, (float)(entryLeft + 1), (float)(entryTop + entryHeight + 1), 0.0F).next();
                    buffer.vertex(matrix, (float)(selectionRight - 1), (float)(entryTop + entryHeight + 1), 0.0F).next();
                    buffer.vertex(matrix, (float)(selectionRight - 1), (float)(entryTop - 1), 0.0F).next();
                    buffer.vertex(matrix, (float)(entryLeft + 1), (float)(entryTop - 1), 0.0F).next();
                    tessellator_1.draw();
                    RenderSystem.enableTexture();
                }

                entryLeft = this.getRowLeft();
                entry.render(matrices, index, entryTop, entryLeft, rowWidth, entryHeight, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPos(mouseX, mouseY), entry), delta);
            }
        }


    }

    public final ConfigListEntry getEntryAtPos(double x, double y) {
        int int_5 = MathHelper.floor(y - (double)this.top) - this.headerHeight + (int)this.getScrollAmount() - 4;
        int index = int_5 / this.itemHeight;
        return x < (double)this.getScrollbarPositionX() && x >= (double)this.getRowLeft() && x <= (double)(this.getRowLeft() + this.getRowWidth()) && index >= 0 && int_5 >= 0 && index < this.getItemCount() ? this.children().get(index) : null;
    }

    @Override
    protected int addEntry(ConfigListEntry entry) {
        if (this.addedConfigs.contains(entry.getKey())) {
            return 0;
        } else {
            this.addedConfigs.add(entry.getKey());
            int i = super.addEntry(entry);
            if (entry.getKey().equals(this.selectedConfigId)) {
                this.setSelected(entry);
            }

            return i;
        }
    }

    public void select(ConfigListEntry entry) {
        this.setSelected(entry);
//        if (entry != null) {
//            ModMetadata metadata = entry.getMetadata();
//            NarratorManager.INSTANCE.narrate((new TranslatableText("narrator.select", HardcodedUtil.formatFabricModuleName(metadata.getName()))).getString());
//        }

    }

    @Override
    protected void updateScrollingState(double mouseX, double mouseY, int button) {
        super.updateScrollingState(mouseX, mouseY, button);
        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPositionX() && mouseX < (double)(this.getScrollbarPositionX() + 6);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (!this.isMouseOver(mouseX, mouseY)) {
            return false;
        } else {
            ConfigListEntry entry = this.getEntryAtPos(mouseX, mouseY);
            if (entry != null) {
                if (entry.mouseClicked(mouseX, mouseY, button)) {
                    this.setFocused(entry);
                    this.setDragging(true);
                    return true;
                }
            } else if (button == 0) {
                this.clickedHeader((int)(mouseX - (double)(this.left + this.width / 2 - this.getRowWidth() / 2)), (int)(mouseY - (double)this.top) + (int)this.getScrollAmount() - 4);
                return true;
            }

            return this.scrolling;
        }
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.width - 6;
    }

    @Override
    public int getRowLeft() {
        return this.left + 6;
    }

    @Override
    public int getRowWidth() {
        return this.width - (Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)) > 0 ? 18 : 12);
    }

    public int getTop() {
        return this.top;
    }

    @Override
    protected int getMaxPosition() {
        return super.getMaxPosition() + 4;
    }

    public int getDisplayedCountFor(Set<String> set) {
        int count = 0;

        for (ConfigListEntry configListEntry : this.children()) {
            if (set.contains(configListEntry.getKey())) {
                ++count;
            }
        }

        return count;
    }

    public int getWidth() {
        return this.width;
    }

    public int getDisplayedCount() {
        return this.children().size();
    }

    public CppOptionsGui getParent() {
        return this.parent;
    }

    @Override
    public void close() {

        for (NativeImageBackedTexture tex : this.configIconsCache.values()) {
            tex.close();
        }

    }

    NativeImageBackedTexture getCachedModIcon(Path path) {
        return this.configIconsCache.get(path);
    }

    void cacheModIcon(Path path, NativeImageBackedTexture tex) {
        this.configIconsCache.put(path, tex);
    }

    public Set<String> getCurrentModSet() {
        return this.addedConfigs;
    }

}
