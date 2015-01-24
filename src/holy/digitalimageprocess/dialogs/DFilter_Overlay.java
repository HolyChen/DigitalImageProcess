package holy.digitalimageprocess.dialogs;

import holy.digitalimageprocess.controller.MenuFileController;
import holy.digitalimageprocess.data.ImageData;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 显示一个对话框，让用户选择一张图片，然后对两张图片进行透明度叠加。<br>
 * 注意：这是一个图像会随用户操作即时变化的对话窗口，需要用getResult获取结果。
 */
public class DFilter_Overlay extends IJDialogWithImageInstanceChange {

	private static final long serialVersionUID = -9088111910809678528L;

	// 打开的要进行渐变叠加的照片
	ImageData imageData = new ImageData();
	/*
	 * 渐变叠加的比例，0为原图，100为新图。 采用公式如下： [f(x,y)] = (1-radio)[f0(x, y)] + radio[f1(x,
	 * y)].
	 */
	double radio = 0.0;
	// 对文件进行控制存取的类
	MenuFileController mnpFile;

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JButton btnOpen;
	private JPanel panel;
	private JLabel lblValue;
	private JLabel lblTips;

	/**
	 * 测试对话框
	 * 
	 * @param args
	 *            无用
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			DFilter_Overlay dialog = new DFilter_Overlay(null);
			new Thread(new Runnable() {

				@Override
				public void run() {
					dialog.showDialog();
				}
			}).start();
			while (!dialog.isEnterOrCancel()) {
				System.out.println(Arrays.toString(dialog.getResult()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 使用打开对话框的帧初始化“滤镜-图像叠加”对话框。
	 * 
	 * @param owner
	 *            打开这个对话框的帧
	 */
	public DFilter_Overlay(Frame owner) {
		super(owner);

		mnpFile = new MenuFileController(owner);

		setBounds(100, 100, 322, 149);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panel = new JPanel();
		panel.setBounds(8, 31, 299, 46);
		contentPanel.add(panel);
		panel.setLayout(null);

		// 显示渐变比例的label
		lblValue = new JLabel("0");
		lblValue.setEnabled(false);
		lblValue.setBounds(284, 10, 27, 26);
		panel.add(lblValue);

		// 控制渐变比例的滑动条
		slider = new JSlider();
		slider.setToolTipText("\u201C\u8BF7\u5148\u9009\u62E9\u56FE\u7247\"");
		slider.setEnabled(false);
		slider.setBounds(74, 10, 200, 26);
		panel.add(slider);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// 将数值显示在label中
				lblValue.setText(Integer.toString(slider.getValue()));
				// 将数值存入radio中
				radio = (double) slider.getValue()
						/ (double) slider.getMaximum();
			}
		});

		slider.setValue(0);

		// 打开要进行渐变叠加的图片
		btnOpen = new JButton("\u9009\u62E9\u56FE\u7247");
		btnOpen.setBounds(111, 10, 93, 23);
		btnOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				mnpFile.openFile(imageData);
				if (imageData.getImage() != null) {
					slider.setEnabled(true);
					lblTips.setEnabled(true);
					lblValue.setEnabled(true);
				} else {
					slider.setEnabled(false);
					lblTips.setEnabled(false);
					lblValue.setEnabled(false);
				}
			}
		});
		contentPanel.add(btnOpen);

		lblTips = new JLabel("\u53E0\u52A0\u6BD4\u4F8B");
		lblTips.setEnabled(false);
		lblTips.setBounds(10, 15, 54, 15);
		panel.add(lblTips);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("确定");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						ok();
					}
				});
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
	 * 获取用户的输入结果。<br>
	 * 下列为返回结果顺序：<br>
	 * 0-(int)用户操作的状态，为IJDialogWithImageChange中的STATUS_常量,<br>
	 * 1-(Image)用户选择要进行叠加的图片,<br>
	 * 2-(double)用户选择的渐变比例。
	 */
	@Override
	public Object[] getResult() {
		return new Object[] { getStatus(), imageData.getImage(), radio };
	}
}
