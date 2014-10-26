/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.data;

import java.awt.Image;

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
		IProcessAPixelIntCoordinate method = new IProcessAPixelIntCoordinate() {
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

				int up = (int) Math.floor(x),
						down = Math.min((int) Math.ceil(x), height - 1),
						left = (int) Math.floor(y),
						right = Math.min((int) Math.ceil(y), width - 1);		

				/*
				 * �ο�˫���Բ�ֵ��ʽ��עpixel[0]~pixel[3]��ͼʾ: 
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

	// --------------- ���������ԡ����� ---------------

	static private ProcessAdjust instance;

	/**
	 * ��ȡImageAdjustΨһʵ����
	 * 
	 * @return ImageAdjust��ʵ��
	 */
	static public ProcessAdjust getInstance() {
		if (instance == null) {
			instance = new ProcessAdjust();
		}
		return instance;
	}
}
