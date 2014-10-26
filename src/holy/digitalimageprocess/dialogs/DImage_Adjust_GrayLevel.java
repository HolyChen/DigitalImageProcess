/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * ��һ���Ի������û�����ҶȽ��������û�ȷ���ǣ��ı�ҶȽ���
 */
public class DImage_Adjust_GrayLevel extends IJDialogWithoutImageInstanceChange {
	/**
	 * ��һ�棬���������˵��ķ�ʽѡȡ2�������η�����
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<Integer> cbGrayLevels;
	private JLabel lblNewLabel;

	// ѡ��ĻҶȽ���
	private int level = 256;

	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			System.out.println(Arrays
					.toString(new DImage_Adjust_GrayLevel(null).showDialog()));
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * �򿪻ҶȽ���ѡ��Ի��򣬲���Ҫ���û���2��4��8��16��32��64��128��256����
	 * ֮��ѡ��һ�����ʵ�ֵ�����ѡ���з���������û�ȡ��ѡ�񣬽�����0����ʱ ����û��ѡ��һ���ҶȽ�����
	 * 
	 * @return �����û��Ƿ������ѡ�񡢺��û�ѡ��ĻҶȽ���(2��4��8��16��32��64��128��256)�� ˳�����£�<br>
	 *         0-(int)�û��Ĳ���״̬����Ӧ��IJMyDialog��STATUS_����;<br>
	 *         ��<br>
	 *         1-(int)�û�ѡ��ĻҶȽ�����
	 * 
	 */
	@Override
	public Object[] showDialog() {
		try {
			super.showDialog();
			return new Object[] { getStatus(), level, };
		} catch (Exception e) {
			// ����������ʱ��ȡ���Ҷ�ѡ��
			return null;
		}
	}

	/**
	 * ����һ��ѡ��ҶȵĶԻ��򣬲�����������Ϊ�����ߡ�
	 * 
	 * @param owner
	 *            ������Ի���ĸ����档
	 */
	public DImage_Adjust_GrayLevel(Frame owner) {
		super(owner);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		this.setBounds(400, 400, 163, 108);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		cbGrayLevels = new JComboBox<Integer>();
		cbGrayLevels.setAutoscrolls(true);
		cbGrayLevels.setPreferredSize(new Dimension(75, 21));

		// ��ӻҶ�ɫ�׵�ѡ�����Ϊ2��4��8��16��32��64��128��ɫ��
		for (int i = 2; i <= 256;) {
			cbGrayLevels.addItem(i);
			i <<= 1;
		}
		// Ĭ��ѡ��256ɫ��
		cbGrayLevels.setSelectedItem(256);

		lblNewLabel = new JLabel("\u7070\u5EA6\u9636\u6570");
		lblNewLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPanel.add(lblNewLabel);

		contentPanel.add(cbGrayLevels);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("ȷ��");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ok();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("ȡ��");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						cancel();
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * ��¼�û������levelֵ��
	 */
	@Override
	public void ok() {
		level = (int) (cbGrayLevels.getSelectedItem());
		super.ok();
	}

	/**
	 * ��levelֵ��Ϊ0��
	 */
	@Override
	public void cancel() {
		level = 0;
		super.cancel();
	}
}
