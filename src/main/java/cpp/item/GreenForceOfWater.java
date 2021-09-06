package cpp.item;

import cpp.api.IDefaultNbtItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import net.minecraft.block.FluidFillable;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
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

import java.util.List;

public class GreenForceOfWater extends Item implements IDefaultNbtItem {

    public GreenForceOfWater(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound nbt = stack.getOrCreateTag();
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.water"), new TranslatableText("word.infinite")).formatted(Formatting.GREEN));
        tooltip.add(new TranslatableText("misc.cpp", new TranslatableText("block.minecraft.lava"), nbt.getInt("lava")).formatted(Formatting.RED));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (!world.isClient) {
            NbtCompound nbt = itemStack.getOrCreateTag();
            BlockHitResult hitResult = raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
            boolean waterMode = nbt.getString("mode").equals("water");
            // 切换模式
            if (hitResult.getType() == HitResult.Type.MISS && user.isSneaking()) {
                nbt.putString("mode", waterMode ? "lava" : "water");
                user.sendMessage(new TranslatableText("chat.cpp.change", new TranslatableText("block.minecraft." + nbt.getString("mode")).formatted(waterMode ? Formatting.RED : Formatting.GREEN)).formatted(Formatting.GOLD), true);
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(itemStack);
            }else if (hitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetPos = hitResult.getBlockPos();
                Direction direction = hitResult.getSide();
                BlockPos sidePos = targetPos.offset(direction);
                BlockState blockState = world.getBlockState(targetPos);
                if (world.canPlayerModifyAt(user, targetPos) && user.canPlaceOn(sidePos, direction, itemStack)) {
                    // 放
                    if (user.isSneaking()) {
                        Fluid fluid = waterMode ? Fluids.WATER : Fluids.LAVA;
                        BlockPos pos = blockState.getBlock() instanceof FluidFillable && fluid == Fluids.WATER ? targetPos : sidePos;
                        if (user.isCreative() || fluid == Fluids.WATER || nbt.getInt("lava") > 0) {
                            if (blockState.getBlock() == Blocks.CAULDRON) {
                                world.setBlockState(targetPos, fluid == Fluids.WATER ? Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3) : Blocks.LAVA_CAULDRON.getDefaultState());
                                if (!user.isCreative() && fluid == Fluids.LAVA)
                                    nbt.putInt("lava", nbt.getInt("lava") - 1);
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                return TypedActionResult.success(itemStack);
                            } else if (blockState.getBlock() == Blocks.WATER_CAULDRON && !((LeveledCauldronBlock) blockState.getBlock()).isFull(blockState)) {
                                world.setBlockState(targetPos, Blocks.WATER_CAULDRON.getDefaultState().with(LeveledCauldronBlock.LEVEL, 3));
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                return TypedActionResult.success(itemStack);
                            } else if (!(blockState.getBlock() instanceof FluidBlock) && world.setBlockState(pos, fluid.getDefaultState().getBlockState())) {
                                if (user instanceof ServerPlayerEntity) {
                                    Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) user, pos, itemStack);
                                }
                                ((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(fluid == Fluids.WATER ? SoundEvents.ITEM_BUCKET_EMPTY : SoundEvents.ITEM_BUCKET_EMPTY_LAVA, SoundCategory.PLAYERS, user.getX(), user.getY(), user.getZ(), 1.0F, 1.0F));
                                if (fluid == Fluids.LAVA && !user.isCreative()) {
                                    nbt.putInt("lava", nbt.getInt("lava") - 1);
                                }
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                return TypedActionResult.success(itemStack);
                            }
                        }
                    // 收
                    } else {
                        if (blockState.getBlock() == Blocks.WATER_CAULDRON && ((LeveledCauldronBlock) blockState.getBlock()).isFull(blockState)) {
                            world.setBlockState(targetPos, Blocks.CAULDRON.getDefaultState());
                            user.incrementStat(Stats.USED.getOrCreateStat(this));
                            return this.changeNbt(user, itemStack, Fluids.WATER, nbt);
                        } else if (blockState.getBlock() == Blocks.LAVA_CAULDRON) {
                            world.setBlockState(targetPos, Blocks.CAULDRON.getDefaultState());
                            user.incrementStat(Stats.USED.getOrCreateStat(this));
                            return this.changeNbt(user, itemStack, Fluids.LAVA, nbt);
                        } else if (blockState.getBlock() instanceof FluidDrainable fluidDrainable) {
                            ItemStack resultBucket = fluidDrainable.tryDrainFluid(world, targetPos, blockState);
                            if (!resultBucket.isEmpty()) {
                                fluidDrainable.getBucketFillSound().ifPresent((sound) -> ((ServerPlayerEntity) user).networkHandler.sendPacket(new PlaySoundS2CPacket(sound, SoundCategory.PLAYERS, user.getX(), user.getY(), user.getZ(), 1.0F, 1.0F)));
                                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, targetPos);
                                Fluid fluid = resultBucket.getItem() == Items.WATER_BUCKET ? Fluids.WATER : Fluids.LAVA;
                                Criteria.FILLED_BUCKET.trigger((ServerPlayerEntity) user, resultBucket);
                                user.incrementStat(Stats.USED.getOrCreateStat(this));
                                return this.changeNbt(user, itemStack, fluid, nbt);
                            }
                        }
                    }
                }
            }
        }
        return TypedActionResult.pass(itemStack);
    }

    private TypedActionResult<ItemStack> changeNbt(PlayerEntity player, ItemStack item, Fluid fluid, NbtCompound nbt) {
        player.incrementStat(Stats.USED.getOrCreateStat(this));
        if (fluid == Fluids.LAVA) {
            nbt.putInt("lava", nbt.getInt("lava") + 1);
        }
        return TypedActionResult.success(item);
    }

	@Override
	public NbtCompound getDefaultNbt(NbtCompound nbt) {
        nbt.putString("mode", "water");
        nbt.putInt("lava", 0);
        return nbt;
	}
}
