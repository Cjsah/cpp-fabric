package net.cpp.item;

import static net.cpp.Craftingpp.CPP_GROUP_MISC;

import net.cpp.entity.CompressedExperienceBottleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CompressedExperienceBottleItem extends ExperienceBottleItem {

	public CompressedExperienceBottleItem() {
		super(new Item.Settings().group(CPP_GROUP_MISC));
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound((PlayerEntity) null, user.getX(), user.getY(), user.getZ(),
				SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F,
				0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			CompressedExperienceBottleEntity experienceBottleEntity = new CompressedExperienceBottleEntity(world, user);
			experienceBottleEntity.setItem(itemStack);
			experienceBottleEntity.setProperties(user, user.pitch, user.yaw, -20.0F, 0.7F, 1.0F);
			world.spawnEntity(experienceBottleEntity);
		}

		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.abilities.creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}

}
