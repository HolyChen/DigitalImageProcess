package holy.digitalimageprocess.dialogs;

import holy.component.IntegerTextField;
import holy.digitalimageprocess.controller.MenuImageController;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class DImage_Size extends IJDialogWithoutImageInstanceChange {

	/**
	 * 第一个版本
	 */
	private static final long serialVersionUID = 1L;

	// 原图宽度
	private int srcWidth = 1;
	// 原图高度
	private int srcHeight = 1;

	// 宽度
	private int width = 0;
	// 高度
	private int height = 0;
	// 使用的放缩模式
	private String mode = null;
	// 输入数值的单位
	private String unit = null;

	// 是否保持比例
	private boolean holdRadio = true;

	private final JPanel contentPanel = new JPanel();
	private JLabel lblWidth;
	private JLabel lblHeight;
	private IntegerTextField tfWidth;
	private IntegerTextField tfHeight;
	private JComboBox<String> cbMode;
	private JPanel panel;
	private JComboBox<String> cbHoriUnit;
	private JComboBox<String> cbVertiUnit;
	private JCheckBox chkbIsHoldRatio;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
		System.out.println(Arrays.toString(new DImage_Size(null, 800, 600,
				MenuImageController.UNIT_PIXEL).showDialog()));
	}

	/**
	 * 创建一个对话框，让用户输入图片新的高度、宽度、放缩模式、所用单位。<br>
	 * 并将用户输入数据作为一个Object数组进行返回，返回值顺序见return。
	 * 
	 * @param owner
	 *            打开这个对话框的帧
	 * @param srcWidth
	 *            原图像宽度
	 * @param srcHeight
	 *            原图像高度
	 * @param unit
	 *            原图像所用单位，请暂时使用DImageSize中以UNIT_开头常量。
	 * @return 返回值顺序如下：<br> 
	 * 	 	   0-(int)用户的操作状态，对应于IJMyDialog中STATUS_常量;<br>
	 * 		   1-新图像宽度; <br>
	 *         2-新图像高度; 3-新图像所用放缩模式; 4-新图像的单位。
	 * 
	 */
	public Object[] showDialog() {
		try {
			super.showDialog();
			return new Object[] { getStatus(), width, height, mode, unit };
		} catch (Exception e) {
			// 当发生错误时，取消灰度选择
			return null;
		}
	}

	/**
	 * 创建对话框
	 * 
	 * @param owner
	 *            调用创建对话框的窗口
	 * @param srcWidth
	 *            原图像宽度
	 * @param srcHeight
	 *            原图像高度
	 * @param unit
	 *            原图像所用单位
	 */
	public DImage_Size(Frame owner, int srcWidth, int srcHeight, String unit) {
		super(owner);

		// 初始化数据变量
		this.srcWidth = srcWidth;
		this.srcHeight = srcHeight;
		this.unit = unit;

		setBounds(100, 100, 283, 216);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "\u50CF\u7D20\u5927\u5C0F",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.setBounds(10, 10, 254, 80);
		contentPanel.add(panel);
		panel.setLayout(null);
		{
			lblWidth = new JLabel("\u5BBD\u5EA6");
			lblWidth.setBounds(43, 23, 24, 15);
			panel.add(lblWidth);

			lblHeight = new JLabel("\u9AD8\u5EA6");
			lblHeight.setBounds(43, 48, 24, 15);
			panel.add(lblHeight);

			tfWidth = new IntegerTextField();
			tfWidth.setBounds(77, 20, 66, 21);
			// 初始值设置为原图像大小
			tfWidth.setInteger(srcWidth);
			panel.add(tfWidth);
			tfWidth.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// 按下数字键或删除才进行控制比例
					int keycode = e.getKeyCode();
					if ((keycode >= KeyEvent.VK_0 && keycode <= KeyEvent.VK_9)
							|| (keycode >= KeyEvent.VK_NUMPAD0 && keycode <= KeyEvent.VK_NUMPAD9)
							|| (keycode == KeyEvent.VK_BACK_SPACE)
							|| (keycode == KeyEvent.VK_DELETE)) {
						// 宽度值至少为1
						tfWidth.setInteger(Math.max(tfWidth.getInteger(), 1));
						if (holdRadio) {
							adjustHeightByRadio();
						}
					}
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});
			tfWidth.setColumns(10);

			tfHeight = new IntegerTextField();
			tfHeight.setBounds(77, 45, 66, 21);
			// 初始值设置为原图像大小
			tfHeight.setInteger(srcHeight);
			tfHeight.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// 按下数字键或删除才进行控制比例
					int keycode = e.getKeyCode();
					if ((keycode >= KeyEvent.VK_0 && keycode <= KeyEvent.VK_9)
							|| (keycode >= KeyEvent.VK_NUMPAD0 && keycode <= KeyEvent.VK_NUMPAD9)
							|| (keycode == KeyEvent.VK_BACK_SPACE)
							|| (keycode == KeyEvent.VK_DELETE)) {
						// 高度值至少为1
						tfHeight.setInteger(Math.max(tfHeight.getInteger(), 1));
						if (holdRadio) {
							adjustWidthByRadio();
						}
					}

				}

				@Override
				public void keyPressed(KeyEvent e) {
				}
			});

			panel.add(tfHeight);
			tfHeight.setColumns(10);

			// 水平单位
			cbHoriUnit = new JComboBox<String>();
			cbHoriUnit.setBounds(153, 20, 56, 21);
			panel.add(cbHoriUnit);
			cbHoriUnit.addItem(MenuImageController.UNIT_PIXEL);
			cbHoriUnit.setSelectedItem(unit);

			cbVertiUnit = new JComboBox<String>();
			cbVertiUnit.setBounds(153, 45, 56, 21);
			panel.add(cbVertiUnit);
			cbVertiUnit.addItem(MenuImageController.UNIT_PIXEL);
			cbVertiUnit.setSelectedItem(unit);
		}

		// 是否像素保持比例的选择项
		chkbIsHoldRatio = new JCheckBox("\u4FDD\u6301\u6BD4\u4F8B");
		chkbIsHoldRatio.setBounds(10, 96, 103, 23);
		// 设置默认为选中
		chkbIsHoldRatio.setSelected(true);
		chkbIsHoldRatio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				holdRadio = chkbIsHoldRatio.isSelected();
				if (holdRadio) {
					// 立即按比例调整heigth
					adjustHeightByRadio();
				}
			}
		});
		contentPanel.add(chkbIsHoldRatio);

		// 放缩模式
		cbMode = new JComboBox<String>();
		cbMode.setBounds(60, 125, 156, 21);
		contentPanel.add(cbMode);
		cbMode.addItem(MenuImageController.MODE_NEAREST);
		cbMode.addItem(MenuImageController.MODE_BILINEAR);
		// cbMode.addItem(MenuProcessImage.MODE_BICUBIC);
		cbMode.setSelectedItem(MenuImageController.MODE_BILINEAR);

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

	// 按比例调整宽度值
	private void adjustWidthByRadio() {
		// 按比例调整宽度，但是最小宽度为1
		tfWidth.setInteger(Math.max(
				(int) (((double) srcWidth / (double) srcHeight) * tfHeight
						.getInteger()), 1));
	}

	// 按比例调整高度值
	private void adjustHeightByRadio() {
		// 按比例调整高度，但是最小高度为1
		tfHeight.setInteger(Math.max(
				(int) (((double) srcHeight / (double) srcWidth) * tfWidth
						.getInteger()), 1));
	}

	/**
	 * 设置长度、高度、放缩模式、单位为用户输入。
	 */
	@Override
	public void ok() {
		width = tfWidth.getInteger();
		height = tfHeight.getInteger();
		mode = (String) cbMode.getSelectedItem();
		unit = (String) cbHoriUnit.getSelectedItem();
		super.ok();
	}

	/**
	 * 设置长度、高度、放缩模式、单位为0。
	 */
	@Override
	public void cancel() {
		width = 0;
		height = 0;
		mode = null;
		unit = null;
		super.cancel();
	}
}
