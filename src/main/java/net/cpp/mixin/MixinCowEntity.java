package net.cpp.mixin;

import net.cpp.api.CodingTool;
import org.spongepowered.asm.mixin.Mixin;

import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(CowEntity.class)
public abstract class MixinCowEntity extends AnimalEntity {
	protected MixinCowEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == CowEntity.class) CodingTool.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_COW, true, null);
	}
}
