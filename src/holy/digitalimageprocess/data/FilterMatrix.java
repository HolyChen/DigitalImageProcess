package holy.digitalimageprocess.data;

/**
 * ���ھ����ͼ���������<br>
 * ���������һ���������󣬵��ǿ���ʹ�ø���������ϵ���Ծ���������з�����<br>
 * ���������±�ӣ�0,0����ʼ���㣬����row-1, column-1)������
 */
public class FilterMatrix {
	/**
	 * ���������
	 */
	private int row = 0;
	/**
	 * ���������
	 */
	private int column = 0;
	/**
	 * �Ծ�������ķ������ӣ����������������Ԫ�س���scaling��
	 */
	private double scaling = 1.0;
	private int[][] matrix = null;

	/**
	 * ����һ��ͼ�����������ͨ����������л��з���ϵ����
	 * 
	 * @param row
	 *            ���������������Ǵ���0������ֵ��
	 * @param column
	 *            ���������������Ǵ���0������ֵ
	 * @param scaling
	 *            �������ķ���ϵ���������������ֵ�����������������ʵ����
	 * @param entity
	 *            �������������Ԫ��ʵ����������飬��С����Ϊrow * column.
	 * @throws ���row����columnΪ�����������鳤�Ȳ�ƥ��
	 *             ���׳��Ƿ������쳣IllegalArgumentException��
	 */
	public FilterMatrix(int row, int column, double scaling, int[] entity)
			throws IllegalArgumentException {
		this(row, column, scaling);
		this.setEntity(entity);
	}

	/**
	 * ����һ��ͼ�����������
	 * 
	 * @param row
	 *            ���������������Ǵ���0������ֵ��
	 * @param column
	 *            ���������������Ǵ���0������ֵ
	 * @param scaling
	 *            �������ķ���ϵ���������������ֵ�����������������ʵ����
	 * @throws ���row����columnΪ������
	 *             ���׳��Ƿ������쳣IllegalArgumentException��
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
	 * ͨ��һ��int����������������е�Ԫ��ʵ�塣
	 * 
	 * @param entity
	 *            �������þ���Ԫ�ص����飬��С����Ϊrow * column.
	 * @throws �������С�����ƥ��ʱ
	 *             ���׳�IllegalArgumentException�쳣��
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
	 * ��ȡ��������
	 * @return ���ؾ�������
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * ��ȡ��������
	 * @return ���ؾ�������
	 */
	public int getColumn() {
		return column;
	}
	
	/**
	 * ��ȡ����ϵ��
	 * @return ����ϵ��
	 */
	public double getScaling() {
		return scaling;
	}
	
	/**
	 * ��ȡ�����x�е�y��Ԫ�ء�
	 * @param x ����Ԫ�ص������꣬��0��ʼ
	 * @param y ����Ԫ�ص������꣬��0��ʼ
	 * @return �����x�е�y�е�Ԫ��
	 * @throws ���x��yԽ�磬���׳�ArrayIndexOutOfBoundsException
	 */
	public int getEntity(int x, int y) throws ArrayIndexOutOfBoundsException {
		if (x >= row || y >= column || x < 0 || y < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return matrix[x][y];
	}
}
