package cpp.api;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonSyntaxException;
import cpp.Craftingpp;
import cpp.entity.ADarkAnimalEntity;
import cpp.init.CppItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIntArray;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.packet.s2c.play.EntityPositionS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.tag.ServerTagManagerHolder;
import net.minecraft.tag.Tag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static cpp.api.CppChat.say;

public class Utils {
	public static class SachetFleeGoal extends FleeEntityGoal<PlayerEntity> {
		public final PathAwareEntity entity;

		public SachetFleeGoal(PathAwareEntity entity) {
			super(entity, PlayerEntity.class, 16, 1, 1.2);
			this.entity = entity;
		}

		@Override
		public boolean canStart() {
			boolean b = false;
			TargetPredicate targetPredicate = TargetPredicate.createNonAttackable();
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

	private Utils() {

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
	 * @see Utils#intArrayToUUID(NbtIntArray)
	 * @param uuid 要转化为数组的uuid
	 * @return 转化的数组
	 */
	@Nonnull
	@SuppressWarnings("unused")
	public static int[] uuidToIntArray(@Nonnull UUID uuid) {
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
	 * @param uuidListNbt 含有uuid的nbt
	 * @return 转化的UUID
	 */
	@Nonnull
	@SuppressWarnings("unused")
	public static UUID intArrayToUUID(@Nonnull NbtIntArray uuidListNbt) {
		long mostSigBits = uuidListNbt.get(0).longValue() << 32;
		mostSigBits += uuidListNbt.get(1).longValue();
		long leastSigBits = uuidListNbt.get(2).longValue() << 32;
		leastSigBits += uuidListNbt.get(3).longValue();
		return new UUID(mostSigBits, leastSigBits);
	}

	/**
	 * 将tick值转化为计时时间
	 *
	 * @return **:**:**
	 */
	public static String ticksToTime(int ticks) {
		int seconds = (int) Math.ceil((double) ticks / 20D);
		int hours = seconds / 60 / 60;
		int minutes = (seconds - (hours * 60 * 60)) / 60;
		seconds = seconds - (hours * 60 * 60) - (minutes * 60);
		return String.format("%s:%s:%s", addZero(hours), addZero(minutes), addZero(seconds));
	}

	@Nonnull
	private static String addZero(int num) {
		return num < 10 ? "0" + num : String.valueOf(num);
	}

	/**
	 * 给物品添加文本lore
	 * @param item 要添加lore的物品
	 * @param isText 是否是text
	 * @param textOrTranslated 名字
	 * @param color 颜色
	 * @param italic 倾斜
	 * @param bold 粗体
	 * @param underlined 下划线
	 * @param strikethrough 删除线
	 * @param obfuscated 乱码
	 */
	public static void addLore(ItemStack item, boolean isText, String textOrTranslated, @Nullable String color, boolean italic, boolean bold, boolean underlined, boolean strikethrough, boolean obfuscated) {
		String text = "{";
		if (isText) text += addLoreString("text", textOrTranslated);
		else text += addLoreString("translate", textOrTranslated);
		if (color != null) text += addLoreString("color", color);
		if (!italic) text += addLoreString("italic", false);
		if (bold) text += addLoreString("bold", true);
		if (underlined) text += addLoreString("underlined", true);
		if (strikethrough) text += addLoreString("strikethrough", true);
		if (obfuscated) text += addLoreString("obfuscated", true);
		text = text.substring(0, text.length()-1) + "}";
		addLore(item, NbtString.of(text));

//		NbtCompound tag = new NbtCompound();
//		if (isText) tag.putString("text", textOrTranslated);
//		else tag.putString("translate", textOrTranslated);
//		if (color != null) tag.putString("color", color);
//		if (!italic) tag.putBoolean("italic", false);
//		if (bold) tag.putBoolean("bold", true);
//		if (underlined) tag.putBoolean("underlined", true);
//		if (strikethrough) tag.putBoolean("strikethrough", true);
//		if (obfuscated) tag.putBoolean("obfuscated", true);
//		Text text = NbtHelper.toPrettyPrintedText(tag);
//		TranslatableText translatableText = new TranslatableText("text.cpp.test", text);
//		System.out.println(translatableText);
//		System.out.println(translatableText.getKey());
//		System.out.println(translatableText.asString());
//
//		addLore(item, NbtString.of(text.asString()));
	}

	private static String addLoreString(String key, Object value) {
		return String.format("\"%s\":\"%s\",", key, value);
	}

	/**
	 * 给物品添加lore
	 * @param item 要添加lore的物品
	 * @param lore 要添加的lore
	 */
	public static void addLore(ItemStack item, NbtString lore) {
		NbtCompound nbt = item.getOrCreateTag();
		if (nbt.contains("display")) {
			if (nbt.contains("Lore")) {
				nbt.getCompound("display").getList("Lore", 8).add(lore);
			}else {
				NbtList list = new NbtList();
				list.add(lore);
				nbt.getCompound("display").put("Lore", list);
			}
		}else {
			NbtCompound display = new NbtCompound();
			NbtList list = new NbtList();
			list.add(lore);
			display.put("Lore", list);
			nbt.put("display", display);
		}
	}


	/**
	 * 黑暗生物和正常生物之间转化
	 *
	 * @param entity 		当前实体
	 * @param entityType 	要转化成的实体类型
	 * @param toDark		是否转化为黑暗生物
	 * @author Cjsah
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Entity> void darkTransform(@Nonnull ServerWorld world, Entity entity, EntityType<T> entityType, boolean toDark, @Nullable Consumer<T> entityWith) {
		if (world.getTimeOfDay() % 24000 == (toDark ? 13189 : 22814) &&
				Objects.requireNonNull(Objects.requireNonNull(entity.getServer()).getPredicateManager()
						.get(new Identifier(Craftingpp.MOD_ID3, (toDark ? "dark" : "back") + "_animal")))
						.test(new LootContext.Builder(world).random(world.random).build(LootContextTypes.EMPTY))) {
			T changed = entityType.create(world);
			assert changed != null;
			changed.refreshPositionAndAngles(entity.getX(), entity.getY(), entity.getZ(), entity.getYaw(), entity.getPitch());
			changed.setVelocity(entity.getVelocity());
			if (entityWith != null) entityWith.accept(changed);
			if (toDark && changed instanceof ADarkAnimalEntity) {
				((ADarkAnimalEntity<T>)changed).setTransform(true);
				((ADarkAnimalEntity<T>)changed).initialize(world, world.getLocalDifficulty(changed.getBlockPos()), SpawnReason.CONVERSION, null, null);
			}
			else {
				((AnimalEntity)changed).initialize(world, world.getLocalDifficulty(changed.getBlockPos()), SpawnReason.CONVERSION, null, null);
			}
			world.shouldCreateNewEntityWithPassenger(changed);
			entity.discard();
		}
	}

	/**
	 * 获取玩家指向的物品实体
	 *
	 * @author Cjsah
	 * @param player 玩家
	 * @return 物品实体
	 */
	@Nullable
	public static ItemEntity rayItem(@Nonnull PlayerEntity player) {
		double length = .05D;
		Vec3d playerPos = player.getCameraPosVec(1.0F);
		double yaw = player.getYaw();
		double pitch = player.getPitch();
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
	 * 吸引物品
	 * 
	 * @param pos         物品需要被吸引到的地方
	 * @param serverWorld 需要吸引物品的世界
	 * @param needPickup  拾取延迟需要为0
	 * @param tp          直接传送
	 */
	public static void attractItems(Vec3d pos, @Nonnull ServerWorld serverWorld, boolean needPickup, boolean tp) {
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
	public static void timeChecker(@Nonnull World world) {
		if (!world.isClient) {
			long time = world.getTimeOfDay();
			int todayTime = (int) (time - time / 24000 * 24000);
			switch (todayTime) {
				case 0 -> sendTimeMessage(world, "morning", true);
				case 6000 -> sendTimeMessage(world, "noon", false);
				case 12000 -> sendTimeMessage(world, "night", true);
				case 18000 -> sendTimeMessage(world, "midnight", false);
			}
		}
	}

	public static void sendTimeMessage(@Nonnull World world, String name, boolean playSound) {
		for (PlayerEntity player : world.getPlayers()) {
			say(player, new TranslatableText("chat.cpp.time." + name));
			if (playSound) ((ServerPlayerEntity) player).networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, player.getX(), player.getY(), player.getZ(), 20.0F, 1.5F));
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
	public static void excavate(@Nonnull ServerWorld world, @Nonnull LivingEntity entity, BlockPos pos, @Nullable List<ItemStack> droppeds) {
		BlockState blockState = world.getBlockState(pos);
		Block block = blockState.getBlock();
		BlockEntity blockEntity = world.getBlockEntity(pos);
		ItemStack toolStack = entity.getMainHandStack();
		if (canHarvest(toolStack, blockState, world, pos)) {
			boolean b = true;
			if (entity instanceof ServerPlayerEntity player) {
				if (b = !player.isCreative()) {
					player.incrementStat(Stats.MINED.getOrCreateStat(block));
					toolStack.postMine(world, blockState, pos, player);
				}
			} else {
				if (blockState.getHardness(world, pos) != 0)
					toolStack.damage(1, world.random, null);
			}
			if (b) {
				Block.dropStacks(blockState, world, pos, null, entity, toolStack);
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
	public static void give(PlayerEntity player, @Nonnull ItemStack... stacks) {
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
	public static void drop(World world, Vec3d pos, @Nonnull List<ItemStack> stacks) {
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
	public static void transfer(@Nonnull Inventory source, Inventory target) {
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
	 */
	public static boolean canHarvest(ItemStack stack, @Nonnull BlockState state) {
		return !state.isToolRequired() || stack.isSuitableFor(state);
	}

	/**
	 * 测试工具能否挖掘方块，考虑基岩之类的把硬度设为{@code -1}的方块
	 * 
	 * @param stack 工具
	 * @param state 方块
	 * @param world 世界
	 * @param pos   位置
	 */
	public static boolean canHarvest(ItemStack stack, BlockState state, World world, BlockPos pos) {
//		System.out.println(state.getBlock() + ": " + state.getHardness(world, pos));
		return canHarvest(stack, state) && state.getHardness(world, pos) >= 0;
	}

	/**
	 * 把{@link Inventory}储存到{@link NbtCompound}
	 * 
	 * @param inventory 物品栏
	 * @param nbt       标签
	 */
	public static void inventoryToNbt(@Nonnull Inventory inventory, NbtCompound nbt) {
		DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
		for (int i = 0; i < stacks.size(); i++) {
			stacks.set(i, inventory.getStack(i));
		}
		Inventories.writeNbt(nbt, stacks);
	}

	/**
	 * 从{@link NbtCompound}读取{@link Inventory}
	 * 
	 * @param inventory 物品栏
	 * @param nbt       标签
	 */
	public static void inventoryFromNbt(@Nonnull Inventory inventory, NbtCompound nbt) {
		DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
		Inventories.readNbt(nbt, stacks);
		for (int i = 0; i < inventory.size(); i++) {
			inventory.setStack(i, stacks.get(i));
		}
	}

	/**
	 * 用经验值修复物品，就像经验修补
	 * 
	 * @param stack 损坏的物品
	 * @param exp   经验值
	 * @return 剩余的经验值
	 */
	public static int mend(@Nonnull ItemStack stack, int exp) {
		if (stack.isDamaged()) {
			int amount = Math.min(stack.getDamage(), exp * 2);
			stack.setDamage(stack.getDamage() - amount);
			exp -= amount / 2 + ((amount & 2) == 0 ? 0 : (Math.random() < .5 ? 1 : 0));
		}
		return exp;
	}

	/**
	 * 拾取经验球并转化为经验值
	 * 
	 * @param world  世界
	 * @param pos    位置
	 * @param radius 半径或半棱长
	 * @param globe  是球体
	 * @return 拾取到的经验值
	 */
	public static int collectExpOrbs(@Nonnull World world, Vec3d pos, double radius, boolean globe) {
		int exp = 0;
		for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(pos, pos).expand(radius), orb -> !globe || orb.getPos().isInRange(pos, radius))) {
			exp += orb.getExperienceAmount();
			orb.discard();
		}
		return exp;
	}
	
	/**
	 * 如果当前状态效果的倍率等于指定的倍率，时间小于等于指定的时间，如果有隐藏的状态效果，则使用隐藏的状态效果，否则直接移除
	 * 
	 * @param living    生物
	 * @param effect    状态效果
	 * @param amplifier 倍率
	 * @param duration  时间
	 */
	public static void removeEffectExceptHidden(@Nonnull LivingEntity living, StatusEffect effect, int amplifier, int duration) {
		StatusEffectInstance effectInstance = living.getStatusEffect(effect);
		if (effectInstance != null && effectInstance.getAmplifier() == amplifier && effectInstance.getDuration() <= duration) {
			NbtCompound nbt1 = effectInstance.writeNbt(new NbtCompound());
			if (nbt1.contains("HiddenEffect")) {
				NbtCompound nbt2 = nbt1.getCompound("HiddenEffect");
				living.addStatusEffect(StatusEffectInstance.fromNbt(nbt2));
			} else {
				living.removeStatusEffect(effect);
			}
		}
	}

	public static void actionbar(@Nonnull ServerPlayerEntity player, String translationKey, Object... params) {
		player.sendMessage(new TranslatableText(translationKey, params), true);
	}

	public static void tellraw(@Nonnull ServerPlayerEntity player, String translationKey, Object... params) {
		player.sendMessage(new TranslatableText(translationKey, params), false);
	}

	public static <T> ImmutableList<T> findByKeyword(@Nonnull DefaultedRegistry<T> registry, String keyword) {
		ImmutableList.Builder<T> builder = ImmutableList.builder();
		for (Entry<RegistryKey<T>, T> entry : registry.getEntries()) {
			if (entry.getKey().getValue().getPath().contains(keyword)) {
				builder.add(entry.getValue());
			}
		}
		return builder.build();
	}

	public static boolean canInsert(List<ItemStack> items, Inventory inventory, int beginIndex, int endIndex) {
		List<ItemStack> copy = Lists.newArrayListWithCapacity(endIndex - beginIndex);
		for (int i = beginIndex; i < endIndex; i++) {
			copy.add(inventory.getStack(i).copy());
		}
		for (ItemStack stack : items) {
			for (int i = 0; i < copy.size() && !stack.isEmpty(); i++) {
				if (copy.get(i).isEmpty()) {
					copy.set(i, stack.copy());
					stack.decrement(stack.getCount());
				} else {
					ItemStack stack2 = copy.get(i);
					if (equal(stack, stack2)) {
						int c = Math.min(stack.getCount(), stack2.getMaxCount() - stack2.getCount());
						stack.decrement(c);
						stack2.increment(c);
					}
				}
			}
			if (!stack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static void insert(@Nonnull List<ItemStack> items, Inventory inventory, int beginIndex, int endIndex) {
		for (ItemStack stack : items) {
			for (int i = beginIndex; i < endIndex && !stack.isEmpty(); i++) {
				if (inventory.getStack(i).isEmpty()) {
					inventory.setStack(i, stack.copy());
					stack.decrement(stack.getCount());
				} else {
					ItemStack stack2 = inventory.getStack(i);
					if (equal(stack, stack2)) {
						int c = Math.min(stack.getCount(), stack2.getMaxCount() - stack2.getCount());
						stack.decrement(c);
						stack2.increment(c);
					}
				}
			}
		}
	}

	public static boolean equal(ItemStack stack1, ItemStack stack2) {
		return ItemStack.areItemsEqual(stack1, stack2) && ItemStack.areTagsEqual(stack1, stack2);
	}

	public static <T> Tag<T> getTag(String id, RegistryKey<Registry<T>> registryKey) {
		return ServerTagManagerHolder.getTagManager().getTag(registryKey, new Identifier(id), id1 -> new JsonSyntaxException("Unknown item tag '" + id1 + "'"));
	}

	public static void playSound(@Nonnull World world, PlaySoundS2CPacket packet) {
		if (!world.isClient()) {
			for (PlayerEntity player: world.getPlayers(TargetPredicate.DEFAULT, null, new Box(packet.getX(),packet.getY(),packet.getZ(),packet.getX(),packet.getY(),packet.getZ()).expand(packet.getVolume() * 16))) {
				((ServerPlayerEntity)player).networkHandler.sendPacket(packet);
			}
		}
	}
	
	public static void addPlayerSlots(Consumer<Slot> addSlot, PlayerInventory playerInventory) {
		addPlayerSlots(addSlot, playerInventory, 8, 84);
	}
	
	public static void addPlayerSlots(Consumer<Slot> addSlot, PlayerInventory playerInventory, int x, int y) {
		int m;
		int l;
		for (m = 0; m < 3; ++m) {
			for (l = 0; l < 9; ++l) {
				addSlot.accept(new Slot(playerInventory, l + m * 9 + 9, x + l * 18, y + m * 18));
			}
		}
		
		for (m = 0; m < 9; ++m) {
			addSlot.accept(new Slot(playerInventory, m, x + m * 18, y + 58));
		}
	}

	public static void hsbFromRGB(int rgb, float[] hsb) {
		Color.RGBtoHSB((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff, hsb);
	}
}
