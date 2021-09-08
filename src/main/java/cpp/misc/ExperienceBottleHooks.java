package cpp.misc;

import cpp.api.Utils;
import cpp.ducktyping.IMultiple;
import cpp.init.CppItems;
import cpp.item.CompressedItem;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.LinkedList;
import java.util.List;

/**
 * 与附魔之瓶有关的工具类
 */
public class ExperienceBottleHooks {
public static final String KEY = "multiple";

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

/**
 * 当玩家副手持有漏斗时，经验会以每刻9点的速度转化为附魔之瓶。
 * <p>
 * 如果此时玩家主手还持有压缩器，经验会以每刻576点的速度转化为压缩重数为1的附魔之瓶。
 *
 * @param player 玩家
 * @return 是转化了附魔之瓶
 */
public static boolean transferExpToBottle(PlayerEntity player) {
	if (!player.getOffHandStack().isOf(Items.HOPPER))
		return false;
	int v = 9;
	ItemStack stack = Items.EXPERIENCE_BOTTLE.getDefaultStack();
	if (player.getMainHandStack().isOf(CppItems.COMPRESSOR)) {
		v *= 64;
		CompressedItem.setMultiple(stack, 1);
	}
	if (calcTotal(player.experienceLevel) >= v) {
		player.addExperience(-v);
		Utils.give(player, stack);
		return true;
	}
	return false;
}

/**
 * 重写{@link net.minecraft.item.ExperienceBottleItem#use(World, PlayerEntity, Hand)}，由mixin类调用此方法
 */
public static TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
	ItemStack handStack = user.getStackInHand(hand);
	int multiple = CompressedItem.getMultiple(handStack);
	world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
	if (!world.isClient) {
		ExperienceBottleEntity experienceBottleEntity = new ExperienceBottleEntity(world, user);
		experienceBottleEntity.setItem(handStack);
		experienceBottleEntity.setProperties(user, user.getPitch(), user.getYaw(), -20.0F, 0.7F, 1.0F);
		setMultiple(experienceBottleEntity, multiple);
		world.spawnEntity(experienceBottleEntity);
	}
	handStack.decrement(1);
	user.incrementStat(Stats.USED.getOrCreateStat(Items.EXPERIENCE_BOTTLE));
	return TypedActionResult.success(handStack, world.isClient());
}

/**
 * 把经验值转化为附魔之瓶，如果不是9的倍数，则按照概率转化。例如，5点经验有5/9的概率转化1个附魔之瓶
 *
 * @param exp 经验值
 * @return 附魔之瓶列表，每个物品叠不会超过最大堆叠数量，并且只有最后一个可能没有到达最大堆叠
 */
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

/**
 * 生成经验球
 *
 * @param world  世界
 * @param pos    位置
 * @param amount 每个经验球的经验量
 * @param count  经验球的数量
 */
public static void spawnOrbs(World world, Vec3d pos, int amount, int count) {
	for (int i = 0; i < count; i++)
		world.spawnEntity(new ExperienceOrbEntity(world, pos.getX(), pos.getY(), pos.getZ(), amount));
}

/**
 * 给予最近的玩家经验等级+经验点数
 *
 * @param world  世界
 * @param pos    搜索中心
 * @param levels 经验等级
 * @param amount 经验点数
 */
public static void giveCloset(World world, Vec3d pos, int levels, long amount) {
	PlayerEntity player = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 32, false);
	if (player != null) {
		player.addExperienceLevels(levels);
		for (long l = amount / Integer.MAX_VALUE; l > 0; l--)
			player.addExperience(Integer.MAX_VALUE);
		player.addExperience((int)(amount % Integer.MAX_VALUE));
	}
}

/**
 * 获取压缩附魔之瓶的经验值
 *
 * @param bottle 压缩附魔之瓶
 * @return 一瓶的经验值
 */
public static int calc(ItemStack bottle) {
	return 9 << (6 * CompressedItem.getMultiple(bottle));
}

public static int getMultiple(ExperienceBottleEntity bottle) {
	return CompressedItem.getMultiple(bottle.getStack());
}

public static void setMultiple(ExperienceBottleEntity bottle, int multiple) {
	 CompressedItem.setMultiple(bottle.getStack(), multiple);
}
}
