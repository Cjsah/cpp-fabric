package net.cpp.item;

import static net.cpp.init.CppItemTags.RARE_DROPS;
import static net.cpp.init.CppItems.AGENTIA_OF_AGILENESS;
import static net.cpp.init.CppItems.AGENTIA_OF_BOUNCE;
import static net.cpp.init.CppItems.AGENTIA_OF_CHAIN;
import static net.cpp.init.CppItems.AGENTIA_OF_EXTREMENESS;
import static net.cpp.init.CppItems.AGENTIA_OF_EYESIGHT;
import static net.cpp.init.CppItems.AGENTIA_OF_FIRE_SHIELD;
import static net.cpp.init.CppItems.AGENTIA_OF_LIGHTNESS;
import static net.cpp.init.CppItems.AGENTIA_OF_REJUVENESS;
import static net.cpp.init.CppItems.AGENTIA_OF_SHARPNESS;
import static net.cpp.init.CppItems.AGENTIA_OF_SHIELD;
import static net.cpp.init.CppItems.AGENTIA_OF_TIDE;
import static net.cpp.init.CppItems.AGENTIA_OF_TRANSPARENTNESS;
import static net.cpp.init.CppItems.AGENTIA_OF_WATERLESS;
import static net.cpp.init.CppItems.BROKEN_SPAWNER;
import static net.cpp.init.CppItems.COLD_DRINK;
import static net.cpp.init.CppItems.MAGNET;
import static net.cpp.init.CppItems.SHARD_OF_THE_DARKNESS;
import static net.minecraft.block.Blocks.BEDROCK;
import static net.minecraft.block.Blocks.DISPENSER;
import static net.minecraft.block.Blocks.EMERALD_BLOCK;
import static net.minecraft.block.Blocks.GOLD_BLOCK;
import static net.minecraft.block.Blocks.LAPIS_BLOCK;
import static net.minecraft.block.Blocks.MAGMA_BLOCK;
import static net.minecraft.block.Blocks.OBSIDIAN;
import static net.minecraft.entity.effect.StatusEffects.CONDUIT_POWER;
import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;
import static net.minecraft.entity.effect.StatusEffects.HASTE;
import static net.minecraft.entity.effect.StatusEffects.INVISIBILITY;
import static net.minecraft.entity.effect.StatusEffects.JUMP_BOOST;
import static net.minecraft.entity.effect.StatusEffects.NIGHT_VISION;
import static net.minecraft.entity.effect.StatusEffects.REGENERATION;
import static net.minecraft.entity.effect.StatusEffects.RESISTANCE;
import static net.minecraft.entity.effect.StatusEffects.SATURATION;
import static net.minecraft.entity.effect.StatusEffects.SLOW_FALLING;
import static net.minecraft.entity.effect.StatusEffects.SPEED;
import static net.minecraft.entity.effect.StatusEffects.STRENGTH;
import static net.minecraft.entity.effect.StatusEffects.WATER_BREATHING;
import static net.minecraft.item.Items.AMETHYST_BLOCK;
import static net.minecraft.item.Items.AMETHYST_SHARD;
import static net.minecraft.item.Items.BOOK;
import static net.minecraft.item.Items.BUDDING_AMETHYST;
import static net.minecraft.item.Items.ENCHANTED_BOOK;
import static net.minecraft.item.Items.EXPERIENCE_BOTTLE;
import static net.minecraft.item.Items.GOLD_INGOT;
import static net.minecraft.item.Items.LAPIS_LAZULI;
import static net.minecraft.item.Items.SPAWNER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;

import net.cpp.api.CodingTool;
import net.cpp.api.CppEffect;
import net.cpp.ducktype.ITemperancable;
import net.cpp.init.CppEffects;
import net.cpp.init.CppItemTags;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.resource.ServerResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class Wand extends Item {
	public static final Map<Item, StatusEffect> EFFECTS = new HashMap<>();
	public static final Map<StatusEffect, EquipmentSlot> RIGHT_SLOTS = new HashMap<>();
	public static final CppEffect MAGNETIC = new CppEffect(StatusEffectType.NEUTRAL, 0);
	public static final UUID ATTRIBUTE_UUID = new UUID(0x0123456789ABCDEFL, 0x0123456789ABCDEFL);
	private static Map<Item, Integer> randoms = new HashMap<>();
	private static int tickSpent = 1200;
	private final int level;

	public Wand(int level, Settings settings) {
		super(settings);
		this.level = level;
	}

	@Override
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return TypedActionResult.pass(user.getStackInHand(hand));
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			ServerWorld world = (ServerWorld) context.getWorld();
			ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
			BlockPos blockPos = context.getBlockPos();
			BlockState state = world.getBlockState(blockPos);
			{
				ItemFrameEntity frame = null;
				int baseType = 0;
				if (state.isOf(DISPENSER)) {
					if (checkBase1(world, blockPos)) {
						frame = getFrame(world, blockPos);
						baseType = 1;
					} else if (checkBase2(world, blockPos)) {
						frame = getFrame(world, blockPos);
						baseType = 2;
					}
				}
				if (frame != null) {
					DispenserBlockEntity blockEntity = (DispenserBlockEntity) world.getBlockEntity(blockPos);
					boolean failed = true;
					if (baseType == 1) {
						if (level >= 1 && level <= 3 && checkOblation1(blockEntity, frame)) {
							failed = false;
							IRitualFrame.setTypeTime(frame, 1, tickSpent);
						} else if (level >= 2 && level <= 3 && checkOblation2(blockEntity, frame)) {
							failed = false;
							IRitualFrame.setTypeTime(frame, 2, tickSpent);
						} else if (level == 3 && checkOblation3(blockEntity, frame)) {
							failed = false;
							IRitualFrame.setTypeTime(frame, 3, tickSpent);
						}
					} else if (baseType == 2) {
						if (level == 16 && checkOblation4(blockEntity, frame)) {
							failed = false;
							player.damage(DamageSource.MAGIC, 12);
							IRitualFrame.setTypeTime(frame, 4, tickSpent);
						} else if (level >= 2 && level <= 3 && checkOblation5(blockEntity, frame)) {
							failed = false;
							player.damage(DamageSource.MAGIC, 5);
							IRitualFrame.setTypeTime(frame, 5, tickSpent);
						}
					}
					if (failed) {
						CodingTool.actionbar(player, "info.cpp.rituals.fail");
						return ActionResult.FAIL;
					} else {
						player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
						CodingTool.tellraw(player, "info.cpp.rituals.start");
						return ActionResult.SUCCESS;
					}
				}
			}
		}
		return ActionResult.PASS;
	}

	public int getLevel() {
		return level;
	}

	public static boolean checkFrameItem23(ItemStack frameStack) {
		Item frameItem = frameStack.getItem();
		return frameItem instanceof ToolItem || frameItem instanceof ShieldItem || frameItem instanceof ArmorItem || frameItem instanceof TridentItem;
	}

	public static EquipmentSlot getSlot(Item item) {
		if (item instanceof ToolItem || item instanceof TridentItem)
			return EquipmentSlot.MAINHAND;
		if (item instanceof ShieldItem)
			return EquipmentSlot.OFFHAND;
		if (item instanceof ArmorItem)
			return ((ArmorItem) item).getSlotType();
		return EquipmentSlot.MAINHAND;
	}

	public static void loadRandoms(MinecraftServer server) {
		ServerWorld world = server.getWorlds().iterator().next();
		ServerCommandSource commandSource = new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, world, 4, "更多的合成：仪式：读取预留随机数", LiteralText.EMPTY, server, null);
		int l = CppItemTags.RARE_DROPS.values().size();
		List<Integer> rs = new ArrayList<>(l);
		for (int i = 0; i < l; i++)
			rs.add(world.random.nextInt(i + 1), i);
		for (Item item : CppItemTags.RARE_DROPS.values()) {
			String id = Registry.ITEM.getId(item).toString();
			int a = server.getCommandManager().execute(commandSource, String.format("data get storage cpp:constants rituals.enchantmentRandoms.%s 1", id)) - 1;
			if (rs.contains(a)) {
				rs.removeAll(ImmutableList.of(a));
			} else {
				a = rs.remove(world.random.nextInt(rs.size()));
				String command = String.format("data modify storage cpp:constants rituals.enchantmentRandoms.%s set value %d", id, a + 1);
				server.getCommandManager().execute(commandSource, command);
			}
			randoms.put(item, a);
		}
	}

	public static void tickEffect(PlayerEntity player) {
		if (((ITemperancable) player).isEffectEnabled() && player.world.getTime() % 20 == 0) {
			for (EquipmentSlot slot : EquipmentSlot.values()) {
				if (player.totalExperience <= 0)
					break;
				ItemStack stack = player.getEquippedStack(slot);
				if (slot == getSlot(stack.getItem())) {
					String id = stack.getOrCreateTag().getString("statusEffect");
					if (id.length() != 0) {
						boolean effected = false;
						if ("cpp:magnetic".equals(id)) {
							if (!player.world.isClient) {
								Magnet.tick((ServerPlayerEntity) player);
								effected = true;
							}
						} else {
							StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(id));
							if (slot == RIGHT_SLOTS.get(effect)) {
								if (effect == SATURATION) {
									if (player.world.getTime() % 200 == 0) {
										player.addStatusEffect(new StatusEffectInstance(effect, 1, 0, true, true));
									}
								} else if (effect == NIGHT_VISION) {
									player.addStatusEffect(new StatusEffectInstance(effect, 230, 254, true, true));
								} else {
									player.addStatusEffect(new StatusEffectInstance(effect, 20, 0, true, true));
								}
								effected = true;
							}
						}
						if (effected) {
							player.addExperience(-1);
						}
					}
				}
			}
			CodingTool.removeEffectExceptHidden(player, NIGHT_VISION, 254, 210);
		}
	}

	public static void tickFrame(ItemFrameEntity frame) {
		IRitualFrame iRitualFrame = (IRitualFrame) frame;
		if (iRitualFrame.getRitualType() > 0) {
			if (checkOblation(frame)) {
				if (iRitualFrame.getRitualTime() > 0) {
					for (ServerPlayerEntity player : ((ServerWorld) frame.world).getPlayers(player -> player.getPos().isInRange(frame.getPos(), 32))) {
						player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.ENCHANT, false, frame.getX(), frame.getY() + 1.1, frame.getZ(), 0f, 0, 0f, 2, 4));
					}
					iRitualFrame.setRitualTime(iRitualFrame.getRitualTime() - 1);
				} else {
					done(frame);
					iRitualFrame.setRitualType(0);
				}
			} else {
				ServerPlayerEntity player = (ServerPlayerEntity) frame.world.getClosestPlayer(frame.getX(), frame.getY(), frame.getZ(), 128, false);
				CodingTool.tellraw(player, "info.cpp.rituals.break");
				player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
				iRitualFrame.setRitualType(0);
				iRitualFrame.setRitualTime(0);
			}
		}
	}

	static {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			loadRandoms(server);
		});
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((MinecraftServer server, ServerResourceManager serverResourceManager, boolean success) -> {
			if (success) {
				loadRandoms(server);
			}
		});

		EFFECTS.put(AGENTIA_OF_AGILENESS, SPEED);
		EFFECTS.put(AGENTIA_OF_BOUNCE, JUMP_BOOST);
		EFFECTS.put(AGENTIA_OF_EXTREMENESS, HASTE);
		EFFECTS.put(AGENTIA_OF_SHARPNESS, STRENGTH);
		EFFECTS.put(AGENTIA_OF_REJUVENESS, REGENERATION);
		EFFECTS.put(AGENTIA_OF_SHIELD, RESISTANCE);
		EFFECTS.put(AGENTIA_OF_FIRE_SHIELD, FIRE_RESISTANCE);
		EFFECTS.put(AGENTIA_OF_TRANSPARENTNESS, INVISIBILITY);
		EFFECTS.put(AGENTIA_OF_WATERLESS, WATER_BREATHING);
		EFFECTS.put(AGENTIA_OF_EYESIGHT, NIGHT_VISION);
		EFFECTS.put(COLD_DRINK, SATURATION);
		EFFECTS.put(AGENTIA_OF_LIGHTNESS, SLOW_FALLING);
		EFFECTS.put(AGENTIA_OF_TIDE, CONDUIT_POWER);
		EFFECTS.put(AGENTIA_OF_CHAIN, CppEffects.CHAIN);
		EFFECTS.put(MAGNET, MAGNETIC);
		
		RIGHT_SLOTS.put(SPEED, EquipmentSlot.FEET);
		RIGHT_SLOTS.put(JUMP_BOOST, EquipmentSlot.FEET);
	}

	public static boolean checkBase1(World world, BlockPos blockPos) {
		return world.getBlockState(blockPos.down()).isOf(LAPIS_BLOCK) && world.getBlockState(blockPos.add(1, -1, 0)).isOf(GOLD_BLOCK) && world.getBlockState(blockPos.add(-1, -1, 0)).isOf(GOLD_BLOCK) && world.getBlockState(blockPos.add(0, -1, 1)).isOf(GOLD_BLOCK) && world.getBlockState(blockPos.add(0, -1, -1)).isOf(GOLD_BLOCK) && world.getBlockState(blockPos.add(1, -1, 1)).isOf(EMERALD_BLOCK) && world.getBlockState(blockPos.add(1, -1, -1)).isOf(EMERALD_BLOCK) && world.getBlockState(blockPos.add(-1, -1, 1)).isOf(EMERALD_BLOCK) && world.getBlockState(blockPos.add(-1, -1, -1)).isOf(EMERALD_BLOCK);
	}

	public static boolean checkBase2(World world, BlockPos blockPos) {
		return world.getBlockState(blockPos.down()).isOf(BEDROCK) && world.getBlockState(blockPos.add(1, -1, 0)).isOf(MAGMA_BLOCK) && world.getBlockState(blockPos.add(-1, -1, 0)).isOf(MAGMA_BLOCK) && world.getBlockState(blockPos.add(0, -1, 1)).isOf(MAGMA_BLOCK) && world.getBlockState(blockPos.add(0, -1, -1)).isOf(MAGMA_BLOCK) && world.getBlockState(blockPos.add(1, -1, 1)).isOf(OBSIDIAN) && world.getBlockState(blockPos.add(1, -1, -1)).isOf(OBSIDIAN) && world.getBlockState(blockPos.add(-1, -1, 1)).isOf(OBSIDIAN) && world.getBlockState(blockPos.add(-1, -1, -1)).isOf(OBSIDIAN);
	}

	@Nullable
	public static ItemFrameEntity getFrame(World world, BlockPos blockPos) {
		List<ItemFrameEntity> itemFrames = world.getEntitiesByClass(ItemFrameEntity.class, new Box(blockPos.up()), itemFrame -> itemFrame.getRotationClient().x == -90 && !itemFrame.getHeldItemStack().isEmpty());
		if (!itemFrames.isEmpty()) {
			return itemFrames.get(0);
		}
		return null;
	}

	public static boolean checkOblation(ItemFrameEntity frame) {
		IRitualFrame iRitualFrame = (IRitualFrame) frame;
		BlockEntity blockEntity0 = frame.world.getBlockEntity(frame.getBlockPos().down());
		if (blockEntity0 instanceof DispenserBlockEntity) {
			DispenserBlockEntity blockEntity = (DispenserBlockEntity) blockEntity0;
			switch (iRitualFrame.getRitualType()) {
			case 1:
				return checkOblation1(blockEntity, frame);
			case 2:
				return checkOblation2(blockEntity, frame);
			case 3:
				return checkOblation3(blockEntity, frame);
			case 4:
				return checkOblation4(blockEntity, frame);
			case 5:
				return checkOblation5(blockEntity, frame);
			default:
				return false;
			}
		}
		return false;
	}

	public static boolean checkOblation1(Inventory inventory, ItemFrameEntity frame) {
		return frame.getHeldItemStack().isOf(BOOK) && inventory.getStack(0).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(0).getCount() >= 16 && inventory.getStack(2).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(2).getCount() >= 16 && inventory.getStack(6).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(6).getCount() >= 16 && inventory.getStack(8).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(8).getCount() >= 16 && inventory.getStack(1).isIn(RARE_DROPS) && inventory.getStack(7).isIn(RARE_DROPS) && inventory.getStack(3).isOf(LAPIS_LAZULI) && inventory.getStack(4).isOf(LAPIS_LAZULI) && inventory.getStack(5).isOf(LAPIS_LAZULI);
	}

	public static boolean checkOblation2(Inventory inventory, ItemFrameEntity frame) {
		return checkFrameItem23(frame.getHeldItemStack()) && inventory.getStack(1).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(1).getCount() >= 16 && inventory.getStack(3).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(3).getCount() >= 16 && inventory.getStack(5).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(5).getCount() >= 16 && inventory.getStack(7).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(7).getCount() >= 16 && inventory.getStack(4).isIn(RARE_DROPS) && EFFECTS.containsKey(inventory.getStack(0).getItem()) && ItemStack.areItemsEqual(inventory.getStack(0), inventory.getStack(2)) && ItemStack.areItemsEqual(inventory.getStack(0), inventory.getStack(6)) && ItemStack.areItemsEqual(inventory.getStack(0), inventory.getStack(8));
	}

	public static boolean checkOblation3(Inventory inventory, ItemFrameEntity frame) {
		return checkFrameItem23(frame.getHeldItemStack()) && inventory.getStack(1).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(1).getCount() >= 16 && inventory.getStack(3).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(3).getCount() >= 16 && inventory.getStack(5).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(5).getCount() >= 16 && inventory.getStack(7).isOf(EXPERIENCE_BOTTLE) && inventory.getStack(7).getCount() >= 16 && inventory.getStack(4).isIn(RARE_DROPS) && inventory.getStack(0).isOf(GOLD_INGOT) && inventory.getStack(2).isOf(GOLD_INGOT) && inventory.getStack(6).isOf(GOLD_INGOT) && inventory.getStack(8).isOf(GOLD_INGOT);
	}

	public static boolean checkOblation4(Inventory inventory, ItemFrameEntity frame) {
		return frame.getHeldItemStack().isOf(BROKEN_SPAWNER) && inventory.getStack(0).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(1).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(2).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(3).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(5).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(6).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(7).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(8).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(4).isIn(RARE_DROPS);
	}

	public static boolean checkOblation5(Inventory inventory, ItemFrameEntity frame) {
		return frame.getHeldItemStack().isOf(AMETHYST_BLOCK) && inventory.getStack(0).isOf(AMETHYST_SHARD) && inventory.getStack(1).isOf(AMETHYST_SHARD) && inventory.getStack(2).isOf(AMETHYST_SHARD) && inventory.getStack(3).isOf(AMETHYST_SHARD) && inventory.getStack(5).isOf(AMETHYST_SHARD) && inventory.getStack(6).isOf(AMETHYST_SHARD) && inventory.getStack(7).isOf(AMETHYST_SHARD) && inventory.getStack(8).isOf(AMETHYST_SHARD) && inventory.getStack(4).isIn(RARE_DROPS);
	}

	public static interface IRitualFrame {
		void setRitualType(int type);

		int getRitualType();

		void setRitualTime(int time);

		int getRitualTime();

		static void setTypeTime(ItemFrameEntity frame, int type, int time) {
			((Wand.IRitualFrame) frame).setRitualType(type);
			((Wand.IRitualFrame) frame).setRitualTime(time);
		}
	}

	public static void decrease1(Inventory inventory) {
		for (int i = 0; i < 9; i++) {
			if ((i & 1) == 0 && i != 4) {
				inventory.getStack(i).decrement(16);
			} else {
				inventory.getStack(i).decrement(1);
			}
		}
	}

	public static void decrease23(Inventory inventory) {
		for (int i = 0; i < 9; i++) {
			if ((i & 1) != 0) {
				inventory.getStack(i).decrement(16);
			} else {
				inventory.getStack(i).decrement(1);
			}
		}
	}

	public static void decrease45(Inventory inventory) {
		for (int i = 0; i < 9; i++) {
			inventory.getStack(i).decrement(1);
		}
	}

	public static void done(ItemFrameEntity frame) {
		IRitualFrame iRitualFrame = (IRitualFrame) frame;
		BlockEntity blockEntity0 = frame.world.getBlockEntity(frame.getBlockPos().down());
		if (blockEntity0 instanceof DispenserBlockEntity) {
			DispenserBlockEntity blockEntity = (DispenserBlockEntity) blockEntity0;
			switch (iRitualFrame.getRitualType()) {
			case 1:
				done1(blockEntity, frame);
				break;
			case 2:
				done2(blockEntity, frame);
				break;
			case 3:
				done3(blockEntity, frame);
				break;
			case 4:
				done4(blockEntity, frame);
				break;
			case 5:
				done5(blockEntity, frame);
				break;
			}
			ServerPlayerEntity player = (ServerPlayerEntity) frame.world.getClosestPlayer(frame.getX(), frame.getY(), frame.getZ(), 128, false);
			CodingTool.tellraw(player, "info.cpp.rituals.finish");
			player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
		}
	}

	public static void done1(Inventory inventory, ItemFrameEntity frame) {
		Set<Entry<RegistryKey<Enchantment>, Enchantment>> entries = Registry.ENCHANTMENT.getEntries();
		int r = (randoms.get(inventory.getStack(1).getItem()) + randoms.get(inventory.getStack(7).getItem()) * CppItemTags.RARE_DROPS.values().size()) % entries.size();
		Iterator<Entry<RegistryKey<Enchantment>, Enchantment>> iterator = entries.iterator();
		while (--r > 0) {
			iterator.next();
		}
		Enchantment enchantment = iterator.next().getValue();
		ItemStack enchantedBook = ENCHANTED_BOOK.getDefaultStack();
		EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
		frame.setHeldItemStack(enchantedBook);
		decrease1(inventory);
	}

	public static void done2(Inventory inventory, ItemFrameEntity frame) {
		StatusEffect effect = EFFECTS.get(inventory.getStack(0).getItem());
		ItemStack frameStack = frame.getHeldItemStack();
		String s = effect == MAGNETIC ? "cpp:magnetic" : Registry.STATUS_EFFECT.getId(effect).toString();
		frameStack.getOrCreateTag().putString("statusEffect", s);
		decrease23(inventory);
	}

	public static void done3(Inventory inventory, ItemFrameEntity frame) {
		int r = randoms.get(inventory.getStack(4).getItem()) ^ frame.getBlockPos().down().hashCode();
		double amount = 0;
		Item frameItem = frame.getHeldItemStack().getItem();
		EntityAttribute attribute = EntityAttributes.HORSE_JUMP_STRENGTH;
		if (frameItem instanceof ToolItem || frameItem instanceof TridentItem) {
			if ((r & 1) == 0) {
				attribute = EntityAttributes.GENERIC_ATTACK_SPEED;
				amount = (r >> 1) % 3;
			} else {
				attribute = EntityAttributes.GENERIC_ATTACK_DAMAGE;
				amount = (r >> 1) % 8;
			}
		} else {
			switch (r % 4) {
			case 0:
				attribute = EntityAttributes.GENERIC_MAX_HEALTH;
				amount = (r / 4) % 6;
				break;
			case 1:
				attribute = EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE;
				amount = (r / 4) % 4;
				break;
			case 2:
				attribute = EntityAttributes.GENERIC_MOVEMENT_SPEED;
				amount = (r / 4) % 16 / 100.;
				break;
			case 3:
				attribute = EntityAttributes.GENERIC_LUCK;
				amount = (r / 4) % 3;
				break;
			}
		}
		ItemStack frameStack = frame.getHeldItemStack();
		EquipmentSlot slot = getSlot(frameItem);
		ListTag attributeModifiers = frameStack.getOrCreateTag().getList("AttributeModifiers", 10);
		if (attributeModifiers.isEmpty()) {
			Item rawItem = frameStack.getItem();
			for (Entry<EntityAttribute, EntityAttributeModifier> entry : rawItem.getAttributeModifiers(slot).entries()) {
				frameStack.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
			}
		} else {
			for (Iterator<Tag> iterator = attributeModifiers.iterator(); iterator.hasNext();) {
				Tag tag = iterator.next();
				if (tag instanceof CompoundTag) {
					CompoundTag compoundTag = (CompoundTag) tag;
					if (NbtHelper.toUuid(compoundTag.get("UUID")).equals(ATTRIBUTE_UUID)) {
						iterator.remove();
						break;
					}
				}
			}
			frameStack.putSubTag("AttributeModifiers", attributeModifiers);
		}
		frameStack.addAttributeModifier(attribute, new EntityAttributeModifier(ATTRIBUTE_UUID, "仪式：属性附加", amount, Operation.ADDITION), slot);
		decrease23(inventory);
	}

	public static void done4(Inventory inventory, ItemFrameEntity frame) {
		ItemStack newStack = SPAWNER.getDefaultStack();
		newStack.setTag(frame.getHeldItemStack().getTag());
		frame.setHeldItemStack(newStack);
		decrease45(inventory);
	}

	public static void done5(Inventory inventory, ItemFrameEntity frame) {
		frame.setHeldItemStack(BUDDING_AMETHYST.getDefaultStack());
		decrease45(inventory);
	}
}
