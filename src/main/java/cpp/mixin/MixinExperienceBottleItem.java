package cpp.mixin;

import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpp.api.IDefaultNbtItem;
import cpp.ducktyping.IMultiple;
import cpp.item.CompressedItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@Mixin(ExperienceBottleItem.class)
@SuppressWarnings("unused")
public class MixinExperienceBottleItem extends Item implements IDefaultNbtItem {

	public MixinExperienceBottleItem(Settings settings) {
		super(settings);
	}

	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		int multiple = stack.getOrCreateTag().getByte("multiple");
		if (multiple != 0)
			tooltip.add(new TranslatableText("tooltip.cpp.multiple", multiple).formatted(Formatting.DARK_AQUA));
	}

	/**
	 * @author Phoupraw
	 * @reason null
	 */
	@Overwrite
	@SuppressWarnings("ConstantConditions")
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		int multiple = itemStack.getOrCreateTag().getByte("multiple");
		if (multiple <= 1) {
			world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_EXPERIENCE_BOTTLE_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
			if (!world.isClient) {
				ExperienceBottleEntity experienceBottleEntity = new ExperienceBottleEntity(world, user);
				experienceBottleEntity.setItem(itemStack);
				experienceBottleEntity.setProperties(user, user.getPitch(), user.getYaw(), -20.0F, 0.7F, 1.0F);
				((IMultiple) experienceBottleEntity).setMultiple(multiple);
				world.spawnEntity(experienceBottleEntity);
			}

			if (!user.getAbilities().creativeMode) {
				itemStack.decrement(1);
			}
		} else {
			CompressedItem.uncompressAndGive(user, itemStack);
		}
		user.incrementStat(Stats.USED.getOrCreateStat(this));

		return TypedActionResult.success(itemStack, world.isClient());
	}

	@Override
	public Text getName(ItemStack stack) {
		int multiple = stack.getOrCreateTag().getByte("multiple");
		return multiple == 0 ? super.getName(stack) : new TranslatableText("tooltip.cpp.compressed").formatted(Formatting.DARK_AQUA).append(((MutableText)super.getName(stack)).formatted(stack.getRarity().formatting));
	}
	
	@Override
	public NbtCompound modifyDefaultNbt(NbtCompound nbt) {
		nbt.putByte("multiple", (byte) 0);
		return nbt;
	}
}
