package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.cpp.api.INutrition;
import net.cpp.api.ITemperancable;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity implements ITemperancable {

	protected MixinPlayerEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	private int weight = 0;
	protected boolean effectEnabled;

	@Inject(at = @At("RETURN"), method = "tick")
	public void tick(CallbackInfo info) {
		if (!this.world.isClient) {
			ServerPlayerEntity this0 = (ServerPlayerEntity) (Object) this;
			int value = weight / 100;
			String fat = "normal";
			if (value > 0) {
				this.applyStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, value - 1, false, false));
				fat = "fat" + Math.min(value, 2);
			} else if (value < 0) {
				this.applyStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2, -value - 1, false, false));
				fat = "thin" + Math.min(-value, 2);
			}
			BlockState blockState = this.world.getBlockState(this.getBlockPos());
			if (!this.isSpectator() && blockState.getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) {
				(this0).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new TranslatableText("misc.cpp", new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD), new TranslatableText("chat.cpp.weight", weight, new TranslatableText("cpp.chat.weight." + fat)))));
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "eatFood")
	public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
		if (stack.isFood() && stack.getItem() instanceof INutrition) {
			this.weight += ((INutrition) stack.getItem()).getNutrition(stack);
		}
	}

	@Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
	public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		tag.putInt("weight", weight);
		tag.putBoolean("effectEnabled", effectEnabled);
	}

	@Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
	public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		weight = tag.getInt("weight");
		effectEnabled = tag.getBoolean("effectEnabled");
	}
	
	public void setEffectEnabled(boolean effectEnabled) {
		this.effectEnabled = effectEnabled;
	}
	public boolean isEffectEnabled() {
		return effectEnabled;
	}
}
