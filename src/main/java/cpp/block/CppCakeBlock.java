package cpp.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CppCakeBlock extends CakeBlock {
    private final StatusEffect effect;
    private final int duration;
    private final int amplifier;


    public CppCakeBlock(StatusEffect effect, int duration, int amplifier, Settings settings) {
        super(settings);
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ActionResult result = super.onUse(state, world, pos, player, hand, hit);
        if (player.canConsume(false)) player.addStatusEffect(new StatusEffectInstance(this.effect, this.duration, this.amplifier));
        return result;
    }
}
