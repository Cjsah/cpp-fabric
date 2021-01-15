package net.cpp.item;

import com.google.common.collect.ImmutableList;

import net.cpp.ducktype.IMaterialGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;

public class OrangeForceOfDirt extends Item {

	public OrangeForceOfDirt(Settings settings) {
		super(settings);
	}

	public static final ImmutableList<Block> OTHERS = ImmutableList.of(Blocks.STONE, Blocks.GRANITE, Blocks.DIORITE, Blocks.ANDESITE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.NETHERRACK, Blocks.BLACKSTONE, Blocks.BASALT);

	@Override
	@Environment(EnvType.CLIENT)
	public Text getName(ItemStack stack) {
		return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
		boolean suc = canMine(blockState.getBlock());
		if (suc && !context.getWorld().isClient) {
			context.getWorld().breakBlock(context.getBlockPos(), !context.getPlayer().isCreative(), context.getPlayer());
			postMine(context.getStack(), context.getWorld(), blockState, context.getBlockPos(), context.getPlayer());
		}
		return suc ? ActionResult.SUCCESS : ActionResult.PASS;
	}

	public static boolean canMine(Block block) {
		return isMaterial(block, Material.SOIL) || isMaterial(block, Material.SOLID_ORGANIC) || isMaterial(block, Material.AGGREGATE) || OTHERS.contains(block);
	}

	public static boolean isMaterial(Block block, Material material) {
		return material == ((IMaterialGetter) block).getMaterial();
	}
}
