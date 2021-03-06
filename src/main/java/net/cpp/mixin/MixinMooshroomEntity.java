package net.cpp.mixin;

import net.cpp.Craftingpp;
import net.cpp.api.CodingTool;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.PassiveEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import net.cpp.entity.DarkMooshroomEntity;
import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(MooshroomEntity.class)
public abstract class MixinMooshroomEntity extends CowEntity {
	public MixinMooshroomEntity(EntityType<? extends CowEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	public abstract MooshroomEntity.Type getMooshroomType();

	@Override
	public void tick() {
		super.tick();
		if (!this.world.isClient) CodingTool.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_MOOSHROOM, true, (entity) -> ((DarkMooshroomEntity)entity).setType(this.getMooshroomType()));
	}
}
