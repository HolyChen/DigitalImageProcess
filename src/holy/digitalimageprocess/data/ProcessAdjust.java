/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.data;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * ��ImageData��ͼ����е����������ࡣ<br>
 * ������������������һ�������ࡣ��ͨ��getInstance��)��ȡ����
 */
public class ProcessAdjust extends IImageProcess {
	/**
	 * ���������ع�����
	 */
	private ProcessAdjust() {
	}

	/**
	 * ��ͼ��ת��Ϊ�Ҷ�ͼ����ָ���ĻҶȽ����������µ�ͼ�񷵻�
	 * 
	 * @param image
	 *            ��Ҫ���д����ͼ��
	 * @param level
	 *            �Ҷ�ֵ�Ľ���������2, 4, 8, 16, 32, 64, 128, 256.
	 * @return ���ĵ��ĻҶ�ͼ�񷵻ء�
	 */
	public Image toBlackWhite(Image image, int level) {
		IProcessAPixelIndex method = new IProcessAPixelIndex() {
			// �Ե������ؽ��лҶȻ��ķ���
			@Override
			public int process(int argbs[], int index) {
				int[] argbArray = IImageProcess.decARGB(argbs[index]);
				// ����ת��Ϊ256�׻Ҷ�
				// ����NTSC�Ƽ��Ĳ�ɫͼ��ͼ����лҶȴ���ʽ
				int average = (int) Math.round(0.299 * argbArray[1] + 0.587
						* argbArray[2] + 0.114 * argbArray[3]);
				/*
				 * ��256�׻ҶȰ���level������������ ����256���Խ������зֶΣ�Ȼ���ٳ���ÿһ�ζ�Ӧ��ǿ�ȴ�С��
				 */
				average = (int) ((average / (256 / level)) * (255.0 / (level - 1)));
				return IImageProcess.comARGB(argbArray[0], average, average,
						average);
			}
		};
		return process(image, method);
	}

	/**
	 * ��ͼ��ͨ������������ֵ�����������ô�С��
	 * 
	 * @param image
	 *            Ҫ�������ô�С��ͼ��
	 * @param width
	 *            �µ�ͼƬ�Ŀ��
	 * @param height
	 *            �µ�ͼƬ�ĸ߶�
	 * @return �µ�ͼƬ
	 */
	public Image resizeImageNearest(Image image, int width, int height) {
		IProcessAPixelFloatCoordinate method = new IProcessAPixelFloatCoordinate() {

			@Override
			public int process(int[] rgbs, int width, int height, double x,
					double y) {
				// �Զ�����һ������ȡֵ�����ڽ���ֵ
				return rgbs[(int) (Math.floor(x) * width + Math.floor(y))];
			}
		};
		return process(image, width, height, method);
	}

	/**
	 * ��ͼ��ͨ����˫���Բ�ֵ�����������ô�С��
	 * 
	 * @param image
	 *            Ҫ�������ô�С��ͼ��
	 * @param width
	 *            �µ�ͼƬ�Ŀ��
	 * @param height
	 *            �µ�ͼƬ�ĸ߶�
	 * @return �µ�ͼƬ
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
				 * �ο�˫���Բ�ֵ��ʽ��עpixel[0]~pixel[3]��ͼʾ: |0 1| |2 3|
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
	 * ͳ��һ��ͼƬ��ֱ��ͼ�������˷�����RGBͨ���ϵ�ֱ��ͼͳ�ƽ����
	 * 
	 * @param image
	 *            Ҫ����ֱ��ͼ��ͼ��
	 * @return double�����б������ÿһ���һ��256���ȵ�double���顣<br>
	 *         0 - ��ɫͨ��ͳ�ƽ����<br>
	 *         1 - ��ɫͨ����ͳ�ƽ����<br>
	 *         2 - ��ɫͨ����ͳ�ƽ����<br>
	 */
	public double[][] statisticHistogram(Image image) {
		int width = image.getWidth(null);
		int heigth = image.getHeight(null);
		int size = width * heigth;
		int[] argbs = getPixelMatrix(image);

		double[] redHistogram = new double[256];
		double[] greenHistogram = new double[256];
		double[] blueHistogram = new double[256];

		// ��ʱ��Ž�������argb����
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
	 * ��ͼ�����ֱ��ͼ���⻯��<br>
	 * ���⻯��Ҫ��֪��ֱ��ͼͳ�����ݣ������Щͳ�������ǲ������ģ��������Զ�ͳ�ơ�
	 * 
	 * @param image
	 *            Ҫ���о��⻯��ͼ��
	 * @param redData
	 *            ��ɫͨ����ֱ��ͼͳ�����ݣ�Ӧ����һ������256�����飬 ���������ݶ����ڵ���0�����ۺ�Ϊ1��double���顣
	 * @param greenData
	 *            ��ɫͨ����ֱ��ͼͳ�����ݣ�Ӧ����һ������256�����飬 ���������ݶ����ڵ���0�����ۺ�Ϊ1��double���顣
	 * @param BlueData
	 *            ��ɫͨ����ֱ��ͼͳ�����ݣ�Ӧ����һ������256�����飬 ���������ݶ����ڵ���0�����ۺ�Ϊ1��double���顣
	 * @return �������⻯��ͼ��
	 */
	public Image histogramEqulization(Image image, double[] redData,
			double[] greenData, double[] BlueData) {
		double[][] channelData = new double[][] { null, null, null };
		// ���ͳ�������ǲ�ȱ�ģ����������ͳ��
		if (redData == null || greenData == null || BlueData == null) {
			channelData = statisticHistogram(image);
		} else {
			channelData[0] = redData;
			channelData[1] = greenData;
			channelData[2] = BlueData;
		}

		// ��ͳ�����ݼ������ܶȽ����ۻ�
		double[][] accumulate = new double[][] { new double[256],
				new double[256], new double[256] };
		for (int i = 0; i < 3; i++) {
			accumulate[i][0] = channelData[i][0];
			for (int j = 1; j < 256; j++) {
				accumulate[i][j] = accumulate[i][j - 1] + channelData[i][j];
			}
		}

		// ���⻯ӳ���
		int[][] equlizationMap = new int[][] { new int[256], new int[256],
				new int[256] };
		// ���վ��⻯�㷨����ӳ���
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 256; j++) {
				equlizationMap[i][j] = (int) (Math
						.round(255 * accumulate[i][j]));
			}
		}

		// ֱ��ͼ���⻯
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
	 * ��ͼ����о��βü�
	 * 
	 * @param image
	 *            Ҫ���вü���ͼ��
	 * @param originX
	 *            �ü���������ϽǺ�����
	 * @param originY
	 *            �ü���������Ͻ�������
	 * @param endX
	 *            �ü��������½Ǻ�����
	 * @param endY
	 *            �ü��������½�������
	 * @return ԭͼ����(orginX, orginY),(endX, endY)���ɵľ��������е�ͼ�񣨰����߽磩��
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

	// --------------- ���������ԡ����� ---------------

	private static ProcessAdjust instance;

	/**
	 * ��ȡImageAdjustΨһʵ����
	 * 
	 * @return ImageAdjust��ʵ��
	 */
	public static ProcessAdjust getInstance() {
		if (instance == null) {
			instance = new ProcessAdjust();
		}
		return instance;
	}
}
