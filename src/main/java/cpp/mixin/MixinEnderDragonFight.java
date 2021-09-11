package cpp.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Blocks;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.EndPortalFeature;

@Mixin(EnderDragonFight.class)
public abstract class MixinEnderDragonFight {
	@Shadow
	private boolean previouslyKilled;
	@Final
	@Shadow
	private ServerWorld world;

	@Inject(at = @At(value = "INVOKE", target = "net/minecraft/entity/boss/dragon/EnderDragonFight.generateNewEndGateway()V", shift = Shift.AFTER), method = { "dragonKilled" })
	public void enhance(EnderDragonEntity dragon, CallbackInfo info) {
		if (this.previouslyKilled) {
			this.world.setBlockState(this.world.getTopPosition(Heightmap.Type.MOTION_BLOCKING, EndPortalFeature.ORIGIN), Blocks.DRAGON_EGG.getDefaultState());
		}
	}

}
