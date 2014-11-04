package holy.digitalimageprocess.data;

import java.awt.Image;

/**
 * ��ͼƬ�����˾��������ࡣ<br>
 * ����һ��ֻ������ͼ�������ĵ����࣬����getInstance()��ȡ����
 */
public class ProcessFilter extends IImageProcess {

	/**
	 * ��ͼ��A����ͼ��B����radio��͸���������е��ӡ�<br>
	 * ��radio��0ʱ�����ص�ͼƬ��Aһ�£���radio��1ʱ����Bһ�¡�<br>
	 * A��B����ͼƬ��С������ȫһ�¡�������ͼƬ��С��һ�»���radio����
	 * [0, 1]ʱ, �׳�IllegalArgumentException.
	 * 
	 * @param imageA
	 *            Ҫ���е��ӵ�ͼƬA��
	 * @param imageB
	 *            Ҫ���е��ӵ�ͼƬB��
	 * @param radio
	 *            ����ͼƬ�ĵ��ӱ�����
	 * @return ���Ӻ��ͼƬ��
	 */
	public Image imageOverlay(Image imageA, Image imageB, double radio)
			throws IllegalArgumentException {
		if (imageA.getWidth(null) != imageB.getWidth(null)
				|| imageA.getHeight(null) != imageB.getHeight(null)) {
			throw new IllegalArgumentException("ͼ��A��B�Ĵ�С��һ��!");
		}
		if (radio < 0 || radio > 1) {
			throw new IllegalArgumentException("���ӱ�������[0, 1]��Χ��!");
		}

		int[] argbB = getPixelMatrix(imageB);

		IProcessAPixelIndex method = new IProcessAPixelIndex() {

			@Override
			public int process(int[] argbs, int index) {
				// ��һ��argb����������A��R��G��B����
				int[] argbArrayA = decARGB(argbs[index]);
				int[] argbArrayB = decARGB(argbB[index]);
				int[] result = new int[4];
				for (int i = 0; i < 4; i++) {
					// �������뱣֤��ȷ��
					result[i] = (int) Math.round(argbArrayA[i] * (1 - radio)
							+ argbArrayB[i] * radio);
				}
				// ����ѹ��Ϊһ��argb����
				return comARGB(result[0], result[1], result[2], result[3]);
			}
		};
		return process(imageA, method);
	}
	
	/**
	 * ��ͼ�����ģ��������
	 * @param image Ҫ����ģ����ͼ��
	 * @param size ģ��ѡȡ��С
	 * @return ģ�����ͼ��
	 */
	public Image blur(Image image, int size) {
		// �ȹ����˾����Ӿ���
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
	 * ��ͼ������񻯲�����
	 * @param image Ҫ�����񻯵�ͼ��
	 * @return �񻯺��ͼ��
	 */
	public Image sharpen(Image image) {
		// �ȹ����˾����Ӿ�������һ��2��������˹����
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
	 * ��ͼ��Sobel�����˲�
	 * @param image Ҫ�����񻯵�ͼ��
	 * @return �񻯺��ͼ��
	 */
	public Image sobelHorizontal(Image image) {
		// �ȹ����˾����Ӿ�������һ��Soble��������
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
	 * ��ͼ��Sobel�����˲�
	 * @param image Ҫ�����񻯵�ͼ��
	 * @return �񻯺��ͼ��
	 */
	public Image sobelVertical(Image image) {
		// �ȹ����˾����Ӿ�������һ��Soble��������
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
	 * ��ȡȫ��ʵ����
	 * 
	 * @return ProcessFilter���ȫ��ʵ����
	 */
	public static ProcessFilter getInstance() {
		if (instance == null) {
			instance = new ProcessFilter();
		}
		return instance;
	}

}
