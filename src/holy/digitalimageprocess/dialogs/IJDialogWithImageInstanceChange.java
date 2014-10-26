package holy.digitalimageprocess.dialogs;

import holy.util.IOKorCancelDialog;

import java.awt.Frame;

/**
 * ���Ի����е���Ϣ���û����룩�����ı�ʱ��ͼ����֮�ı䡣
 */
public abstract class IJDialogWithImageInstanceChange extends IOKorCancelDialog {	
	/**
	 * �ô�������Ի����֡��Ϊ�����߳�ʼ��������ڡ�
	 * @param owner ��������Ի���Ĵ���֡��
	 */
	public IJDialogWithImageInstanceChange(Frame owner) {
		super(owner);
	}

	private static final long serialVersionUID = -3388934762879416367L;

	/**
	 * ��ʾ�Ի���
	 */
	public void showDialog() {
		try {
			initialize();
			waitForUserAct();
			clear();
		} catch (Exception e) {
			// ����������ʱ��ȡ��ѡ��
			e.printStackTrace(System.err);
		}
	}
		
	/**
	 * �����û�
	 */
	
	/**
	 * ��ȡ�Ի����û�����������
	 * 
	 * @retrn ����ֵ�ĵ�0�������û�����Ϊ״̬��UNDECIDEΪ�û�δȷ��Ҳδȡ����
	 *         ENTEREDΪ�û��Ѿ�ȷ����CANCELEDΪ�û��Ѿ�ȡ�������������μ����嶨�塣
	 */
	public abstract Object[] getResult();

}
