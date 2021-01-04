package net.cpp.item;

import java.util.List;
import java.util.Objects;

import com.google.gson.JsonObject;
import net.cpp.api.ICppConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidFillable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class GreenForceOfWater extends Item implements IDefaultTagItem, ICppConfig {

    private static JsonObject json;

    public GreenForceOfWater(Settings settings) {
        super(settings);
        json = this.getConfig();
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = stack.getOrCreateTag();
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.water"), new TranslatableText("word.infinite")).formatted(Formatting.GREEN));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.lava"), tag.getInt("lava")).formatted(Formatting.RED));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            CompoundTag tag = itemStack.getOrCreateTag();
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            BlockPos blockPos = hitResult.getBlockPos();
            BlockState blockState = world.getBlockState(blockPos);


            // 收水/岩浆
            if (!user.isSneaking()) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    Direction direction = hitResult.getSide();
                    if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos.offset(direction), direction, itemStack)) {
                        if (blockState.getBlock() == Blocks.WATER_CAULDRON && ((LeveledCauldronBlock)blockState.getBlock()).isFull(blockState)) {
                            world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                            user.incrementStat(Stats.USED.getOrCreateStat(this));
                            return this.changeTag(user, itemStack, Fluids.WATER, tag);
                        }else if (blockState.getBlock() == Blocks.LAVA_CAULDRON) {
                            world.setBlockState(blockPos, Blocks.CAULDRON.getDefaultState());
                            user.incrementStat(Stats.USED.getOrCreateStat(this));
                            return this.changeTag(user, itemStack, Fluids.LAVA, tag);
                        }else if (blockState.getBlock() instanceof FluidDrainable) {
                            FluidDrainable fluidDrainable = (FluidDrainable) blockState.getBlock();
                            ItemStack itemStack2 = fluidDrainable.tryDrainFluid(world, blockPos, blockState);
                            if (!itemStack2.isEmpty()) {
                                fluidDrainable.getDrainSound().ifPresent((sound) -> ((ServerPlayerEntity)user).networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.PLAYERS, user.getX(), user.getY(), user.getZ(), 1.0F, 1.0F)));
                                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);
                                Fluid fluid = itemStack2.getItem() == Items.WATER_BUCKET ? Fluids.WATER : Fluids.LAVA;
                                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) user, itemStack2);
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                return this.changeTag(user, itemStack, fluid, tag);

                            }
                        }
                    }
                }
                return TypedActionResult.pass(itemStack);

            // 切换模式
            } else if (hitResult.getType() == HitResult.Type.MISS) {
                boolean waterMode = Objects.equals(tag.getString("mode"), "water");
                tag.putString("mode", waterMode ? "lava" : "water");
                ((ServerPlayerEntity)user).networkHandler.sendPacket(new TitleS2CPacket(TitleS2CPacket.Action.ACTIONBAR,
                        new TranslatableText("chat.cpp.change", new TranslatableText("block.minecraft." + tag.getString("mode")).formatted(waterMode ? Formatting.RED : Formatting.GREEN)).formatted(Formatting.GOLD)
                ));
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack);

            // 放水/岩浆
            } else {
                Fluid fluid = Objects.equals(tag.getString("mode"), "water") ? Fluids.WATER : Fluids.LAVA;
                BlockPos blockPos3 = blockState.getBlock() instanceof FluidFillable && fluid == Fluids.WATER ? blockPos : blockPos.offset(hitResult.getSide());

                if ((fluid == Fluids.LAVA && (user.isCreative() || tag.getInt("lava") > 0)) || fluid == Fluids.WATER) {
                    if (blockState.getBlock() == Blocks.CAULDRON) {
                        world.setBlockState(blockPos, fluid == Fluids.WATER ? Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3) : Blocks.LAVA_CAULDRON.getDefaultState());
                        if (fluid == Fluids.LAVA && !user.isCreative()) {
                            tag.putInt("lava", tag.getInt("lava") - 1);
                        }
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        return TypedActionResult.success(itemStack);
                    }else if (blockState.getBlock() == Blocks.WATER_CAULDRON && !((LeveledCauldronBlock)blockState.getBlock()).isFull(blockState)){
                        world.setBlockState(blockPos, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3));
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        return TypedActionResult.success(itemStack);
//                    }else if (!(blockState.getBlock() instanceof FluidBlock) && this.placeFluid(user, world, blockPos3, hitResult, fluid)) {
                    }else if (!(blockState.getBlock() instanceof FluidBlock) && world.setBlockState(blockPos3, fluid.getDefaultState().getBlockState())) {
                        if (user instanceof ServerPlayerEntity) {
                            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, blockPos3, itemStack);
                        }
                        ((ServerPlayerEntity)user).networkHandler.sendPacket(new PlaySoundS2CPacket(fluid == Fluids.WATER ? SoundEvents.ITEM_BUCKET_EMPTY : SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, user.getX(), user.getY(), user.getZ(), 1.0F, 1.0F));
                        if (fluid == Fluids.LAVA && !user.isCreative()) {
                            tag.putInt("lava", tag.getInt("lava") - 1);
                        }
                        user.incrementStat(Stats.USED.getOrCreateStat(this));
                        return TypedActionResult.success(itemStack);
                    }
                }
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    private TypedActionResult<ItemStack> changeTag(PlayerEntity player, ItemStack item, Fluid fluid, CompoundTag tag) {
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        if (fluid == Fluids.LAVA) {
            tag.putInt("lava", tag.getInt("lava") + 1);
        }
        return TypedActionResult.success(item);
    }

	@Override
	public CompoundTag modifyDefaultTag(CompoundTag tag) {
		tag.putString("mode", "water");
        tag.putInt("lava", 0);
        return tag;
	}

    @Override
    public String getConfigName() {
        return "green_force_of_water";
    }

    @Override
    public JsonObject defaultConfig(JsonObject json) {
        json.addProperty("StartLevel", 2);
        return json;
    }
}
