package cpp.mixin;

import java.util.Collections;

import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import cpp.api.Utils;
import cpp.block.entity.BeaconEnhancerBlockEntity;
import cpp.init.CppItems;
import cpp.item.Magnet;
import cpp.item.Wand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
@SuppressWarnings("unused")
public abstract class MixinServerPlayerEntity extends PlayerEntity {

	public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}
	
	private int elderSWordCoolDown = 50;

	@SuppressWarnings("ConstantConditions")
	@Inject(at = @At("RETURN"), method = "playerTick")
	private void tickBroom(CallbackInfo info) {
		ServerPlayerEntity this0 = (ServerPlayerEntity) ((Object) this);// 自己的引用，便于行事
		Magnet.tick(this0);
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
				Utils.give(this0, stack);
			}
		}
		Wand.tickEffect(this0);
		BeaconEnhancerBlockEntity.tickEffect(this0);
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	public void writeCustomDataToNbt(NbtCompound tag, CallbackInfo info) {
		tag.putInt("elder_s_word_cooldown", elderSWordCoolDown);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	public void readCustomDataFromNbt(NbtCompound tag, CallbackInfo info) {
		elderSWordCoolDown = tag.getInt("elder_s_word_cooldown");
	}
}
