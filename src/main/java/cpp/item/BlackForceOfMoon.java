package cpp.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidDrainable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static cpp.api.CppChat.say;

public class BlackForceOfMoon extends Item {
    public BlackForceOfMoon(Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Text getName(ItemStack stack) {
        return new TranslatableText(this.getTranslationKey()).formatted(Formatting.GOLD);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            int value = 0;
            if (user.isCreative()) {
                fill(world, user);
                value++;
            }else if (user.experienceLevel >= 4) {
                fill(world, user);
                user.addExperience(-40);
                value++;
            }else say(user, new TranslatableText("chat.cpp.exp.less"));

            if (value > 0) {
                user.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(item);
            }
        }
        return TypedActionResult.pass(item);
    }

    private void fill(World world, PlayerEntity user) {
        float yaw = user.getYaw();
        int value = (int)(yaw + (yaw >= 0 ? 22.5 : -22.5)) / 45;
        int x = value % 4;
        if (x != 0) x = x > 0 ? -1 : 1;
        int z = Math.abs(value) - 2;
        if (z != 0) z = z > 0 ? -1 : 1;
        BlockPos pos = user.getBlockPos().add(0, -1, 0);
        for (int i = 0; i < 30; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int k = -1; k <= 1; k++) {
                    BlockPos setPos = new BlockPos(pos).add(j, 0, k);
                    Block block = world.getBlockState(setPos).getBlock();
                    if (block == Blocks.AIR) world.setBlockState(setPos, Blocks.DIRT.getDefaultState());
                    else if (block instanceof FluidDrainable) {
                        if ((((FluidDrainable) block).tryDrainFluid(world, setPos, world.getBlockState(setPos))).isEmpty()) world.setBlockState(setPos, Blocks.DIRT.getDefaultState());
                        else world.setBlockState(setPos, block.getDefaultState());
                    }
                }
            }
            pos = pos.add(x, 0, z);
        }
    }
}
