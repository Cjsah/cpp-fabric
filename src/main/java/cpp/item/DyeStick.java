package cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class DyeStick extends Item {

	public DyeStick(Settings settings) {
		super(settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		String colorString = stack.getOrCreateTag().getString("color");
		tooltip.add(new TranslatableText("color.minecraft." + colorString).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(DyeColor.byName(colorString, DyeColor.WHITE).getSignColor()))));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			String colorString = context.getStack().getOrCreateTag().getString("color");
			@SuppressWarnings("unused")
			int time = context.getStack().getOrCreateTag().getInt("time");
			if (colorString.length() > 0) {
				BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
				Block block = blockState.getBlock();
				Identifier identifier = Registry.BLOCK.getId(block);
				for (DyeColor dyeColor : DyeColor.values()) {
					String path = identifier.getPath();
					if (path.contains(dyeColor.toString())) {
						Identifier identifier2 = new Identifier(identifier.getNamespace(), path.replaceFirst(dyeColor.toString(), colorString));
						if (Registry.BLOCK.containsId(identifier2)) {
							context.getWorld().setBlockState(context.getBlockPos(), Registry.BLOCK.get(identifier2).getDefaultState());
//						context.getStack().getOrCreateTag().putInt("time", context.getStack().getOrCreateTag());
							return ActionResult.SUCCESS;
						}
					}
				}
				Identifier id3 = new Identifier(identifier.getNamespace(), colorString + "_" + identifier.getPath());
				if (Registry.BLOCK.containsId(id3)) {
					context.getWorld().setBlockState(context.getBlockPos(), Registry.BLOCK.get(id3).getDefaultState());
					return ActionResult.SUCCESS;
				}
				Identifier id4 = new Identifier(identifier.getNamespace(), colorString + "_stained_" + identifier.getPath());
				if (Registry.BLOCK.containsId(id4)) {
					context.getWorld().setBlockState(context.getBlockPos(), Registry.BLOCK.get(id4).getDefaultState());
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.CONSUME;
	}
}
