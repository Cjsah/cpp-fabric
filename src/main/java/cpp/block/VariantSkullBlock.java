package cpp.block;

import cpp.block.entity.VariantSkullBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.*;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import javax.annotation.Nullable;

public class VariantSkullBlock extends BlockWithEntity {
	public static final IntProperty ROTATION = Properties.ROTATION;
	public static final BooleanProperty WALL = BooleanProperty.of("wall");
	
	public VariantSkullBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(ROTATION).add(WALL);
	}
	
	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new VariantSkullBlockEntity(pos,state);
	}
	
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return super.getOutlineShape(state, world, pos, context);
	}
	
	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos) {
		return super.getCullingShape(state, world, pos);
	}
	
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return super.getRenderType(state);
	}
	
	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return super.mirror(state, mirror);
	}
	
	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return super.rotate(state, rotation);
	}
}
