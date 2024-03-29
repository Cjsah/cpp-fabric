package cpp.item;

import cpp.api.Utils;
import cpp.ducktyping.IPlayerVaccine;
import cpp.vaccine.VaccineInstance;
import cpp.vaccine.Vaccines;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class VaccineItem extends Item {
    private final Vaccines vaccine;

    public VaccineItem(Vaccines vaccine, Settings settings) {
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
        tooltip.add(new TranslatableText("tooltip.cpp.vaccine", new TranslatableText("effect." + this.vaccine.getName().replaceAll(":", ".")).formatted(Formatting.DARK_PURPLE), Utils.ticksToTime(vaccine.getDuration())).formatted(Formatting.BLUE));
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
    public int getMaxUseTime(ItemStack stack) {
        return 8;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public static Collection<StatusEffect> getVaccineEffects(LivingEntity user) {
        Collection<StatusEffect> list = new ArrayList<>();
        if (user instanceof ServerPlayerEntity) {
            NbtCompound playerNbt = new NbtCompound();
            user.writeCustomDataToNbt(playerNbt);
            for (NbtElement vaccine : playerNbt.getList("Vaccines", 10)) {
                StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier((Objects.requireNonNull(Vaccines.byRawId(((NbtCompound) vaccine).getByte("Id"))).getName())));
                if (effect != null) list.add(effect);
            }
        }
        return list;
    }
}
