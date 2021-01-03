package net.cpp.mixin;

import java.util.Collections;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.GameProfile;

import net.cpp.api.CodingTool;
import net.cpp.api.INutrition;
import net.cpp.init.CppItems;
import net.cpp.item.Magnet;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {

	public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	private int elderSWordCoolDown = 50;

	@Inject(at = @At("RETURN"), method = "playerTick")
	private void tickBroom(CallbackInfo info) {
		ServerPlayerEntity this0 = (ServerPlayerEntity) ((Object) this);// 自己的引用，便于行事
		for (int i = 0; i < getInventory().size(); i++) {//磁铁
			ItemStack itemStack = getInventory().getStack(i);
			if (itemStack.isOf(CppItems.MAGNET) && Magnet.isEnabled(itemStack)) {
				CodingTool.attractItems(getPos().add(0, 1, 0), (ServerWorld) world, true, false);
				for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(getPos(), getPos()).expand(16), orb -> orb.getPos().isInRange(getPos(), 16))) {
					orb.teleport(getPos().x, getPos().y, getPos().z);
				}
				break;
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
				CodingTool.give(this0, stack);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.putInt("elder_s_word_cooldown", elderSWordCoolDown);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		elderSWordCoolDown = tag.getInt("elder_s_word_cooldown");
	}
}
