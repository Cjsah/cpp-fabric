package cpp.mixin;

import cpp.item.CompressedItem;
import cpp.misc.ExperienceBottleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpp.ducktyping.IMultiple;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

@Mixin(ExperienceBottleEntity.class)
public abstract class MixinExperienceBottleEntity extends ThrownItemEntity implements IMultiple {
@Deprecated
private int multiple = 0;

public MixinExperienceBottleEntity(EntityType<? extends ThrownItemEntity> entityType, double d, double e, double f, World world) {
	super(entityType, d, e, f, world);
}

public void readNbt(NbtCompound nbt) {
	multiple = nbt.getInt("multiple");
	super.readNbt(nbt);
}

public NbtCompound writeNbt(NbtCompound nbt) {
	nbt.putInt("multiple", multiple);
	return super.writeNbt(nbt);
}

/**
 * 附魔之瓶存储的经验值修改为固定的9点。扔出由压缩器压缩过的附魔之瓶的效果如下：
 * <p>
 * 未压缩的附魔之瓶产生一个9点的经验球；
 * <p>
 * 压缩重数为1的附魔之瓶产生两个共576点的经验球；
 * <p>
 * 压缩重数为2的附魔之瓶产生四个共36864点的经验球；
 * <p>
 * 压缩重数为3的附魔之瓶产生四个共36864点的经验球，并给予最近的玩家2322432点经验值；
 * <p>
 * 压缩重数为4的附魔之瓶产生四个共36864点的经验球，并给予最近的玩家150958080点经验值；
 * <p>
 * 压缩重数为5的附魔之瓶产生四个共36864点的经验球，并给予最近的玩家9663639552点经验值；
 * <p>
 * 压缩重数为6的附魔之瓶产生四个共36864点的经验球，给予最近的玩家9666761492点经验值，并增加最近的玩家367837级；
 * <p>
 * 压缩重数为7的附魔之瓶产生四个共36864点的经验球，给予最近的玩家9670933166点经验值，并增加最近的玩家3310414级；
 * <p>
 * 压缩重数为8或更高的附魔之瓶产生四个共36864点的经验球，给予最近的玩家9855576089点经验值，并增加最近的玩家26850904级。
 *
 * @author Phoupraw
 * @reason 压缩附魔之瓶
 */
@Overwrite
public void onCollision(HitResult hitResult) {
	super.onCollision(hitResult);
	if (this.world instanceof ServerWorld) {
		this.world.syncWorldEvent(2002, this.getBlockPos(), PotionUtil.getColor(Potions.WATER));
//		int amount = 9;
		int multiple = CompressedItem.getMultiple(getStack());
		switch (multiple) {
			case 0 -> ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 9, 1);
			case 1 -> ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 576, 2);
			case 2 -> ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
			case 3 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 0, 2322432);
			}
			case 4 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 0, 150958080);
			}
			case 5 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 0, 9663639552L);
			}
			case 6 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 367837, 9666761492L);
			}
			case 7 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 3310414, 9670933166L);
			}
			case 8 -> {
				ExperienceBottleHooks.spawnOrbs(world, hitResult.getPos(), 36864, 4);
				ExperienceBottleHooks.giveCloset(world, hitResult.getPos(), 26850904, 9855576089L);
			}
		}
//		long amount = 9L << (multiple * 6);
//		while (amount > 0x7fff) {
//			world.spawnEntity(new ExperienceOrbEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, 0x7fff));
//			amount -= 0x7fff;
//		}
//		world.spawnEntity(new ExperienceOrbEntity(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, amount));
		this.discard();
	}
}

public void setMultiple(int multiple) {
	CompressedItem.setMultiple(getStack(), multiple);
}

public int getMultiple() {
	return CompressedItem.getMultiple(getStack());
}
}
