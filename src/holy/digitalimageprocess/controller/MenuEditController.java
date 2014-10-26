package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ImageData;
import holy.util.IController;

import java.awt.Frame;

/**
 * ���༭���˵��Ĺ��ܴ�����
 */
public class MenuEditController extends IController {

	/**
	 * �������󣬲����������frame��Ϊ�����ġ�
	 * @param frame
	 *            ��������˵���������Frame
	 */
	public MenuEditController(Frame frame) {
		super(frame);
	}
	
	/**
	 * ��ͼƬ����һ��������ֻ�ܳ������޲���
	 * @param imageData Ҫ���г���������ͼƬ
	 * @return �ɳ����Ĳ���
	 */
	public int undo(ImageData imageData) {
		return imageData.undo();
	}
	
	/**
	 * ��ͼƬ�ָ�һ��������ֻ�ָܻ����޲���
	 * @param imageData Ҫ��������������ͼƬ
	 * @return �������Ĳ���
	 */
	public int redo(ImageData imageData) {
		return imageData.redo();
	}

}
