package net.cpp.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public enum CppArmorMaterial implements ArmorMaterial {
    RED_LIP("red_lip"),
    PURPLE_EYE("purple_eye"),
    LASH("lash"),
    CAT_BREAD("cat_breed"),
    GARLAND("garland"),
    BLACK_FRAMED_GLASSES("black_framed_glasses"),
    ORANGE_FRAMED_GLASSES("orange_framed_glasses"),
    JOKING_GLASSES("joking_glasses"),
    MINION_GOGGLES("minion_goggles"),
    PANTS("pants"),
    EMPIRE_HAT("empire_hat"),
    GLASS_HELMET("glass_helmet"),
    GLOW_HAT("glow_hat"),
    GREEN_HAT("green_hat"),
    BLACK_HAT("black_hat"),
    NURSE_HAT("nurse_hat");

    private final String name;

    CppArmorMaterial(String name) {
        this.name = name;
    }

    @Override
    public int getDurability(EquipmentSlot slot) {
        return 0;
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return 0;
    }

    @Override
    public int getEnchantability() {
        return 0;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_LEATHER;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.EMPTY;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getName() {
        return this.name;
    }

    @Override
    public float getToughness() {
        return 0.0F;
    }

    @Override
    public float getKnockbackResistance() {
        return 0.0F;
    }
}
