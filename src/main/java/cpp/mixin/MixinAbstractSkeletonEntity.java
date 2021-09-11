package cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import cpp.misc.MobEnhancing;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.world.World;

@Mixin(AbstractSkeletonEntity.class)
public abstract class MixinAbstractSkeletonEntity extends HostileEntity {

	protected MixinAbstractSkeletonEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@SuppressWarnings("ConstantConditions")
	@Inject(at = @At("RETURN"), method = "<init>(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/World;)V")
	public void enhance(CallbackInfo info) {
		MobEnhancing.equipArmor(this);
		if ((Class<?>) this.getClass() == WitherSkeletonEntity.class) {
			MobEnhancing.equipWeapon(this);
		} else {
			MobEnhancing.equipArrow(this);
		}
		MobEnhancing.setDropChances(this);
	}

}
