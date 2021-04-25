package cpp.state;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
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
    public NbtCompound writeNbt(@Nonnull NbtCompound tag) {
        tag.putBoolean("IslandMode", this.data.get("IslandMode").getAsBoolean());
        tag.putInt("IslandID", this.data.get("IslandID").getAsInt());
        tag.putInt("Around", this.data.get("Around").getAsInt());
        NbtList list = new NbtList();
        for (JsonElement inIsland : data.get("InIsland").getAsJsonArray()) {
            list.add(NbtString.of(inIsland.getAsString()));
        }
        tag.put("InIsland", list);
        tag.put("EnchantingRituals", getEnchantingRecipes());
        return tag;
    }

    @Nonnull
    private NbtList getEnchantingRecipes() {
        NbtList list = new NbtList();
        JsonArray enchantingRituals = this.data.get("EnchantingRituals").getAsJsonArray();
        if (enchantingRituals.size() != 0) {
            for (JsonElement json : enchantingRituals) {
                NbtCompound tag = new NbtCompound();
                tag.putString("rare1", json.getAsJsonObject().get("rare1").getAsString());
                tag.putString("rare2", json.getAsJsonObject().get("rare2").getAsString());
                tag.putString("result", json.getAsJsonObject().get("result").getAsString());
                list.add(tag);
            }
        }else {
            List<Identifier> enchants = new ArrayList<>(Registry.ENCHANTMENT.getIds());
            Collections.shuffle(enchants);
            for (Identifier enchant : enchants) {
                list.add(NbtString.of(enchant.toString()));
                enchantingRituals.add(enchant.toString());
            }
        }
        return list;
    }

    protected void readNbt(@Nonnull NbtCompound tag) {
        this.data.addProperty("IslandMode", tag.getBoolean("IslandMode"));
        this.data.addProperty("IslandID", tag.getInt("IslandID"));
        this.data.addProperty("Around", tag.getInt("Around"));
        for (NbtElement inIsland : tag.getList("InIsland", 8)) {
            this.data.get("InIsland").getAsJsonArray().add(inIsland.asString());
        }
        for (NbtElement enchant : tag.getList("EnchantingRituals", 8)) {
            this.data.get("EnchantingRituals").getAsJsonArray().add(enchant.asString());
        }
    }
}
