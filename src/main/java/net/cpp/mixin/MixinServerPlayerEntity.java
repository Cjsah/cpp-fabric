package net.cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.authlib.GameProfile;

import net.cpp.init.CppItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(ServerPlayerEntity.class)
public abstract class MixinServerPlayerEntity extends PlayerEntity {

	public MixinServerPlayerEntity(World world, BlockPos pos, float yaw, GameProfile profile) {
		super(world, pos, yaw, profile);
	}

	@Inject(at = @At("RETURN"), method = "playerTick")
	private void tickBroom(CallbackInfo info) {
		ServerPlayerEntity this0 = (ServerPlayerEntity) ((Object) this);// 自己的引用，便于行事
		if (getMainHandStack().isOf(CppItems.BROOM) || getOffHandStack().isOf(CppItems.BROOM)) {
			double vy = isSneaking() ? -1.1 : 1.1;
			setVelocity(0, vy, 0);
//				System.out.println(getVelocity());
//				double dvy = getVelocity().y - vy;
//				if (dvy < 0) {
//					addVelocity(0, dvy < -1 ? 1 : -dvy, 0);
//				}

			this0.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.FIREWORK, false, getX(), getY(), getZ(), .3f, 0, .3f, .01f, 1));
		}
	}
}
