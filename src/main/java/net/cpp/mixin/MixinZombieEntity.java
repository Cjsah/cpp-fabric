package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.misc.MobEnhancing;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.World;

@Mixin(ZombieEntity.class)
@SuppressWarnings("unused")
public abstract class MixinZombieEntity extends HostileEntity {

	protected MixinZombieEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	public void enhance(CallbackInfo info) {
		MobEnhancing.equipArmor(this);
		MobEnhancing.equipWeapon(this);
		MobEnhancing.setDropChances(this);
	}

	@Inject(at = @At("HEAD"), method = { "tick" })
	public void destroy(CallbackInfo info) {
		MobEnhancing.destroy( this);
	}
}
