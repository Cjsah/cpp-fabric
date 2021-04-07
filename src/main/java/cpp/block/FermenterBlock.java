package cpp.block;

import static net.minecraft.block.ComposterBlock.*;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.Random;

public class FermenterBlock extends Block implements InventoryProvider {
	private static final VoxelShape RAY_TRACE_SHAPE;
	private static final VoxelShape[] LEVEL_TO_COLLISION_SHAPE;
	
	
	public FermenterBlock(AbstractBlock.Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(LEVEL, 0));
	}
	
	@Environment(EnvType.CLIENT)
	public static void playEffects(World world, BlockPos pos, boolean fill) {
		BlockState blockState = world.getBlockState(pos);
		world.playSound(pos.getX(), pos.getY(), pos.getZ(), fill ? SoundEvents.BLOCK_COMPOSTER_FILL_SUCCESS : SoundEvents.BLOCK_COMPOSTER_FILL, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
		double d = blockState.getOutlineShape(world, pos).getEndingCoord(Direction.Axis.Y, 0.5D, 0.5D) + 0.03125D;
		double e = 0.13124999403953552D;
		double f = 0.737500011920929D;
		Random random = world.getRandom();
		
		for (int i = 0; i < 10; ++i) {
			double g = random.nextGaussian() * 0.02D;
			double h = random.nextGaussian() * 0.02D;
			double j = random.nextGaussian() * 0.02D;
			world.addParticle(ParticleTypes.COMPOSTER, (double) pos.getX() + 0.13124999403953552D + 0.737500011920929D * (double) random.nextFloat(), (double) pos.getY() + d + (double) random.nextFloat() * (1.0D - d), (double) pos.getZ() + 0.13124999403953552D + 0.737500011920929D * (double) random.nextFloat(), g, h, j);
		}
		
	}
	
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LEVEL_TO_COLLISION_SHAPE[state.get(LEVEL)];
	}
	
	public VoxelShape getRaycastShape(BlockState state, BlockView world, BlockPos pos) {
		return RAY_TRACE_SHAPE;
	}
	
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return LEVEL_TO_COLLISION_SHAPE[0];
	}
	
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
		if (state.get(LEVEL) == 7) {
			world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20);
		}
		
	}
	
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int i = state.get(LEVEL);
		ItemStack itemStack = player.getStackInHand(hand);
		if (i < 8 && ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(itemStack.getItem())) {
			if (i < 7 && !world.isClient) {
				BlockState blockState = addToComposter(state, world, pos, itemStack);
				world.syncWorldEvent(1500, pos, state != blockState ? 1 : 0);
				if (!player.getAbilities().creativeMode) {
					itemStack.decrement(1);
				}
			}
			
			return ActionResult.success(world.isClient);
		} else if (i == 8) {
			emptyFullComposter(state, world, pos);
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}
	
	public static BlockState compost(BlockState state, ServerWorld world, ItemStack stack, BlockPos pos) {
		int i = state.get(LEVEL);
		if (i < 7 && ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem())) {
			BlockState blockState = addToComposter(state, world, pos, stack);
			stack.decrement(1);
			return blockState;
		} else {
			return state;
		}
	}
	
	public static BlockState emptyFullComposter(BlockState state, World world, BlockPos pos) {
		if (!world.isClient) {
			float f = 0.7F;
			double d = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
			double e = (double) (world.random.nextFloat() * 0.7F) + 0.06000000238418579D + 0.6D;
			double g = (double) (world.random.nextFloat() * 0.7F) + 0.15000000596046448D;
			ItemEntity itemEntity = new ItemEntity(world, (double) pos.getX() + d, (double) pos.getY() + e, (double) pos.getZ() + g, new ItemStack(Items.DIRT, world.getRandom().nextInt(3) + 1));
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
		}
		
		BlockState blockState = emptyComposter(state, world, pos);
		world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
		return blockState;
	}
	
	private static BlockState emptyComposter(BlockState state, WorldAccess world, BlockPos pos) {
		BlockState blockState = state.with(LEVEL, 0);
		world.setBlockState(pos, blockState, 3);
		return blockState;
	}
	
	private static BlockState addToComposter(BlockState state, WorldAccess world, BlockPos pos, ItemStack item) {
		int i = state.get(LEVEL);
		float f = ITEM_TO_LEVEL_INCREASE_CHANCE.getFloat(item.getItem());
		if ((i != 0 || f <= 0.0F) && world.getRandom().nextDouble() >= (double) f) {
			return state;
		} else {
			int j = i + 1;
			BlockState blockState = state.with(LEVEL, j);
			world.setBlockState(pos, blockState, 3);
			if (j == 7) {
				world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20);
			}
			
			return blockState;
		}
	}
	
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (state.get(LEVEL) == 7) {
			world.setBlockState(pos, state.cycle(LEVEL), 3);
			world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_READY, SoundCategory.BLOCKS, 1.0F, 1.0F);
		}
		
	}
	
	public boolean hasComparatorOutput(BlockState state) {
		return true;
	}
	
	public int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		return state.get(LEVEL);
	}
	
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(LEVEL);
	}
	
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}
	
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		int i = state.get(LEVEL);
		if (i == 8) {
			return new FullComposterInventory(state, world, pos, new ItemStack(Items.DIRT, world.getRandom().nextInt(2) + 1));
		} else {
			return i < 7 ? new ComposterInventory(state, world, pos) : new DummyInventory();
		}
	}
	
	static {
		RAY_TRACE_SHAPE = VoxelShapes.fullCube();
		LEVEL_TO_COLLISION_SHAPE = Util.make(new VoxelShape[9], (voxelShapes) -> {
			for (int i = 0; i < 8; ++i) {
				voxelShapes[i] = VoxelShapes.combineAndSimplify(RAY_TRACE_SHAPE, Block.createCuboidShape(2.0D, Math.max(2, 1 + i * 2), 2.0D, 14.0D, 16.0D, 14.0D), BooleanBiFunction.ONLY_FIRST);
			}
			
			voxelShapes[8] = voxelShapes[7];
		});
	}
	
	public static class ComposterInventory extends SimpleInventory implements SidedInventory {
		private final BlockState state;
		private final WorldAccess world;
		private final BlockPos pos;
		private boolean dirty;
		
		public ComposterInventory(BlockState state, WorldAccess world, BlockPos pos) {
			super(1);
			this.state = state;
			this.world = world;
			this.pos = pos;
		}
		
		public int getMaxCountPerStack() {
			return 1;
		}
		
		public int[] getAvailableSlots(Direction side) {
			return side == Direction.UP ? new int[]{0} : new int[0];
		}
		
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return !this.dirty && dir == Direction.UP && ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(stack.getItem());
		}
		
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return false;
		}
		
		public void markDirty() {
			ItemStack itemStack = this.getStack(0);
			if (!itemStack.isEmpty()) {
				this.dirty = true;
				BlockState blockState = addToComposter(this.state, this.world, this.pos, itemStack);
				this.world.syncWorldEvent(1500, this.pos, blockState != this.state ? 1 : 0);
				this.removeStack(0);
			}
			
		}
	}
	
	public static class FullComposterInventory extends SimpleInventory implements SidedInventory {
		private final BlockState state;
		private final WorldAccess world;
		private final BlockPos pos;
		private boolean dirty;
		
		public FullComposterInventory(BlockState state, WorldAccess world, BlockPos pos, ItemStack outputItem) {
			super(outputItem);
			this.state = state;
			this.world = world;
			this.pos = pos;
		}
		
		public int getMaxCountPerStack() {
			return 2;
		}
		
		public int[] getAvailableSlots(Direction side) {
			return side == Direction.DOWN ? new int[]{0} : new int[0];
		}
		
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return false;
		}
		
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return !this.dirty && dir == Direction.DOWN;
		}
		
		public void markDirty() {
			if (getStack(0).isEmpty()) {
				emptyComposter(this.state, this.world, this.pos);
				this.dirty = true;
			}
		}
	}
	
	public static class DummyInventory extends SimpleInventory implements SidedInventory {
		public DummyInventory() {
			super(0);
		}
		
		public int[] getAvailableSlots(Direction side) {
			return new int[0];
		}
		
		public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
			return false;
		}
		
		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return false;
		}
	}
}
