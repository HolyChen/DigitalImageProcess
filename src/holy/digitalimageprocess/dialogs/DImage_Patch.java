package holy.digitalimageprocess.dialogs;

import holy.component.IntegerTextField;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DImage_Patch extends IJDialogWithoutImageInstanceChange {

	/**
	 * 生成的序列号
	 */
	private static final long serialVersionUID = 8807302585030547754L;

	// 原图像宽度
	private int width = 0;
	// 原图像高度
	private int height = 0;
	// 裁剪矩形的起始点横坐标
	private int oriX = 0;
	// 裁剪矩形的起始点纵坐标
	private int oriY = 0;
	// 裁剪矩形的终止点横坐标
	private int endX = 0;
	// 裁剪矩形的终止点纵坐标
	private int endY = 0;

	private final JPanel contentPanel = new JPanel();
	private JPanel panel;
	private JLabel label;
	private JLabel lblX;
	private JLabel lblT;
	private IntegerTextField tfOriX;
	private IntegerTextField tfOriY;
	private JLabel label_1;
	private IntegerTextField tfEndX;
	private IntegerTextField tfEndY;

	/**
	 * Launch the application.
	 * @param args 无用
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			DImage_Patch dialog = new DImage_Patch(null, 630, 853);
			System.out.println(Arrays.toString(dialog.showDialog()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建一个获取用户裁剪图像参数输入的对话框。
	 * 
	 * @param owner
	 *            创建这个对话框的窗口。
	 * @param width
	 *            原图像的宽度
	 * @param height
	 *            原图像高度
	 */
	public DImage_Patch(Frame owner, int width, int height) {
		super(owner);

		this.width = width;
		this.height = height;

		setBounds(100, 100, 232, 172);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u77E9\u5F62\u88C1\u526A",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 202, 91);
		contentPanel.add(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		lblX = new JLabel("X\u5750\u6807");
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.gridx = 1;
		gbc_lblX.gridy = 0;
		panel.add(lblX, gbc_lblX);

		lblT = new JLabel("Y\u5750\u6807");
		GridBagConstraints gbc_lblT = new GridBagConstraints();
		gbc_lblT.insets = new Insets(0, 0, 5, 0);
		gbc_lblT.gridx = 2;
		gbc_lblT.gridy = 0;
		panel.add(lblT, gbc_lblT);

		label = new JLabel("\u8D77\u59CB\u5750\u6807");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		panel.add(label, gbc_label);

		tfOriX = new IntegerTextField();
		GridBagConstraints gbc_tfOriX = new GridBagConstraints();
		gbc_tfOriX.insets = new Insets(0, 0, 5, 5);
		gbc_tfOriX.gridx = 1;
		gbc_tfOriX.gridy = 1;
		panel.add(tfOriX, gbc_tfOriX);
		tfOriX.setColumns(10);

		tfOriY = new IntegerTextField();
		tfOriY.setColumns(10);
		GridBagConstraints gbc_tfOriY = new GridBagConstraints();
		gbc_tfOriY.insets = new Insets(0, 0, 5, 0);
		gbc_tfOriY.gridx = 2;
		gbc_tfOriY.gridy = 1;
		panel.add(tfOriY, gbc_tfOriY);

		label_1 = new JLabel("\u7EC8\u6B62\u5750\u6807");
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 0, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		panel.add(label_1, gbc_label_1);

		tfEndX = new IntegerTextField();
		tfEndX.setColumns(10);
		GridBagConstraints gbc_tfEndX = new GridBagConstraints();
		gbc_tfEndX.insets = new Insets(0, 0, 0, 5);
		gbc_tfEndX.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfEndX.gridx = 1;
		gbc_tfEndX.gridy = 2;
		panel.add(tfEndX, gbc_tfEndX);

		tfEndY = new IntegerTextField();
		tfEndY.setColumns(10);
		GridBagConstraints gbc_tfEndY = new GridBagConstraints();
		gbc_tfEndY.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfEndY.gridx = 2;
		gbc_tfEndY.gridy = 2;
		panel.add(tfEndY, gbc_tfEndY);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("\u786E\u5B9A");
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
				JButton cancelButton = new JButton("\u53D6\u6D88");
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
	 * 呈现对话框，并返回用户输入。
	 * 
	 * @return 返回一个对象数组，包含用户输入。<br>
	 *         0 - 用户是否选择了确定，true为选择；false为未选择<br>
	 *         1 - 截取区域起始点的x坐标<br>
	 *         2 - 截取区域起始点的y坐标<br>
	 *         3 - 截取区域终止点的x坐标<br>
	 *         4 - 截取区域终止点的y坐标<br>
	 */
	@Override
	public Object[] showDialog() {
		super.showDialog();
		return new Object[] { isEntered(), oriX, oriY, endX, endY, };
	}

	@Override
	public void ok() {
		this.oriX = tfOriX.getInteger();
		this.oriY = tfOriY.getInteger();
		this.endX = tfEndX.getInteger();
		this.endY = tfEndY.getInteger();
		// 如果参数是不合法的，终止确定操作，并弹出对话框
		if (!checkValid()) {
			return;
		}
		super.ok();
	}

	/**
	 * 检测用户输入的参数是否是有误的，如果有误弹出对话框提示。
	 * 
	 * @return 如果用户输入的正确返回true，反之返回false.
	 */
	private boolean checkValid() {
		boolean isValid = true;
		// 左上角的值必须必右下角的值小，且他们都在图像之内
		if (endX > width - 1 || endY > height - 1 || oriX >= endX
				|| oriY >= endY) {
			JOptionPane.showMessageDialog(this, "输入参数有误！");
			isValid = false;
		}
		return isValid;
	}
}
