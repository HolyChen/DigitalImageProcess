package holy.digitalimageprocess.data;

/**
 * 用于卷积的图像过滤器。<br>
 * 这个矩阵是一个整数矩阵，但是可以使用浮点数放缩系数对矩阵整体进行放缩。<br>
 * 这个矩阵的下标从（0,0）开始计算，到（row-1, column-1)结束。
 */
public class FilterMatrix {
	/**
	 * 矩阵的行数
	 */
	private int row = 0;
	/**
	 * 矩阵的列数
	 */
	private int column = 0;
	/**
	 * 对矩阵整体的放缩因子，即对这个矩阵所有元素乘以scaling。
	 */
	private double scaling = 1.0;
	private int[][] matrix = null;

	/**
	 * 构造一个图像过滤器矩阵，通过矩阵的行列还有放缩系数。
	 * 
	 * @param row
	 *            这个矩阵的行数，是大于0的整数值。
	 * @param column
	 *            这个矩阵的列数，是大于0的整数值
	 * @param scaling
	 *            这个矩阵的放缩系数，即整个矩阵的值乘以这个数，是任意实数。
	 * @param entity
	 *            包含这个矩阵中元素实体的整数数组，大小必须为row * column.
	 * @throws 如果row或者column为非正数或数组长度不匹配
	 *             ，抛出非法参数异常IllegalArgumentException。
	 */
	public FilterMatrix(int row, int column, double scaling, int[] entity)
			throws IllegalArgumentException {
		this(row, column, scaling);
		this.setEntity(entity);
	}

	/**
	 * 构造一个图像过滤器矩阵。
	 * 
	 * @param row
	 *            这个矩阵的行数，是大于0的整数值。
	 * @param column
	 *            这个矩阵的列数，是大于0的整数值
	 * @param scaling
	 *            这个矩阵的放缩系数，即整个矩阵的值乘以这个数，是任意实数。
	 * @throws 如果row或者column为非正数
	 *             ，抛出非法参数异常IllegalArgumentException。
	 */
	public FilterMatrix(int row, int column, double scaling)
			throws IllegalArgumentException {
		if (row <= 0) {
			throw new IllegalArgumentException("Row <= 0");
		}
		this.row = row;
		if (column <= 0) {
			throw new IllegalArgumentException("Column <= 0");
		}
		this.column = column;
		this.scaling = scaling;
		this.matrix = new int[row][column];
	}

	/**
	 * 通过一个int数组设置这个矩阵中的元素实体。
	 * 
	 * @param entity
	 *            用于设置矩阵元素的数组，大小必须为row * column.
	 * @throws 当数组大小与矩阵不匹配时
	 *             ，抛出IllegalArgumentException异常。
	 */
	public void setEntity(int[] entity) throws IllegalArgumentException {
		if (entity.length != row * column) {
			throw new IllegalArgumentException(
					"Size of array is not compatible");
		}
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				matrix[i][j] = entity[i * column + j];
			}
		}
	}
	
	/**
	 * 获取矩阵行数
	 * @return 返回矩阵行数
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * 获取矩阵列数
	 * @return 返回矩阵列数
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * 获取放缩系数
	 * @return 放缩系数
	 */
	public double getScaling() {
		return scaling;
	}
	
	/**
	 * 获取矩阵第x行第y个元素。
	 * @param x 矩阵元素的行坐标，从0开始
	 * @param y 矩阵元素的列坐标，从0开始
	 * @return 矩阵第x行第y列的元素
	 * @throws 如果x、y越界，会抛出ArrayIndexOutOfBoundsException
	 */
	public int getEntity(int x, int y) throws ArrayIndexOutOfBoundsException {
		if (x >= row || y >= column || x < 0 || y < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return matrix[x][y];
	}
}
