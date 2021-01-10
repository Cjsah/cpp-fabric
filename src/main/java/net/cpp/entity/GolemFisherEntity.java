package net.cpp.entity;

import io.netty.util.internal.IntegerHolder;
import net.cpp.api.CodingTool;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class GolemFisherEntity extends AGolemEntity {
	private int time = 400;

	public GolemFisherEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient && time-- <= 0) {
			time = MathHelper.nextInt(random, 100, 600) - EnchantmentHelper.getLure(getMainHandStack()) * 100;
			ItemStack tool = getMainHandStack();
			LootContext.Builder builder = new LootContext.Builder((ServerWorld) world).parameter(LootContextParameters.ORIGIN, getPos()).parameter(LootContextParameters.TOOL, tool).parameter(LootContextParameters.THIS_ENTITY, this).random(world.random).luck(0);
			LootTable lootTable = world.getServer().getLootManager().getTable(LootTables.FISHING_GAMEPLAY);
			lootTable.generateLoot(builder.build(LootContextTypes.FISHING), itemStack -> {
				CodingTool.drop(world, getPos(), itemStack);
			});
			int exp = random.nextInt(6) + 1;
			tool.damage(1, random, null);
			if (EnchantmentHelper.getLevel(Enchantments.MENDING, tool) > 0) {
				exp = CodingTool.mend(tool, exp);
			}
			if (random.nextDouble() < exp / 9) {
				CodingTool.drop(world, getPos(), Items.EXPERIENCE_BOTTLE.getDefaultStack());
			}
		}
	}

	@Override
	protected void reactBlock() {
		if (!world.getFluidState(getBlockPos()).isIn(FluidTags.WATER))
			killed = true;
	}

	@Override
	public void fromTag(CompoundTag tag) {
		time = tag.getInt("time");
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("time", time);
		return super.toTag(tag);
	}
}
