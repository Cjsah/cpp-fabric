package net.cpp.entity;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;

public class GolemFisherEntity extends AGolemEntity {
	private final UUID bobberUuid = UUID.randomUUID();
	private OtherClientPlayerEntity player = new OtherClientPlayerEntity(MinecraftClient.getInstance().world, new GameProfile(bobberUuid, "傀儡渔夫的鱼钩"));
	private FishingBobberEntity bobber;

	public GolemFisherEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient) {
			ServerPlayerEntity player = new ServerPlayerEntity(((ServerWorld)world).getServer(), (ServerWorld) world, new GameProfile(bobberUuid, "傀儡渔夫的鱼钩"));
			if (bobber == null || world.getEntityById(bobber.getEntityId()) == null) {
				bobber = new FishingBobberEntity(player, world, EnchantmentHelper.getLure(getMainHandStack()), EnchantmentHelper.getLuckOfTheSea(getMainHandStack()));
				world.spawnEntity(bobber);
			}
		}
	}

	@Override
	protected void reactBlock() {
		if (!world.getFluidState(getBlockPos()).isIn(FluidTags.WATER))
			killed = true;
	}

	@Override
	public void remove(RemovalReason reason) {
		player.remove(reason);
		bobber.remove(reason);
		super.remove(reason);
	}
}
