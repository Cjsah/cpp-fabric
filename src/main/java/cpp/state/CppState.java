package cpp.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.PersistentState;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CppState extends PersistentState {
    private final JsonObject data;

    public CppState (JsonObject data) {
        this.data = data;
    }

    @Override
    public CompoundTag toNbt(@Nonnull CompoundTag tag) {
        tag.putBoolean("IslandMode", this.data.get("IslandMode").getAsBoolean());
        tag.putInt("IslandID", this.data.get("IslandID").getAsInt());
        tag.putInt("Around", this.data.get("Around").getAsInt());
        ListTag list = new ListTag();
        for (JsonElement inIsland : data.get("InIsland").getAsJsonArray()) {
            list.add(StringTag.of(inIsland.getAsString()));
        }
        tag.put("InIsland", list);
        tag.put("EnchantingRituals", getEnchantingRecipes());
        return tag;
    }

    @Nonnull
    private ListTag getEnchantingRecipes() {
        ListTag list = new ListTag();
        if (this.data.has("EnchantingRituals")) {
            for (JsonElement json : this.data.get("EnchantingRituals").getAsJsonArray()) {
                CompoundTag tag = new CompoundTag();
                tag.putString("rare1", json.getAsJsonObject().get("rare1").getAsString());
                tag.putString("rare2", json.getAsJsonObject().get("rare2").getAsString());
                tag.putString("result", json.getAsJsonObject().get("result").getAsString());
                list.add(tag);
            }
        }else {
            JsonArray jsonArray = new JsonArray();
            List<Identifier> enchants = new ArrayList<>(Registry.ENCHANTMENT.getIds());
            Collections.shuffle(enchants);
            for (Identifier enchant : enchants) {
                list.add(StringTag.of(enchant.toString()));
                jsonArray.add(enchant.toString());
            }
            this.data.add("EnchantingRituals", jsonArray);
        }
        return list;
    }

    protected CppState readNbt(@Nonnull CompoundTag tag) {
        this.data.addProperty("IslandMode", tag.getBoolean("IslandMode"));
        this.data.addProperty("IslandID", tag.getInt("IslandID"));
        this.data.addProperty("Around", tag.getInt("Around"));
        JsonArray inIslandArray = new JsonArray();
        for (Tag inIsland : tag.getList("InIsland", 10)) {
            inIslandArray.add(inIsland.asString());
        }
        this.data.add("InIsland", inIslandArray);
        JsonArray enchantsArray = new JsonArray();
        for (Tag enchant : tag.getList("EnchantingRituals", 10)) {
            enchantsArray.add(enchant.asString());
        }
        this.data.add("EnchantingRituals", enchantsArray);
        return this;
    }
}
