package net.cpp.mixin;

import net.cpp.api.CodingTool;
import org.spongepowered.asm.mixin.Mixin;

import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(PigEntity.class)
public abstract class MixinPigEntity extends AnimalEntity {
	protected MixinPigEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == PigEntity.class) CodingTool.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_PIG, true, null);
	}
}
