package net.cpp.block.entity;

import net.cpp.init.CppBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;

public class VariantSkullBlockEntity extends BlockEntity {
	private String textureName = "";
	public VariantSkullBlockEntity( BlockPos pos, BlockState state) {
		super(CppBlockEntities.VARIANT_SKULL, pos, state);
	}
	
	public String getTextureName() {
		return textureName;
	}
	
	public void setTextureName(String textureName) {
		this.textureName = textureName;
	}
	
	@Override
	public CompoundTag toTag(CompoundTag tag) {
		tag.putString("textureName",getTextureName());
		return super.toTag(tag);
	}
	
	@Override
	public void fromTag(CompoundTag tag) {
		super.fromTag(tag);
		setTextureName(tag.getString("textureName"));
	}
}
