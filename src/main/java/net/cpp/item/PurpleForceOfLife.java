package net.cpp.item;

import net.cpp.init.CppItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static net.cpp.api.CodingTool.rayItem;

public class PurpleForceOfLife extends Item {
    public static final Map<Item, Item> EXCHANGE = new HashMap<>();

    public PurpleForceOfLife(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            ItemEntity itemEntity = rayItem(user);
            if (itemEntity != null && EXCHANGE.get(itemEntity.getStack().getItem()) != null && itemEntity.getStack().getCount() == 2) {
                ItemEntity spawnItem = new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(EXCHANGE.get(itemEntity.getStack().getItem())));
                spawnItem.setToDefaultPickupDelay();
                itemEntity.kill();
                world.spawnEntity(spawnItem);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }
        }
        return TypedActionResult.pass(item);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (!user.world.isClient) {
            System.out.println(stack);
            System.out.println(entity);
        }
        return ActionResult.PASS;
    }
    static {
        EXCHANGE.put(Items.OAK_SAPLING, Items.SPRUCE_SAPLING);
        EXCHANGE.put(Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING);
        EXCHANGE.put(Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING);
        EXCHANGE.put(Items.JUNGLE_SAPLING, Items.ACACIA_SAPLING);
        EXCHANGE.put(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING);
        EXCHANGE.put(Items.DARK_OAK_SAPLING, Items.OAK_SAPLING);

        EXCHANGE.put(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
        EXCHANGE.put(Items.BEETROOT_SEEDS, Items.CARROT);
        EXCHANGE.put(Items.CARROT, Items.POTATO);
        EXCHANGE.put(Items.POTATO, Items.PUMPKIN_SEEDS);
        EXCHANGE.put(Items.PUMPKIN_SEEDS, Items.MELON_SEEDS);
        EXCHANGE.put(Items.MELON_SEEDS, Items.SUGAR_CANE);
        EXCHANGE.put(Items.SUGAR_CANE, Items.CACTUS);
        EXCHANGE.put(Items.CACTUS, Items.COCOA_BEANS);
        EXCHANGE.put(Items.COCOA_BEANS, Items.BAMBOO);
        EXCHANGE.put(Items.BAMBOO, Items.SWEET_BERRIES);
        EXCHANGE.put(Items.SWEET_BERRIES, Items.WHEAT_SEEDS);

        EXCHANGE.put(Items.DANDELION, Items.POPPY);
        EXCHANGE.put(Items.POPPY, Items.AZURE_BLUET);
        EXCHANGE.put(Items.AZURE_BLUET, Items.BLUE_ORCHID);
        EXCHANGE.put(Items.BLUE_ORCHID, Items.ALLIUM);
        EXCHANGE.put(Items.ALLIUM, Items.OXEYE_DAISY);
        EXCHANGE.put(Items.OXEYE_DAISY, Items.RED_TULIP);
        EXCHANGE.put(Items.RED_TULIP, Items.ORANGE_TULIP);
        EXCHANGE.put(Items.ORANGE_TULIP, Items.WHITE_TULIP);
        EXCHANGE.put(Items.WHITE_TULIP, Items.PINK_TULIP);
        EXCHANGE.put(Items.PINK_TULIP, Items.CORNFLOWER);
        EXCHANGE.put(Items.CORNFLOWER, Items.LILY_OF_THE_VALLEY);
        EXCHANGE.put(Items.LILY_OF_THE_VALLEY, Items.ROSE_BUSH);
        EXCHANGE.put(Items.ROSE_BUSH, Items.SUNFLOWER);
        EXCHANGE.put(Items.SUNFLOWER, Items.LILAC);
        EXCHANGE.put(Items.LILAC, Items.PEONY);
        EXCHANGE.put(Items.PEONY, Items.DANDELION);

        EXCHANGE.put(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
        EXCHANGE.put(Items.RED_MUSHROOM, Items.CRIMSON_FUNGUS);
        EXCHANGE.put(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS);
        EXCHANGE.put(Items.WARPED_FUNGUS, Items.BROWN_MUSHROOM);
    }
}
