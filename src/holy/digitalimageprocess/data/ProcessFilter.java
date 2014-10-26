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
