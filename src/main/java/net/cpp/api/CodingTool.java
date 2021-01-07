package net.cpp.api;

import static net.cpp.api.CppChat.say;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.cpp.init.CppItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CodingTool {
	public static class SachetFleeGoal extends FleeEntityGoal<PlayerEntity> {
		public final PathAwareEntity entity;

		public SachetFleeGoal(PathAwareEntity entity) {
			super(entity, PlayerEntity.class, 16, 1, 1.2);
			this.entity = entity;
		}

		public boolean canStart() {
			boolean b = false;
			TargetPredicate targetPredicate = new TargetPredicate();
			targetPredicate.setPredicate(livingEntity -> livingEntity.getPos().isInRange(entity.getPos(), 16));
			for (PlayerEntity playerEntity : entity.world.getPlayers(targetPredicate, entity, new Box(entity.getPos(), entity.getPos()).expand(16))) {
				if (playerEntity.getInventory().contains(CppItems.SACHET.getDefaultStack())) {
					b = true;
//					System.out.println(1);
					break;
				}
			}
//			targetEntity = mob.world.getClosestEntity(mob.world.getEntitiesByClass(classToFleeFrom, mob.getBoundingBox().expand(fleeDistance, 3, fleeDistance), entity -> entity.getInventory().contains(CppItems.SACHET.getDefaultStack())), targetPredicate, mob, mob.getX(), mob.getY(), mob.getZ());
			if (!b)
				return false;
			super.canStart();
			return targetEntity != null;
		}

		@Override
		public void stop() {
			targetEntity = null;
		}
	}

	private CodingTool() {

	}

	private static int syncId;

	/**
	 * 获取当前同步ID，并让其+1
	 * 
	 * @return 当前同步ID
	 */
	public static int nextSyncId() {
		return syncId++;
	}

	/**
	 * 按照小箱子的GUI排布来获取格子的X坐标
	 * 
	 * @param m 格子所在的列数-1
	 * @return 格子的X坐标
	 */
	public static int x(int m) {
		return 8 + m * 18;
	}

	/**
	 * 按照小箱子的GUI排布来获取格子的Y坐标
	 * 
	 * @param n 格子所在的行数-1
	 * @return 格子的Y坐标
	 */
	public static int y(int n) {
		return 18 + n * 18;
	}

	/**
	 * 将UUID转化为包含4个元素的int型数组<br>
	 * （本类未使用）
	 *
	 * @author Phoupraw
	 * @see CodingTool#intArrayToUUID(IntArrayTag)
	 * @param uuid 要转化为数组的uuid
	 * @return 转化的数组
	 */
	public static int[] uuidToIntArray(UUID uuid) {
		int[] arr = new int[4];
		arr[0] = (int) (uuid.getMostSignificantBits() >> 32);
		arr[1] = (int) uuid.getMostSignificantBits();
		arr[2] = (int) (uuid.getLeastSignificantBits() >> 32);
		arr[3] = (int) uuid.getLeastSignificantBits();
		return arr;
	}

	/**
	 * 将长为4的int数组标签转化为UUID<br>
	 * （本类未使用）
	 *
	 * @author Phoupraw
	 * @see #uuidToIntArray(UUID)
	 * @param uuidListTag 含有uuid的tag
	 * @return 转化的UUID
	 */
	public static UUID intArrayToUUID(IntArrayTag uuidListTag) {
		long mostSigBits = uuidListTag.get(0).getLong() << 32;
		mostSigBits += uuidListTag.get(1).getLong();
		long leastSigBits = uuidListTag.get(2).getLong() << 32;
		leastSigBits += uuidListTag.get(3).getLong();
		return new UUID(mostSigBits, leastSigBits);
	}

	/**
	 * 获取某个玩家的当前等级的经验值
	 *
	 * @author Cjsah
	 * @param player 被获取经验值的玩家
	 * @return 当前等级的经验值
	 */
	public static int getExperience(PlayerEntity player) {
		return Math.round(player.experienceProgress * player.getNextLevelExperience());
	}

	/**
	 * 获取玩家指向的物品实体
	 *
	 * @author Cjsah
	 * @param player 玩家
	 * @return 物品实体
	 */
	public static ItemEntity rayItem(PlayerEntity player) {
		double length = .05D;
		Vec3d playerPos = player.getCameraPosVec(1.0F);
		double yaw = player.yaw;
		double pitch = player.pitch;
		double y = -Math.sin(pitch * Math.PI / 180D) * length;
		double x = -Math.sin(yaw * Math.PI / 180D);
		double z = Math.cos(yaw * Math.PI / 180D);
		double proportion = Math.sqrt((((length * length) - (y * y)) / ((x * x) + (z * z))));
		x *= proportion;
		z *= proportion;
		for (Vec3d pos = playerPos; Math.sqrt(Math.pow(pos.x - playerPos.x, 2) + Math.pow(pos.y - playerPos.y, 2) + Math.pow(pos.z - playerPos.z, 2)) < 5; pos = pos.add(x, y, z)) {
			if (player.world.getBlockState(new BlockPos(pos)).getBlock() != Blocks.AIR) {
				return null;
			}
			Box box = new Box(pos.x - 0.005, pos.y - 0.2, pos.z - 0.005, pos.x + 0.005, pos.y + 0.005, pos.z + 0.005);
			List<ItemEntity> list = player.world.getEntitiesByClass(ItemEntity.class, box, (entity) -> entity != null && entity.isAlive());
			if (!list.isEmpty()) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取玩家指向的物品实体
	 *
	 * @author Cjsah
	 * @param player 玩家
	 * @return 坐标
	 */
	public static Vec3d rayingPos(PlayerEntity player) {
		double length = .05D;
		Vec3d playerPos = player.getCameraPosVec(1.0F);
		Vec3d pos = playerPos;
		double yaw = player.yaw;
		double pitch = player.pitch;
		double y = -Math.sin(pitch * Math.PI / 180D) * length;
		double x = -Math.sin(yaw * Math.PI / 180D);
		double z = Math.cos(yaw * Math.PI / 180D);
		double proportion = Math.sqrt((((length * length) - (y * y)) / ((x * x) + (z * z))));
		x *= proportion;
		z *= proportion;
		for (; Math.sqrt(Math.pow(pos.x - playerPos.x, 2) + Math.pow(pos.y - playerPos.y, 2) + Math.pow(pos.z - playerPos.z, 2)) < 5; pos = pos.add(x, y, z)) {
			if (player.world.getBlockState(new BlockPos(pos)).getBlock() != Blocks.AIR) {
				return pos.add(-x, -y, -z);
			}
		}
		return pos;
	}

	/**
	 * 使某坐标向玩家的前方移动一段距离
	 *
	 * @author Cjsah
	 * @param pos    位移前坐标
	 * @param length 位移长度
	 * @return 位移后的坐标
	 */
	public static Vec3d move(PlayerEntity player, Vec3d pos, float length) {
		double yaw = player.yaw;
		double x = -Math.sin(yaw * Math.PI / 180D) * length;
		double z = Math.cos(yaw * Math.PI / 180D) * length;
		return pos.add(x, 0, z);
	}

	public static ItemStack newItemStack(Item item, int count, @Nullable CompoundTag nbt) {
		ItemStack rst = new ItemStack(item, count);
		if (nbt != null) {
			rst.setTag(nbt);
		}
		return rst;
	}

	/**
	 * 吸引物品
	 * 
	 * @param pos         物品需要被吸引到的地方
	 * @param serverWorld 需要吸引物品的世界
	 * @param needPickup  拾取延迟需要为0
	 * @param tp          直接传送
	 */
	public static void attractItems(Vec3d pos, ServerWorld serverWorld, boolean needPickup, boolean tp) {
		for (ItemEntity itemEntity : serverWorld.getEntitiesByType(EntityType.ITEM, new Box(pos, pos).expand(16), itemEntity -> pos.isInRange(itemEntity.getPos(), 16) && (!needPickup || !itemEntity.cannotPickup()))) {
			if (tp) {
				itemEntity.teleport(pos.x, pos.y, pos.z);
				for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers(player -> pos.distanceTo(player.getPos()) < 32)) {
					serverPlayerEntity.networkHandler.sendPacket(new EntityPositionS2CPacket(itemEntity));
				}
			} else {
				Vec3d v = pos.subtract(itemEntity.getPos());
				double d = pos.distanceTo(itemEntity.getPos());
				if (d > 1)
					v = v.multiply(1 / v.length());
				itemEntity.setVelocity(v);
				for (ServerPlayerEntity serverPlayerEntity : serverWorld.getPlayers(player -> pos.distanceTo(player.getPos()) < 32)) {
					serverPlayerEntity.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(itemEntity));
				}
			}
		}
	}

	/**
	 * 报时
	 */
	public static void timeChecker(World world) {
		if (!world.isClient) {
			long time = world.getTimeOfDay();
			int todayTime = (int) (time - time / 24000 * 24000);
			switch (todayTime) {
			case 0: {
				sendMessage(world, "morning", true);
				break;
			}
			case 6000: {
				sendMessage(world, "noon", false);
				break;
			}
			case 12000: {
				sendMessage(world, "night", true);
				break;
			}
			case 18000: {
				sendMessage(world, "midnight", false);
				break;
			}
			}
		}
	}

	public static void sendMessage(World world, String name, boolean playSound) {
		for (PlayerEntity player : world.getPlayers()) {
			say(player, new TranslatableText("chat.cpp.time." + name));
			if (playSound)
				((ServerPlayerEntity) player).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 20.0F, 1.5F));
		}
	}

	/**
	 * 挖掘方块
	 * 
	 * @param world    世界
	 * @param entity   玩家
	 * @param pos      位置
	 * @param droppeds 掉落物列表，方块的掉落物将会被加入该列表，如果为{@code null}，则掉落物直接消失
	 */
	public static void excavate(ServerWorld world, LivingEntity entity, BlockPos pos, @Nullable List<ItemStack> droppeds) {
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		ItemStack toolStack = entity.getMainHandStack();
		if (canHarvest(toolStack, blockState, world, pos)) {
			boolean b = true;
			if (entity instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity) entity;
				if (b = !player.isCreative()) {
					player.incrementStat(Stats.MINED.getOrCreateStat(block));
					toolStack.postMine(world, blockState, pos, player);
				}
			} else {
				if (blockState.getHardness(world, pos) != 0)
					toolStack.damage(1, world.random, null);
			}
			if (b) {
				block.onStacksDropped(blockState, world, pos, toolStack);
				if (droppeds != null) {
					droppeds.addAll(Block.getDroppedStacks(blockState, world, pos, blockEntity, entity, toolStack));
				}
			}
			world.breakBlock(pos, false, entity);
		}
	}

	/**
	 * 给予物品，先试图塞入物品栏，塞满后生成在玩家脚下并设定Owner，就像/give命令
	 * 
	 * @param player 玩家
	 * @param stacks 物品
	 */
	public static void give(PlayerEntity player, ItemStack... stacks) {
		for (ItemStack stack : stacks) {
			ItemEntity itemEntity = new ItemEntity(player.world, player.getX(), player.getY(), player.getZ(), stack);
			itemEntity.setOwner(player.getUuid());
			player.world.spawnEntity(itemEntity);
		}
	}

	/**
	 * 掉落物品
	 * 
	 * @param world  世界
	 * @param pos    位置
	 * @param stacks 被掉落的物品
	 */
	public static void drop(World world, Vec3d pos, ItemStack... stacks) {
		drop(world, pos, Arrays.asList(stacks));
	}

	/**
	 * 掉落物品
	 * 
	 * @param world  世界
	 * @param pos    位置
	 * @param stacks 被掉落的物品
	 */
	public static void drop(World world, Vec3d pos, List<ItemStack> stacks) {
		for (ItemStack stack : stacks) {
			ItemEntity itemEntity = new ItemEntity(world, pos.x, pos.y, pos.z, stack);
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
		}
	}

	/**
	 * 在物品栏间移动物品
	 * 
	 * @param source 源物品栏
	 * @param target 目标物品栏
	 */
	public static void transfer(Inventory source, Inventory target) {
		for (int i = 0; i < source.size(); i++) {
			ItemStack stack1 = source.getStack(i);
			for (int j = 0; j < target.size() && !stack1.isEmpty(); j++) {
				ItemStack stack2 = target.getStack(j);
				if (stack2.isEmpty()) {
					target.setStack(j, stack1.copy());
					stack1.decrement(stack1.getCount());
				} else if (ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areTagsEqual(stack1, stack2)) {
					int c = stack1.getMaxCount() - stack1.getCount() - stack2.getCount();
					stack2.increment(c);
					stack1.decrement(c);
				}
			}
		}
	}

	/**
	 * 测试工具能否挖掘方块，但不考虑基岩之类的把硬度设为{@code -1}的方块
	 * 
	 * @param stack 工具
	 * @param state 方块
	 * @return
	 */
	public static boolean canHarvest(ItemStack stack, BlockState state) {
		return (!state.isToolRequired() || stack.isSuitableFor(state));
	}

	/**
	 * 测试工具能否挖掘方块，考虑基岩之类的把硬度设为{@code -1}的方块
	 * 
	 * @param stack 工具
	 * @param state 方块
	 * @param world 世界
	 * @param pos   位置
	 * @return
	 */
	public static boolean canHarvest(ItemStack stack, BlockState state, World world, BlockPos pos) {
		return canHarvest(stack, state) && state.getHardness(world, pos) >= 0;
	}
}
