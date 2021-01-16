package net.cpp.item;

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
import static net.cpp.init.CppItems.COLD_DRINK;
import static net.cpp.init.CppItems.MAGNET;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableList;

import net.cpp.api.CodingTool;
import net.cpp.api.CppEffect;
import net.cpp.ducktype.IRitualStackHolder;
import net.cpp.ducktype.ITemperancable;
import net.cpp.init.CppEffects;
import net.cpp.init.CppItemTags;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributeModifier.Operation;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.player.PlayerEntity;
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
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
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
import net.minecraft.text.TranslatableText;
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
	public static final CppEffect MAGNETIC = new CppEffect(StatusEffectType.NEUTRAL, 0);
	public static final UUID ATTRIBUTE_UUID = new UUID(0x0123456789ABCDEFL, 0x0123456789ABCDEFL);
	private static Map<Item, Integer> randoms = new HashMap<>();
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
			int suc = 0;
			ServerWorld world = (ServerWorld) context.getWorld();
			ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
			BlockPos blockPos = context.getBlockPos();
			BlockState state = world.getBlockState(blockPos);
			if (state.isOf(Blocks.DISPENSER)) {
				int type = 1;
				for1: for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						Block block = world.getBlockState(blockPos.add(i, -1, j)).getBlock();
						if (i == 0 && j == 0) {
							if (block != Blocks.LAPIS_BLOCK) {
								type = 0;
								break for1;
							}
						} else if (((i + j) & 1) == 0) {
							if (block != Blocks.EMERALD_BLOCK) {
								type = 0;
								break for1;
							}
						} else {
							if (block != Blocks.GOLD_BLOCK) {
								type = 0;
								break for1;
							}
						}
					}
				}
				if (type == 0) {
					type = 2;
					for1: for (int i = -1; i < 2; i++) {
						for (int j = -1; j < 2; j++) {
							Block block = world.getBlockState(blockPos.add(i, -1, j)).getBlock();
							if (i == 0 && j == 0) {
								if (block != Blocks.BEDROCK) {
									type = 0;
									break for1;
								}
							} else if (((i + j) & 1) == 0) {
								if (block != Blocks.OBSIDIAN) {
									type = 0;
									break for1;
								}
							} else {
								if (block != Blocks.MAGMA_BLOCK) {
									type = 0;
									break for1;
								}
							}
						}
					}
				}
				if (type > 0) {
					List<ItemFrameEntity> itemFrames = world.getEntitiesByClass(ItemFrameEntity.class, new Box(blockPos.up()), itemFrame -> itemFrame.getRotationClient().x == -90 && !itemFrame.getHeldItemStack().isEmpty());
					if (!itemFrames.isEmpty()) {
						ItemFrameEntity itemFrame = itemFrames.get(0);
						ItemStack frameStack = itemFrame.getHeldItemStack();
						DispenserBlockEntity inv = (DispenserBlockEntity) world.getBlockEntity(blockPos);
						int level = ((Wand) context.getStack().getItem()).getLevel();
						if (type == 1) {
							if (inv.getStack(0).isOf(EXPERIENCE_BOTTLE)) {
								if (level >= 1 && frameStack.isOf(BOOK)) {
									boolean b2 = true;
									for (int i = 0; i < 9; i++) {
										ItemStack stack = inv.getStack(i);
										if ((i & 1) == 0 && i != 4) {
											if (!stack.isOf(EXPERIENCE_BOTTLE) || stack.getCount() < 16) {
												b2 = false;
												break;
											}
										} else if (i >= 3 && i <= 5) {
											if (!stack.isOf(LAPIS_LAZULI)) {
												b2 = false;
												break;
											}
										} else {
											if (!stack.isIn(CppItemTags.RARE_DROPS)) {
												b2 = false;
												break;
											}
										}
									}
									if (b2) {
										if (level < 1) {
											suc = 1 << 2 | 0b10;
										} else {
											ItemStack stack1 = inv.getStack(1).getItem().getDefaultStack();
											ItemStack stack2 = inv.getStack(7).getItem().getDefaultStack();
											for (int i = 0; i < 9; i++) {
												ItemStack stack = inv.getStack(i);
												if ((i & 1) == 0 && i != 4) {
													stack.decrement(16);
												} else {
													stack.decrement(1);
												}
											}
											Set<Entry<RegistryKey<Enchantment>, Enchantment>> entries = Registry.ENCHANTMENT.getEntries();
											int r = (randoms.get(stack1.getItem()) + randoms.get(stack2.getItem()) * CppItemTags.RARE_DROPS.values().size()) % entries.size();
											Iterator<Entry<RegistryKey<Enchantment>, Enchantment>> iterator = entries.iterator();
											while (--r > 0) {
												iterator.next();
											}
											Enchantment enchantment = iterator.next().getValue();
											ItemStack enchantedBook = ENCHANTED_BOOK.getDefaultStack();
											EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentLevelEntry(enchantment, enchantment.getMaxLevel()));
											setRitualStack(itemFrame, enchantedBook);
											suc = 1;
										}
									}
								}
							} else if (inv.getStack(0).isOf(GOLD_INGOT)) {
								Item frameItem = frameStack.getItem();
								if (checkFrameItem23(frameItem)) {
									boolean b2 = true;
									for (int i = 0; i < 9; i++) {
										ItemStack stack = inv.getStack(i);
										if ((i & 1) != 0) {
											if (!stack.isOf(EXPERIENCE_BOTTLE) || stack.getCount() < 16) {
												b2 = false;
												break;
											}
										} else if ((i & 1) == 0 && i != 4) {
											if (!stack.isOf(GOLD_INGOT)) {
												b2 = false;
												break;
											}
										} else {
											if (!stack.isIn(CppItemTags.RARE_DROPS)) {
												b2 = false;
												break;
											}
										}
									}
									if (b2) {
										if (level < 3) {
											suc = 3 << 2 | 0b10;
										} else {
											ItemStack stack1 = inv.getStack(4).getItem().getDefaultStack();
											for (int i = 0; i < 9; i++) {
												ItemStack stack = inv.getStack(i);
												if ((i & 1) != 0) {
													stack.decrement(16);
												} else {
													stack.decrement(1);
												}
											}
											int r = randoms.get(stack1.getItem()) ^ blockPos.hashCode();
											double amount = 0;
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
											ItemStack modifiedStack = frameStack.copy();
											EquipmentSlot slot = getSlot(frameItem);
											ListTag attributeModifiers = modifiedStack.getOrCreateTag().getList("AttributeModifiers", 10);
											if (attributeModifiers.isEmpty()) {
												Item rawItem = modifiedStack.getItem();
												for (Entry<EntityAttribute, EntityAttributeModifier> entry : rawItem.getAttributeModifiers(slot).entries()) {
													modifiedStack.addAttributeModifier(entry.getKey(), entry.getValue(), slot);
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
												modifiedStack.putSubTag("AttributeModifiers", attributeModifiers);
											}
											modifiedStack.addAttributeModifier(attribute, new EntityAttributeModifier(ATTRIBUTE_UUID, "仪式：属性附加", amount, Operation.ADDITION), slot);
											setRitualStack(itemFrame, modifiedStack);
											suc = 1;
										}
									}
								}
							} else if (inv.getStack(1).isOf(EXPERIENCE_BOTTLE)) {
								Item frameItem = frameStack.getItem();
								if (checkFrameItem23(frameItem)) {
									boolean b2 = true;
									for (int i = 0; i < 9; i++) {
										ItemStack stack = inv.getStack(i);
										if ((i & 1) != 0) {
											if (!stack.isOf(EXPERIENCE_BOTTLE) || stack.getCount() < 16) {
												b2 = false;
												break;
											}
										} else if (i == 4) {
											if (!stack.isIn(CppItemTags.RARE_DROPS)) {
												b2 = false;
												break;
											}
										} else {
											if (!checkPotion(stack.getItem()) || !ItemStack.areItemsEqual(inv.getStack(0), stack)) {
												b2 = false;
												break;
											}
										}
									}
									if (b2) {
										if (level < 2) {
											suc = 2 << 2 | 0b10;
										} else {
											StatusEffect effect = EFFECTS.get(inv.getStack(0).getItem());
											for (int i = 0; i < 9; i++) {
												ItemStack stack = inv.getStack(i);
												if ((i & 1) != 0) {
													stack.decrement(16);
												} else {
													stack.decrement(1);
												}
											}
											ItemStack modifiedStack = frameStack.copy();
											String s = effect == MAGNETIC ? "cpp:magnetic" : Registry.STATUS_EFFECT.getId(effect).toString();
											modifiedStack.getOrCreateTag().putString("statusEffect", s);
											setRitualStack(itemFrame, modifiedStack);
											suc = 1;
										}
									}
								}
							}
						} else if (type == 2) {
							if (frameStack.isOf(AMETHYST_BLOCK)) {
								boolean b2 = true;
								for (int i = 0; i < 9; i++) {
									ItemStack stack = inv.getStack(i);
									if (i != 4) {
										if (!stack.isOf(AMETHYST_SHARD)) {
											b2 = false;
											break;
										}
									} else {
										if (!stack.isIn(CppItemTags.RARE_DROPS)) {
											b2 = false;
											break;
										}
									}
								}
								if (b2) {
									if (level < 2) {
										suc = 2 << 2 | 0b10;
									} else {
										context.getPlayer().damage(DamageSource.MAGIC, 5);
										for (int i = 0; i < 9; i++) {
											inv.getStack(i).decrement(1);
										}
										Wand.setRitualStack(itemFrame, BUDDING_AMETHYST.getDefaultStack());
										suc = 1;
									}
								}
							}
						}
					}
				}
			}
			if (suc == 1) {
				player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, 1, 1));
				return ActionResult.SUCCESS;
			} else if ((suc & 0b10) == 0b10) {
				player.networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR, new TranslatableText("chat.cpp.wand_level_error", suc >> 2, level)));
				return ActionResult.CONSUME;
			} else {
				return ActionResult.PASS;
			}
		}
		return ActionResult.PASS;
	}

	public int getLevel() {
		return level;
	}

	public static boolean checkFrameItem23(Item frameItem) {
		return frameItem instanceof ToolItem || frameItem instanceof ShieldItem || frameItem instanceof ArmorItem || frameItem instanceof TridentItem;
	}

	public static boolean checkPotion(Item item) {
		return EFFECTS.containsKey(item);
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

	public static void setRitualStack(ItemFrameEntity itemFrame, ItemStack ritualStack) {
		ritualStack.getOrCreateTag().putInt("delay", 1200);
		((IRitualStackHolder) itemFrame).setRitualStack(ritualStack);
		itemFrame.setInvulnerable(true);
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
						boolean b1 = false;
						if ("cpp:magnetic".equals(id)) {
							if (!player.world.isClient) {
								Magnet.tick((ServerPlayerEntity) player);
								b1 = true;
							}
						} else {
							StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier(id));
							if (effect == SATURATION) {
								if (player.world.getTime() % 200 == 0) {
									player.addStatusEffect(new StatusEffectInstance(effect, 1, 0, true, true));
								}
							} else if (effect == NIGHT_VISION) {
								player.addStatusEffect(new StatusEffectInstance(effect, 230, 254, true, true));
							} else {
								player.addStatusEffect(new StatusEffectInstance(effect, 20, 0, true, true));
							}
							b1 = true;
						}
						if (b1) {
							player.addExperience(-1);
						}
					}
				}
			}
			CodingTool.removeEffectExceptHidden(player, NIGHT_VISION, 254, 210);
		}
	}

	public static void tickFrame(ItemFrameEntity frame) {
		ItemStack ritualStack = ((IRitualStackHolder) frame).getRitualStack();
		if (!ritualStack.isEmpty()) {
			int delay = ritualStack.getOrCreateTag().getInt("delay");
			if (delay-- <= 0) {
				ritualStack.removeSubTag("delay");
				frame.setHeldItemStack(ritualStack);
				((IRitualStackHolder) frame).setRitualStack(ItemStack.EMPTY);
				frame.setInvulnerable(false);
				for (ServerPlayerEntity player : ((ServerWorld) frame.world).getPlayers(player -> player.getPos().isInRange(frame.getPos(), 32))) {
					player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_POWER_SELECT, SoundCategory.PLAYERS, frame.getX(), frame.getY(), frame.getZ(), 5, 1));
				}
			} else {
				ritualStack.getOrCreateTag().putInt("delay", delay);
				for (ServerPlayerEntity player : ((ServerWorld) frame.world).getPlayers(player -> player.getPos().isInRange(frame.getPos(), 32))) {
					player.networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.ENCHANT, false, frame.getX(), frame.getY() + 1, frame.getZ(), 0.2f, 0, 0.2f, 1, 1));
				}
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
	}
}
