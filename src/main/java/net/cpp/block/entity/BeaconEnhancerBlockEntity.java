package net.cpp.block.entity;

import static net.minecraft.entity.effect.StatusEffects.FIRE_RESISTANCE;
import static net.minecraft.entity.effect.StatusEffects.GLOWING;
import static net.minecraft.entity.effect.StatusEffects.INVISIBILITY;
import static net.minecraft.entity.effect.StatusEffects.NIGHT_VISION;
import static net.minecraft.entity.effect.StatusEffects.POISON;
import static net.minecraft.entity.effect.StatusEffects.SATURATION;
import static net.minecraft.entity.effect.StatusEffects.SLOWNESS;
import static net.minecraft.entity.effect.StatusEffects.WATER_BREATHING;
import static net.minecraft.entity.effect.StatusEffects.WEAKNESS;
import static net.minecraft.entity.effect.StatusEffects.WITHER;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.mojang.brigadier.StringReader;

import net.cpp.api.CodingTool;
import net.cpp.api.Effect;
import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppBlocks;
import net.cpp.init.CppEffects;
import net.cpp.screen.handler.BeaconEnhancerScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.EntitySelectorReader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HoglinEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * 信标增强器方块实体
 *
 * @author Ph-苯
 */
public class BeaconEnhancerBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
	public static final StatusEffect ATTRACTING = new Effect(StatusEffectType.HARMFUL, 0);
	public static final List<StatusEffect> AVAILABLE_PLAYER_EFFECTS = Collections.unmodifiableList(Arrays.asList(FIRE_RESISTANCE, NIGHT_VISION, WATER_BREATHING, INVISIBILITY, SATURATION, CppEffects.CHAIN));
	public static final List<StatusEffect> AVAILABLE_MOB_EFFECTS = Collections.unmodifiableList(Arrays.asList(WEAKNESS, SLOWNESS, GLOWING, POISON, WITHER, ATTRACTING));
	/** 当前选择的状态效果 */
	private StatusEffect playerEffect = StatusEffects.ABSORPTION, mobEffect = StatusEffects.ABSORPTION;
	/** 当前选择的状态效果的代号，用于储存NBT和同步客户端 */
	private int playerEffectCode, mobEffectCode;
	/** 负面效果是否仅用于敌对生物 */
	private boolean onlyAdverse;
	/** 用于目标选择器的命令源 */
	private int timeCounter;
	protected ServerCommandSource serverCommandSource;
	private int cooldown = 160;
	public final PropertyDelegate propertyDelegate = new PropertyDelegate() {
		
		@Override
		public int size() {
			return 3;
		}
		
		@Override
		public void set(int index, int value) {
			switch (index) {
				case 0:
					playerEffectCode = value % AVAILABLE_PLAYER_EFFECTS.size();
					playerEffect = AVAILABLE_PLAYER_EFFECTS.get(playerEffectCode);
					break;
				case 1:
					mobEffectCode = value % AVAILABLE_MOB_EFFECTS.size();
					mobEffect = AVAILABLE_MOB_EFFECTS.get(mobEffectCode);
					break;
				case 2:
					onlyAdverse = value != 0;
					break;
				default:
					break;
			}
		}
		
		@Override
		public int get(int index) {
			switch (index) {
				case 0:
					return playerEffectCode;
				case 1:
					return mobEffectCode;
				case 2:
					return onlyAdverse ? 1 : 0;
				default:
					return -1;
			}
		}
	};
	
	public BeaconEnhancerBlockEntity() {
		this(BlockPos.ORIGIN, CppBlocks.BEACON_ENHANCER.getDefaultState());
	}
	
	public BeaconEnhancerBlockEntity(BlockPos blockPos, BlockState blockState) {
		super(CppBlockEntities.BEACON_ENHANCER, blockPos, blockState);
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putInt("playerEffect", playerEffectCode);
		tag.putInt("mobEffect", mobEffectCode);
		tag.putBoolean("onlyAdverse", onlyAdverse);
		return super.toTag(tag);
	}
	
	/**
	 * 获取服务端命令源，用于构建目标选择器
	 *
	 * @return 服务端命令源
	 */
	protected ServerCommandSource getServerCommandSource() {
		if (!world.isClient && serverCommandSource == null)
			serverCommandSource = new ServerCommandSource(CommandOutput.DUMMY, Vec3d.of(pos), Vec2f.ZERO, (ServerWorld) world, 4, "", LiteralText.EMPTY, world.getServer(), null);
		return serverCommandSource;
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		playerEffectCode = tag.getInt("playerEffect");
		playerEffect = AVAILABLE_PLAYER_EFFECTS.get(playerEffectCode);
		mobEffectCode = tag.getInt("mobEffect");
		mobEffect = AVAILABLE_MOB_EFFECTS.get(mobEffectCode);
		onlyAdverse = tag.getBoolean("onlyAdverse");
		super.fromTag(tag);
	}
	
	public static void tick(World world, BlockPos pos, BlockState state, BeaconEnhancerBlockEntity blockEntity) {
		if (!world.isClient) {
			if (++blockEntity.timeCounter >= blockEntity.cooldown) {
				blockEntity.timeCounter = 0;
				BlockEntity tempBlockEntity = world.getBlockEntity(pos.down());
				if (tempBlockEntity instanceof BeaconBlockEntity) {
					BeaconBlockEntity beaconBlockEntity = (BeaconBlockEntity) tempBlockEntity;
					int level = beaconBlockEntity.toInitialChunkDataTag().getInt("Levels");
					blockEntity.cooldown = 160 - level * 20;
					// 仅当信标激活时才工作
					if (level >= 0) {
						
						// 是否正确摆放了日石和月石
						boolean sunMoonStone = level >= 4;
						if (sunMoonStone) {
							for1:
							for (int i = 0; i < 3; i++) {
								for (int j = 0; j < 3; j++) {
									Block block = world.getBlockState(pos.down(2).east(i - 1).south(j - 1)).getBlock();
									if (sunMoonStone = (i + j & 1) == 1 ? block != CppBlocks.SUN_STONE : block != CppBlocks.MOON_STONE)
										break for1;
								}
							}
						}
						try {
							// 直接用目标选择器来选取玩家和生物
							List<ServerPlayerEntity> players = new EntitySelectorReader(new StringReader("@a" + (sunMoonStone ? "" : String.format("[distance=..%d]", 10 * (level + 1))))).read().getPlayers(blockEntity.getServerCommandSource());
							List<? extends Entity> entities0 = new EntitySelectorReader(new StringReader(String.format("@e[distance=..%d]", sunMoonStone ? 128 : 10 * (level + 1)))).read().getEntities(blockEntity.getServerCommandSource());
							List<MobEntity> entities = new LinkedList<MobEntity>();
							// 把entities里的非生物实体和玩家去除
							for (Iterator<? extends Entity> iterator = entities0.iterator(); iterator.hasNext(); ) {
								Entity e = iterator.next();
								if (e instanceof MobEntity) {
									if (!blockEntity.onlyAdverse || !(e instanceof GolemEntity && !(e instanceof ShulkerEntity) || (e instanceof PassiveEntity && !(e instanceof HoglinEntity)) || e instanceof AmbientEntity))
										entities.add((MobEntity) e);
								}
							}
							// 施加状态效果
							for (PlayerEntity e : players) {
								
								if (blockEntity.playerEffect == StatusEffects.NIGHT_VISION) {
									e.addStatusEffect(new StatusEffectInstance(blockEntity.playerEffect, 400, 253, true, true));
								} else {
									int duratioin;
									if (blockEntity.playerEffect == StatusEffects.SATURATION)
										duratioin = 1;
									else
										duratioin = 200;
									e.addStatusEffect(new StatusEffectInstance(blockEntity.playerEffect, duratioin, 0, true, true));
								}
							}
							for (MobEntity e : entities) {
								if (blockEntity.getMobEffect() != ATTRACTING)
									e.addStatusEffect(new StatusEffectInstance(blockEntity.mobEffect, 400, 0, true, true));
								else {
									e.teleport(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);
								}
							}
						} catch (Throwable e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
	
	@Override
	public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
		return new BeaconEnhancerScreenHandler(syncId, inv, this);
	}
	
	public Text getDisplayName() {
		return new TranslatableText(getCachedState().getBlock().getTranslationKey());
	}
	
	/**
	 * 获取当前施加于玩家的状态效果
	 *
	 * @return 施加于玩家的状态效果
	 */
	public StatusEffect getPlayerEffect() {
		return playerEffect;
	}
	
	/**
	 * 切换施加于玩家的状态效果
	 */
	public void shiftPlayerEffect() {
		propertyDelegate.set(0, playerEffectCode + 1);
	}
	
	/**
	 * 获取当前施加于生物的状态效果
	 *
	 * @return 施加于生物的状态效果
	 */
	public StatusEffect getMobEffect() {
		return mobEffect;
	}
	
	/**
	 * 切换施加于生物的状态效果
	 */
	public void shiftMobEffect() {
		propertyDelegate.set(1, mobEffectCode + 1);
	}
	
	/**
	 * 查看当前施加于生物的状态效果是否仅仅施加于敌对生物
	 *
	 * @return 施加于生物的状态效果仅仅施加于敌对生物
	 */
	public boolean isOnlyAdverse() {
		return onlyAdverse;
	}
	
	/**
	 * 切换施加于生物的状态效果是否仅仅施加于敌对生物
	 */
	public void shiftOnlyAdverse() {
		propertyDelegate.set(2, onlyAdverse ? 0 : 1);
	}
	
	public static void tickEffect(PlayerEntity player) {
		CodingTool.removeEffectExceptHidden(player, NIGHT_VISION, 253, 199);
	}
}
