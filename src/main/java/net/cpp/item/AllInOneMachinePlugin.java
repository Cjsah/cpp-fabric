package net.cpp.item;

import net.cpp.block.entity.AllInOneMachineBlockEntity;
import net.cpp.block.entity.AllInOneMachineBlockEntity.Degree;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

public class AllInOneMachinePlugin extends Item {
	/**
	 * 高或低
	 */
	public final Degree degree;
	/**
	 * 是温度，否则是压强
	 */
	public final boolean isTemperature;

	public AllInOneMachinePlugin(Degree degree, boolean isTemperature, Settings settings) {
		super(settings);
		this.degree = degree;
		this.isTemperature = isTemperature;
	}

	/**
	 * 如果已经安装了对应插件，则略过
	 */
	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			World world = context.getWorld();
			BlockEntity blockEntity = world.getBlockEntity(context.getBlockPos());
			if (blockEntity instanceof AllInOneMachineBlockEntity) {
				AllInOneMachineBlockEntity allInOneMachineBlockEntity = (AllInOneMachineBlockEntity) blockEntity;
				if (isTemperature && allInOneMachineBlockEntity.addAvailableTemperature(degree) || !isTemperature && allInOneMachineBlockEntity.addAvailablePressure(degree)) {
					context.getStack().decrement(1);
					return ActionResult.SUCCESS;
				}
			}
		}
		return ActionResult.PASS;
	}
}
