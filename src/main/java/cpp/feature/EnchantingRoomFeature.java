package cpp.feature;

import static cpp.Craftingpp.MOD_ID3;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.mojang.serialization.Codec;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.SimpleStructurePiece;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureStart;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class EnchantingRoomFeature extends StructureFeature<DefaultFeatureConfig> {
	public EnchantingRoomFeature(Codec<DefaultFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory() {
		return Start::new;
	}

	public static class Start extends StructureStart<DefaultFeatureConfig> {
		public Start(StructureFeature<DefaultFeatureConfig> structureFeature, ChunkPos chunkPos, int references, long seed) {
			super(structureFeature, chunkPos, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, ChunkPos pos, Biome biome, DefaultFeatureConfig config, HeightLimitView world) {
			int x = ChunkSectionPos.getBlockCoord(pos.getStartX());
			int z = ChunkSectionPos.getBlockCoord(pos.getStartZ());
			int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG, world);
			BlockPos blockPos = new BlockPos(x, y, z);
			BlockRotation blockRotation = BlockRotation.random(random);
			Generator.addPieces(manager, blockPos, blockRotation, this.children);
			this.setBoundingBoxFromChildren();

		}
	}

	public static class Generator {
		public static final Identifier STRUCTURE_ID = new Identifier(MOD_ID3, "enchanting_room");

		public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces) {
			pieces.add(new Piece(manager, pos, STRUCTURE_ID, rotation));
		}
	}

	public static class Piece extends SimpleStructurePiece {
		public static final StructurePieceType PIECE_TYPE = Piece::new;
		private final BlockRotation rotation;
		private final Identifier template;

		public Piece(StructureManager structureManager, NbtCompound nbtCompound) {
			super(PIECE_TYPE, nbtCompound);
			rotation = BlockRotation.valueOf(nbtCompound.getString("Rot"));
			template = new Identifier(nbtCompound.getString("Template"));
			initializeStructureData(structureManager);
		}

		public Piece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation) {
			super(PIECE_TYPE, 0);
			this.pos = pos;
			this.rotation = rotation;
			this.template = template;
			initializeStructureData(structureManager);
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random, BlockBox boundingBox) {
			// TODO 自动生成的方法存根
//			System.out.println(this.boundingBox);
		}

		private void initializeStructureData(StructureManager structureManager) {
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData placementData = (new StructurePlacementData()).setRotation(this.rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, placementData);
			
		}

		protected void toNbt(NbtCompound tag) {
			super.toNbt(tag);
			tag.putString("Template", Optional.of(template).orElseThrow(NullPointerException::new).toString());
			tag.putString("Rot", Optional.of(rotation).orElseThrow(NullPointerException::new).name());
		}
	}
}
