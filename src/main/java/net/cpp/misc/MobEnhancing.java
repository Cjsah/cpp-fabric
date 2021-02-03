package net.cpp.misc;

import java.util.Map;
import java.util.Set;

import javax.xml.ws.Holder;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;

import net.cpp.ducktyping.IDarkAnimal;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class MobEnhancing {
	public static final Map<Item, String> MATERIALS = ImmutableMap.<Item, String>builder().put(Items.LEATHER, "leather").put(Items.GOLD_INGOT, "golden").put(Items.CHAIN, "chainmail").put(Items.IRON_INGOT, "iron").put(Items.DIAMOND, "diamond").put(Items.NETHERITE_INGOT, "netherite").build();
	public static final Identifier ARMOR = new Identifier("cpp:misc/armor");
	public static final Identifier WEAPON = new Identifier("cpp:misc/weapon");
	public static final Identifier ARROW = new Identifier("cpp:misc/arrow");
	public static final Identifier ENCHANCE = new Identifier("cpp:enhance");
	public static final Map<EquipmentSlot, String> ARMOR_NAMES = ImmutableMap.<EquipmentSlot, String>builder().put(EquipmentSlot.HEAD, "helmet").put(EquipmentSlot.CHEST, "chestplate").put(EquipmentSlot.LEGS, "leggings").put(EquipmentSlot.FEET, "boots").build();

	public static void equipArmor(LivingEntity living) {
		if (!living.world.isClient) {
			MinecraftServer server = living.getServer();
			LootTable lootTable = server.getLootManager().getTable(ARMOR);
			LootFunction lootFunction = server.getItemModifierManager().get(ENCHANCE);
			for (EquipmentSlot slot : new EquipmentSlot[] { EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET }) {
				if (living.getEquippedStack(slot).isEmpty()) {
					Holder<Item> holder = new Holder<>(Items.AIR);
					LootContext lootContext = new LootContext.Builder((ServerWorld) living.world).random(living.world.random).build(LootContextTypes.EMPTY);
					lootTable.generateLoot(lootContext, stack -> {
						holder.value = stack.getItem();
					});
					String material = MATERIALS.get(holder.value);
					if (material != null) {
						Item armorItem = Registry.ITEM.get(new Identifier(material + "_" + ARMOR_NAMES.get(slot)));
						if (armorItem != Items.AIR) {
							ItemStack stack = lootFunction.apply(armorItem.getDefaultStack(), lootContext);
							living.equipStack(slot, stack);
						}
					}
				}
			}
		}
	}

	public static void equipWeapon(LivingEntity living) {
		if (!living.world.isClient) {
			MinecraftServer server = living.getServer();
			LootTable lootTable = server.getLootManager().getTable(WEAPON);
			LootFunction lootFunction = server.getItemModifierManager().get(ENCHANCE);
			EquipmentSlot slot = EquipmentSlot.MAINHAND;
			if (living.getEquippedStack(slot).isEmpty()) {
				Holder<ItemStack> holder = new Holder<>(ItemStack.EMPTY);
				LootContext lootContext = new LootContext.Builder((ServerWorld) living.world).random(living.world.random).build(LootContextTypes.EMPTY);
				lootTable.generateLoot(lootContext, stack -> {
					holder.value = stack;
				});
				if (!holder.value.isEmpty()) {
					ItemStack stack = lootFunction.apply(holder.value, lootContext);
					living.equipStack(slot, stack);
				}
			}
		}
	}

	public static void equipArrow(LivingEntity living) {
		if (!living.world.isClient) {
			MinecraftServer server = living.getServer();
			LootTable lootTable = server.getLootManager().getTable(ARROW);
			EquipmentSlot slot = EquipmentSlot.OFFHAND;
			if (living.getEquippedStack(slot).isEmpty()) {
				Holder<ItemStack> holder = new Holder<>(ItemStack.EMPTY);
				LootContext lootContext = new LootContext.Builder((ServerWorld) living.world).random(living.world.random).build(LootContextTypes.EMPTY);
				lootTable.generateLoot(lootContext, stack -> {
					holder.value = stack;
				});
				if (!holder.value.isEmpty()) {
					living.equipStack(slot, holder.value);
				}
			}
		}
	}

	public static void setDropChances(MobEntity mob) {
		for (EquipmentSlot slot : EquipmentSlot.values()) {
			mob.setEquipmentDropChance(slot, .085f);
		}
	}

	public static void destroy(Entity entity) {
		World world = entity.world;
		if (!world.isClient) {
			Set<Block> set = Sets.newHashSet(Blocks.TORCH, Blocks.LANTERN, Blocks.END_ROD, Blocks.SEA_PICKLE);
			if (set.contains(world.getBlockState(entity.getBlockPos()).getBlock())) {
				world.breakBlock(entity.getBlockPos(), true, entity);
			}
		}
	}

	public static void darken(Entity entity) {
		if (!entity.world.isClient) {
			if (entity.getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) entity.world).random(entity.world.random).build(LootContextTypes.EMPTY))) {
				((IDarkAnimal) entity).setDarkness(true);
			}
		}
	}
	
	public static void darkAttack(Entity entity) {
		if (!entity.world.isClient) {
			if (((IDarkAnimal) entity).getDarkness()) {
				for (PlayerEntity player : entity.world.getPlayers()) {
					if (player.getPos().isInRange(entity.getPos(), 2)) {
						
					}
				}
			}
		}
	}
}
