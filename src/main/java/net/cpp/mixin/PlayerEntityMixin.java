package net.cpp.mixin;

import java.util.Collections;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.cpp.api.CodingTool;
import net.cpp.init.CppItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

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
	 * 当玩家携带启用的磁铁时，吸引16米内的物品和经验球
	 */
	@Inject(at = @At("HEAD"), method = "tick()V")
	public void tick(CallbackInfo callbackInfo) {
		if (!world.isClient) {
			ServerPlayerEntity this0 = (ServerPlayerEntity) ((Object) this);// 自己的引用，便于行事
			if (getInventory().contains(CppItems.MAGNET.getDefaultStack())) {// 磁铁
				CodingTool.attractItems(getPos().add(0, 1, 0), (ServerWorld) world, true, false);
				for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(getPos(), getPos()).expand(16), orb -> orb.getPos().isInRange(getPos(), 16))) {
					orb.teleport(getPos().x, getPos().y, getPos().z);
				}
			}
			if (getInventory().containsAny(Collections.singleton(CppItems.ELDER_S_WORDS))) {// 年长者之教诲
				if (elderSWordCoolDown-- <= 0) {
					elderSWordCoolDown = 50;
					addExperience(1);
				}
			}
			if (getOffHandStack().isOf(Items.HOPPER)) {// 副手漏斗
				int round = 9;
				ItemStack stack = Items.EXPERIENCE_BOTTLE.getDefaultStack();
				if (getMainHandStack().isOf(CppItems.COMPRESSOR)) {
					round <<= 6;
					stack.getOrCreateTag().putByte("multiple", (byte) 1);
				}
				if (this0.totalExperience >= round) {
					this0.addExperience(-round);
					this0.giveItemStack(stack);
					if (!stack.isEmpty()) {
						this0.dropStack(stack);
					}
				}
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "tick()V")
	private void tickBroom(CallbackInfo info) {
		if (!world.isClient) {
			ServerPlayerEntity this0 = (ServerPlayerEntity) ((Object) this);// 自己的引用，便于行事
			if (getMainHandStack().isOf(CppItems.BROOM) || getOffHandStack().isOf(CppItems.BROOM)) {
				double vy = isSneaking() ? -.1 : .1;
				setVelocity(0, vy, 0);
				System.out.println(getVelocity());
//				double dvy = getVelocity().y - vy;
//				if (dvy < 0) {
//					addVelocity(0, dvy < -1 ? 1 : -dvy, 0);
//				}
				this0.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FIREWORK, false, getX(), getY(), getZ(), .3f, 0, .3f, .01f, 1));
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
