package cpp.block;

import static net.minecraft.block.Blocks.STRUCTURE_BLOCK;

import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StructureBlock;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.block.enums.StructureBlockMode;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class StructureGenerationBlock extends Block {
	private static final Map<String, Integer> GENERATION_MAP = Maps.newLinkedHashMap();
	private static int weights;

	public StructureGenerationBlock(Settings settings) {
		super(settings);
	}

	@Override
	public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
//		System.out.println("added");
//		new CppFeatures();
//		System.out.println(1);
		if (!world.isClient) {
			world.setBlockState(pos, STRUCTURE_BLOCK.getDefaultState().with(StructureBlock.MODE, StructureBlockMode.LOAD));
			StructureBlockBlockEntity blockEntity = (StructureBlockBlockEntity) world.getBlockEntity(pos);
			blockEntity.setIgnoreEntities(false);
			String stt = random();
//			stt="enchanting_room";
			switch (stt) {
			case "enchanting_room":
				blockEntity.setOffset(new BlockPos(-3, 0, -3));
				blockEntity.setStructureName("cpp:enchanting_room");
				blockEntity.loadStructure((ServerWorld) world);
				blockEntity.loadStructure((ServerWorld) world);
				break;

			default:
				blockEntity.setStructureName(stt);
				break;
			}
		}else {
			MinecraftClient.getInstance().player.sendMessage(new LiteralText("Hi!"), false);
		}
	}
	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
//		System.out.println("repalced");
		super.onStateReplaced(state, world, pos, newState, moved);
		
	}
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		// TODO 自动生成的方法存根
		super.onPlaced(world, pos, state, placer, itemStack);
	}
	@Override
	public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
		// TODO 自动生成的方法存根
		return super.onSyncedBlockEvent(state, world, pos, type, data);
	}
	private static void add(String s, int weight) {
		GENERATION_MAP.put(s, weight);
		weights += weight;
	}

	private static String random() {
		int r = (int) (Math.random() * weights);
		for (Entry<String, Integer> entry : GENERATION_MAP.entrySet()) {
			if (r < entry.getValue()) {
				return entry.getKey();
			} else {
				r -= entry.getValue();
			}
		}
		return "";
	}

	static {
		add("enchanting_room", 1);
		add("totem_pillar", 1);
		add("fruit_tree", 5);
		add("ore_tree", 5);
		add("wool_tree", 5);
		add("saruka_tree", 5);
		add("wheats", 4);
		add("beetroots", 4);
		add("carrots", 4);
		add("potatoes", 4);
		add("flowergrasses", 64);
	}
}
