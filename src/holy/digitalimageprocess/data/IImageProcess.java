/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * ��ͼ����д�����ࡣʵ�������ȡImageData��ͼ������ؾ���Ȼ���������
 */
public abstract class IImageProcess {
	/**
	 * ��ȡimage�е�ͼ���RGB���󣬲����䰴�մ��ϵ��¡������ҵ�˳�򱣴� ��int�����в����ء�ARGB��һ��int�е�˳��ΪA-R-G��B.
	 * �������κδ���ʱ�� ����null��
	 * 
	 * @param image
	 *            Ҫ��ȡ���ؾ����Image
	 * @return imageData�е�����RGBֵ���ɵ����顣����ڹ����з������󣬷���null
	 */
	public int[] getPixelMatrix(Image image) {
		try {
			BufferedImage bfImage = (BufferedImage) image;
			// ��ȡRGB��ɫ��������鵽rgbs�У���ǰ���õ�rgbs�Ĵ�С
			int height = bfImage.getHeight();
			int width = bfImage.getWidth();
			int[] rgbs = new int[width * height];
			bfImage.getRGB(0, 0, width, height, rgbs, 0, width);
			return rgbs;
		} catch (Exception e) {
			// �����������󣬷���null
			return null;
		}
	}

	/**
	 * �����µ�ͼƬ��ͨ��ͼƬ�Ŀ�ȡ��߶Ⱥ�ARGB���顣<br>
	 * Ĭ��ͼƬ������ΪTYPE_INT_RGB.
	 * 
	 * @param width
	 *            �µ�ͼƬ�Ŀ��
	 * @param height
	 *            �µ�ͼƬ�ĸ߶�
	 * @param rgbs
	 *            �µ�ͼƬ��ARGB����
	 * @return �²�����ͼƬ
	 */
	public Image makeNewImage(int width, int height, int[] rgbs) {
		try {
			// ����RGB��ɫ����rgbs��ͼ���У���ǰ���õ�rgbs�Ĵ�С
			// �½�һ��ͼƬ����ľɵ�ͼƬ
			BufferedImage newBfImage = new BufferedImage(width, height,
					BufferedImage.TYPE_INT_ARGB);
			newBfImage.setRGB(0, 0, width, height, rgbs, 0, width);
			return newBfImage;
		} catch (Exception e) {
			System.out.println("��rgbs�д�����ͼ��������");
		}
		return null;
	}

	/**
	 * ��ͼ����������صĴ��� rgbs�ǰ�������ͼ������ص�rgbֵ���ɵ����顣<br>
	 * ����������ı�ͼ��Ĵ�С��
	 * 
	 * @param image
	 *            Ҫ���д����ͼ��
	 * 
	 * @param method
	 *            ������������Ԫ�صķ���
	 * 
	 * @return ������ͼ��
	 */
	protected Image process(Image image, IProcessAPixelIndex method) {
		try {
			int[] rgbs = getPixelMatrix(image);
			int[] newRgbs = new int[rgbs.length];
			// �������������������̴߳���ÿ1500000���ؿ�һ���̣߳�
			// ����4����
			int numOfThread = Math.min(rgbs.length / 1500000 + 1, 4);
			ArrayList<Thread> threads = new ArrayList<Thread>();

			// ���ﲻ�ܹ�ͨ�������ķ�ʽ�������̣߳���Ϊ�ֲ��಻�ܷ��ʾֲ�����
			// һ���߳�
			if (numOfThread == 1) {
				threads.add(new Thread(new Runnable() {

					@Override
					public void run() {
						pieceProcess(rgbs, newRgbs, 0, rgbs.length, method);
					}
				}));
			}
			// �����߳�
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
			// �����߳�
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
			// �����߳�
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
			// �߳̿�ʼ
			for (Thread t : threads) {
				t.start();
			}
			// �ȴ��߳̽���
			for (Thread t : threads) {
				t.join();
			}
			return makeNewImage(image.getWidth(null), image.getHeight(null),
					newRgbs);
		} catch (Exception e) {
			System.out.println("�����ش���������");
			return null;
		}
	}

	/**
	 * ��ͼ��������򼶴���ͼ���С�������ı䡣
	 * 
	 * @param image
	 *            Ҫ���д����ͼ��
	 * @param method
	 * @return
	 */
	protected Image process(Image image, IProcessAPixelFloatCoordinate method) {
		return process(image, image.getWidth(null), image.getWidth(null),
				method);
	}

	/**
	 * ��ͼ��������򼶵Ĵ������Զ�ͼ�δ�С�����ı䡣
	 * 
	 * @param image
	 *            Ҫ���д����ͼ��
	 * @param newWidth
	 *            ��ͼ��Ŀ�ȣ���������ԭͼ��һ�¡�
	 * @param newHeight
	 *            ��ͼ��ĸ߶ȣ���������ԭͼ��һ�¡�
	 * @param method
	 *            ����������ķ�������ͼ���е����ء�
	 * @return �µ�ͼ��
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
			System.out.println("���������");
			return null;
		}
	}

	/**
	 * ��һ��rgb������зֶδ�����start<=i<end֮�䣬�����ڶ��̡߳�
	 * 
	 * @param rgbs
	 *            ԭargb����
	 * @param newRgbs
	 *            �µ�argb����
	 * @param start
	 *            ��ʼ�˵�
	 * @param end
	 *            ��ֹ�˵㣬��end - 1����ֹ
	 * @param method
	 *            ������
	 */
	private void pieceProcess(int[] rgbs, int[] newRgbs, int start, int end,
			IProcessAPixelIndex method) {
		for (int i = start; i < end; i++) {
			newRgbs[i] = method.process(rgbs, i);
		}
	}

	/**
	 * ��ͼ����о��������<br>
	 * ����������RGB����ͨ��ͬʱ���С�<br>
	 * ��������ڱ߽���ø�����չ�ķ�ʽ��<br>
	 * 
	 * @param image
	 *            Ҫ���о��������ͼ��
	 * @param filterMatrix
	 *            ���о�������Ĺ��˾���
	 * @return ���о���������ͼ��
	 */
	public Image convolution(Image image, FilterMatrix filterMatrix) {
		BufferedImage bfImage = (BufferedImage) image;

		int width = bfImage.getWidth();
		int height = bfImage.getHeight();
		int row = filterMatrix.getRow();
		int column = filterMatrix.getColumn();

		// ����������ɵ�ͼ��
		BufferedImage newImage = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_ARGB);

		// ���˾����ں������ʱ�ĸ�ƫ����
		int negBiasX = -column / 2;
		// ���˾������������ʱ�ĸ�ƫ����
		int negBiasY = -row / 2;
		// ���˾����ں������ʱ����ƫ����
		int posBiasX = column + negBiasX;
		// ���˾������������ʱ����ƫ����
		int posBiasY = row + negBiasY;

		// �ڽ���������ʱ��ȡͼ���е��������
		int imageRow;
		// �ڽ���������ʱ��ȡͼ���е��������
		int imageColumn;
		// �ڽ���������ʱ��ͼ���е�argbֵ
		int argb;
		// �ڽ���������ʱ��ͼ���е�argb�����ɵ�ARGB����
		int[] argbArray;
		// �ڽ���������ʱ��ÿһ�μ��������ɵ�ARGBֵ����
		int[] resultArgbArray;
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				// ��ʼ�����Ϊ0����͸����Ϊ255
				resultArgbArray = new int[] { 255, 0, 0, 0 };
				for (int y = negBiasY; y < posBiasY; y++) {
					for (int x = negBiasX; x < posBiasX; x++) {
						imageRow = i + y;
						imageColumn = j + x;
						// ��Խ����м��
						if (imageRow < 0 || imageRow >= height
								|| imageColumn < 0 || imageColumn >= width) {
							// �ڱ�Ե�����ظ�����ķ�ʽ
							argb = bfImage.getRGB(j, i);
						} else {
							argb = bfImage.getRGB(imageColumn, imageRow);
						}
						argbArray = decARGB(argb);
						for (int n = 1; n < 4; n++) {
							// ��ƫ����תΪԭ����
							resultArgbArray[n] += argbArray[n]
									* filterMatrix.getEntity(y - negBiasY, x
											- negBiasX);
						}
					}
				}
				// �Լ�����������нضϺͷ���
				for (int n = 1; n < 4; n++) {
					// ȫ��ȡ����ֵ
					resultArgbArray[n] = Math.abs(resultArgbArray[n]);
					resultArgbArray[n] = (int) Math.round(resultArgbArray[n]
							* filterMatrix.getScaling());
					// ������255
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
	 * ����һ����������һ�����ص�ARGBֵ����ϣ��ֽ��ARGB���ɵ����顣
	 * 
	 * @param argb
	 *            Ҫ��ȡARGB�����ص�������ʾ��ARGBֵ��
	 * @return ���鳤��Ϊ4��[0]-A, [1]-R, [2]-G, [3]-B
	 */
	static public int[] decARGB(int argb) {
		return new int[] { (argb >> 24) & 0xFF, (argb >> 16) & 0xFF,
				(argb >> 8) & 0xFF, argb & 0xFF };
	}

	/**
	 * ��ARGB�ĸ�ͨ���ϳ�Ϊһ������˵�ARGB������
	 * 
	 * @param a
	 *            alphaͨ��ֵ
	 * @param r
	 *            redͨ��ֵ
	 * @param g
	 *            greenͨ��ֵ
	 * @param b
	 *            blueͨ��ֵ
	 * @return ARGBͨ��ֵ�����
	 */
	static public int comARGB(int a, int r, int g, int b) {
		return a << 24 | r << 16 | g << 8 | b;
	}
}
