package net.cpp.mixin;

import net.cpp.api.INutrition;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    private int weight = 0;

    @Inject(at = @At("HEAD"), method = "tick()V")
    public void tick(CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity)((Object)this);
        if (!entity.world.isClient && entity.isPlayer()) {
            PlayerEntity player = (PlayerEntity) entity;
            int value = weight / 100;
            String fat = "normal";
            if (value > 0) {
                player.applyStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 2, value - 1, false, false));
                fat = "fat" + Math.min(value, 2);
            }else if (value < 0) {
                player.applyStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, 2, -value - 1, false, false));
                fat = "thin" + Math.min(-value, 2);
            }
            BlockState blockState = player.world.getBlockState(player.getBlockPos());
            if (!player.isSpectator() && blockState.getBlock() == Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                ((ServerPlayerEntity)player).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new TranslatableText("misc.cpp", new TranslatableText("chat.cpp.title").formatted(Formatting.GOLD), new TranslatableText("chat.cpp.weight", weight, new TranslatableText("cpp.chat.weight." + fat)))));
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "eatFood")
    public void eatFood(World world, ItemStack stack, CallbackInfoReturnable<ItemStack> info) {
        if (!world.isClient && stack.isFood() && stack.getItem() instanceof INutrition) {
            this.weight += ((INutrition)stack.getItem()).getNutrition(stack);
            System.out.println(weight);
        }
    }

    @Inject(at = @At("HEAD"), method = "writeCustomDataToTag")
    public void writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
        tag.putInt("weight", this.weight);
    }

    @Inject(at = @At("HEAD"), method = "readCustomDataFromTag")
    public void readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
        this.weight = tag.getInt("weight");
    }
}
