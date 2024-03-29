package cpp.item;

import cpp.block.entity.TradeMachineBlockEntity;
import cpp.block.entity.TradeMachineBlockEntity.Recipe;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

public class TradePlugin extends Item {

	public TradePlugin(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		Recipe recipe = TradeMachineBlockEntity.SELL_TABLE.get(stack.getOrCreateTag().getInt("code"));
		if (recipe != null) {
			tooltip.add(LiteralText.EMPTY);
			recipe.modifyTooltip(tooltip, TooltipContext.Default.NORMAL);
		}
	}
}
