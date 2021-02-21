package net.cpp.mixin;

import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.entity.DarkMooshroomEntity;
import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(MooshroomEntity.class)
public abstract class MixinMooshroomEntity extends CowEntity {
	public MixinMooshroomEntity(EntityType<? extends CowEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract MooshroomEntity.Type getMooshroomType();

	@Shadow
	public abstract MooshroomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity);

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			DarkMooshroomEntity dark = CppEntities.DARK_MOOSHROOM.create(world);
			dark.setPos(getX(), getY(), getZ());
			dark.yaw = yaw;
			dark.pitch = pitch;
			dark.setVelocity(getVelocity());
			dark.setType(getMooshroomType());
			world.spawnEntity(dark);
			discard();
		}
	}
}
