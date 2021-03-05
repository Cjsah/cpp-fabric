package net.cpp.mixin;

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

	@Shadow
	public abstract MooshroomEntity createChild(ServerWorld serverWorld, PassiveEntity passiveEntity);

	@Override
	public void tick() {
		super.tick();
		if (!world.isClient && world.getTime() % 100 == 0 && world.getLightLevel(getBlockPos()) <= 7 && getServer().getPredicateManager().get(new Identifier("cpp:dark_animal")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
			DarkMooshroomEntity darkMooshroom = CppEntities.DARK_MOOSHROOM.create(world);
			darkMooshroom.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
			darkMooshroom.setVelocity(this.getVelocity());
			darkMooshroom.setType(this.getMooshroomType());
			darkMooshroom.initialize(((ServerWorld)this.world), this.world.getLocalDifficulty(darkMooshroom.getBlockPos()), SpawnReason.CONVERSION, null, null);
			((ServerWorld)this.world).shouldCreateNewEntityWithPassenger(darkMooshroom);
			this.discard();

		}
	}
}
