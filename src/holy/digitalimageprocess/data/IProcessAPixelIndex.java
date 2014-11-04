package holy.digitalimageprocess.data;

/**
 * 对一个像素的进行处理的函数接口，像素的行、列坐标为int型。<br>
 * 对一个像素的进行处理，最终返回成一个像素的rgb整数。
 */
public interface IProcessAPixelIndex {
	/**
	 * 对一个像素位置的进行邻域处理操作。
	 * @param argbs 包含该像素的ARGB数组
	 * @param index 这个像素在argbs中的位置
	 * @return 返回新产生的像素
	 */
	int process(int argbs[], int index);
}
