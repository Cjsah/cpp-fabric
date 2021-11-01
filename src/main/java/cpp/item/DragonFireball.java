package cpp.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DragonFireball extends Item {
    public DragonFireball(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack item = user.getStackInHand(hand);
        if (!world.isClient) {
            double speed = 1;
            double yaw = user.getYaw();
            double pitch = user.getPitch();
            double dy = -Math.sin(pitch * Math.PI / 180D) * speed;
            double dx = -Math.sin(yaw * Math.PI / 180D);
            double dz = Math.cos(yaw * Math.PI / 180D);
            double proportion = Math.sqrt((((speed * speed) - (dy * dy)) / ((dx * dx) + (dz * dz))));
            dx *= proportion;
            dz *= proportion;
            DragonFireballEntity ball = new DragonFireballEntity(world, user, dx, dy, dz);
            ball.refreshPositionAndAngles(user.getX(), user.getEyeY(), user.getZ(), user.getYaw(), user.getPitch());
            world.spawnEntity(ball);

        }
        return TypedActionResult.pass(item);
    }
}
