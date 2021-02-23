package net.cpp.api;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class CppVaccine extends Item {
    private final Effect vaccine;

    public CppVaccine(Effect vaccine, Settings settings) {
        super(settings);
        this.vaccine = vaccine;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof IPlayerVaccine) ((IPlayerVaccine)user).addVaccine(this.vaccine);
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 32;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return ItemUsage.consumeHeldItem(world, user, hand);
    }

    public enum Effect{
        POISON((byte) 0, 200),
        BLINDNESS((byte) 1, 200),
        MINING_FATIGUE((byte) 2, 200),
        WITHER((byte) 3, 200),
        DARKNESS((byte) 4, 864000);

        private final byte rawId;
        private final int duration;

        Effect(byte rawId, int duration){
            this.rawId = rawId;
            this.duration = duration;
        }

        public byte getRawId() {
            return rawId;
        }

        public int getDuration() {
            return this.duration;
        }
    }
}
