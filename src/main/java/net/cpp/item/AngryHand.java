package net.cpp.item;

import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AngryHand extends Item implements ITickableInItemFrame {
	public static final Set<Class<? extends Entity>> INVULNERABLE = ImmutableSet.of(PlayerEntity.class, EnderDragonEntity.class, WitherEntity.class);

	public AngryHand(Settings settings) {
		super(settings);
	}

	@Override
	public boolean tick(ItemFrameEntity itemFrameEntity) {
		boolean b = false;
		Vec3d pos = ITickableInItemFrame.getPos(itemFrameEntity);
		World world = itemFrameEntity.world;
		if (world.getTime() % 10 == 0) {
			for (Entity entity : itemFrameEntity.world.getEntitiesByClass(LivingEntity.class, new Box(pos, pos).expand(1.5), entity -> {
				for (Class<? extends Entity> class1 : INVULNERABLE)
					if (class1.isInstance(entity))
						return false;
				return true;
			})) {
				entity.damage(DamageSource.GENERIC, 20f);
				b = true;
			}
		}
		return b;
	}

}