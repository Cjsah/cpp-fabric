package cpp.mixin;

import cpp.api.Utils;
import org.spongepowered.asm.mixin.Mixin;

import cpp.init.CppEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

@Mixin(SheepEntity.class)
public abstract class MixinSheepEntity extends AnimalEntity {
	protected MixinSheepEntity(EntityType<? extends AnimalEntity> entityType, World world) {
		super(entityType, world);
	}

	@SuppressWarnings("ConstantConditions")
	public void tick() {
		super.tick();
		if (!this.world.isClient && ((Object)this.getClass()) == SheepEntity.class) Utils.darkTransform((ServerWorld) this.world, this, CppEntities.DARK_SHEEP, true, null);
	}
}
