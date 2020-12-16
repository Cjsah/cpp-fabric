package net.cpp.entity;

import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class CompressedExperienceBottleEntity extends ExperienceBottleEntity {

	public CompressedExperienceBottleEntity(EntityType<? extends ExperienceBottleEntity> entityType, World world) {
		super(entityType, world);
		// TODO 自动生成的构造函数存根
	}

	public CompressedExperienceBottleEntity(World world, LivingEntity owner) {
		super(world, owner);
		// TODO 自动生成的构造函数存根
	}

	public CompressedExperienceBottleEntity(World world, double x, double y, double z) {
		super(world, x, y, z);
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected Item getDefaultItem() {
		return CppItems.COMPRESSED_EXPERIENCE_BOTTLE;
	}

	protected void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (!this.world.isClient) {
			this.world.syncWorldEvent(2002, this.getBlockPos(), PotionUtil.getColor(Potions.WATER));
			int i = 3 + this.world.random.nextInt(6) + this.world.random.nextInt(6);

			while (i > 0) {
				int j = ExperienceOrbEntity.roundToOrbSize(i);
				i -= j;
				this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY(), this.getZ(), j));
			}

			this.remove(RemovalReason.DISCARDED);
		}

	}
}
