package net.cpp.entity;

import com.mojang.authlib.GameProfile;

import net.cpp.api.CodingTool;
import net.cpp.item.AngryHand;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class GolemWarriorEntity extends AGolemEntity {
	private PlayerEntity player;

	public GolemWarriorEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void work() {
		if (!world.isClient && world.getTime() % 10 == 0) {
			if (player == null) {
				player = new ServerPlayerEntity(world.getServer(), (ServerWorld) world, new GameProfile(getUuid(), ""));
			}
			boolean ed = false;
			for (MobEntity entity : world.getEntitiesByClass(MobEntity.class, new Box(getPos(), getPos()).expand(1.5), entity -> !AngryHand.INVULNERABLE.contains(entity.getClass()))) {
				float attack = (float) (EnchantmentHelper.getAttackDamage(getMainHandStack(), entity.getGroup()) + getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
				entity.damage(DamageSource.player(player), attack);
				ed = true;
			}
			if (ed) {
				mainHandStack.damage(1, random, null);
			}
			int exp = CodingTool.collectExpOrbs(world, getPos(), 3, false);
			exp = CodingTool.mend(mainHandStack, exp);
			CodingTool.drop(world, getPos(), CodingTool.expToBottle(exp));
		}

	}

}
