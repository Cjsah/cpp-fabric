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
import java.util.List;

import net.cpp.init.CppBlockEntities;
import net.cpp.init.CppEffects;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.Tickable;

public class BeaconEnhancerBlockEntity extends BlockEntity implements Tickable {
	public static final List<StatusEffect> AVAILABLE_PLAYER_EFFECTS = Collections.unmodifiableList(
			Arrays.asList(FIRE_RESISTANCE, NIGHT_VISION, WATER_BREATHING, INVISIBILITY, SATURATION, CppEffects.CHAIN));
	public static final List<StatusEffect> AVAILABLE_MOB_EFFECTS = Collections.unmodifiableList(
			Arrays.asList(WEAKNESS,SLOWNESS,GLOWING,POISON,WITHER));
	private StatusEffect playerEffect = StatusEffects.ABSORPTION;
	private StatusEffect mobEffect = StatusEffects.ABSORPTION;
	private int playerEffectCode;
	private int mobEffectCode ;

	public BeaconEnhancerBlockEntity() {
		super(CppBlockEntities.BEACON_ENHANCER);
	}
	@Override
	public void tick() {
		if (!world.isClient) {
			EntitySelector e;
		}
		
	}
}
