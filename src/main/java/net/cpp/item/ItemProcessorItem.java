package net.cpp.item;

import net.cpp.init.CppBlocks;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemProcessorItem extends BlockItem {//TODO
	public ItemProcessorItem(Settings settings) {
		super(CppBlocks.ITEM_PROCESSOR, settings);
	}
	
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getPlayer().isSneaking()) {
			return use(context.getWorld(), context.getPlayer(), context.getHand()).getResult();
		}
		return super.useOnBlock(context);
	}
	
	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		if (!world.isClient()){
		
		}
		return super.use(world, user, hand);
	}
}
