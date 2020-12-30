package net.cpp.item;

import java.util.List;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.EndGatewayBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.Item.Settings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.ParticleS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CoordinateRecorder extends BlockItem {
	public static final String POS_KEY = "pos";

	public CoordinateRecorder(Settings settings) {
		super(Blocks.END_GATEWAY, settings);
	}

	@Override
	public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
		CompoundTag tag = stack.getOrCreateTag().getCompound(POS_KEY);
		if (tag.getSize() == 3) {
			BlockPos coordinate = NbtHelper.toBlockPos(tag);
			tooltip.add(new TranslatableText("tooltip.cpp.coordinate", String.format("%d/%d/%d", coordinate.getX(), coordinate.getY(), coordinate.getZ())));
		}
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
			context.getPlayer().incrementStat(Stats.USED.getOrCreateStat(this));
			ItemPlacementContext placementContext = new ItemPlacementContext(context);
			if (!context.getPlayer().isSneaking()) {
				BlockPos coordinate = new BlockPos(placementContext.getBlockPos());
				context.getStack().getOrCreateTag().put(POS_KEY, NbtHelper.fromBlockPos(coordinate));
				context.getPlayer().sendMessage(new TranslatableText("tooltip.cpp.coordinate", String.format("%d/%d/%d", coordinate.getX(), coordinate.getY(), coordinate.getZ())), true);
				((ServerPlayerEntity) context.getPlayer()).networkHandler.sendPacket(new ParticleS2CPacket(ParticleTypes.PORTAL, false, coordinate.getX(), coordinate.getY(), coordinate.getZ(), 0, 0, 0, .1f, 5));
				return ActionResult.CONSUME;
			} else {
				CompoundTag tag = context.getStack().getOrCreateTag().getCompound(POS_KEY);
				if (tag.getSize() == 3) {
					BlockPos coordinate = NbtHelper.toBlockPos(tag);
					BlockPos blockPos = new BlockPos(placementContext.getBlockPos());
					if (place(placementContext) == ActionResult.CONSUME) {
						BlockEntity blockEntity = context.getWorld().getBlockEntity(blockPos);
//						System.out.println(blockEntity);
						if (blockEntity instanceof EndGatewayBlockEntity) {
							EndGatewayBlockEntity endGatewayBlockEntity = (EndGatewayBlockEntity) blockEntity;
							endGatewayBlockEntity.setExitPortalPos(coordinate, true);
							return ActionResult.CONSUME;
						}
						return ActionResult.FAIL;
					} else {
						return ActionResult.FAIL;
					}
				} else {
					context.getPlayer().sendMessage(new TranslatableText("tooltip.cpp.coordinate_absent"), true);
					return ActionResult.FAIL;
				}
			}
		}
		return ActionResult.SUCCESS;
	}
	@Override
	public String getTranslationKey() {
		// TODO 自动生成的方法存根
		return getOrCreateTranslationKey();
	}
}
