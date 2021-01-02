package net.cpp.misc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.cpp.init.CppLootTableFunctions;
import net.cpp.item.AncientScroll;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;

public class AncientScrollRandomTag extends ConditionalLootFunction {
    protected AncientScrollRandomTag(LootCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        return AncientScroll.randomGetOne();
    }

    @Override
    public LootFunctionType getType() {
        return CppLootTableFunctions.ANCIENT_SCROLL_RANDOM_TAG;
    }

    public static class Serializer extends net.minecraft.loot.function.ConditionalLootFunction.Serializer<AncientScrollRandomTag> {
        public Serializer() { }

        @Override
        public AncientScrollRandomTag fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new AncientScrollRandomTag(conditions);
        }

    }

}
