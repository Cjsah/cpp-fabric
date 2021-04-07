package cpp.mixin;

import cpp.api.Utils;
import org.spongepowered.asm.mixin.Mixin;

import cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(ChickenEntity.class)
@SuppressWarnings("unused")
public abstract class MixinChickenEntity extends AnimalEntity {
	protected MixinChickenEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@SuppressWarnings("ConstantConditions")
	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == ChickenEntity.class) Utils.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_CHICKEN, true, null);
	}
}
