package cpp.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;

public class ChestTransporter extends ToolItem {

	public ChestTransporter(Settings settings) {
		super(ToolMaterials.IRON, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
		if (!(blockEntity instanceof LootableContainerBlockEntity))
			return ActionResult.PASS;
		if (!context.getWorld().isClient) {
			ServerWorld world = (ServerWorld) context.getWorld();
			BlockState blockState = world.getBlockState(context.getBlockPos());
			ItemStack itemStack = blockState.getBlock().asItem().getDefaultStack();
			NbtCompound tag = blockEntity.writeNbt(new NbtCompound());
			tag.remove("x");
			tag.remove("y");
			tag.remove("z");
			tag.remove("id");
			itemStack.putSubTag("BlockEntityTag", tag);
			blockEntity.markRemoved();
			world.setBlockState(context.getBlockPos(), Blocks.AIR.getDefaultState(), 0b0100011);// MJSB
			Block.dropStack(world, context.getBlockPos(), itemStack);
			if (!context.getPlayer().isCreative())
				context.getStack().damage(1, world.random, (ServerPlayerEntity) context.getPlayer());
		}
		return ActionResult.SUCCESS;
	}
}
