package cpp.entity;

import static net.minecraft.block.Blocks.BLUE_WOOL;
import static net.minecraft.block.Blocks.CYAN_WOOL;
import static net.minecraft.block.Blocks.GREEN_WOOL;
import static net.minecraft.block.Blocks.MAGENTA_WOOL;
import static net.minecraft.block.Blocks.RED_WOOL;
import static net.minecraft.block.Blocks.WHITE_WOOL;
import static net.minecraft.block.Blocks.YELLOW_WOOL;

import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import cpp.api.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AMovingGolemEntity extends AGolemEntity {
	/**
	 * 控制方块
	 */
	public static final Set<Block> CONTROLS = ImmutableSet.of(RED_WOOL, YELLOW_WOOL, BLUE_WOOL, GREEN_WOOL, CYAN_WOOL, MAGENTA_WOOL, WHITE_WOOL, Blocks.CHEST, Blocks.TRAPPED_CHEST);

	protected Direction movingDirection = Direction.EAST;
	protected SimpleInventory inventory = new SimpleInventory(27);
	protected int continuousDisplacement;
	protected int experience;

	public AMovingGolemEntity(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override
	public void tick() {
		pickup();
		if (experience >= 9) {
			experience -= 9;
			inventory.addStack(Items.EXPERIENCE_BOTTLE.getDefaultStack());
		}
		super.tick();
	}

	/**
	 * 对{@link #CONTROLS}里面方块的反应
	 */
	protected void reactBlock() {
		Block block = getBlockState().getBlock();
		if (block == RED_WOOL)
			setMovingDirection(Direction.EAST);
		else if (block == YELLOW_WOOL)
			setMovingDirection(Direction.SOUTH);
		else if (block == BLUE_WOOL)
			setMovingDirection(Direction.WEST);
		else if (block == GREEN_WOOL)
			setMovingDirection(Direction.NORTH);
		else if (block == CYAN_WOOL)
			setMovingDirection(Direction.UP);
		else if (block == MAGENTA_WOOL)
			setMovingDirection(Direction.DOWN);
		else if (block == WHITE_WOOL && !world.isClient)
			killed = true;
		else if (block instanceof TrappedChestBlock)
			Utils.transfer((TrappedChestBlockEntity) world.getBlockEntity(getBlockPos()), inventory);
		else if (block instanceof ChestBlock)
			Utils.transfer(inventory, (ChestBlockEntity) world.getBlockEntity(getBlockPos()));

	}

	/**
	 * 捡起物品和经验球
	 */
	protected void pickup() {
		for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()).expand(1), item -> true)) {
			itemEntity.setStack(inventory.addStack(itemEntity.getStack()));
		}
		experience += Utils.collectExpOrbs(world, getPos(), 1, false);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		continuousDisplacement = tag.getInt("continuousDisplacement");
		experience = tag.getInt("experience");
		Utils.inventoryFromTag(inventory, tag);
		Vec3d rotation = getRotationVector();
		movingDirection = Direction.getFacing(rotation.x, rotation.y, rotation.z);
		super.readNbt(tag);
	}

	@Override
	public NbtCompound writeNbt(NbtCompound tag) {
		tag.putInt("continuousDisplacement", continuousDisplacement);
		tag.putInt("experience", experience);
		Utils.inventoryToTag(inventory, tag);
		return super.writeNbt(tag);
	}

	@Override
	public void kill() {
		Utils.drop(world, getPos(), inventory.clearToList());
		if (!world.isClient) {
			ExperienceOrbEntity.spawn((ServerWorld) world, getPos(), experience);
		}
		super.kill();
	}

	public void setMovingDirection(Direction movingDirection) {
		if (this.movingDirection != movingDirection)
			continuousDisplacement = 0;
		this.movingDirection = movingDirection;
		setRotation(movingDirection.asRotation(), -movingDirection.getOffsetY() * 90);
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
		teleport(getPos().x + movingDirection.getOffsetX(), getPos().y + movingDirection.getOffsetY(), getPos().z + movingDirection.getOffsetZ());
		continuousDisplacement++;
		if (continuousDisplacement > 64 && !world.isClient)
			killed = true;
	}

	protected void listMerge(List<ItemStack> droppeds) {
		for (int i = 0; i < droppeds.size(); i++) {
			droppeds.set(i, inventory.addStack(droppeds.get(i)));
		}
		Utils.drop(world, getPos(), droppeds);
	}
}
