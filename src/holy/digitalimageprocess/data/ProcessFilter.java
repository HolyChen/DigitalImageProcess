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

		IProcessAPixelIntCoordinate method = new IProcessAPixelIntCoordinate() {

			@Override
			public int process(int[] argbs, int index) {
				int[] argbArrayA = decARGB(argbs[index]);
				int[] argbArrayB = decARGB(argbB[index]);
				int[] result = new int[4];
				for (int i = 0; i < 4; i++) {
					result[i] = (int) Math.round(argbArrayA[i] * (1 - radio)
							+ argbArrayB[i] * radio);
				}
				return comARGB(result[0], result[1], result[2], result[3]);
			}
		};
		return process(imageA, method);
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
