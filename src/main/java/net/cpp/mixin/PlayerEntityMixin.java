package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.Collections;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
	private int elderSWordCoolDown = 50;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract void addExperience(int experience);

	@Shadow
	public abstract PlayerInventory getInventory();

	/**
	 * 当玩家携带启用的磁铁时，吸引16米内的物品
	 */
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			if (getInventory().containsAny(Collections.singleton(CppItems.MAGNET))) {
				CodingTool.attractItems(getPos().add(0, 1, 0), (ServerWorld) world, true, false);
			}
			if (getInventory().containsAny(Collections.singleton(CppItems.ELDER_S_WORDS))) {
				if (elderSWordCoolDown-- <= 0) {
					elderSWordCoolDown = 50;
					addExperience(1);
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void fromTag1(CompoundTag tag, CallbackInfo info) {
		elderSWordCoolDown = tag.getInt("elder_s_word_cooldown");
	}

	@Inject(at = @At("RETURN"), method = "writeCustomDataToTag")
	public void toTag1(CompoundTag tag, CallbackInfo info) {
		tag.putInt("elder_s_word_cooldown", elderSWordCoolDown);
	}
}
