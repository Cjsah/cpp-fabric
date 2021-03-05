package net.cpp.mixin;

import net.minecraft.entity.SpawnReason;
import org.spongepowered.asm.mixin.Mixin;

import net.cpp.entity.DarkCowEntity;
import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(CowEntity.class)
public abstract class MixinCowEntity extends AnimalEntity {
	protected MixinCowEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!this.world.isClient && this.world.getTime() % 24000 == 13245 && this.world.getLightLevel(this.getBlockPos()) <= 7 && this.getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) this.world).random(this.world.random).build(LootContextTypes.EMPTY))) {
			DarkCowEntity darkCow = CppEntities.DARK_COW.create(this.world);
			darkCow.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
			darkCow.setVelocity(this.getVelocity());
			darkCow.initialize(((ServerWorld)this.world), this.world.getLocalDifficulty(darkCow.getBlockPos()), SpawnReason.CONVERSION, null, null);
			((ServerWorld)this.world).shouldCreateNewEntityWithPassenger(darkCow);
			this.discard();
		}
	}
}
