package cpp.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import javax.annotation.Nullable;

public class BlockPlacerItem extends Item {
	protected final Block block;

	public BlockPlacerItem(Block block, Settings settings) {
		super(settings);
		this.block = block;
	}

	public Block getBlock() {
		return block;
	}

	public ActionResult useOnBlock(ItemUsageContext context) {
		ActionResult actionResult = this.place(new ItemPlacementContext(context));
		return !actionResult.isAccepted() && this.isFood() ? this.use(context.getWorld(), context.getPlayer(), context.getHand()).getResult() : actionResult;
	}

	public ActionResult place(ItemPlacementContext context) {
		if (!context.canPlace()) {
			return ActionResult.FAIL;
		} else {
			ItemPlacementContext itemPlacementContext = this.getPlacementContext(context);
			if (itemPlacementContext == null) {
				return ActionResult.FAIL;
			} else {
				BlockState blockState = this.getPlacementState(itemPlacementContext);
				if (blockState == null) {
					return ActionResult.FAIL;
				} else if (!this.place(itemPlacementContext, blockState)) {
					return ActionResult.FAIL;
				} else {
					BlockPos blockPos = itemPlacementContext.getBlockPos();
					World world = itemPlacementContext.getWorld();
					PlayerEntity playerEntity = itemPlacementContext.getPlayer();
					ItemStack itemStack = itemPlacementContext.getStack();
					BlockState blockState2 = world.getBlockState(blockPos);
					if (blockState2.isOf(blockState.getBlock())) {
						blockState2 = this.placeFromTag(blockPos, world, itemStack, blockState2);
						this.postPlacement(blockPos, world, playerEntity, itemStack, blockState2);
						blockState2.getBlock().onPlaced(world, blockPos, blockState2, playerEntity, itemStack);
						if (playerEntity instanceof ServerPlayerEntity) {
							Criteria.PLACED_BLOCK.trigger((ServerPlayerEntity) playerEntity, blockPos, itemStack);
						}
					}

					BlockSoundGroup blockSoundGroup = blockState2.getSoundGroup();
					world.playSound(playerEntity, blockPos, this.getPlaceSound(blockState2), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);
					world.emitGameEvent(playerEntity, GameEvent.BLOCK_PLACE, blockPos);
					if (playerEntity == null || !playerEntity.getAbilities().creativeMode) {
						itemStack.decrement(1);
					}

					return ActionResult.success(world.isClient);
				}
			}
		}
	}

	protected boolean place(ItemPlacementContext context, BlockState state) {
		return context.getWorld().setBlockState(context.getBlockPos(), state, 11);
	}

	@Nullable
	public ItemPlacementContext getPlacementContext(ItemPlacementContext context) {
		return context;
	}

	protected SoundEvent getPlaceSound(BlockState state) {
		return state.getSoundGroup().getPlaceSound();
	}

	@Nullable
	protected BlockState getPlacementState(ItemPlacementContext context) {
		BlockState blockState = this.getBlock().getPlacementState(context);
		return blockState != null && this.canPlace(context, blockState) ? blockState : null;
	}

	protected boolean canPlace(ItemPlacementContext context, BlockState state) {
		PlayerEntity playerEntity = context.getPlayer();
		ShapeContext shapeContext = playerEntity == null ? ShapeContext.absent() : ShapeContext.of(playerEntity);
		return (!this.checkStatePlacement() || state.canPlaceAt(context.getWorld(), context.getBlockPos())) && context.getWorld().canPlace(state, context.getBlockPos(), shapeContext);
	}

	protected boolean checkStatePlacement() {
		return true;
	}

	protected BlockState placeFromTag(BlockPos pos, World world, ItemStack stack, BlockState state) {
		BlockState blockState = state;
		NbtCompound compoundTag = stack.getTag();
		if (compoundTag != null) {
			NbtCompound compoundTag2 = compoundTag.getCompound("BlockStateTag");
			StateManager<Block, BlockState> stateManager = state.getBlock().getStateManager();

			for (String string : compoundTag2.getKeys()) {
				Property<?> property = stateManager.getProperty(string);
				if (property != null) {
					String string2 = compoundTag2.get(string).asString();
					blockState = with(blockState, property, string2);
				}
			}
		}

		if (blockState != state) {
			world.setBlockState(pos, blockState, 2);
		}

		return blockState;
	}

	protected boolean postPlacement(BlockPos pos, World world, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
		return writeTagToBlockEntity(world, player, pos, stack);
	}

	public static boolean writeTagToBlockEntity(World world, @Nullable PlayerEntity player, BlockPos pos, ItemStack stack) {
		MinecraftServer minecraftServer = world.getServer();
		if (minecraftServer != null) {
			NbtCompound compoundTag = stack.getSubTag("BlockEntityTag");
			if (compoundTag != null) {
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if (blockEntity != null) {
					if (!world.isClient && blockEntity.copyItemDataRequiresOperator() && (player == null || !player.isCreativeLevelTwoOp())) {
						return false;
					}

					NbtCompound compoundTag2 = blockEntity.writeNbt(new NbtCompound());
					NbtCompound compoundTag3 = compoundTag2.copy();
					compoundTag2.copyFrom(compoundTag);
					compoundTag2.putInt("x", pos.getX());
					compoundTag2.putInt("y", pos.getY());
					compoundTag2.putInt("z", pos.getZ());
					if (!compoundTag2.equals(compoundTag3)) {
						blockEntity.readNbt(compoundTag2);
						blockEntity.markDirty();
						return true;
					}
				}
			}

		}
		return false;
	}

	protected static <T extends Comparable<T>> BlockState with(BlockState state, Property<T> property, String name) {
		return property.parse(name).map((value) -> state.with(property, value)).orElse(state);
	}
}
