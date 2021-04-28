package cpp.api;

import net.minecraft.nbt.NbtCompound;

public interface IDefaultNbtItem {
	/**
	 * 添加默认NBT
	 * @param nbt 要被添加的tag
	 * @return 添加之后的tag，与参数相同
	 */
	NbtCompound modifyDefaultNbt(NbtCompound nbt);
}
