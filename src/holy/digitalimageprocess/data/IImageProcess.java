/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * 对图像进行处理的类。实现了如获取ImageData中图像的像素矩阵等基本方法。
 */
public abstract class IImageProcess {
	/**
	 * 抽取image中的图像的RGB矩阵，并将其按照从上到下、从左到右的顺序保存 在int数组中并返回。ARGB在一个int中的顺序为A-R-G—B.
	 * 当发生任何错误时， 返回null。
	 * 
	 * @param image
	 *            要抽取像素矩阵的Image
	 * @return imageData中的像素RGB值构成的数组。如果在过程中发生错误，返会null
	 */
	public int[] getPixelMatrix(Image image) {
		try {
			BufferedImage bfImage = (BufferedImage) image;
			// 读取RGB颜色矩阵的数组到rgbs中，提前设置到rgbs的大小
			int height = bfImage.getHeight();
			int width = bfImage.getWidth();
			int[] rgbs = new int[width * height];
			bfImage.getRGB(0, 0, width, height, rgbs, 0, width);
			return rgbs;
		} catch (Exception e) {
			// 发生任意错误后，返回null
			return null;
		}
	}

	/**
	 * 创建新的图片，通过图片的宽度、高度和ARGB数组。<br>
	 * 默认图片的类型为TYPE_INT_RGB.
	 * 
	 * @param width
	 *            新的图片的宽度
	 * @param height
	 *            新的图片的高度
	 * @param rgbs
	 *            新的图片的ARGB数组
	 * @return 新产生的图片
	 */
	public Image makeNewImage(int width, int height, int[] rgbs) {
		try {
			// 储存RGB颜色矩阵rgbs到图像中，提前设置到rgbs的大小
			// 新建一个图片代替的旧的图片
			BufferedImage newBfImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			newBfImage.setRGB(0, 0, width, height, rgbs, 0, width);
			return newBfImage;
		} catch (Exception e) {
			System.out.println("从rgbs中创建新图像发生错误");
		}
		return null;
	}

	/**
	 * 对图像进行逐像素的处理 rgbs是包含了由图像的像素的rgb值构成的数组。<br>
	 * 这个方法不改变图像的大小。
	 * 
	 * @param image
	 *            要进行处理的图像
	 * 
	 * @param method
	 *            用来处理像素元素的方法
	 * 
	 * @return 处理后的图像
	 */
	protected Image process(Image image, IProcessAPixelIndex method) {
		try {
			int[] rgbs = getPixelMatrix(image);
			int[] newRgbs = new int[rgbs.length];
			// 如果数组过长，采用条线程处理，每1500000像素开一条线程，
			// 至多4条。
			int numOfThread = Math.min(rgbs.length / 1500000 + 1, 4);
			ArrayList<Thread> threads = new ArrayList<Thread>();

			// 这里不能够通过变量的方式来创建线程，因为局部类不能访问局部变量
			// 一条线程
			if (numOfThread == 1) {
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, 0, rgbs.length, method);
					}
				}));
			}
			// 两条线程
			if (numOfThread == 2) {
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, 0, rgbs.length / 2, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length / 2,
								rgbs.length, method);
					}
				}));
			}
			// 三条线程
			if (numOfThread == 3) {
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, 0, rgbs.length / 3, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length / 3,
								rgbs.length * 2 / 3, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length * 2 / 3,
								rgbs.length, method);
					}
				}));
			}
			// 四条线程
			if (numOfThread == 4) {
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, 0, rgbs.length / 4, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length / 4,
								rgbs.length * 2 / 4, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length * 2 / 4,
								rgbs.length * 3 / 4, method);
					}
				}));
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, rgbs.length * 3 / 4,
								rgbs.length, method);
					}
				}));
			}
			// 线程开始
			for (Thread t : threads) {
				t.start();
			}
			// 等待线程结束
			for (Thread t : threads) {
				t.join();
			}
			return makeNewImage(image.getWidth(null), image.getHeight(null),
					newRgbs);
		} catch (Exception e) {
			System.out.println("逐像素处理发生错误");
			return null;
		}
	}

	/**
	 * 对图像进行邻域级处理。图像大小不发生改变。
	 * 
	 * @param image
	 *            要进行处理的图象
	 * @param method
	 * @return
	 */
	protected Image process(Image image, IProcessAPixelFloatCoordinate method) {
		return process(image, image.getWidth(null), image.getWidth(null),
				method);
	}

	/**
	 * 对图像进行邻域级的处理。可以对图形大小做出改变。
	 * 
	 * @param image
	 *            要进行处理的图像。
	 * @param newWidth
	 *            新图像的宽度，它可以与原图像一致。
	 * @param newHeight
	 *            新图像的高度，它可以与原图像一致。
	 * @param method
	 *            进行邻域处理的方法，新图像中的像素。
	 * @return 新的图像
	 */
	protected Image process(Image image, int newWidth, int newHeight,
			IProcessAPixelFloatCoordinate method) {
		try {
			int[] rgbs = getPixelMatrix(image);
			int[] newRGbs = new int[newWidth * newHeight];
			int width = image.getWidth(null);
			int height = image.getHeight(null);

			double radioWidth = (width * 1.0) / (newWidth * 1.0), radioHeight = (height * 1.0)
					/ (newHeight * 1.0);

			for (int i = 0; i < newHeight; i++) {
				for (int j = 0; j < newWidth; j++) {
					newRGbs[i * newWidth + j] = method.process(rgbs, width,
							height, (double) i * radioHeight, (double) j
									* radioWidth);
				}
			}
			return makeNewImage(newWidth, newHeight, newRGbs);
		} catch (Exception e) {
			System.out.println(e);
			System.out.println("邻域处理错误");
			return null;
		}
	}

	/**
	 * 对一个rgb数组进行分段处理在start<=i<end之间，适用于多线程。
	 * 
	 * @param rgbs
	 *            原argb数组
	 * @param newRgbs
	 *            新的argb数组
	 * @param start
	 *            起始端点
	 * @param end
	 *            终止端点，在end - 1处终止
	 * @param method
	 *            处理方法
	 */
	private void pieceProcess(int[] rgbs, int[] newRgbs, int start, int end,
			IProcessAPixelIndex method) {
		for (int i = start; i < end; i++) {
			newRgbs[i] = method.process(rgbs, i);
		}
	}

	/**
	 * 对图像进行卷积操作。<br>
	 * 这个操作会对RGB三个通道同时进行。<br>
	 * 卷积操作在边界采用复制扩展的方式。<br>
	 * 
	 * @param image
	 *            要进行卷积操作的图像。
	 * @param filterMatrix
	 *            进行卷积操作的过滤矩阵。
	 * @return 进行卷积操作后的图像
	 */
	public Image convolution(Image image, FilterMatrix filterMatrix) {
		BufferedImage bfImage = (BufferedImage) image;

		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		int row = filterMatrix.getRow();
		int column = filterMatrix.getColumn();

		// 处理后新生成的图像
		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		// 过滤矩阵在横向计算时的负偏移量
		int negBiasX = -column / 2;
		// 过滤矩阵在纵向计算时的负偏移量
		int negBiasY = -row / 2;
		// 过滤矩阵在横向计算时的正偏移量
		int posBiasX = column + negBiasX;
		// 过滤矩阵在纵向计算时的正偏移量
		int posBiasY = row + negBiasY;

		// 在接下来计算时，取图像中点的行坐标
		int imageRow;
		// 在接下来计算时，取图像中点的列坐标
		int imageColumn;
		// 在接下来计算时，图像中点argb值
		int argb;
		// 在接下来计算时，图像中点argb解析成的ARGB向量
		int[] argbArray;
		// 在接下来计算时，每一次计算结果生成的ARGB值向量
		int[] resultArgbArray;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// 初始化结果为0，不透明度为255
				resultArgbArray = new int[] { 255, 0, 0, 0 };
				for (int y = negBiasY; y < posBiasY; y++) {
					for (int x = negBiasX; x < posBiasX; x++) {
						imageRow = i + y;
						imageColumn = j + x;
						// 对越界进行检测
						if (imageRow < 0 || imageRow >= height
								|| imageColumn < 0 || imageColumn >= width) {
							// 在边缘采用重复补齐的方式
							argb = bfImage.getRGB(j, i);
						} else {
							argb = bfImage.getRGB(imageColumn, imageRow);
						}
						argbArray = decARGB(argb);
						for (int n = 1; n < 4; n++) {
							// 将偏移量转为原坐标
							resultArgbArray[n] += argbArray[n]
									* filterMatrix.getEntity(y - negBiasY, x
											- negBiasX);
						}
					}
				}
				// 对计算结果整体进行截断和放缩
				for (int n = 1; n < 4; n++) {
					// 全部取绝对值
					resultArgbArray[n] = Math.abs(resultArgbArray[n]);
					resultArgbArray[n] = (int) Math.round(resultArgbArray[n]
							* filterMatrix.getScaling());
					// 不超过255
					resultArgbArray[n] = Math.min(resultArgbArray[n], 255);
				}
				newImage.setRGB(
						j,
						i,
						comARGB(resultArgbArray[0], resultArgbArray[1],
								resultArgbArray[2], resultArgbArray[3]));
			}
		}
		return newImage;
	}

	/**
	 * 对于一个整数代表一个像素的ARGB值的组合，分解成ARGB构成的数组。
	 * 
	 * @param argb
	 *            要获取ARGB的像素的整数表示的ARGB值。
	 * @return 数组长度为4，[0]-A, [1]-R, [2]-G, [3]-B
	 */
	static public int[] decARGB(int argb) {
		return new int[] { (argb >> 24) & 0xFF, (argb >> 16) & 0xFF,
				(argb >> 8) & 0xFF, argb & 0xFF };
	}

	/**
	 * 将ARGB四个通道合称为一个组合了的ARGB整数。
	 * 
	 * @param a
	 *            alpha通道值
	 * @param r
	 *            red通道值
	 * @param g
	 *            green通道值
	 * @param b
	 *            blue通道值
	 * @return ARGB通道值的组合
	 */
	static public int comARGB(int a, int r, int g, int b) {
		return a << 24 | r << 16 | g << 8 | b;
	}
}
