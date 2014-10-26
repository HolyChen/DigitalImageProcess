package holy.digitalimageprocess.dialogs;

import holy.util.IOKorCancelDialog;

import java.awt.Frame;

/**
 * ���Ի����е���Ϣ���û����룩�ı�ʱ��ͼ�񲢲���֮�ı䡣
 */
public abstract class IJDialogWithoutImageInstanceChange extends IOKorCancelDialog {

	private static final long serialVersionUID = 6767800558229141738L;

	/**
	 * �����жԻ����֡��ʼ����Ի���
	 * 
	 * @param owner
	 *            ���жԻ����֡����������
	 */
	public IJDialogWithoutImageInstanceChange(Frame owner) {
		super(owner);
	}

	/**
	 * ��ʾ�Ի��򣬲����ؽ�������Ｏ���˻����Ĳ����������������������
	 * 
	 * @return ��0��ֵ����Ϊ�û��Ƿ������ѡ��
	 */
	public Object[] showDialog() {
		initialize();
		waitForUserAct();
		clear();
		return null;
	}

}
