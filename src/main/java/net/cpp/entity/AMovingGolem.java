package net.cpp.entity;

import static net.minecraft.block.Blocks.BLUE_WOOL;
import static net.minecraft.block.Blocks.CYAN_WOOL;
import static net.minecraft.block.Blocks.GREEN_WOOL;
import static net.minecraft.block.Blocks.MAGENTA_WOOL;
import static net.minecraft.block.Blocks.RED_WOOL;
import static net.minecraft.block.Blocks.WHITE_WOOL;
import static net.minecraft.block.Blocks.YELLOW_WOOL;

import java.util.Collections;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.cpp.api.CodingTool;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AMovingGolem extends LivingEntity {
	/**
	 * 控制方块
	 */
	public static final Set<Block> CONTROLS = ImmutableSet.of(RED_WOOL, YELLOW_WOOL, BLUE_WOOL, GREEN_WOOL, CYAN_WOOL, MAGENTA_WOOL, WHITE_WOOL, Blocks.CHEST, Blocks.TRAPPED_CHEST);
	protected ItemStack mainHandStack = ItemStack.EMPTY;
	protected Arm mainArm = Arm.RIGHT;
	protected Direction movingDirection = Direction.EAST;
	protected SimpleInventory inventory = new SimpleInventory(27);
	protected int continuousDisplacement;
	protected int experience;
	protected boolean killed;

	public AMovingGolem(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
		setNoGravity(true);
		setInvulnerable(true);
		noClip = true;
	}

	@Override
	public Iterable<ItemStack> getArmorItems() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getEquippedStack(EquipmentSlot slot) {
		return slot == EquipmentSlot.MAINHAND ? mainHandStack : ItemStack.EMPTY;
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack) {
		if (slot == EquipmentSlot.MAINHAND)
			mainHandStack = stack;
	}

	@Override
	public Arm getMainArm() {
		return mainArm;
	}

	@Override
	public void tick() {
		reactBlock();
		pickup();
		work();
		teleport(getPos().x + movingDirection.getOffsetX(), getPos().y + movingDirection.getOffsetY(), getPos().z + movingDirection.getOffsetZ());
		continuousDisplacement++;
		if (experience >= 9) {
			experience -= 9;
			inventory.addStack(Items.EXPERIENCE_BOTTLE.getDefaultStack());
		}
		if (continuousDisplacement > 64 && !world.isClient)
			killed = true;
		super.tick();
		if (killed)
			kill();
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
			CodingTool.transfer((TrappedChestBlockEntity) world.getBlockEntity(getBlockPos()), inventory);
		else if (block instanceof ChestBlock)
			CodingTool.transfer(inventory, (ChestBlockEntity) world.getBlockEntity(getBlockPos()));

	}

	/**
	 * 针对当前方块的操作
	 */
	public abstract void work();

	/**
	 * 捡起物品和经验球
	 */
	protected void pickup() {
		for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()).expand(1), item -> true)) {
			itemEntity.setStack(inventory.addStack(itemEntity.getStack()));
			System.out.println(1);
		}
		for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(getPos(), getPos()).expand(1), orb -> true)) {
			experience += orb.getExperienceAmount();
			orb.discard();
		}
	}

	@Override
	public void fromTag(CompoundTag tag) {
		mainHandStack = ItemStack.fromTag(tag.getCompound("mainHandStack"));
		mainArm = tag.getBoolean("rightArm") ? Arm.RIGHT : Arm.LEFT;
		Vec3d rotation = getRotationVector();
		movingDirection = Direction.getFacing(rotation.x, rotation.y, rotation.z);
		experience = tag.getInt("experience");
		super.fromTag(tag);
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.put("mainHandStack", mainHandStack.toTag(new CompoundTag()));
		return super.toTag(tag);
	}

	protected void setFacing(Direction direction) {
		movingDirection = direction;
		setRotation(direction.asRotation(), -direction.getOffsetY() * 90);
	}

	@Override
	public double getHeightOffset() {
		return 0;
	}

	@Override
	public PistonBehavior getPistonBehavior() {
		return PistonBehavior.IGNORE;
	}

	@Override
	public boolean collides() {
		return false;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return false;
	}

	@Override
	public void kill() {
		CodingTool.drop(world, getPos(), inventory.clearToList());
		CodingTool.drop(world, getPos(), mainHandStack);
		if (!world.isClient) {
			ExperienceOrbEntity.spawn((ServerWorld) world, getPos(), experience);
		}
		discard();
	}

	public void setMovingDirection(Direction movingDirection) {
		if (this.movingDirection != movingDirection)
			continuousDisplacement = 0;
		this.movingDirection = movingDirection;
	}

	@Override
	public void tickMovement() {
		super.tickMovement();
	}

	public void setMainHandStack(ItemStack mainHandStack) {
		this.mainHandStack = mainHandStack;
	}
}
