package cpp.mixin;

import java.util.List;

import cpp.misc.ExperienceBottleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import cpp.api.IDefaultNbtItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ExperienceBottleItem.class)
@SuppressWarnings("unused")
public abstract class MixinExperienceBottleItem extends Item implements IDefaultNbtItem {
	
	@Shadow
	public abstract boolean hasGlint(ItemStack stack);
	
	public MixinExperienceBottleItem(Settings settings) {
		super(settings);
	}
	
	@Environment(EnvType.CLIENT)
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		int multiple = stack.getOrCreateTag().getByte(ExperienceBottleHooks.MULTIPLE_TAG_NAME);
		if (multiple != 0)
			tooltip.add(new TranslatableText("tooltip.cpp.multiple", multiple).formatted(Formatting.DARK_AQUA));
	}
	
	/**
	 * @author Phoupraw
	 * @reason null
	 */
	@Overwrite
	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		return ExperienceBottleHooks.useItem(world, user, hand);
	}
	
	@Override
	public Text getName(ItemStack stack) {
		int multiple = stack.getOrCreateTag().getByte(ExperienceBottleHooks.MULTIPLE_TAG_NAME);
		return multiple == 0 ? super.getName(stack) : new TranslatableText("tooltip.cpp.compressed").formatted(Formatting.DARK_AQUA)
		  .append(((MutableText) super.getName(stack))
			.formatted(stack.getRarity().formatting));
	}
	
	@Override
	public NbtCompound modifyDefaultNbt(NbtCompound nbt) {
		nbt.putByte(ExperienceBottleHooks.MULTIPLE_TAG_NAME, (byte) 0);
		return nbt;
	}
}
