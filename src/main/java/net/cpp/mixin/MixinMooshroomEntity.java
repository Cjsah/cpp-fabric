package net.cpp.mixin;

import net.cpp.api.Utils;
import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(MooshroomEntity.class)
@SuppressWarnings("unused")
public abstract class MixinMooshroomEntity extends CowEntity {
	public MixinMooshroomEntity(EntityType<? extends CowEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract MooshroomEntity.Type getMooshroomType();

	@Shadow
	public abstract MooshroomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity);

	@Override
	@SuppressWarnings("ConstantConditions")
	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == MooshroomEntity.class) Utils.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_MOOSHROOM, true, (entity) -> entity.setType(this.getMooshroomType()));
	}
}
