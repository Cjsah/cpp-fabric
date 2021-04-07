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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

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

    public static Collection<StatusEffect> getVaccineEffects(LivingEntity user) {
        Collection<StatusEffect> list = new ArrayList<>();
        if (user instanceof ServerPlayerEntity) {
            CompoundTag playerTag = new CompoundTag();
            user.writeCustomDataToTag(playerTag);
            for (Tag vaccine : playerTag.getList("Vaccines", 10)) {
                StatusEffect effect = Registry.STATUS_EFFECT.get(new Identifier((Objects.requireNonNull(Vaccines.byRawId(((CompoundTag) vaccine).getByte("Id"))).getName())));
                if (effect != null) list.add(effect);
            }
        }
        return list;
    }
}
