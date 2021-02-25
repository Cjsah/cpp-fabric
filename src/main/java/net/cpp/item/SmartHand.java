package net.cpp.item;

import net.cpp.ducktyping.ITickableInItemFrame;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SmartHand extends Item implements ITickableInItemFrame {

	public SmartHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		World world = itemFrameEntity.world;
		Vec3d pos = itemFrameEntity.getPos();
		Vec3d fishingPos = ITickableInItemFrame.getPos(itemFrameEntity);
		if (world.isWater(new BlockPos(fishingPos))) {
			int time = itemFrameEntity.getHeldItemStack().getOrCreateTag().getInt("time") - 1;
			if (time <= 0) {
				time = MathHelper.nextInt(world.random, 100, 600);
				double chance = Math.pow((time - 100.) / 800, 1);
				if (world.random.nextDouble() < chance) {
					LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.ORIGIN, fishingPos).parameter(LootContextParameters.TOOL, ItemStack.EMPTY).parameter(LootContextParameters.THIS_ENTITY, itemFrameEntity).random(world.random).luck(0);
					LootTable lootTable = world.getServer().getLootManager().getTable(LootTables.FISHING_GAMEPLAY);
					lootTable.generateLoot(builder.build(LootContextTypes.FISHING), itemStack -> {
						world.spawnEntity(new ItemEntity(world, pos.x, pos.y, pos.z, itemStack));
					});
				}
			}
			itemFrameEntity.getHeldItemStack().getOrCreateTag().putInt("time", time);
			return true;
		}
		return false;
	}
}
