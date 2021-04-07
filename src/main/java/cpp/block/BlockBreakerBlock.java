package cpp.block;

import com.google.common.collect.Maps;
import cpp.Craftingpp;
import cpp.api.Utils;
import cpp.block.entity.BlockBreakerBlockEntity;
import cpp.init.CppBlockEntities;
import cpp.init.CppStats;
import net.minecraft.block.*;

import static net.minecraft.block.Blocks.*;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Map;

public class BlockBreakerBlock extends BlockWithEntity {
    public static final Map<Block, Pair<Block, Identifier>> PRODUCES = Maps.newHashMap();
    public static final String ID = "block_breaker";
    public BlockBreakerBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, @Nullable PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (breakBlock(world, pos, player)) {
            player.incrementStat(CppStats.INTERACT_WITH_BLOCK_BREAKER);
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
        if (!world.isClient() && pos.down().equals(fromPos)) {
            BlockEntity blockEntity = world.getBlockEntity(fromPos.down());
            if (blockEntity instanceof PistonBlockEntity) {
                PistonBlockEntity pistonBlockEntity = (PistonBlockEntity) blockEntity;
                if (!pistonBlockEntity.isExtending() && pistonBlockEntity.getFacing() == Direction.UP && pistonBlockEntity.getPushedBlock().getBlock() instanceof PistonBlock) {
                    breakBlock(world, pos, null);
                }
            }
        }
        super.neighborUpdate(state, world, pos, block, fromPos, notify);
    }

    public boolean breakBlock(World world, BlockPos pos, @Nullable PlayerEntity player) {
        BlockState blockState = world.getBlockState(pos.up());
        Block block = blockState.getBlock();
        if (PRODUCES.containsKey(block)) {
            if (!world.isClient()){
                Pair<Block, Identifier> pair = PRODUCES.get(block);
//                CodingTool.playSound(world,new PlaySoundS2CPacket(block.getSoundGroup(block.getDefaultState()).getBreakSound(), SoundCategory.BLOCKS, pos.getX(), pos.getY(), pos.getZ(), 1, 1) );
                if (pair.getLeft()!=null){
                    world.breakBlock(pos.up(),false);
                    world.setBlockState(pos.up(),pair.getLeft().getDefaultState());
                } else {
                    Identifier lootTableId = pair.getRight();
                    Vec3d vec3d = new Vec3d(pos.getX()+0.5, pos.getY()+1,pos.getZ()+0.5);
                    Utils.drop(world, vec3d, world.getServer().getLootManager().getTable(lootTableId).generateLoot((new LootContext.Builder((ServerWorld) world)).random(world.random).build(LootContextTypes.EMPTY)));
                    world.breakBlock(pos.up(),false);
                }
            }
            return true;
        }
        return false;
    }
    static {
        PRODUCES.put(COBBLESTONE, new Pair<>(SAND, null));
        PRODUCES.put(STONE, new Pair<>(GRAVEL, null));
        PRODUCES.put(SOUL_SOIL, new Pair<>(SOUL_SAND,null ));
        for (Block block: new Block[] {DIRT,SOUL_SAND,GRAVEL,SAND}) {
            PRODUCES.put(block, new Pair<>(null, new Identifier(Craftingpp.MOD_ID3, "misc/"+ID+"/"+ Registry.BLOCK.getId(block).getPath())));
        }
    }
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }
    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BlockBreakerBlockEntity(pos,state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(world, type, CppBlockEntities.BLOCK_BREAKER);
    }

    @Nullable
    protected static <T extends BlockEntity> BlockEntityTicker<T> checkType(World world, BlockEntityType<T> givenType, BlockEntityType<? extends BlockBreakerBlockEntity> expectedType) {
        return world.isClient ? null : checkType(givenType, expectedType, BlockBreakerBlockEntity::tick);
    }

}
