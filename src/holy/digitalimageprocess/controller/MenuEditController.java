package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ImageData;
import holy.util.IController;

import java.awt.Frame;

/**
 * “编辑”菜单的功能处理类
 */
public class MenuEditController extends IController {

	/**
	 * 创建对象，并将包含其的frame作为上下文。
	 * @param frame
	 *            包含这个菜单处理器的Frame
	 */
	public MenuEditController(Frame frame) {
		super(frame);
	}
	
	/**
	 * 对图片撤销一步操作。只能撤销有限步。
	 * @param imageData 要进行撤销操作的图片
	 * @return 可撤销的步数
	 */
	public int undo(ImageData imageData) {
		return imageData.undo();
	}
	
	/**
	 * 对图片恢复一步操作。只能恢复有限步。
	 * @param imageData 要进行重做操作的图片
	 * @return 可重做的步数
	 */
	public int redo(ImageData imageData) {
		return imageData.redo();
	}

}
