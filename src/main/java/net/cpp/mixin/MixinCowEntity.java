package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.cpp.entity.DarkCowEntity;
import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(CowEntity.class)
public abstract class MixinCowEntity extends AnimalEntity {
	protected MixinCowEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!world.isClient && (Class<?>) getClass() == CowEntity.class && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			DarkCowEntity darkCow = CppEntities.DARK_COW.create(world);
			darkCow.setPos(getX(), getY(), getZ());
			darkCow.yaw = yaw;
			darkCow.pitch = pitch;
			darkCow.setVelocity(getVelocity());
			world.spawnEntity(darkCow);
			discard();
		}
	}
}
