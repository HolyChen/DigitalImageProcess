package holy.digitalimageprocess.dialogs;

import holy.util.IOKorCancelDialog;

import java.awt.Frame;

/**
 * 当对话框中的信息（用户输入）改变时，图像并不随之改变。
 */
public abstract class IJDialogWithoutImageInstanceChange extends IOKorCancelDialog {

	private static final long serialVersionUID = 6767800558229141738L;

	/**
	 * 以所有对话框的帧初始这个对话框
	 * 
	 * @param owner
	 *            所有对话框的帧，即父窗口
	 */
	public IJDialogWithoutImageInstanceChange(Frame owner) {
		super(owner);
	}

	/**
	 * 显示对话框，并返回结果。这里集成了基本的操作，必须重载这个函数。
	 * 
	 * @return 第0个值必须为用户是否进行了选择。
	 */
	public Object[] showDialog() {
		initialize();
		waitForUserAct();
		clear();
		return null;
	}

}
