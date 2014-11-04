package holy.util;

import java.awt.Frame;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

/**
 * ��ʾ�Ի���Ҫ���û���ȷ������ȡ�����ĳ����ࡣ<br>
 * ʹ��ʱ�븴дok��cancel�ࡣ
 */
public abstract class IOKorCancelDialog extends JDialog {
	private static final long serialVersionUID = -7477365516594545561L;

	// �û������״̬����Ӧ��CANCELED, ENTERED, UNDECIDED.
	private int status = STATUS_UNDECIDED;

	/**
	 * �����û���ǰ�Ĳ���״̬��
	 * 
	 * @return CANCELED���û�ȡ���˲���,<br>
	 *         ENTERED���û�ȷ���˲���<br>
	 *         UNDECIDED���û���δȷ������<br>
	 */
	public int getUserActStatus() {
		return status;
	}

	/**
	 * �����жԻ����֡��ʼ����Ի���
	 * 
	 * @param owner
	 *            ���жԻ����֡����������
	 */
	public IOKorCancelDialog(Frame owner) {
		super(owner);
		// ����Ϊ���ɸı䴰�ڴ�С
		setResizable(false);
		// ����Ϊ�����Ի���
		setType(Type.POPUP);
	}

	/**
	 * ��ʼ���Ի��򣬱����ڹ��캯������������
	 */
	public void initialize() {
		// �������öԻ������
		this.setVisible(true);
		// ��ֹ����ԭ����
		getOwner().setEnabled(false);
	}

	/**
	 * �ڶԻ�����������
	 */
	public void clear() {
		// �ظ�ԭ���ڿ�����
		getOwner().setEnabled(true);
	}

	/**
	 * ��ȡ�û��Ĳ���״̬��
	 * @return IJMyDialog�е�Status_����
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * �û��Ƿ�����ȷ��
	 * @return ����û������ȷ���򷵻�true����֮false
	 */
	public boolean isEntered() {
		return status == STATUS_ENTERED;
	}

	/**
	 * �û����ȷ��
	 */
	public void usertEntered() {
		status = STATUS_ENTERED;
	}

	/**
	 * �û��Ƿ�����ȡ��
	 * @return �û���������ȡ�����򷵻�true����֮false
	 */
	public boolean isCanceled() {
		return status == STATUS_CANCELED;
	}

	/**
	 * �û�ȡ��
	 */
	public void userCanceled() {
		status = STATUS_CANCELED;
	}

	/**
	 * �ж��û��Ƿ��ȡ�˲�����
	 * 
	 * @return �����û������ˡ�ȷ�������ǡ�ȡ�������������û���ȡ�˲�����������true�� ��֮else
	 */
	public boolean isEnterOrCancel() {
		return status != STATUS_UNDECIDED;
	}

	/**
	 * ���رմ��ڲ���ת��Ϊȡ������
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
	 * Ĭ�ϵ�ȷ���������������Ѿ�ȷ�������رմ��ڡ� ��дʱӦ�ȶ��û�������д���Ȼ�����Ĭ�ϵ�ok������
	 */
	public void ok() {
		usertEntered();
		dispose();
	}

	/**
	 * Ĭ�ϵĹرղ������������û��Ѿ�ȡ�������رմ��ڡ� ��дʱӦ�ȶ��û�������д���Ȼ�����Ĭ�ϵ�cancel������
	 */
	public void cancel() {
		userCanceled();
		dispose();
	}

	/**
	 * �ȴ��û�����
	 */
	public void waitForUserAct() {
		while (!isEnterOrCancel()) {
			try {
				this.wait();
			} catch (Exception e) {
				// ʲôҲ����
			}
		}
	}

	/**
	 * �û�����״̬
	 */
	public static final int
	// ѡ����ȡ��
			STATUS_CANCELED = 0,
			// ѡ��ȷ��
			STATUS_ENTERED = 1,
			// δ����
			STATUS_UNDECIDED = 2;
}
