package cpp.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

@Mixin(CreeperEntity.class)
@SuppressWarnings("unused")
public abstract class MixinCreeperEntity extends HostileEntity {

	protected MixinCreeperEntity(EntityType<? extends HostileEntity> entityType, World world) {
		super(entityType, world);
	}

	@Shadow
	private static TrackedData<Boolean> CHARGED;

	@Shadow
	private int fuseTime;

	@SuppressWarnings("ConstantConditions")
	@Inject(at = @At("RETURN"), method = { "<init>" })
	public void init(CallbackInfo info) {
		if (!world.isClient) {
			fuseTime = 10;
			if (getServer().getPredicateManager().get(new Identifier("cpp:enchance_creeper")).test(new LootContext.Builder((ServerWorld) world).random(world.random).build(LootContextTypes.EMPTY))) {
				dataTracker.set(CHARGED, true);
			}
		}
	}

}
