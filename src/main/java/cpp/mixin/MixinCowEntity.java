package cpp.mixin;

import cpp.api.Utils;
import org.spongepowered.asm.mixin.Mixin;

import cpp.init.CppEntities;
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

	@SuppressWarnings("ConstantConditions")
	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == CowEntity.class) Utils.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_COW, true, null);
	}
}
