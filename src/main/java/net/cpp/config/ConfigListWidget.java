package net.cpp.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.texture.NativeImageBackedTexture;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ConfigListWidget extends AlwaysSelectedEntryListWidget<ConfigListEntry> implements AutoCloseable {

    private final CppOptionsGui parent;
    private Set<String> configKeys = new HashSet<>();
    private final Map<Path, NativeImageBackedTexture> configIconsCache = new HashMap<>();
    private List<String> ConfigContainerList = null;
    private Set<String> addedConfigs = new HashSet<>();
    private String selectedConfigId = null;
    private boolean scrolling;


    protected ConfigListWidget(MinecraftClient client, int width, int height, int y1, int y2, int entryHeight, String searchTerm, JsonObject configs, CppOptionsGui parent) {
        super(client, width, height, y1, y2, entryHeight);
        this.parent = parent;
        for (Map.Entry<String, JsonElement> config  : configs.entrySet()) {
            configKeys.add(config.getKey());
        }

        this.filter(searchTerm, false);
        this.setScrollAmount(parent.getScrollPercent() * (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));

    }

    public void setScrollAmount(double amount) {
        super.setScrollAmount(amount);
        int denominator = Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4));
        if (denominator <= 0) {
            this.parent.updateScrollPercent(0.0D);
        } else {
            this.parent.updateScrollPercent(this.getScrollAmount() / (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
        }

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

//        Iterator var7 = matched.iterator();

//        while(true) {
//            String modId;
//            List children;
//            ParentEntry parent;
//            do {
//                while(true) {
//                    ModContainer container;
//                    boolean library;
//                    do {
//                        do {
//                            if (!var7.hasNext()) {
//                                if ((this.parent.getSelectedEntry() == null || this.children().isEmpty()) && (this.getSelected() == null || ((ModListEntry)this.getSelected()).getMetadata() == this.parent.getSelectedEntry().getMetadata())) {
//                                    if (this.getSelected() == null && !this.children().isEmpty() && this.getEntry(0) != null) {
//                                        this.setSelected((ModListEntry)this.getEntry(0));
//                                    }
//                                } else {
//                                    var7 = this.children().iterator();
//
//                                    while(var7.hasNext()) {
//                                        ModListEntry entry = (ModListEntry)var7.next();
//                                        if (entry.getMetadata().equals(this.parent.getSelectedEntry().getMetadata())) {
//                                            this.setSelected(entry);
//                                        }
//                                    }
//                                }
//
//                                if (this.getScrollAmount() > (double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4))) {
//                                    this.setScrollAmount((double)Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)));
//                                }
//
//                                return;
//                            }
//
//                            container = (ModContainer)var7.next();
//                            ModMetadata metadata = container.getMetadata();
//                            modId = metadata.getId();
//                            library = ModMenu.LIBRARY_MODS.contains(modId);
//                        } while(library && !ModMenuConfigManager.getConfig().showLibraries());
//                    } while(ModMenu.PARENT_MAP.values().contains(container));
//
//                    if (ModMenu.PARENT_MAP.keySet().contains(container)) {
//                        children = ModMenu.PARENT_MAP.get(container);
//                        children.sort(ModMenuConfigManager.getConfig().getSorting().getComparator());
//                        parent = new ParentEntry(container, children, this);
//                        this.addEntry((ModListEntry)parent);
//                        break;
//                    }
//
//                    this.addEntry((ModListEntry)(new IndependentEntry(container, this)));
//                }
//            } while(!this.parent.showModChildren.contains(modId));
//
//            List<ModContainer> validChildren = ModListSearch.search(this.parent, searchTerm, children);
//
//            for (ModContainer child : validChildren) {
//                this.addEntry((ModListEntry) (new ChildEntry(child, parent, this, validChildren.indexOf(child) == validChildren.size() - 1)));
//            }
//        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getDisplayedCount() {
        return this.children().size();
    }

    @Override
    public void close() {

    }
}
