package holy.util;

import java.awt.Frame;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

/**
 * 显示对话框并要求用户“确定”或“取消”的抽象类。<br>
 * 使用时请复写ok和cancel类。
 */
public abstract class IOKorCancelDialog extends JDialog {
	private static final long serialVersionUID = -7477365516594545561L;

	// 用户输入的状态，对应于CANCELED, ENTERED, UNDECIDED.
	private int status = STATUS_UNDECIDED;

	/**
	 * 返回用户当前的操作状态。
	 * 
	 * @return CANCELED，用户取消了操作,<br>
	 *         ENTERED，用户确认了操作<br>
	 *         UNDECIDED，用户还未确定操作<br>
	 */
	public int getUserActStatus() {
		return status;
	}

	/**
	 * 以所有对话框的帧初始这个对话框
	 * 
	 * @param owner
	 *            所有对话框的帧，即父窗口
	 */
	public IOKorCancelDialog(Frame owner) {
		super(owner);
		// 设置为不可改变窗口大小
		setResizable(false);
		// 设置为弹出对话框
		setType(Type.POPUP);
	}

	/**
	 * 初始化对话框，必须在构造函数后立即调用
	 */
	public void initialize() {
		// 首先设置对话框可视
		this.setVisible(true);
		// 禁止访问原窗口
		getOwner().setEnabled(false);
	}

	/**
	 * 在对话框结束后调用
	 */
	public void clear() {
		// 回复原窗口可用性
		getOwner().setEnabled(true);
	}

	/**
	 * 获取用户的操作状态。
	 * @return IJMyDialog中的Status_常量
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * 用户是否点击了确定
	 * @return 如果用户点击了确定则返回true，反之false
	 */
	public boolean isEntered() {
		return status == STATUS_ENTERED;
	}

	/**
	 * 用户点击确定
	 */
	public void usertEntered() {
		status = STATUS_ENTERED;
	}

	/**
	 * 用户是否点击了取消
	 * @return 用户如果点击了取消，则返回true，反之false
	 */
	public boolean isCanceled() {
		return status == STATUS_CANCELED;
	}

	/**
	 * 用户取消
	 */
	public void userCanceled() {
		status = STATUS_CANCELED;
	}

	/**
	 * 判断用户是否采取了操作。
	 * 
	 * @return 无论用户进行了“确定”还是“取消”，都算作用户采取了操作，即返回true， 反之else
	 */
	public boolean isEnterOrCancel() {
		return status != STATUS_UNDECIDED;
	}

	/**
	 * 将关闭窗口操作转化为取消操作
	 */
	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			cancel();
		} else {
			super.processWindowEvent(e);
		}
	}

	/**
	 * 默认的确定操作即设置用已经确定，并关闭窗口。 重写时应先对用户输入进行处理，然后调用默认的ok操作。
	 */
	public void ok() {
		usertEntered();
		dispose();
	}

	/**
	 * 默认的关闭操作，即设置用户已经取消，并关闭窗口。 重写时应先对用户输入进行处理，然后调用默认的cancel操作。
	 */
	public void cancel() {
		userCanceled();
		dispose();
	}

	/**
	 * 等待用户输入
	 */
	public void waitForUserAct() {
		while (!isEnterOrCancel()) {
			try {
				this.wait();
			} catch (Exception e) {
				// 什么也不做
			}
		}
	}

	/**
	 * 用户操作状态
	 */
	public static final int
	// 选择了取消
			STATUS_CANCELED = 0,
			// 选择确定
			STATUS_ENTERED = 1,
			// 未决定
			STATUS_UNDECIDED = 2;
}
