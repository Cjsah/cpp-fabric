package net.cpp.mixin;

import net.cpp.entity.DarkPigEntity;
import net.minecraft.entity.SpawnReason;
import org.spongepowered.asm.mixin.Mixin;

import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(PigEntity.class)
public abstract class MixinPigEntity extends AnimalEntity {
	protected MixinPigEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			DarkPigEntity darkPig = CppEntities.DARK_PIG.create(world);
			darkPig.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
			darkPig.setVelocity(this.getVelocity());
			darkPig.initialize(((ServerWorld)this.world), this.world.getLocalDifficulty(darkPig.getBlockPos()), SpawnReason.CONVERSION, null, null);
			((ServerWorld)this.world).shouldCreateNewEntityWithPassenger(darkPig);
			this.discard();
		}
	}
}
