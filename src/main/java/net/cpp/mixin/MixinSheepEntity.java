package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.cpp.init.CppEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public abstract class MixinSheepEntity extends AnimalEntity {
	protected MixinSheepEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			Entity darkCow = CppEntities.DARK_SHEEP.create(world);
			darkCow.setPos(getX(), getY(), getZ());
			darkCow.yaw = yaw;
			darkCow.pitch = pitch;
			darkCow.setVelocity(getVelocity());
			world.spawnEntity(darkCow);
			discard();
		}
	}
}
