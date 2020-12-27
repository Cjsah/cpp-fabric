package net.cpp.item;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

import static net.cpp.api.CodingTool.rayItem;

public class PurpleForceOfLife extends Item {

    private static final Map<Item, Item> exchange = new HashMap<>();

    public PurpleForceOfLife(Settings settings) {
        super(settings);
        this.register();
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            ItemEntity itemEntity = rayItem(world, user, 0.05F);
            if (itemEntity != null && exchange.get(itemEntity.getStack().getItem()) != null && itemEntity.getStack().getCount() == 2) {
                ItemEntity spawnItem = new ItemEntity(world, itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), new ItemStack(exchange.get(itemEntity.getStack().getItem())));
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

    private void register() {
        exchange.put(Items.OAK_SAPLING, Items.SPRUCE_SAPLING);
        exchange.put(Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING);
        exchange.put(Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING);
        exchange.put(Items.JUNGLE_SAPLING, Items.ACACIA_SAPLING);
        exchange.put(Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING);
        exchange.put(Items.DARK_OAK_SAPLING, Items.OAK_SAPLING);

        exchange.put(Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS);
        exchange.put(Items.BEETROOT_SEEDS, Items.CARROT);
        exchange.put(Items.CARROT, Items.POTATO);
        exchange.put(Items.POTATO, Items.PUMPKIN_SEEDS);
        exchange.put(Items.PUMPKIN_SEEDS, Items.MELON_SEEDS);
        exchange.put(Items.MELON_SEEDS, Items.SUGAR_CANE);
        exchange.put(Items.SUGAR_CANE, Items.CACTUS);
        exchange.put(Items.CACTUS, Items.COCOA_BEANS);
        exchange.put(Items.COCOA_BEANS, Items.BAMBOO);
        exchange.put(Items.BAMBOO, Items.SWEET_BERRIES);
        exchange.put(Items.SWEET_BERRIES, Items.WHEAT_SEEDS);

        exchange.put(Items.DANDELION, Items.POPPY);
        exchange.put(Items.POPPY, Items.AZURE_BLUET);
        exchange.put(Items.AZURE_BLUET, Items.BLUE_ORCHID);
        exchange.put(Items.BLUE_ORCHID, Items.ALLIUM);
        exchange.put(Items.ALLIUM, Items.OXEYE_DAISY);
        exchange.put(Items.OXEYE_DAISY, Items.RED_TULIP);
        exchange.put(Items.RED_TULIP, Items.ORANGE_TULIP);
        exchange.put(Items.ORANGE_TULIP, Items.WHITE_TULIP);
        exchange.put(Items.WHITE_TULIP, Items.PINK_TULIP);
        exchange.put(Items.PINK_TULIP, Items.CORNFLOWER);
        exchange.put(Items.CORNFLOWER, Items.LILY_OF_THE_VALLEY);
        exchange.put(Items.LILY_OF_THE_VALLEY, Items.ROSE_BUSH);
        exchange.put(Items.ROSE_BUSH, Items.SUNFLOWER);
        exchange.put(Items.SUNFLOWER, Items.LILAC);
        exchange.put(Items.LILAC, Items.PEONY);
        exchange.put(Items.PEONY, Items.DANDELION);

        exchange.put(Items.BROWN_MUSHROOM, Items.RED_MUSHROOM);
        exchange.put(Items.RED_MUSHROOM, Items.CRIMSON_FUNGUS);
        exchange.put(Items.CRIMSON_FUNGUS, Items.WARPED_FUNGUS);
        exchange.put(Items.WARPED_FUNGUS, Items.BROWN_MUSHROOM);
    }
}
