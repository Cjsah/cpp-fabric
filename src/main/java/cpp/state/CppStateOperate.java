package cpp.state;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.List;

public class CppStateOperate {
    private final JsonObject data = new JsonObject();

    public CppStateOperate() {
        this.initData();
    }

    private final List<Runnable> updateListeners = Lists.newArrayList();

    private void addUpdateListener(Runnable listener) {
        this.updateListeners.add(listener);
    }

    protected void runUpdateListeners() {
        for (Runnable runnable : this.updateListeners) {
            runnable.run();
        }
    }

    public CppState stateFromNbt(CompoundTag tag) {
        CppState state = new CppState(this.data);
        this.addUpdateListener(state::markDirty);
        state.readNbt(tag);
        return state;
    }

    public CppState initState() {
        CppState state = new CppState(this.data);
        this.addUpdateListener(state::markDirty);
        this.runUpdateListeners();
        return state;
    }

    private void initData() {
        this.data.addProperty("IslandMode", false);
        this.data.addProperty("IslandID", 1);
        this.data.addProperty("Around", 1);
        this.data.add("InIsland", new JsonArray());
        this.data.add("EnchantingRituals", new JsonArray());
    }

    public boolean contains(String name) {
        for (JsonElement jsonElement : this.data.get("InIsland").getAsJsonArray()) {
            if (jsonElement.getAsString().equals(name)) return true;
        }
        return false;
    }

    public void setIslandMode(boolean bl) {
        this.data.addProperty("IslandMode", bl);
        this.runUpdateListeners();
    }

    public void setIslandID(int id) {
        this.data.addProperty("IslandID", id);
        this.runUpdateListeners();
    }

    public void setAround(int value) {
        this.data.addProperty("Around", value);
        this.runUpdateListeners();
    }

    public void addInIsland(String name) {
        if (!this.contains(name)) {
            this.data.get("InIsland").getAsJsonArray().add(name);
            this.runUpdateListeners();
        }
    }

    public void removeInIsland(String name) {
        if (this.contains(name)) {
            JsonArray array = this.data.get("InIsland").getAsJsonArray();
            for (JsonElement jsonElement : array) {
                if (jsonElement.getAsString().equals(name)) array.remove(jsonElement);
                this.runUpdateListeners();
            }
        }
    }

    public boolean getIslandMode() {
        return this.data.get("IslandMode").getAsBoolean();
    }

    public int getIslandID() {
        return this.data.get("IslandID").getAsInt();
    }

    public int getAround() {
        return this.data.get("Around").getAsInt();
    }

    public String getEnchantment(int index) {
        index -= index / 38 * 38;
        return this.data.get("EnchantingRituals").getAsJsonArray().get(index).getAsString();
    }

    public boolean inIsland(String name) {
        for (JsonElement names : this.data.get("InIsland").getAsJsonArray()) {
            if (names.getAsString().equals(name)) return true;
        }
        return false;
    }

    public List<String> listInIsland() {
        List<String> names = new ArrayList<>();
        for (JsonElement name : this.data.get("InIsland").getAsJsonArray()) {
            names.add(name.getAsString());
        }
        return names;
    }

}
