package cpp.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import cpp.api.Effect;
import cpp.api.Utils;
import cpp.ducktyping.ICppState;
import cpp.ducktyping.ITemperancable;
import cpp.init.CppEffects;
import cpp.init.CppItemTags;
import cpp.rituals.RitualResult;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
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
import net.minecraft.item.ToolItem;
import net.minecraft.item.TridentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import static cpp.init.CppBlocks.BROKEN_SPAWNER;
import static cpp.init.CppItemTags.RARE_DROPS;
import static cpp.init.CppItems.*;
import static net.minecraft.block.Blocks.BEDROCK;
import static net.minecraft.block.Blocks.DISPENSER;
import static net.minecraft.block.Blocks.EMERALD_BLOCK;
import static net.minecraft.block.Blocks.GOLD_BLOCK;
import static net.minecraft.block.Blocks.LAPIS_BLOCK;
import static net.minecraft.block.Blocks.MAGMA_BLOCK;
import static net.minecraft.block.Blocks.OBSIDIAN;
import static net.minecraft.entity.attribute.EntityAttributes.*;
import static net.minecraft.entity.effect.StatusEffects.*;
import static net.minecraft.item.Items.*;

public class Wand extends Item {
	public static final Effect MAGNETIC = new Effect(StatusEffectType.NEUTRAL, 0);
	public static final ImmutableMap<Item, StatusEffect> EFFECTS = ImmutableMap.<Item, StatusEffect>builder().put(AGENTIA_OF_AGILENESS, SPEED).put(AGENTIA_OF_BOUNCE, JUMP_BOOST).put(AGENTIA_OF_EXTREMENESS, HASTE).put(AGENTIA_OF_SHARPNESS, STRENGTH).put(AGENTIA_OF_REJUVENESS, REGENERATION).put(AGENTIA_OF_SHIELD, RESISTANCE).put(AGENTIA_OF_FIRE_SHIELD, FIRE_RESISTANCE).put(AGENTIA_OF_TRANSPARENTNESS, INVISIBILITY).put(AGENTIA_OF_WATERLESS, WATER_BREATHING).put(AGENTIA_OF_EYESIGHT, NIGHT_VISION).put(COLD_DRINK, SATURATION).put(AGENTIA_OF_LIGHTNESS, SLOW_FALLING).put(AGENTIA_OF_TIDE, CONDUIT_POWER).put(AGENTIA_OF_CHAIN, CppEffects.CHAIN).put(MAGNET, MAGNETIC).build();
	public static final ImmutableMultimap<StatusEffect, EquipmentSlot> CORRECT_SLOTS = ImmutableMultimap.<StatusEffect, EquipmentSlot>builder().put(SPEED, EquipmentSlot.FEET).put(JUMP_BOOST, EquipmentSlot.FEET).put(HASTE, EquipmentSlot.MAINHAND).put(STRENGTH, EquipmentSlot.MAINHAND).put(REGENERATION, EquipmentSlot.HEAD).put(REGENERATION, EquipmentSlot.CHEST).put(REGENERATION, EquipmentSlot.LEGS).put(REGENERATION, EquipmentSlot.FEET).put(RESISTANCE, EquipmentSlot.HEAD).put(RESISTANCE, EquipmentSlot.CHEST).put(RESISTANCE, EquipmentSlot.LEGS).put(RESISTANCE, EquipmentSlot.FEET).put(FIRE_RESISTANCE, EquipmentSlot.HEAD).put(FIRE_RESISTANCE, EquipmentSlot.CHEST).put(FIRE_RESISTANCE, EquipmentSlot.LEGS).put(FIRE_RESISTANCE, EquipmentSlot.FEET).put(INVISIBILITY, EquipmentSlot.HEAD).put(INVISIBILITY, EquipmentSlot.CHEST).put(INVISIBILITY, EquipmentSlot.LEGS).put(INVISIBILITY, EquipmentSlot.FEET).put(WATER_BREATHING, EquipmentSlot.HEAD).put(NIGHT_VISION, EquipmentSlot.HEAD).put(SATURATION, EquipmentSlot.HEAD).put(SLOW_FALLING, EquipmentSlot.FEET).put(CONDUIT_POWER, EquipmentSlot.HEAD).put(CppEffects.CHAIN, EquipmentSlot.MAINHAND).put(MAGNETIC, EquipmentSlot.MAINHAND).put(MAGNETIC, EquipmentSlot.OFFHAND).put(MAGNETIC, EquipmentSlot.HEAD).put(MAGNETIC, EquipmentSlot.CHEST).put(MAGNETIC, EquipmentSlot.LEGS).put(MAGNETIC, EquipmentSlot.FEET).build();
	public static final long HIGH_UUID = 0x0123456789ABCDEFL;
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
						Utils.actionbar(player, "info.cpp.rituals.fail");
						return ActionResult.FAIL;
					} else {
						player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
						Utils.tellraw(player, "info.cpp.rituals.start");
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
		return frameItem instanceof ToolItem /* || frameItem instanceof ShieldItem */ || frameItem instanceof ArmorItem || frameItem instanceof TridentItem;
	}

	@Nullable
	public static EquipmentSlot getSlot(Item item) {
		if (item instanceof ToolItem || item instanceof TridentItem)
			return EquipmentSlot.MAINHAND;
//		if (item instanceof ShieldItem)
//			return EquipmentSlot.OFFHAND;
		if (item instanceof ArmorItem)
			return ((ArmorItem) item).getSlotType();
		return null;
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
							if (CORRECT_SLOTS.get(effect).contains(slot)) {
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
			Utils.removeEffectExceptHidden(player, NIGHT_VISION, 254, 210);
		}
	}

	public static void tickFrame(ItemFrameEntity frame) {
		IRitualFrame iRitualFrame = (IRitualFrame) frame;
		if (iRitualFrame.getRitualType() > 0) {
			if (checkOblation(frame)) {
				if (iRitualFrame.getRitualTime() > 0) {
					for (ServerPlayerEntity player : ((ServerWorld) frame.world).getPlayers(player -> player.getPos().isInRange(frame.getPos(), 32))) {
						player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.ENCHANT, false, frame.getX(), frame.getY() + 1.1, frame.getZ(), 0f, 0, 0f, 2, 4));
						if (frame.world.getTime() % 100 == 0)
							player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_AMBIENT, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
					}
					iRitualFrame.setRitualTime(iRitualFrame.getRitualTime() - 1);
				} else {
					done(frame);
					iRitualFrame.setRitualType(0);
				}
			} else {
				ServerPlayerEntity player = (ServerPlayerEntity) frame.world.getClosestPlayer(frame.getX(), frame.getY(), frame.getZ(), 128, false);
				Utils.tellraw(player, "info.cpp.rituals.break");
				player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_DEACTIVATE, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
				iRitualFrame.setRitualType(0);
				iRitualFrame.setRitualTime(0);
			}
		}
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
		return frame.getHeldItemStack().isOf(BROKEN_SPAWNER.asItem()) && inventory.getStack(0).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(1).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(2).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(3).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(5).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(6).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(7).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(8).isOf(SHARD_OF_THE_DARKNESS) && inventory.getStack(4).isIn(RARE_DROPS);
	}

	public static boolean checkOblation5(Inventory inventory, ItemFrameEntity frame) {
		return frame.getHeldItemStack().isOf(AMETHYST_BLOCK) && inventory.getStack(0).isOf(AMETHYST_SHARD) && inventory.getStack(1).isOf(AMETHYST_SHARD) && inventory.getStack(2).isOf(AMETHYST_SHARD) && inventory.getStack(3).isOf(AMETHYST_SHARD) && inventory.getStack(5).isOf(AMETHYST_SHARD) && inventory.getStack(6).isOf(AMETHYST_SHARD) && inventory.getStack(7).isOf(AMETHYST_SHARD) && inventory.getStack(8).isOf(AMETHYST_SHARD) && inventory.getStack(4).isIn(RARE_DROPS);
	}

	public interface IRitualFrame {
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
			Utils.tellraw(player, "info.cpp.rituals.finish");
			player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.BLOCKS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
		}
	}

	public static void done1(Inventory inventory, ItemFrameEntity frame) {
		frame.setHeldItemStack(RitualResult.getEnchantingResult(inventory.getStack(1).getItem(), inventory.getStack(7).getItem()));
		decrease1(inventory);
	}

	public static void done2(Inventory inventory, ItemFrameEntity frame) {
		StatusEffect effect = EFFECTS.get(inventory.getStack(0).getItem());
		ItemStack frameStack = frame.getHeldItemStack();
		String s = effect == MAGNETIC ? "cpp:magnetic" : Registry.STATUS_EFFECT.getId(effect).toString();
		frameStack.getOrCreateTag().putString("statusEffect", s);
		frame.setHeldItemStack(frameStack);
		decrease23(inventory);
	}

	public static void done3(Inventory inventory, ItemFrameEntity frame) {
		attachAttibutes(frame.getHeldItemStack(), frame.world.random);
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
	
	public static ItemStack attachAttibutes(ItemStack stack, Random random) {
		Item item = stack.getItem();
		EquipmentSlot slot = getSlot(item);
		ListTag attributeModifiers = stack.getOrCreateTag().getList("AttributeModifiers", 10);
		double attackSpeed = 0, damage = 0, health = 0, resistance = 0, movementSpeed = 0, luck = 0;
		for (Iterator<Tag> iterator = attributeModifiers.iterator(); iterator.hasNext();) {
			Tag tag = iterator.next();
			if (tag instanceof CompoundTag) {
				CompoundTag compoundTag = (CompoundTag) tag;
				UUID uuid = NbtHelper.toUuid(compoundTag.get("UUID"));
				if (uuid.getMostSignificantBits() == HIGH_UUID && uuid.getLeastSignificantBits() > 0 && uuid.getLeastSignificantBits() <= 36) {
					iterator.remove();
				}
			}
		}
		stack.putSubTag("AttributeModifiers", attributeModifiers);
		if (attributeModifiers.isEmpty()) {
			for (Entry<EntityAttribute, EntityAttributeModifier> entry : item.getAttributeModifiers(slot).entries()) {
				EntityAttribute attribute = entry.getKey();
				EntityAttributeModifier modifier = entry.getValue();
				Operation operation = modifier.getOperation();
				double amount = modifier.getValue();
				if (attribute == GENERIC_ATTACK_SPEED && operation == Operation.ADDITION) {
					attackSpeed = amount;
				} else if (attribute == GENERIC_ATTACK_DAMAGE && operation == Operation.ADDITION) {
					damage = amount;
				} else if (attribute == GENERIC_MAX_HEALTH && operation == Operation.ADDITION) {
					health = amount;
				} else if (attribute == GENERIC_KNOCKBACK_RESISTANCE && operation == Operation.ADDITION) {
					resistance = amount;
				} else if (attribute == GENERIC_MOVEMENT_SPEED && operation == Operation.MULTIPLY_BASE) {
					movementSpeed = amount;
				} else if (attribute == GENERIC_LUCK && operation == Operation.ADDITION) {
					luck = amount;
				} else {
					stack.addAttributeModifier(attribute, modifier, slot);
				}
			}
		}
		if (item instanceof ToolItem || item instanceof TridentItem) {
			stack.addAttributeModifier(GENERIC_ATTACK_SPEED, new EntityAttributeModifier(new UUID(HIGH_UUID, 1L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", attackSpeed + random.nextDouble() * 2, Operation.ADDITION), slot);
			stack.addAttributeModifier(GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(new UUID(HIGH_UUID, 2L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", damage + random.nextDouble() * 8, Operation.ADDITION), slot);
		} else {
			stack.addAttributeModifier(GENERIC_MAX_HEALTH, new EntityAttributeModifier(new UUID(HIGH_UUID, 3L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", health + random.nextDouble() * 5, Operation.ADDITION), slot);
			stack.addAttributeModifier(GENERIC_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(new UUID(HIGH_UUID, 4L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", resistance + random.nextDouble() * .3, Operation.ADDITION), slot);
			stack.addAttributeModifier(GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(new UUID(HIGH_UUID, 5L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", movementSpeed + random.nextDouble() * .15, Operation.MULTIPLY_BASE), slot);
			stack.addAttributeModifier(GENERIC_LUCK, new EntityAttributeModifier(new UUID(HIGH_UUID, 6L * slot.ordinal() + 1), "更多的合成：仪式：属性附加", luck + random.nextDouble() * 2, Operation.ADDITION), slot);
		}
		return stack;
	}
}
