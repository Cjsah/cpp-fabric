package net.cpp.mixin;

import net.cpp.item.Vaccine;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.init.CppItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {
	public MixinLivingEntity(EntityType<?> type, World world) {
		super(type, world);
	}
	
	@Shadow
	public abstract ItemStack getMainHandStack();
	
	@Shadow
	public abstract ItemStack getOffHandStack();

	@Shadow
	public Map<StatusEffect, StatusEffectInstance> activeStatusEffects;

	@Shadow
	protected abstract void onStatusEffectRemoved(StatusEffectInstance effect);


	@Inject(at = @At("RETURN"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if ((Object)this instanceof PlayerEntity) this.activeStatusEffects.entrySet().removeIf(effect -> {
			boolean remove = Vaccine.getVaccineEffects((PlayerEntity) (Object)this).contains(effect.getKey());
			if (remove) this.onStatusEffectRemoved(effect.getValue());
			return remove;
		});
		if (!isSpectator() && canFly()) {
			/*
			 * if ((Object) this instanceof PlayerEntity) { PlayerEntity player = ((PlayerEntity) (Object) this); if (!player.isCreative()) player.getAbilities().flying = getMainHandStack().isOf(CppItems.SHOOTING_STAR); } else
			 */
			if (getMainHandStack().isOf(CppItems.SHOOTING_STAR)) {
				double vy = isSneaking() ? -.5 : 0;
				double dvy = getVelocity().y - vy;
				if (dvy < -.3)
					addVelocity(0, .3, 0);
				else if (dvy > .3)
					addVelocity(0, -.3, 0);
				else
					addVelocity(0, -dvy, 0);
				fallDistance = 0;
			} else if (getOffHandStack().isOf(CppItems.SHOOTING_STAR)) {
				setVelocity(getRotationVector());
				fallDistance = 0;
			} else if (getMainHandStack().isOf(CppItems.BROOM) || getOffHandStack().isOf(CppItems.BROOM)) {
				double vy = isSneaking() ? -.255 : .045;
				double dvy = getVelocity().y - vy;
				if (dvy < 0) {
					addVelocity(0, dvy < -.2 ? .2 : -dvy, 0);
				}
				if ((Object) this instanceof ServerPlayerEntity) {
					((ServerPlayerEntity) (Object) this).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FIREWORK, false, getX(), getY(), getZ(), .3f, 0, .3f, .01f, 1));
				}
				fallDistance = 0;
			}
		}
	}
	@Inject(at = @At("HEAD"), method = "addStatusEffect", cancellable = true)
	public void addStatusEffect1(StatusEffectInstance effect, CallbackInfoReturnable<Boolean> info) {
		if (Vaccine.getVaccineEffects((LivingEntity) (Object)this).contains(effect.getEffectType())) {
			info.setReturnValue(false);
		}
	}
}
