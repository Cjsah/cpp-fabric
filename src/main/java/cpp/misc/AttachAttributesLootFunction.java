package cpp.misc;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import cpp.item.Wand;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AttachAttributesLootFunction extends ConditionalLootFunction {
	public static final LootFunctionType RANDOM_ATTRIBUTES = Registry.register(Registry.LOOT_FUNCTION_TYPE, new Identifier("cpp:attach_attributes"), new LootFunctionType(new Serializer()));

	protected AttachAttributesLootFunction(LootCondition[] conditions) {
		super(conditions);
	}

	@Override
	public LootFunctionType getType() {
		return RANDOM_ATTRIBUTES;
	}

	@Override
	public ItemStack process(ItemStack stack, LootContext context) {
		return Wand.attachAttibutes(stack, context.getRandom());
	}

	public static void init() {}

	public static class Serializer extends ConditionalLootFunction.Serializer<AttachAttributesLootFunction> {

		@Override
		public AttachAttributesLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
			return new AttachAttributesLootFunction(conditions);
		}

	}
}
