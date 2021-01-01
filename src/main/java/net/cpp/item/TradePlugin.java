package net.cpp.item;

import java.util.List;

import net.cpp.block.entity.TradeMachineBlockEntity;
import net.cpp.block.entity.TradeMachineBlockEntity.Recipe;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

public class TradePlugin extends Item {

	public TradePlugin(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		Recipe recipe = TradeMachineBlockEntity.SELL_TABLE.get(stack.getOrCreateTag().getInt("code"));
		if (recipe != null) {
			recipe.modifyTooltip(tooltip, TooltipContext.Default.NORMAL);
		}
	}
}
