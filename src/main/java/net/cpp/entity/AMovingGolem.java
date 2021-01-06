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
import net.cpp.init.CppEntities;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.TrappedChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AMovingGolem extends LivingEntity {
	public static final Set<Block> CONTROLS = ImmutableSet.of(RED_WOOL, YELLOW_WOOL, BLUE_WOOL, GREEN_WOOL, CYAN_WOOL, MAGENTA_WOOL, WHITE_WOOL);
	protected ItemStack mainHandStack = ItemStack.EMPTY;
	protected Arm mainArm = Arm.RIGHT;
	protected Direction movingDirection = Direction.EAST;
	protected SimpleInventory inventory = new SimpleInventory(27);
	protected int continuousDisplacement;
	protected int experience;

	public AMovingGolem(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
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
		super.tick();
		pickup();
		work();
		reactBlock();
		teleport(getPos().x + movingDirection.getOffsetX(), getPos().y + movingDirection.getOffsetY(), getPos().z + movingDirection.getOffsetZ());
	}

	protected void reactBlock() {
		Block block = getBlockState().getBlock();
		if (block == RED_WOOL)
			movingDirection = Direction.EAST;
		else if (block == YELLOW_WOOL)
			movingDirection = Direction.SOUTH;
		else if (block == BLUE_WOOL)
			movingDirection = Direction.WEST;
		else if (block == GREEN_WOOL)
			movingDirection = Direction.NORTH;
		else if (block == CYAN_WOOL)
			movingDirection = Direction.UP;
		else if (block == MAGENTA_WOOL)
			movingDirection = Direction.DOWN;
		else if (block == WHITE_WOOL)
			kill();
		else if (block instanceof TrappedChestBlock)
			CodingTool.transfer((TrappedChestBlockEntity) world.getBlockEntity(getBlockPos()), inventory);
		else if (block instanceof ChestBlock)
			CodingTool.transfer(inventory, (ChestBlockEntity) world.getBlockEntity(getBlockPos()));

	}

	public abstract void work();

	protected void pickup() {
		for (ItemEntity itemEntity : world.getEntitiesByClass(ItemEntity.class, new Box(getPos(), getPos()).expand(1), item -> true)) {
			inventory.addStack(itemEntity.getStack());
		}
		for (ExperienceOrbEntity orb : world.getEntitiesByClass(ExperienceOrbEntity.class, new Box(getPos(), getPos()).expand(1), orb -> true)) {
			experience += orb.getExperienceAmount();
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
}
