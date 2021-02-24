package net.cpp.item;

import net.cpp.api.CodingTool;
import net.cpp.api.IPlayerVaccine;
import net.cpp.vaccine.VaccineInstance;
import net.cpp.vaccine.Vaccines;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Vaccine extends Item {
    private final Vaccines vaccine;

    public Vaccine(Vaccines vaccine, Settings settings) {
        super(settings);
        this.vaccine = vaccine;
    }

    @Override
    public String getTranslationKey() {
        return "item.cpp.vaccine";
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("tooltip.cpp.vaccine", new TranslatableText("effect." + this.vaccine.getName().replaceAll(":", ".")).formatted(Formatting.DARK_PURPLE), CodingTool.ticksToTime(vaccine.getDuration())).formatted(Formatting.BLUE));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof IPlayerVaccine) ((IPlayerVaccine)user).addVaccine(new VaccineInstance(this.vaccine));
        return stack;
    }

    @Override
    public boolean hasGlint(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }
}
