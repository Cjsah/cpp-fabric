package net.cpp.mixin;

import net.cpp.Craftingpp;
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
		if (!this.world.isClient && this.world.getTime() % 24000 == 13245 && world.getLightLevel(this.getBlockPos()) <= 7 && this.getServer().getPredicateManager().get(new Identifier(Craftingpp.MOD_ID3, "dark_animal")).test(new LootContext.Builder((ServerWorld) this.world).random(this.world.random).build(LootContextTypes.EMPTY))) {
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
