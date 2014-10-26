/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.data;

import java.awt.Image;

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
		IProcessAPixelIntCoordinate method = new IProcessAPixelIntCoordinate() {
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

				int up = (int) Math.floor(x),
						down = Math.min((int) Math.ceil(x), height - 1),
						left = (int) Math.floor(y),
						right = Math.min((int) Math.ceil(y), width - 1);		

				/*
				 * 参考双线性插值公式。注pixel[0]~pixel[3]如图示: 
				 * |0 1|
				 * |2 3|
				 */
				int[][] pixel = new int[][] {
						decARGB(rgbs[up * width + left]),
						decARGB(rgbs[up * width + right]),
						decARGB(rgbs[down * width + left]),
						decARGB(rgbs[down * width + right]),

				};
				int[] newArgb = new int[4];
				for (int i = 0; i < 4; i++) {
					newArgb[i] = (int) Math.round(
							(1 - u) * (1 - v) * pixel[0][i] + 
							(1 - u) * v * pixel[1][i] +
							u * (1 - v) * pixel[2][i] +
							u * v * pixel[3][i]);
				}
				int result = comARGB(newArgb[0], newArgb[1], newArgb[2],
						newArgb[3]);
				return result;
			}
		};
		return process(image, width, height, method);
	}

	// --------------- 单例类属性、方法 ---------------

	static private ProcessAdjust instance;

	/**
	 * 获取ImageAdjust唯一实例。
	 * 
	 * @return ImageAdjust的实例
	 */
	static public ProcessAdjust getInstance() {
		if (instance == null) {
			instance = new ProcessAdjust();
		}
		return instance;
	}
}
