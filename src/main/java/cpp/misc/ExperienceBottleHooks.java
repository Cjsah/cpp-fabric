package cpp.misc;

import cpp.api.Utils;
import cpp.ducktyping.IMultiple;
import cpp.init.CppItems;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ExperienceBottleHooks {
	public static final String MULTIPLE_TAG_NAME = "multiple";
	
	public static int calcLevelPoints(int level) {
		if (level >= 30)
			return 112 + (level - 30) * 9;
		else
			return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
	}
	
	public static int calcTotal(int level) {
		int sum = 0;
		for (int i = 0; i < level; i++)
			sum += calcLevelPoints(i);
		return sum;
	}
	
	public static boolean transferExpToBottle(PlayerEntity player) {
		if (!player.getOffHandStack().isOf(Items.HOPPER))
			return false;
		int v = 9;
		ItemStack stack = Items.EXPERIENCE_BOTTLE.getDefaultStack();
		if (player.getMainHandStack().isOf(CppItems.COMPRESSOR)) {
			v *= 64;
			stack.getOrCreateTag().putByte(MULTIPLE_TAG_NAME, (byte) 1);
		}
		if (calcTotal(player.experienceLevel) >= v) {
			player.addExperience(-v);
			Utils.give(player, stack);
			return true;
		}
		return false;
	}
	
	public static TypedActionResult<ItemStack> useItem(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		int multiple = itemStack.getOrCreateTag().getByte(MULTIPLE_TAG_NAME);
		world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
		if (!world.isClient) {
			ExperienceBottleEntity experienceBottleEntity = new ExperienceBottleEntity(world, user);
			experienceBottleEntity.setItem(itemStack);
			experienceBottleEntity.setProperties(user, user.getPitch(), user.getYaw(), -20.0F, 0.7F, 1.0F);
			((IMultiple) experienceBottleEntity).setMultiple(multiple);
			world.spawnEntity(experienceBottleEntity);
		}
		itemStack.decrement(1);
		user.incrementStat(Stats.USED.getOrCreateStat(Items.EXPERIENCE_BOTTLE));
		return TypedActionResult.success(itemStack, world.isClient());
	}
	
	/**
	 * 把经验值转化为附魔之瓶，如果不是9的倍数，则按照概率转化。例如，5点经验有5/9的概率转化1个附魔之瓶
	 *
	 * @param exp 经验值
	 *
	 * @return 附魔之瓶列表，每个物品叠不会超过最大堆叠数量，并且只有最后一个可能没有到达最大堆叠
	 */
	@Nonnull
	public static List<ItemStack> expToBottle(int exp) {
		List<ItemStack> list = new LinkedList<>();
		int c1 = exp / 9 + (Math.random() < (exp % 9) / 9. ? 1 : 0);
		int c2 = Items.EXPERIENCE_BOTTLE.getMaxCount();
		while (c1 >= c2) {
			list.add(new ItemStack(Items.EXPERIENCE_BOTTLE, c2));
			c1 -= c2;
		}
		list.add(new ItemStack(Items.EXPERIENCE_BOTTLE, c1));
		return list;
	}
}
