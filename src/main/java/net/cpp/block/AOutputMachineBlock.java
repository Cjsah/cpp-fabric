package net.cpp.block;

import javax.annotation.Nullable;

import net.cpp.block.entity.AMachineBlockEntity;
import net.cpp.block.entity.ItemProcessorBlockEntity;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

/**
 * 机器模板，子类只需要实现两个方法{@link BlockEntityProvider#createBlockEntity(BlockView)}和{@link #getStatIdentifier()}
 * 
 * @author Ph-苯
 *
 */
public abstract class AOutputMachineBlock extends AMachineBlock  {

	public AOutputMachineBlock() {
	}

	public AOutputMachineBlock(Settings settings) {
		super(settings);
	}

}
