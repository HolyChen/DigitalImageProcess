package holy.digitalimageprocess.dialogs;

import holy.component.IntegerTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class DFilter_Blur extends IJDialogWithoutImageInstanceChange {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7885231230718232455L;
	private final JPanel contentPanel = new JPanel();
	private JLabel label;
	private IntegerTextField textField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DFilter_Blur dialog = new DFilter_Blur(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DFilter_Blur(Frame owner) {
		super(owner);
		setBounds(100, 100, 160, 100);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		label = new JLabel("\u6A21\u7CCA\u5F3A\u5EA6");
		label.setBounds(10, 10, 54, 15);
		contentPanel.add(label);

		textField = new IntegerTextField();
		textField.setBounds(74, 7, 66, 21);
		contentPanel.add(textField);
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("ȷ��");
				okButton.setActionCommand("OK");
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
				cancelButton.setActionCommand("Cancel");
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
	 * ��ʾ�Ի��򣬲���ȡ�û����롣
	 * 
	 * @return ����һ������û����������<br>
	 *         0 - (boolean)�û��Ƿ������ȷ��������trueΪȷ����falseΪȡ����<br>
	 *         1 - (int)�û�ѡ��ģ����С
	 * 
	 */
	@Override
	public Object[] showDialog() {
		super.showDialog();
		return new Object[] { isEntered(), textField.getInteger() };
	}

	/**
	 * ���û�ȷ��ʱ�����ݽ��б��洦�����رմ��ڡ�<br>
	 * ����û����������д��򵯳�����Ի���
	 */
	@Override
	public void ok() {
		if (textField.getInteger() > 50 || textField.getInteger() <= 0) {
			JOptionPane.showMessageDialog(this, "ģ��ǿ�ȱ�����1-50֮��");
			return;
		}
		super.ok();
	}
}
