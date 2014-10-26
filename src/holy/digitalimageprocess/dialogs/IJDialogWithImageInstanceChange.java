package holy.digitalimageprocess.dialogs;

import holy.util.IOKorCancelDialog;

import java.awt.Frame;

/**
 * 当对话框中的信息（用户输入）发生改变时，图像随之改变。
 */
public abstract class IJDialogWithImageInstanceChange extends IOKorCancelDialog {	
	/**
	 * 用创建这个对话框的帧作为所有者初始化这个窗口。
	 * @param owner 创建这个对话框的窗口帧。
	 */
	public IJDialogWithImageInstanceChange(Frame owner) {
		super(owner);
	}

	private static final long serialVersionUID = -3388934762879416367L;

	/**
	 * 显示对话框。
	 */
	public void showDialog() {
		try {
			initialize();
			waitForUserAct();
			clear();
		} catch (Exception e) {
			// 当发生错误时，取消选择
			e.printStackTrace(System.err);
		}
	}
		
	/**
	 * 设置用户
	 */
	
	/**
	 * 获取对话框用户的输入结果。
	 * 
	 * @retrn 返回值的第0个总是用户的行为状态。UNDECIDE为用户未确定也未取消，
	 *         ENTERED为用户已经确定，CANCELED为用户已经取消。后续变量参见具体定义。
	 */
	public abstract Object[] getResult();

}
