package cpp.mixin;

import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpp.ducktyping.IMultiple;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@Mixin(ExperienceBottleEntity.class)
@SuppressWarnings("unused")
public abstract class MixinExperienceBottleEntity extends ThrownItemEntity implements IMultiple {
	private int multiple = 0;

	public MixinExperienceBottleEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
		super(entityType, d, e, f, world);
	}

//	@Inject(at = { @At("HEAD") }, method = { "fromTag(Lnet/minecraft/nbt/CompoundTag;)V" })
	public void readNbt(NbtCompound nbt) {
		multiple = nbt.getInt("multiple");
		super.readNbt(nbt);
	}

//	@Inject(at = { @At("RETURN") }, method = { "toTag(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;" })
	public NbtCompound writeNbt(NbtCompound nbt) {
		nbt.putInt("multiple", multiple);
		return super.writeNbt(nbt);
	}

	/**
	 * @author Phoupraw
	 * @reason null
	 */
	@Overwrite
	public void onCollision(HitResult hitResult) {
		super.onCollision(hitResult);
		if (this.world instanceof ServerWorld) {
			this.world.syncWorldEvent(2002, this.getBlockPos(), PotionUtil.getColor(Potions.WATER));
			long amount = 9L << (multiple * 6);
			while (amount > 0x7fff) {
				world.spawnEntity(new ExperienceOrbEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 0x7fff));
				amount -= 0x7fff;
			}
			world.spawnEntity(new ExperienceOrbEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, (int) amount));
			this.discard();
		}
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public int getMultiple() {
		return multiple;
	}
}
