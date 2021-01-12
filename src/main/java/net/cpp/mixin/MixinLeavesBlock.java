package net.cpp.mixin;

import java.util.Random;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(LeavesBlock.class)
public class MixinLeavesBlock extends Block {

	public MixinLeavesBlock(Settings settings) {
		super(settings);
	}

//	@Override
//	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
//		super.neighborUpdate(state, world, pos, block, fromPos, notify);
//		if (!world.isClient && !state.get(LeavesBlock.PERSISTENT) && state.get(LeavesBlock.DISTANCE) == 7) {
//			randomTick(state, (ServerWorld) world, pos, world.random);
//		}
//	}

	@Inject(at = @At("RETURN"), method = "scheduledTick")
	public void scheduledTick1(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
		if (!state.get(LeavesBlock.PERSISTENT) && state.get(LeavesBlock.DISTANCE) == 7) {
			randomTick(state, world, pos, random);
		}
	}
}
