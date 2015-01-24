package holy.digitalimageprocess.data;

import holy.digitalimageprocess.data.ImageChannelMatrix.TraverseBinary;

import java.awt.Image;

/**
 * 对图片进行滤镜操作的类。<br>
 * 这是一个只包含对图像处理方法的单例类，请用getInstance()获取对象。
 */
public class ProcessFilter extends IImageProcess {

    /**
     * 将图像A、和图像B按照radio的透明比例进行叠加。<br>
     * 当radio是0时，返回的图片与A一致，当radio是1时，与B一致。<br>
     * A、B两张图片大小必须完全一致。当两张图片大小不一致或者radio不在 [0, 1]时, 抛出IllegalArgumentException.
     * 
     * @param imageA
     *            要进行叠加的图片A。
     * @param imageB
     *            要进行叠加的图片B。
     * @param radio
     *            两张图片的叠加比例。
     * @return 叠加后的图片。
     */
    public Image imageOverlay(Image imageA, Image imageB, double radio)
            throws IllegalArgumentException {
        if (imageA.getWidth(null) != imageB.getWidth(null)
                || imageA.getHeight(null) != imageB.getHeight(null)) {
            throw new IllegalArgumentException("图像A与B的大小不一致!");
        }
        if (radio < 0 || radio > 1) {
            throw new IllegalArgumentException("叠加比例不在[0, 1]范围内!");
        }

        int[] argbB = getPixelMatrix(imageB);

        IProcessAPixelIndex method = new IProcessAPixelIndex() {

            @Override
            public int process(int[] argbs, int index) {
                // 将一个argb整数解析成A、R、G、B数组
                int[] argbArrayA = decARGB(argbs[index]);
                int[] argbArrayB = decARGB(argbB[index]);
                int[] result = new int[4];
                for (int i = 0; i < 4; i++) {
                    // 四舍五入保证正确性
                    result[i] = (int) Math.round(argbArrayA[i] * (1 - radio)
                            + argbArrayB[i] * radio);
                }
                // 重新压缩为一个argb整数
                return comARGB(result[0], result[1], result[2], result[3]);
            }
        };
        return process(imageA, method);
    }

    /**
     * 对图像进行模糊操作。
     * 
     * @param image
     *            要进性模糊的图像
     * @param size
     *            模糊选取大小
     * @return 模糊后的图像
     */
    public Image blur(Image image, int size) {
        // 先构造滤镜算子矩阵
        int row = size;
        int column = size;
        double scaling = 1.0 / (row * column);
        double[] entity = new double[row * column];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                entity[i * column + j] = 1 * scaling;
            }
        }
        return convolution(image, new ImageChannelMatrix(row, column, entity));
    }

    /**
     * 对图像进行锐化操作。
     * 
     * @param image
     *            要进行锐化的图像
     * @return 锐化后的图像
     */
    public Image sharpen(Image image) {
        // 先构造滤镜算子矩阵，这是一个2阶拉普拉斯算子
        int row = 3;
        int column = 3;
        double[] entity = new double[] { 1, 1, 1, 1, -7, 1, 1, 1, 1 };
        return convolution(image, new ImageChannelMatrix(row, column, entity));
    }

    /**
     * 对图像Sobel横向滤波
     * 
     * @param image
     *            要进行锐化的图像
     * @return 锐化后的图像
     */
    public Image sobelHorizontal(Image image) {
        // 先构造滤镜算子矩阵，这是一个Soble横向算子
        int row = 3;
        int column = 3;
        double[] entity = new double[] { -1, 0, 1, -2, 0, 2, -1, 0, 1 };
        return convolution(image, new ImageChannelMatrix(row, column, entity));
    }

    /**
     * 对图像Sobel纵向滤波
     * 
     * @param image
     *            要进行锐化的图像
     * @return 锐化后的图像
     */
    public Image sobelVertical(Image image) {
        // 先构造滤镜算子矩阵，这是一个Soble横向算子
        int row = 3;
        int column = 3;
        double[] entity = new double[] { -1, -2, -1, 0, 0, 0, 1, 2, 1 };
        return convolution(image, new ImageChannelMatrix(row, column, entity));
    }

    /**
     * 对图像进行去雾操作。 <br>
     * Ref: http://www.cnblogs.com/Imageshop/p/3281703.html
     * 
     * @param image
     *            要进行去雾的图像
     * @return 去雾后的图像
     */
    public Image dehaze(Image image) {
        int[] argbs = getPixelMatrix(image);
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        double[][] argbsArray = decARGBSDouble(argbs);
        int[] a = new int[argbs.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = 255;
        }
        // 计算暗通道
        ImageChannelMatrix r = new ImageChannelMatrix(height, width,
                argbsArray[1]);
        ImageChannelMatrix g = new ImageChannelMatrix(height, width,
                argbsArray[2]);
        ImageChannelMatrix b = new ImageChannelMatrix(height, width,
                argbsArray[3]);
        // 计算暗通道
        ImageChannelMatrix dark = getDark(r, g, b);
        // 计算全球大气光
        double[] AC = getAC(dark, r, g, b);
        double maxAr = AC[0], maxAg = AC[1], maxAb = AC[2];
        // 计算折射率
        ImageChannelMatrix t = getTransmission(AC, dark, r, g, b);
        // 计算初始图像
        ImageChannelMatrix JR = r.dotSubstraction(maxAr).dotDivision(t)
                .dotAddition(maxAr);
        ImageChannelMatrix JG = g.dotSubstraction(maxAg).dotDivision(t)
                .dotAddition(maxAg);
        ImageChannelMatrix JB = b.dotSubstraction(maxAb).dotDivision(t)
                .dotAddition(maxAb);
        // 曝光增强
        // JR = JR.dotMultiplication(255.0 / JR.getValueAt(0.999));
        // JG = JG.dotMultiplication(255.0 / JG.getValueAt(0.999));
        // JB = JB.dotMultiplication(255.0 / JB.getValueAt(0.999));
        return makeNewImage(width, height,
                comARGBS(a, JR.toIntArray(), JG.toIntArray(), JB.toIntArray()));
    }

    /**
     * 仅获取图像的折射率。
     * 
     * @param image
     *            要获取折射率的图像
     * @return 图像的折射率。
     */
    public Image transsmision(Image image) {
        int[] argbs = getPixelMatrix(image);
        int height = image.getHeight(null);
        int width = image.getWidth(null);
        double[][] argbsArray = decARGBSDouble(argbs);
        int[] a = new int[argbs.length];
        for (int i = 0; i < a.length; i++) {
            a[i] = 255;
        }
        // 计算暗通道
        ImageChannelMatrix r = new ImageChannelMatrix(height, width,
                argbsArray[1]);
        ImageChannelMatrix g = new ImageChannelMatrix(height, width,
                argbsArray[2]);
        ImageChannelMatrix b = new ImageChannelMatrix(height, width,
                argbsArray[3]);
        // 计算暗通道
        ImageChannelMatrix dark = getDark(r, g, b);
        // 计算全球大气光
        double[] AC = getAC(dark, r, g, b);
        // 计算折射率
        ImageChannelMatrix t = getTransmission(AC, dark, r, g, b);
        // 计算初始图像
        ImageChannelMatrix T = t.dotMultiplication(255);
        return makeNewImage(width, height,
                comARGBS(a, T.toIntArray(), T.toIntArray(), T.toIntArray()));
    }

    /**
     * 计算折射率。
     * 
     * @param AC
     *            全球大气光
     * @param dark
     *            暗通道矩阵
     * @param r
     *            红色通道矩阵
     * @param g
     *            绿色通道矩阵
     * @param b
     *            蓝色通道矩阵
     * @return 折射率
     */
    private ImageChannelMatrix getTransmission(double[] AC,
            ImageChannelMatrix dark, ImageChannelMatrix r,
            ImageChannelMatrix g, ImageChannelMatrix b) {
        TraverseBinary min = (arg0, arg1) -> {
            return Math.min(arg0, arg1);
        };
        // 窗口半径
        int radiu = 15;
        double maxAr = AC[0], maxAg = AC[1], maxAb = AC[2];
        ImageChannelMatrix t = r.dotDivision(maxAr).minFitler(radiu)
                .binaryTraverse(g.dotDivision(maxAg).minFitler(radiu), min);
        t = b.dotDivision(maxAb).minFitler(radiu).binaryTraverse(t, min);
        // 乘以0.95
        t = t.dotMultiplication(0.95);
        // 1 - t
        t = t.dotSubstractionReverse(1.0);
        // 折射率去0操作，最小值默认为0.1
        t = t.binaryTraverse(0.1, (arg0, arg1) -> {
            return Math.max(arg0, arg1);
        });
        ImageChannelMatrix average = r.dotAddition(g).dotAddition(b)
                .dotDivision(3 * 255);
        return t.guideFilter(average);
    }

    /**
     * 获取黑通道。
     * 
     * @param r
     *            红色通道矩阵
     * @param g
     *            绿色通道矩阵
     * @param b
     *            蓝色通道矩阵
     * @return 黑通道矩阵
     */
    private ImageChannelMatrix getDark(ImageChannelMatrix r,
            ImageChannelMatrix g, ImageChannelMatrix b) {
        TraverseBinary min = (arg0, arg1) -> {
            return Math.min(arg0, arg1);
        };
        // 窗口半径
        int radiu = 15;
        ImageChannelMatrix dark = r.minFitler(radiu).binaryTraverse(g.minFitler(radiu),
                min);
        dark = b.minFitler(radiu).binaryTraverse(dark, min);
        return dark;
    }

    /**
     * 获取全球大气光值。
     * 
     * @param dark
     *            暗通道
     * @param r
     *            红色通道
     * @param g
     *            绿色通道
     * @param b
     *            蓝色通道
     * @return 全球大气光值，依次为Ar, Ag, Ab
     */
    private double[] getAC(ImageChannelMatrix dark, ImageChannelMatrix r,
            ImageChannelMatrix g, ImageChannelMatrix b) {
        int height = dark.getRow();
        int width = dark.getColumn();
        // 通过直方图求A
        double maxAr = 0, maxAg = 0, maxAb = 0;
        int[] histogram = dark.histogram();
        int indensity;
        int count = 0;
        // 统计最高的0.1%
        for (indensity = 255; indensity >= 0; indensity--) {
            count += histogram[indensity];
            if (count >= width * height * 0.001) {
                break;
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (dark.getEntity(i, j) >= indensity) {
                    maxAr = Math.max(maxAr, r.getEntity(i, j));
                    maxAg = Math.max(maxAg, g.getEntity(i, j));
                    maxAb = Math.max(maxAb, b.getEntity(i, j));
                }
            }
        }
        // 限制A值最大为255
        int maxValue = 255;
        return new double[] { Math.min(maxAr, maxValue),
                Math.min(maxAg, maxValue), Math.min(maxAb, maxValue) };
    }

    private static ProcessFilter instance = null;

    /**
     * 获取全局实例。
     * 
     * @return ProcessFilter类的全局实例。
     */
    public static ProcessFilter getInstance() {
        if (instance == null) {
            instance = new ProcessFilter();
        }
        return instance;
    }

}
