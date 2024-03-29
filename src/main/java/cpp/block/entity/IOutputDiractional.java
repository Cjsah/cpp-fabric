package cpp.block.entity;

import net.minecraft.util.math.Direction;
/**
 * 拥有方向输出的
 * @author Ph-苯
 *
 */
public interface IOutputDiractional {
	/**
	 * 设置输出方向
	 * 
	 * @see #shiftOutputDir()
	 */
	void setOutputDir(Direction dir);

	/**
	 * 等同于setOutputDir(byteToDir((byte) dirToByte()))
	 * @param b
	 *  
	 * @see #setOutputDir(Direction)
	 * @see #byteToDir(byte)
	 * @see #dirToByte()
	 */
	default void setOutputDir(byte b) {
		setOutputDir(byteToDir((byte) (dirToByte() + 1)));
	}

	/**
	 * 获取当前输出方向
	 * 
	 * @return 输出方向
	 * @see #shiftOutputDir()
	 */
	Direction getOutputDir();

	/**
	 * 切换输出方向，等同于setOutputDir(byteToDir((byte) (dirToByte() + 1)))
	 * 
	 * @see #getOutputDir()
	 */
	default void shiftOutputDir() {
		setOutputDir(byteToDir((byte) (dirToByte(getOutputDir()) + 1)));
	}

	/**
	 * 
	 * @return dirToByte(getOutputDir())
	 * @see #dirToByte(Direction)
	 * @see #getOutputDir()
	 */
	default byte dirToByte() {
		return dirToByte(getOutputDir());
	}

	/**
	 * 将东西南北上下的dir转化为byte，便于储存，如果不是东西南北上下六个方向，则视为东
	 * 
	 * @see #byteToDir(byte)
	 * @param dir
	 * @return
	 */
	static byte dirToByte(Direction dir) {
		switch (dir) {
			case WEST:
				return 1;
			case UP:
				return 2;
			case DOWN:
				return 3;
			case SOUTH:
				return 4;
			case NORTH:
				return 5;
			case EAST:
			default:
				return 0;
		}
	}

	/**
	 * 将b转化为Direction，是dirToByte的反操作，如果b不属于[0,5]则视为0
	 * 
	 * @see #dirToByte(Direction)
	 * @param b
	 * @return f
	 */
	static Direction byteToDir(byte b) {
		switch (b % 6) {
			case 1:
				return Direction.WEST;
			case 2:
				return Direction.UP;
			case 3:
				return Direction.DOWN;
			case 4:
				return Direction.SOUTH;
			case 5:
				return Direction.NORTH;
			case 0:
			default:
				return Direction.EAST;
		}
	}
}
