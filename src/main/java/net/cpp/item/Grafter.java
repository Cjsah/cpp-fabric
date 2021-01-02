package net.cpp.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import java.util.Set;

import net.cpp.api.CodingTool;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.MiningToolItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class Grafter extends MiningToolItem {
	public static final Map<Block, Block> LEAVES_TO_SALPING = new HashMap<>();

	public Grafter(Settings settings) {
		this(0, 0, ToolMaterials.IRON, settings);
	}

	protected Grafter(float attackDamage, float attackSpeed, ToolMaterial material, Settings settings) {
		super(attackDamage, attackSpeed, material, LEAVES_TO_SALPING.keySet(), settings);
	}

	@Override
	public boolean postMine(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner) {
//		CodingTool.sendMessage(world, "1", false);
		if (LEAVES_TO_SALPING.containsKey(state.getBlock())) {
			Block.dropStack(world, pos, LEAVES_TO_SALPING.get(state.getBlock()).asItem().getDefaultStack());
			stack.damage(1, miner, e -> {
//				e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
			});
			return true;
		}
		return super.postMine(stack, world, state, pos, miner);
	}

	public boolean isSuitableFor(BlockState state) {
		return LEAVES_TO_SALPING.containsKey(state.getBlock());
	}

	@Override
	public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
		if (isSuitableFor(state))
			return 6;
		return super.getMiningSpeedMultiplier(stack, state);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		if (!context.getWorld().isClient) {
//			context.getWorld().breakBlock(context.getBlockPos(), true, context.getPlayer());
//			context.getWorld().setBlockState(context.getBlockPos(), Blocks.AIR.getDefaultState());
//			BlockState blockState = context.getWorld().getBlockState(context.getBlockPos());
//			Block block = blockState.getBlock();
//			BlockEntity blockEntity = context.getWorld().getBlockEntity(context.getBlockPos());
//			block.onBreak(context.getWorld(), context.getBlockPos(), blockState, context.getPlayer());
//			block.onBroken(context.getWorld(), context.getBlockPos(), blockState);
//			block.onStacksDropped(blockState, (ServerWorld) context.getWorld(), context.getBlockPos(), context.getStack());
//			List<ItemStack> droppeds = Block.getDroppedStacks(blockState, (ServerWorld) context.getWorld(), context.getBlockPos(), blockEntity, context.getPlayer(), context.getStack());
//			context.getPlayer().incrementStat(Stats.MINED.getOrCreateStat(block));
//			context.getPlayer().incrementStat(Stats.USED.getOrCreateStat(context.getStack().getItem()));
//			for (ItemStack stack : droppeds)
//				Block.dropStack(context.getWorld(), context.getBlockPos(), stack);
//			context.getWorld().removeBlock(context.getBlockPos(), false);
		}
		return ActionResult.PASS;
	}

	static {
		for (Entry<RegistryKey<Block>, Block> entry : Registry.BLOCK.getEntries()) {
			if (entry.getKey().getValue().getPath().contains("leaves")) {
				Identifier id = new Identifier(entry.getKey().getValue().getNamespace(), entry.getKey().getValue().getPath().replaceFirst("leaves", "sapling"));
				if (Registry.BLOCK.containsId(id)) {
					LEAVES_TO_SALPING.put(entry.getValue(), Registry.BLOCK.get(id));
				}
			}
		}
	}
}
