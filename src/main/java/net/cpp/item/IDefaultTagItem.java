package net.cpp.item;

import net.minecraft.nbt.CompoundTag;

public interface IDefaultTagItem {
	/**
	 * 添加默认NBT
	 * @param tag 要被添加的tag
	 * @return 添加之后的tag，与参数相同
	 */
	CompoundTag modifyDefaultTag(CompoundTag tag);
}
