package cpp.api;

import net.minecraft.nbt.NbtCompound;

public interface IDefaultTagItem {
	/**
	 * 添加默认NBT
	 * @param tag 要被添加的tag
	 * @return 添加之后的tag，与参数相同
	 */
	NbtCompound modifyDefaultTag(NbtCompound tag);
}
