package holy.digitalimageprocess.data;

/**
 * 对一个像素的进行处理的函数接口，像素的行列坐标为double型。<br>
 * 对一个像素的进行处理，最终返回成一个像素的rgb整数。
 */
public interface IProcessAPixelFloatCoordinate {
	/**
	 * 对一个像素位置的进行邻域处理操作。
	 * @param rgbs 包含该像素的ARGB数组
	 * @param width 图像的宽度
	 * @param height 图像的高度
	 * @param x 要处理的像素在图像中的位置，可能是一个浮点位置。
	 * @param y 要处理的像素在图像中的位置，可能是一个浮点位置。
	 * @return 返回新产生的像素
	 */
	int process(int rgbs[], int width, int height, double x, double y);
}
