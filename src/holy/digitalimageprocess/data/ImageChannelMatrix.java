package holy.digitalimageprocess.data;

/**
 * 用于表达、计算一个图像的一个通道M*N的二维矩阵。<br>
 * 矩阵可以表示一个灰度图像，或者一个彩色图像的一个通道。<br>
 * 这个矩阵是一个浮点矩阵，如需获取int[]，请使用toIntArray方法。<br>
 * 这个矩阵的下标从（0,0）开始计算，到（row-1, column-1)结束。
 */
public class ImageChannelMatrix {
    /**
     * 矩阵的行数
     */
    private int row = 0;
    /**
     * 矩阵的列数
     */
    private int column = 0;
    private double[][] matrixData = null;

    /**
     * 构造一个矩阵。如果row或者column为非正数或数组长度不匹配 ，抛出非法参数异常IllegalArgumentException。
     * 
     * @param row
     *            这个矩阵的行数，是大于0的整数值。
     * @param column
     *            这个矩阵的列数，是大于0的整数值
     * @param entity
     *            包含这个矩阵中元素实体的整数数组，大小必须为row * column.
     */
    public ImageChannelMatrix(int row, int column, double[] entity)
            throws IllegalArgumentException {
        this(row, column);
        this.setEntity(entity);
    }

    /**
     * 构造一个矩阵。如果row或者column为非正数或数组长度不匹配 ，抛出非法参数异常IllegalArgumentException。
     * 
     * @param row
     *            这个矩阵的行数，是大于0的整数值。
     * @param column
     *            这个矩阵的列数，是大于0的整数值
     * @param defaultValue
     *            矩阵每一个元素的默认值。
     */
    public ImageChannelMatrix(int row, int column, double defaultValue)
            throws IllegalArgumentException {
        this(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrixData[i][j] = defaultValue;
            }
        }
    }

    /**
     * 构造一个矩阵。如果row或者column为非正数 ，抛出非法参数异常IllegalArgumentException。
     * 
     * @param row
     *            这个矩阵的行数，是大于0的整数值。
     * @param column
     *            这个矩阵的列数，是大于0的整数值
     */
    public ImageChannelMatrix(int row, int column)
            throws IllegalArgumentException {
        if (row <= 0) {
            throw new IllegalArgumentException("Row <= 0");
        }
        this.row = row;
        if (column <= 0) {
            throw new IllegalArgumentException("Column <= 0");
        }
        this.column = column;
        this.matrixData = new double[row][column];
    }

    /**
     * 通过一个int数组设置这个矩阵中的元素实体。当数组大小与矩阵不匹配时 ，抛出IllegalArgumentException异常。
     * 
     * @param entity
     *            用于设置矩阵元素的数组，大小必须为row * column.
     */
    public void setEntity(double[] entity) throws IllegalArgumentException {
        if (entity.length != row * column) {
            throw new IllegalArgumentException(
                    "Size of array is not compatible");
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                matrixData[i][j] = entity[i * column + j];
            }
        }
    }

    /**
     * 获取矩阵行数
     * 
     * @return 返回矩阵行数
     */
    public int getRow() {
        return row;
    }

    /**
     * 获取矩阵列数
     * 
     * @return 返回矩阵列数
     */
    public int getColumn() {
        return column;
    }

    /**
     * 获取矩阵第x行第y个元素。如果x 、y越界，会抛出ArrayIndexOutOfBoundsException
     * 
     * @param x
     *            矩阵元素的行坐标，从0开始
     * @param y
     *            矩阵元素的列坐标，从0开始
     * @return 矩阵第x行第y列的元素
     */
    public double getEntity(int x, int y) throws ArrayIndexOutOfBoundsException {
        if (x >= row || y >= column || x < 0 || y < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return matrixData[x][y];
    }

    /**
     * 设置矩阵第x行第y个元素。如果x 、y越界，会抛出ArrayIndexOutOfBoundsException
     * 
     * @param x
     *            矩阵元素的行坐标，从0开始
     * @param y
     *            矩阵元素的列坐标，从0开始
     * @param value
     *            设定的值
     */
    public void setEntity(int x, int y, int value)
            throws ArrayIndexOutOfBoundsException {
        if (x >= row || y >= column || x < 0 || y < 0) {
            throw new ArrayIndexOutOfBoundsException();
        }
        matrixData[x][y] = value;
    }

    /**
     * 将矩阵的数据转化为一个int数组，便于转化为Image。
     * 
     * @return matrix向量化
     */
    public int[] toIntArray() {
        int[] result = new int[row * column];
        int temp;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                temp = (int) matrixData[i][j];
                // 进行标定，避免溢出
                if (temp < 0) {
                    result[i * column + j] = 0;
                } else if (temp > 255) {
                    result[i * column + j] = 255;
                } else {
                    result[i * column + j] = temp;
                }
            }
        }
        return result;
    }

    /**
     * 获得矩阵中的最大值。
     * 
     * @return 矩阵中的最大值
     */
    public double getMaxValue() {
        double max = matrixData[0][0];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                max = Math.max(max, matrixData[i][j]);
            }
        }
        return max;
    }

    /**
     * 获取处于某一百分比的值
     * 
     * @param percent
     *            百分比值
     * @return 近似处于该百分比的近似值
     */
    public double getValueAt(double percent) {
        int[] h = histogram();
        double count = 0;
        int indensity;
        for (indensity = 0; indensity < 256; indensity++) {
            count += h[indensity];
            if (count / (row * column) >= percent) {
                break;
            }
        }
        return indensity;
    }

    /**
     * 对矩阵进行最小值滤波，并将结果存入新的矩阵中，并返回，原矩阵不会发生改变。
     * 
     * @param r
     *            滤波器的半径，滤波的窗口大小为 (2r + 1) * (2r + 1)
     * @return 经过滤波之后的矩阵
     */
    public ImageChannelMatrix minFitler(int r) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        double min;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                min = matrixData[i][j];
                for (int s = -r; s <= r; s++) {
                    // 越界中断
                    if (i + s < 0 || i + s >= row) {
                        continue;
                    }
                    for (int t = -r; t <= r; t++) {
                        if (j + t < 0 || j + t >= column) {
                            continue;
                        }
                        min = min > matrixData[i + s][j + t] ? matrixData[i + s][j
                                + t]
                                : min;
                    }
                    result.matrixData[i][j] = min;
                }
            }
        }
        return result;
    }

    /**
     * 基于[0, 255]共256个整数区间计算矩阵的直方图。
     * 
     * @return 矩阵的直方图
     */
    public int[] histogram() {
        int[] hist = new int[256];
        int[] intVectorOfMatrix = toIntArray();
        for (int i = 0; i < intVectorOfMatrix.length; i++) {
            hist[intVectorOfMatrix[i]]++;
        }
        return hist;
    }

    /**
     * 包滤波。给定窗口大小，快速将窗口内的所有数值累加，结果作为滤波结果储存到新的矩阵中。 原矩阵不会发生变化。 <br>
     * Ref: http://blog.csdn.net/lanbing510/article/details/28696833
     * 
     * @param r
     *            窗口半径，除边缘外滤波窗口大小为(2r + 1) * (2r + 1)。
     * @return box滤波后的结果.
     */
    public ImageChannelMatrix boxFilter(int r) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        // 临时存放每一个列连续的2r * 1个元素的和
        double[] columnTemp = new double[column];
        // 初始化第一行
        for (int i = 0; i < column; i++) {
            for (int j = 0; j <= r; j++) {
                // 第一行，越界直接中断
                if (j >= row) {
                    break;
                }
                columnTemp[i] += matrixData[j][i];
            }
        }
        // 计算(0, 0)的结果
        for (int j = 0; j <= r; j++) {
            // 越界终止
            if (j >= column) {
                break;
            }
            result.matrixData[0][0] += columnTemp[j];
        }

        // 计算第一行结果
        for (int i = 1; i < column; i++) {
            result.matrixData[0][i] = result.matrixData[0][i - 1];
            if (i - r - 1 >= 0) {
                // 减去最左端一个
                result.matrixData[0][i] -= columnTemp[i - r - 1];
            }
            if (i + r < column) {
                // 加上新的
                result.matrixData[0][i] += columnTemp[i + r];
            }
        }
        // 处理后续的行
        for (int i = 1; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (i - r - 1 >= 0) {
                    // 减去上一次的
                    columnTemp[j] -= matrixData[i - r - 1][j];
                }
                if (i + r < row) {
                    // 加上新的一行的
                    columnTemp[j] += matrixData[i + r][j];
                }
            }
            // 计算(i, 0)的结果
            for (int j = 0; j <= r; j++) {
                // 越界终止
                if (j >= column) {
                    break;
                }
                result.matrixData[i][0] += columnTemp[j];
            }

            // 计算第i行结果
            for (int j = 1; j < column; j++) {
                result.matrixData[i][j] = result.matrixData[i][j - 1];
                if (j - r - 1 >= 0) {
                    // 减去最左端一个
                    result.matrixData[i][j] -= columnTemp[j - r - 1];
                }
                if (j + r < column) {
                    // 加上新的
                    result.matrixData[i][j] += columnTemp[j + r];
                }
            }
        }
        return result;
    }

    /**
     * 对矩阵进行导向滤波，不会改变矩阵本身。
     * 
     * @param guide
     *            导向图。
     * @return 导向滤波的结果。
     */
    public ImageChannelMatrix guideFilter(ImageChannelMatrix guide) {
        int height = guide.getRow();
        int width = guide.getColumn();
        // 半径都使用120, 是最小值滤波的8倍
        int r = 120;
        ImageChannelMatrix N = new ImageChannelMatrix(height, width, 1)
                .boxFilter(r);
        ImageChannelMatrix meanI = guide.boxFilter(r).dotDivision(N);
        ImageChannelMatrix meanP = this.boxFilter(r).dotDivision(N);
        ImageChannelMatrix meanIP = guide.dotMultiplication(this).boxFilter(r)
                .dotDivision(N);
        ImageChannelMatrix covIP = meanIP.dotSubstraction(meanI
                .dotMultiplication(meanP));

        ImageChannelMatrix meanII = guide.dotMultiplication(guide).boxFilter(r)
                .dotDivision(N);
        ImageChannelMatrix varI = meanII.dotSubstraction(meanI
                .dotMultiplication(meanI));

        // 取eps = 0.001
        ImageChannelMatrix a = covIP.dotDivision(varI.dotAddition(0.001));
        ImageChannelMatrix b = meanP
                .dotSubstraction(a.dotMultiplication(meanI));

        ImageChannelMatrix meanA = a.boxFilter(r).dotDivision(N);
        ImageChannelMatrix meanB = b.boxFilter(r).dotDivision(N);

        return meanA.dotMultiplication(guide).dotAddition(meanB);
    }

    /**
     * 对矩阵进行点加操作。如果matrix1的维度和当前矩阵维度不匹配，则抛出IllegalArgument异常。 原矩阵不会改变。
     * 
     * @param matrix1
     *            参与点加的第二个矩阵。
     * @return 点加得到矩阵结果
     */
    public ImageChannelMatrix dotAddition(ImageChannelMatrix matrix1) {
        if (this.row != matrix1.row || matrix1.column != this.column) {
            throw new IllegalArgumentException("矩阵维度不一致");
        }
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j]
                        + matrix1.matrixData[i][j];
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点减操作。如果matrix1的维度和当前矩阵维度不匹配，则抛出IllegalArgument异常。 原矩阵不会改变。
     * 
     * @param matrix1
     *            参与点减的减数矩阵。
     * @return 点减得到矩阵结果
     */
    public ImageChannelMatrix dotSubstraction(ImageChannelMatrix matrix1) {
        if (this.row != matrix1.row || matrix1.column != this.column) {
            throw new IllegalArgumentException("矩阵维度不一致");
        }
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j]
                        - matrix1.matrixData[i][j];
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点乘操作。如果matrix1的维度和当前矩阵维度不匹配，则抛出IllegalArgument异常。 原矩阵不会改变。
     * 
     * @param matrix1
     *            参与点乘的第二个矩阵。
     * @return 点乘得到矩阵结果
     */
    public ImageChannelMatrix dotMultiplication(ImageChannelMatrix matrix1) {
        if (this.row != matrix1.row || matrix1.column != this.column) {
            throw new IllegalArgumentException("矩阵维度不一致");
        }
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j]
                        * matrix1.matrixData[i][j];
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点除操作。如果matrix1的维度和当前矩阵维度不匹配，则抛出IllegalArgument异常。 原矩阵不会改变。
     * 遇到除以0的情况，除数加一个无穷小正值。
     * 
     * @param matrix1
     *            参与点除的被除数矩阵。
     * @return 点除得到矩阵结果
     */
    public ImageChannelMatrix dotDivision(ImageChannelMatrix matrix1) {
        if (this.row != matrix1.row || matrix1.column != this.column) {
            throw new IllegalArgumentException("矩阵维度不一致");
        }
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j]
                        / (matrix1.matrixData[i][j] + Double.MIN_VALUE);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点加操作，使矩阵与一个常数相加。
     * 
     * @param constant
     *            参与点加的常数
     * @return 点加得到矩阵结果
     */
    public ImageChannelMatrix dotAddition(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j] + constant;
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点减操作，用一个常数减去此矩阵。
     * 
     * @param constant
     *            参与点减的常数
     * @return 点减得到矩阵结果
     */
    public ImageChannelMatrix dotSubstraction(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j] - constant;
            }
        }
        return result;
    }

    /**
     * 对矩阵进行被点减操作，用一个常数减去此矩阵。
     * 
     * @param constant
     *            参与点减的常数
     * @return 被点减得到矩阵结果
     */
    public ImageChannelMatrix dotSubstractionReverse(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = constant - this.matrixData[i][j];
            }
        }
        return result;
    }

    /**
     * 对矩阵进行被点乘操作，用一个常数乘此矩阵。
     * 
     * @param constant
     *            参与点乘的常数
     * @return 点乘得到矩阵结果
     */
    public ImageChannelMatrix dotMultiplication(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = this.matrixData[i][j] * constant;
            }
        }
        return result;
    }

    /**
     * 对矩阵进行点除操作，用一个常数除以此矩阵。
     * 
     * @param constant
     *            参与点除的常数
     * @return 点除得到矩阵结果
     */
    public ImageChannelMatrix dotDivision(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = (this.matrixData[i][j])
                        / (constant + Double.MIN_VALUE);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行被点除操作，用一个常数除以此矩阵。
     * 
     * @param constant
     *            参与点除的常数
     * @return 被点除得到矩阵结果
     */
    public ImageChannelMatrix dotDivisionReverse(double constant) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = constant
                        / (this.matrixData[i][j] + Double.MIN_VALUE);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行一元操作。
     * 
     * @param method
     *            一元操作函数
     * @return 操作后的结果
     */
    public ImageChannelMatrix unaryTraverse(TraverseUnary method) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = method.perform(matrixData[i][j]);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行二元操作。
     * 
     * @param matrix1
     *            二元操作的第二个操作数
     * @param method
     *            二元操作函数
     * @return 操作后的结果
     */
    public ImageChannelMatrix binaryTraverse(ImageChannelMatrix matrix1,
            TraverseBinary method) {
        if (this.row != matrix1.row || matrix1.column != this.column) {
            throw new IllegalArgumentException("矩阵维度不一致");
        }
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = method.perform(matrixData[i][j],
                        matrix1.matrixData[i][j]);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行二元操作。
     * 
     * @param constant
     *            二元操作的第二个操作数
     * @param method
     *            二元操作函数
     * @return 操作后的结果
     */
    public ImageChannelMatrix binaryTraverse(double constant,
            TraverseBinary method) {
        ImageChannelMatrix result = new ImageChannelMatrix(row, column);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                result.matrixData[i][j] = method.perform(matrixData[i][j],
                        constant);
            }
        }
        return result;
    }

    /**
     * 对矩阵进行一元遍历操作。
     */
    public interface TraverseUnary {
        /**
         * 对矩阵进行遍历操作
         * 
         * @param arg0
         *            矩阵中的每一个元素
         * @return 处理后的结果
         */
        double perform(double arg0);
    }

    /**
     * 对矩阵进行二元遍历操作。
     */
    public interface TraverseBinary {
        /**
         * 对矩阵进行遍历操作
         * 
         * @param arg0
         *            矩阵0中的每一个元素（运算符左侧）
         * @param arg1
         *            矩阵1中的每一个元素（运算符右侧）
         * @return 运算结果
         */
        double perform(double arg0, double arg1);
    }
}
