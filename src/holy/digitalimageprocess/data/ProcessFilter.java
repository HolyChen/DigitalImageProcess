package holy.digitalimageprocess.data;

import java.awt.Image;

/**
 * 对图片进行滤镜操作的类。<br>
 * 这是一个只包含对图像处理方法的单例类，请用getInstance()获取对象。
 */
public class ProcessFilter extends IImageProcess {

	/**
	 * 将图像A、和图像B按照radio的透明比例进行叠加。<br>
	 * 当radio是0时，返回的图片与A一致，当radio是1时，与B一致。<br>
	 * A、B两张图片大小必须完全一致。当两张图片大小不一致或者radio不在
	 * [0, 1]时, 抛出IllegalArgumentException.
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
	 * @param image 要进性模糊的图像
	 * @param size 模糊选取大小
	 * @return 模糊后的图像
	 */
	public Image blur(Image image, int size) {
		// 先构造滤镜算子矩阵
		int row = size;
		int column = size;
		double scaling = 1.0 / (row * column);
		int[] entity = new int[row * column];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < column; j++) {
				entity[i * column + j] = 1;
			}
		}
		return convolution(image, new FilterMatrix(row, column, scaling, entity));
	}
	
	/**
	 * 对图像进行锐化操作。
	 * @param image 要进行锐化的图像
	 * @return 锐化后的图像
	 */
	public Image sharpen(Image image) {
		// 先构造滤镜算子矩阵，这是一个2阶拉普拉斯算子
		int row = 3;
		int column = 3;
		double scaling = 1.0;
		int[] entity = new int[] {
				1,  1, 1,
				1, -7, 1,
				1,  1, 1
		};
		return convolution(image, new FilterMatrix(row, column, scaling, entity));
	}
	
	/**
	 * 对图像Sobel横向滤波
	 * @param image 要进行锐化的图像
	 * @return 锐化后的图像
	 */
	public Image sobelHorizontal(Image image) {
		// 先构造滤镜算子矩阵，这是一个Soble横向算子
		int row = 3;
		int column = 3;
		double scaling = 1.0;
		int[] entity = new int[] {
				-1,  0, 1,
				-2,  0, 2,
				-1,  0, 1
		};
		return convolution(image, new FilterMatrix(row, column, scaling, entity));
	}
	
	/**
	 * 对图像Sobel纵向滤波
	 * @param image 要进行锐化的图像
	 * @return 锐化后的图像
	 */
	public Image sobelVertical(Image image) {
		// 先构造滤镜算子矩阵，这是一个Soble横向算子
		int row = 3;
		int column = 3;
		double scaling = 1.0;
		int[] entity = new int[] {
				-1,  -2, -1,
				 0,   0,  0,
				 1,   2,  1
		};
		return convolution(image, new FilterMatrix(row, column, scaling, entity));
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
