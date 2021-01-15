package net.cpp.item;

import net.cpp.api.ITickableInItemFrame;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class BreakHand extends Item implements ITickableInItemFrame {

	public BreakHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		Vec3d pos = ITickableInItemFrame.getPos(itemFrameEntity);
		for (PlayerEntity player : itemFrameEntity.world.getPlayers(TargetPredicate.DEFAULT, null, new Box(pos, pos).expand(32))) {
			((ServerPlayerEntity) player).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.CLOUD, false, pos.x, pos.y, pos.z, 0, 0, 0, 0, 1));
		}
		return itemFrameEntity.world.breakBlock(new BlockPos(pos), true);
	}

}
