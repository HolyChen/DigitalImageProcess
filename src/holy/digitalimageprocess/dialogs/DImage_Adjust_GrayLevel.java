/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
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
 * 打开一个对话框，让用户输入灰度阶数，当用户确定是，改变灰度阶数
 */
public class DImage_Adjust_GrayLevel extends IJDialogWithoutImageInstanceChange {
	/**
	 * 第一版，采用下拉菜单的方式选取2的整数次方阶数
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JComboBox<Integer> cbGrayLevels;
	private JLabel lblNewLabel;

	// 选择的灰度阶数
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
	 * 打开灰度结束选择对话框，并且要求用户在2、4、8、16、32、64、128、256阶数
	 * 之中选择一个合适的值。如果选择中发生错误或用户取消选择，将返回0，此时 代表没有选择一个灰度阶数。
	 * 
	 * @return 返回用户是否进行了选择、和用户选择的灰度阶数(2、4、8、16、32、64、128、256)。 顺序如下：<br>
	 *         0-(int)用户的操作状态，对应于IJMyDialog中STATUS_常量;<br>
	 *         ，<br>
	 *         1-(int)用户选择的灰度阶数。
	 * 
	 */
	@Override
	public Object[] showDialog() {
		try {
			super.showDialog();
			return new Object[] { getStatus(), level, };
		} catch (Exception e) {
			// 当发生错误时，取消灰度选择
			return null;
		}
	}

	/**
	 * 创建一个选择灰度的对话框，并将父界面做为所有者。
	 * 
	 * @param owner
	 *            打开这个对话框的父界面。
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

		// 添加灰度色阶的选项，限制为2、4、8、16、32、64、128、色阶
		for (int i = 2; i <= 256;) {
			cbGrayLevels.addItem(i);
			i <<= 1;
		}
		// 默认选择256色阶
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
				JButton okButton = new JButton("确定");
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
	 * 记录用户输入的level值。
	 */
	@Override
	public void ok() {
		level = (int) (cbGrayLevels.getSelectedItem());
		super.ok();
	}

	/**
	 * 将level值设为0。
	 */
	@Override
	public void cancel() {
		level = 0;
		super.cancel();
	}
}
