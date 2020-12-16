package net.cpp.item;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.*;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import static net.cpp.api.CppChat.say;

public class GreenForceOfWater extends Item {

    public GreenForceOfWater(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        // MJSB
//        tooltip.set(0, tooltip.get(0).shallowCopy().formatted(Formatting.GOLD));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.water"), new TranslatableText("word.infinite")).formatted(Formatting.GREEN));
        int lavaCount = stack.getTag() == null ? 0 : stack.getTag().getInt("lava");
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.lava"), lavaCount).formatted(Formatting.RED));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            ItemStack itemStack = user.getStackInHand(hand);
            CompoundTag tag = itemStack.getTag();
            if (tag == null) {
                itemStack.getOrCreateTag();
                tag = itemStack.getTag();
                tag.putString("mode", "water");
                tag.putInt("lava", 0);
                itemStack.setTag(tag);
            }
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);

            // 收水/岩浆
            if (!user.isSneaking()) {
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = hitResult.getBlockPos();
                    Direction direction = hitResult.getSide();
                    if (world.canPlayerModifyAt(user, blockPos) && user.canPlaceOn(blockPos.offset(direction), direction, itemStack)) {
                        BlockState blockState;
                        blockState = world.getBlockState(blockPos);
                        if (blockState.getBlock() instanceof FluidDrainable) {
                            FluidDrainable fluidDrainable = (FluidDrainable)blockState.getBlock();
                            ItemStack itemStack2 = fluidDrainable.tryDrainFluid(world, blockPos, blockState);
                            if (!itemStack2.isEmpty()) {
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                fluidDrainable.getDrainSound().ifPresent((sound) -> {
                                    user.playSound(sound, 1.0F, 1.0F);
                                });
                                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, blockPos);





                                ItemStack itemStack3 = ItemUsage.exchangeStack(itemStack, user, itemStack2);
                                System.out.println(itemStack);
                                System.out.println(itemStack2);
                                System.out.println(itemStack3);
                                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity)user, itemStack2);

                                return TypedActionResult.success(itemStack3, world.isClient());
                            }
                        }


//                        BlockState blockState;
//                        blockState = world.getBlockState(blockPos);
//                        if (blockState.getBlock() instanceof FluidDrainable) {
//                            FluidDrainable fluidDrainable = (FluidDrainable)blockState.getBlock();
//                            ItemStack itemStack2 = ((FluidDrainable)blockState.getBlock()).tryDrainFluid(world, blockPos, blockState);
//                            if (fluid != Fluids.EMPTY) {
//                                user.incrementStat(Stats.USED.getOrCreateStat(this));
//                                user.playSound(fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_FILL_LAVA : SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
//
//                                if (fluid == Fluids.LAVA) {
//                                    tag.putInt("lava", tag.getInt("lava") + 1);
//                                    itemStack.setTag(tag);
//                                }
//
//                                return TypedActionResult.success(itemStack);
//                            }
//                        }
                    }
                }
                return TypedActionResult.fail(itemStack);

            // 切换模式
            }else if (hitResult.getType() == HitResult.Type.MISS){
                if (Objects.equals(tag.getString("mode"), "water")) {
                    tag.putString("mode", "lava");
                    say(user, new TranslatableText("chat.cpp.gfow.change", new TranslatableText("block.minecraft.lava").formatted(Formatting.RED)));
                }else {
                    tag.putString("mode", "water");
                    say(user, new TranslatableText("chat.cpp.gfow.change", new TranslatableText("block.minecraft.water").formatted(Formatting.GREEN)));
                }
                itemStack.setTag(tag);
                return TypedActionResult.success(itemStack);

            // 放水/岩浆
            }else {
                BlockPos blockPos = hitResult.getBlockPos();
                Block targetBlock = world.getBlockState(blockPos).getBlock();
                BlockPos targetBlockPos = targetBlock instanceof FluidFillable && Objects.equals(tag.getString("mode"), "water") ? blockPos : blockPos.offset(hitResult.getSide());
                Fluid fluid = Objects.equals(tag.getString("mode"), "water") ? Fluids.WATER : Fluids.LAVA;
                if ((fluid == Fluids.LAVA && tag.getInt("lava") > 0) || fluid == Fluids.WATER) {

                    if (fluid == Fluids.WATER && targetBlock == Blocks.CAULDRON) {
                        world.setBlockState(blockPos, world.getBlockState(blockPos).with(Properties.LEVEL_3, MathHelper.clamp(3, 0, 3)), 2);
                        world.updateComparators(blockPos, targetBlock);
                        return TypedActionResult.success(itemStack);
                    }else if (this.placeFluid(user, world, targetBlockPos, hitResult, fluid)) {

                        if (user instanceof ServerPlayerEntity) {
                            Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity)user, targetBlockPos, itemStack);
                        }
                        tag.putInt("lava", tag.getInt("lava") - 1);
                        itemStack.setTag(tag);
                        return TypedActionResult.success(itemStack);
                    } else {
                        return TypedActionResult.fail(itemStack);
                    }
                }
            }
        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    private boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult blockHitResult, Fluid fluid) {
        BlockState blockState = world.getBlockState(pos);
        Block block = blockState.getBlock();
        Material material = blockState.getMaterial();
        boolean bl = blockState.canBucketPlace(fluid);
        boolean bl2 = blockState.isAir() || bl || block instanceof FluidFillable && ((FluidFillable)block).canFillWithFluid(world, pos, blockState, fluid);
        if (!bl2) {
            return blockHitResult != null && this.placeFluid(player, world, blockHitResult.getBlockPos().offset(blockHitResult.getSide()), null, fluid);
        } else if (world.getDimension().isUltrawarm() && fluid.isIn(FluidTags.WATER)) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            world.playSound(player, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);

            for(int l = 0; l < 8; ++l) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
            }

            return true;
        } else if (block instanceof FluidFillable && fluid == Fluids.WATER) {
            ((FluidFillable)block).tryFillWithFluid(world, pos, blockState, ((FlowableFluid)fluid).getStill(false));
            world.playSound(player, pos, fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return true;
        } else {
            if (!world.isClient && bl && !material.isLiquid()) {
                world.breakBlock(pos, true);
            }

            if (!world.setBlockState(pos, fluid.getDefaultState().getBlockState(), 11) && !blockState.getFluidState().isStill()) {
                return false;
            } else {
                world.playSound(player, pos, fluid.isIn(FluidTags.LAVA) ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return true;
            }
        }
    }
}
