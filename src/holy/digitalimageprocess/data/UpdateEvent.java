/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.data;

/**
 * ImageData中图片更新事件，包含更新图片的路径信息。
 */
public class UpdateEvent {
	String path;

	/**
	 * 通过更新的图片的路径信息来初始化事件。
	 * 
	 * @param path
	 *            更新图片的路径。
	 */
	public UpdateEvent(String path) {
		this.path = path;
	}
	
	/**
	 * 返回事件按包含的图片路径
	 * @return path
	 */
	public String getPath() {
		return path;
	}
}
