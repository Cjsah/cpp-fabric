package net.cpp.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class WoolLeavesBlock extends LeavesBlock {

	public WoolLeavesBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBroken(WorldAccess world, BlockPos pos, BlockState state) {
		if (!world.isClient() && world.getRandom().nextDouble() < .005 && world instanceof World) {
			SheepEntity sheep = EntityType.SHEEP.create((World) world);
			sheep.teleport(pos.getX()+.5, pos.getY(), pos.getZ()+.5);
			sheep.setColor(DyeColor.byId(world.getRandom().nextInt(DyeColor.values().length)));
			sheep.setBaby(true);
			sheep.setCustomName(new LiteralText("jeb_"));
			world.spawnEntity(sheep);
		}
	}
}
