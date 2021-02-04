package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.world.World;

@Mixin(VillagerEntity.class)
public abstract class MixinVillagerEntity extends MerchantEntity {
	protected MixinVillagerEntity(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	public void initGoals() {
		super.initGoals();
		goalSelector.add(3, new TemptGoal(this, .5, Ingredient.ofItems(Items.EMERALD_BLOCK), false));
	}
}
