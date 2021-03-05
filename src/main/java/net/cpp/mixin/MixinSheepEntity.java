package net.cpp.mixin;

import net.cpp.entity.DarkSheepEntity;
import net.minecraft.entity.SpawnReason;
import org.spongepowered.asm.mixin.Mixin;

import net.cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public abstract class MixinSheepEntity extends AnimalEntity {
	protected MixinSheepEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			DarkSheepEntity darkSheep = CppEntities.DARK_SHEEP.create(world);
			darkSheep.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
			darkSheep.setVelocity(this.getVelocity());
			darkSheep.initialize(((ServerWorld)this.world), this.world.getLocalDifficulty(darkSheep.getBlockPos()), SpawnReason.CONVERSION, null, null);
			((ServerWorld)this.world).shouldCreateNewEntityWithPassenger(darkSheep);
			this.discard();
		}
	}
}
