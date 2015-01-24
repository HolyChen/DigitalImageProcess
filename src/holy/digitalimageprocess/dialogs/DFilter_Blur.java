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
	 * @param args 无用
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
	 * 创建对话框。
	 * 创建对对话框的父窗口。
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
				JButton okButton = new JButton("确定");
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
				JButton cancelButton = new JButton("取消");
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
	 * 显示对话框，并获取用户输入。
	 * 
	 * @return 返回一组包含用户输入的数据<br>
	 *         0 - (boolean)用户是否进行了确定操作。true为确定，false为取消。<br>
	 *         1 - (int)用户选择模糊大小
	 * 
	 */
	@Override
	public Object[] showDialog() {
		super.showDialog();
		return new Object[] { isEntered(), textField.getInteger() };
	}

	/**
	 * 当用户确定时对数据进行保存处理，并关闭窗口。<br>
	 * 如果用户输入数据有错，则弹出报错对话框。
	 */
	@Override
	public void ok() {
		if (textField.getInteger() > 50 || textField.getInteger() <= 0) {
			JOptionPane.showMessageDialog(this, "模糊强度必须在1-50之间");
			return;
		}
		super.ok();
	}
}
