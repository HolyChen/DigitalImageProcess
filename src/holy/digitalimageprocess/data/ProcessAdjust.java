/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.data;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * 对ImageData中图像进行调整操作的类。<br>
 * 这个类仅包含方法，是一个单例类。请通过getInstance（)获取对象。
 */
public class ProcessAdjust extends IImageProcess {
	/**
	 * 单例类隐藏构造器
	 */
	private ProcessAdjust() {
	}

	/**
	 * 将图像转化为灰度图像按照指定的灰度阶数，并将新的图像返回
	 * 
	 * @param image
	 *            需要进行处理的图像。
	 * @param level
	 *            灰度值的阶数，包括2, 4, 8, 16, 32, 64, 128, 256.
	 * @return 将的到的灰度图像返回。
	 */
	public Image toBlackWhite(Image image, int level) {
		IProcessAPixelIndex method = new IProcessAPixelIndex() {
			// 对单个像素进行灰度化的方法
			@Override
			public int process(int argbs[], int index) {
				int[] argbArray = IImageProcess.decARGB(argbs[index]);
				// 首先转化为256阶灰度
				// 采用NTSC推荐的彩色图对图像进行灰度处理公式
				int average = (int) Math.round(0.299 * argbArray[1] + 0.587
						* argbArray[2] + 0.114 * argbArray[3]);
				/*
				 * 对256阶灰度按照level进行了量化。 首先256除以阶数进行分段，然后再乘以每一段对应的强度大小。
				 */
				average = (int) ((average / (256 / level)) * (255.0 / (level - 1)));
				return IImageProcess.comARGB(argbArray[0], average, average,
						average);
			}
		};
		return process(image, method);
	}

	/**
	 * 对图像通过“最近邻域插值法”重新设置大小。
	 * 
	 * @param image
	 *            要重新设置大小的图像。
	 * @param width
	 *            新的图片的宽度
	 * @param height
	 *            新的图片的高度
	 * @return 新的图片
	 */
	public Image resizeImageNearest(Image image, int width, int height) {
		IProcessAPixelFloatCoordinate method = new IProcessAPixelFloatCoordinate() {

			@Override
			public int process(int[] rgbs, int width, int height, double x,
					double y) {
				// 自动向左一个像素取值，即邻近插值
				return rgbs[(int) (Math.floor(x) * width + Math.floor(y))];
			}
		};
		return process(image, width, height, method);
	}

	/**
	 * 对图像通过“双线性插值法”重新设置大小。
	 * 
	 * @param image
	 *            要重新设置大小的图像。
	 * @param width
	 *            新的图片的宽度
	 * @param height
	 *            新的图片的高度
	 * @return 新的图片
	 */
	public Image resizeImageBilinear(Image image, int width, int height) {
		IProcessAPixelFloatCoordinate method = new IProcessAPixelFloatCoordinate() {

			@Override
			public int process(int[] rgbs, int width, int height, double x,
					double y) {
				double u = x - Math.floor(x);
				double v = y - Math.floor(y);

				int up = (int) Math.floor(x), down = Math.min(
						(int) Math.ceil(x), height - 1), left = (int) Math
						.floor(y), right = Math.min((int) Math.ceil(y),
						width - 1);

				/*
				 * 参考双线性插值公式。注pixel[0]~pixel[3]如图示: |0 1| |2 3|
				 */
				int[][] pixel = new int[][] { decARGB(rgbs[up * width + left]),
						decARGB(rgbs[up * width + right]),
						decARGB(rgbs[down * width + left]),
						decARGB(rgbs[down * width + right]),

				};
				int[] newArgb = new int[4];
				for (int i = 0; i < 4; i++) {
					newArgb[i] = (int) Math.round((1 - u) * (1 - v)
							* pixel[0][i] + (1 - u) * v * pixel[1][i] + u
							* (1 - v) * pixel[2][i] + u * v * pixel[3][i]);
				}
				int result = comARGB(newArgb[0], newArgb[1], newArgb[2],
						newArgb[3]);
				return result;
			}
		};
		return process(image, width, height, method);
	}

	/**
	 * 统计一个图片的直方图，并依此返回在RGB通道上的直方图统计结果。
	 * 
	 * @param image
	 *            要计算直方图的图像
	 * @return double数组列表，共三项，每一项都是一个256长度的double数组。<br>
	 *         0 - 红色通道统计结果。<br>
	 *         1 - 绿色通道的统计结果。<br>
	 *         2 - 蓝色通道的统计结果。<br>
	 */
	public double[][] statisticHistogram(Image image) {
		int width = image.getWidth(null);
		int heigth = image.getHeight(null);
		int size = width * heigth;
		int[] argbs = getPixelMatrix(image);

		double[] redHistogram = new double[256];
		double[] greenHistogram = new double[256];
		double[] blueHistogram = new double[256];

		// 临时存放解析出的argb数组
		int[] decArgbTemp = new int[4];

		for (int i = 0; i < size; i++) {
			decArgbTemp = decARGB(argbs[i]);
			redHistogram[decArgbTemp[1]]++;
			greenHistogram[decArgbTemp[2]]++;
			blueHistogram[decArgbTemp[3]]++;
		}
		for (int i = 0; i < 256; i++) {
			redHistogram[i] = redHistogram[i] / (size);
			greenHistogram[i] = greenHistogram[i] / (size);
			blueHistogram[i] = blueHistogram[i] / (size);
		}

		return new double[][] { redHistogram, greenHistogram, blueHistogram };
	}

	/**
	 * 对图像进行直方图均衡化。<br>
	 * 均衡化需要已知的直方图统计数据，如果这些统计数据是不完整的，将进行自动统计。
	 * 
	 * @param image
	 *            要进行均衡化的图像。
	 * @param redData
	 *            红色通道的直方图统计数据，应该是一个长度256的数组， 且所有数据都大于等于0，且综合为1的double数组。
	 * @param greenData
	 *            绿色通道的直方图统计数据，应该是一个长度256的数组， 且所有数据都大于等于0，且综合为1的double数组。
	 * @param BlueData
	 *            蓝色通道的直方图统计数据，应该是一个长度256的数组， 且所有数据都大于等于0，且综合为1的double数组。
	 * @return 经过均衡化的图像。
	 */
	public Image histogramEqulization(Image image, double[] redData,
			double[] greenData, double[] BlueData) {
		double[][] channelData = new double[][] { null, null, null };
		// 如果统计数据是残缺的，则进行重新统计
		if (redData == null || greenData == null || BlueData == null) {
			channelData = statisticHistogram(image);
		} else {
			channelData[0] = redData;
			channelData[1] = greenData;
			channelData[2] = BlueData;
		}

		// 对统计数据即概率密度进行累积
		double[][] accumulate = new double[][] { new double[256],
				new double[256], new double[256] };
		for (int i = 0; i < 3; i++) {
			accumulate[i][0] = channelData[i][0];
			for (int j = 1; j < 256; j++) {
				accumulate[i][j] = accumulate[i][j - 1] + channelData[i][j];
			}
		}

		// 均衡化映射表
		int[][] equlizationMap = new int[][] { new int[256], new int[256],
				new int[256] };
		// 按照均衡化算法构造映射表
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 256; j++) {
				equlizationMap[i][j] = (int) (Math
						.round(255 * accumulate[i][j]));
			}
		}

		// 直方图均衡化
		IProcessAPixelIndex method = new IProcessAPixelIndex() {

			@Override
			public int process(int[] argbs, int index) {
				int[] argb = decARGB(argbs[index]);
				for (int i = 1; i < 4; i++) {
					argb[i] = equlizationMap[i - 1][argb[i]];
				}
				return comARGB(argb[0], argb[1], argb[2], argb[3]);
			}
		};
		return process(image, method);
	}

	/**
	 * 对图像进行矩形裁剪
	 * 
	 * @param image
	 *            要进行裁剪的图像
	 * @param originX
	 *            裁剪区域的左上角横坐标
	 * @param originY
	 *            裁剪区域的左上角纵坐标
	 * @param endX
	 *            裁剪区域右下角横坐标
	 * @param endY
	 *            裁剪区域右下角纵坐标
	 * @return 原图像在(orginX, orginY),(endX, endY)构成的矩形区域中的图像（包括边界）。
	 */
	public Image patch(Image image, int originX, int originY, int endX, int endY) {
		int newWidth = endX - originX + 1;
		int newHeight = endY - originY + 1;
		BufferedImage newImage = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < newHeight; i++) {
			for (int j = 0; j < newWidth; j++) {
				newImage.setRGB(j, i, ((BufferedImage) image).getRGB(j
						+ originX, i + originY));
			}
		}
		return newImage;
	}

	// --------------- 单例类属性、方法 ---------------

	private static ProcessAdjust instance;

	/**
	 * 获取ImageAdjust唯一实例。
	 * 
	 * @return ImageAdjust的实例
	 */
	public static ProcessAdjust getInstance() {
		if (instance == null) {
			instance = new ProcessAdjust();
		}
		return instance;
	}
}
