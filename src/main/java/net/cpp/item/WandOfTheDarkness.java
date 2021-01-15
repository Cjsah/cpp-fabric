package net.cpp.item;

import static net.cpp.init.CppItems.BROKEN_SPAWNER;
import static net.cpp.init.CppItems.SHARD_OF_THE_DARKNESS;
import static net.minecraft.item.Items.SPAWNER;

import java.util.List;

import net.cpp.init.CppItemTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.DispenserBlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

public class WandOfTheDarkness extends Item {
	public WandOfTheDarkness(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			ServerWorld world = (ServerWorld) context.getWorld();
			ServerPlayerEntity player = (ServerPlayerEntity) context.getPlayer();
			BlockPos blockPos = context.getBlockPos();
			BlockState state = world.getBlockState(blockPos);
			if (state.isOf(Blocks.DISPENSER)) {
				boolean b1 = true;
				for1: for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						Block block = world.getBlockState(blockPos.add(i, -1, j)).getBlock();
						if (i == 0 && j == 0) {
							if (block != Blocks.BEDROCK) {
								b1 = false;
								break for1;
							}
						} else if (((i + j) & 1) == 0) {
							if (block != Blocks.OBSIDIAN) {
								b1 = false;
								break for1;
							}
						} else {
							if (block != Blocks.MAGMA_BLOCK) {
								b1 = false;
								break for1;
							}
						}
					}
				}
				if (b1) {
					List<ItemFrameEntity> itemFrames = world.getEntitiesByClass(ItemFrameEntity.class, new Box(blockPos.up()), itemFrame -> itemFrame.getRotationClient().x == -90 && !itemFrame.getHeldItemStack().isEmpty());
					if (!itemFrames.isEmpty()) {
						ItemFrameEntity itemFrame = itemFrames.get(0);
						ItemStack frameStack = itemFrame.getHeldItemStack();
						DispenserBlockEntity inv = (DispenserBlockEntity) world.getBlockEntity(blockPos);
						if (frameStack.isOf(BROKEN_SPAWNER)) {
							boolean b2 = true;
							for (int i = 0; i < 9; i++) {
								ItemStack stack = inv.getStack(i);
								if (i != 4) {
									if (!stack.isOf(SHARD_OF_THE_DARKNESS)) {
										b2 = false;
										break;
									}
								} else {
									if (!stack.isIn(CppItemTags.RARE_DROPS)) {
										b2 = false;
										break;
									}
								}
							}
							if (b2) {
								context.getPlayer().damage(DamageSource.MAGIC, 12);
								for (int i = 0; i < 9; i++) {
									inv.getStack(i).decrement(1);
								}
								ItemStack ritualStack = SPAWNER.getDefaultStack();
								ritualStack.setTag(frameStack.getTag());
								Wand.setRitualStack(itemFrame, ritualStack);
								player.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.BLOCK_BEACON_ACTIVATE, SoundCategory.PLAYERS, blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, 1, 1));
								return ActionResult.SUCCESS;
							}
						}
					}
				}
			}
		}
		return ActionResult.PASS;
	}
}
