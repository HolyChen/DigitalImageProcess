package holy.digitalimageprocess.dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * 显示图像的直方图信息，并询问用户是否对图像进行直方图均衡化。<br>
 * 这是一个图像不随用户操作变化的对话框。
 */
public class DImage_Adjust_Histogram extends IJDialogWithoutImageInstanceChange {

	/**
	 * 序列ID
	 */
	private static final long serialVersionUID = -906444273378320175L;

	// 红色通道的直方图各列频率值，是一个长度为256的数组。
	private double[] redHistogramData = null;
	// 绿色通道的直方图各列频率值，是一个长度为256的数组。
	private double[] greenHistogramData = null;
	// 蓝色通道的直方图各列频率值，是一个长度为256的数组。
	private double[] blueHistogramData = null;
	// 全部颜色通道合并的直方图各列频率值，是一个长度为256的数组。
	private double[] allHistogramData = null;

	// 用户是否要进行均衡化
	private boolean isEqulizaion = false;

	private final JPanel contentPanel = new JPanel();
	private JPanel panel;
	private JComboBox<String> comboBox;
	private JPanel panel_1;
	private JLabel histogramRed;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel histogramBlue;
	private JLabel histogramGreen;
	private JCheckBox ckbxEqu;
	private JLabel histogramAll;

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
			double[] data = new double[256];
			for (int i = 0; i < 256; i++) {
				data[i] = (i / 256.0);
			}
			DImage_Adjust_Histogram dialog = new DImage_Adjust_Histogram(null,
					data, data, data);
			System.out.println(Arrays.toString(dialog.showDialog()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建一个展示图像直方图，并询问用户是否进行直方图均衡化的对话框。
	 * 
	 * @param owner
	 *            创建这个对话框的窗口。
	 * @param redHistogramData
	 *            红色通道的每个强度的直方图数据，是一个长度为256的，每个数值不大于1的数组。
	 * @param greenHistogramData
	 *            绿色通道的每个强度的直方图数据，是一个长度为256的，每个数值不大于1的数组。
	 * @param blueHistogramData
	 *            蓝色通道的每个强度的直方图数据，是一个长度为256的，每个数值不大于1的数组。
	 */
	public DImage_Adjust_Histogram(Frame owner, double[] redHistogramData,
			double[] greenHistogramData, double[] blueHistogramData) {
		super(owner);

		this.redHistogramData = redHistogramData;
		this.greenHistogramData = greenHistogramData;
		this.blueHistogramData = blueHistogramData;

		this.allHistogramData = new double[256];
		for (int i = 0; i < 256; i++) {
			allHistogramData[i] = 1.0 / 3.0 * (redHistogramData[i]
					+ greenHistogramData[i] + blueHistogramData[i]);
		}

		setBounds(100, 100, 311, 279);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		panel = new JPanel();
		panel.setBounds(10, 10, 285, 42);
		contentPanel.add(panel);
		panel.setLayout(null);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(10, 10, 98, 21);
		comboBox.addItem("混合通道");
		comboBox.addItem("红色通道");
		comboBox.addItem("绿色通道");
		comboBox.addItem("蓝色通道");
		comboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				if (comboBox.getSelectedIndex() == 0) {
					histogramAll.setVisible(true);
					histogramRed.setVisible(false);
					histogramGreen.setVisible(false);
					histogramBlue.setVisible(false);
				} else if (comboBox.getSelectedIndex() == 1) {
					histogramAll.setVisible(false);
					histogramRed.setVisible(true);
					histogramGreen.setVisible(false);
					histogramBlue.setVisible(false);
				} else if (comboBox.getSelectedIndex() == 2) {
					histogramAll.setVisible(false);
					histogramRed.setVisible(false);
					histogramGreen.setVisible(true);
					histogramBlue.setVisible(false);
				} else if (comboBox.getSelectedIndex() == 3) {
					histogramAll.setVisible(false);
					histogramRed.setVisible(false);
					histogramGreen.setVisible(false);
					histogramBlue.setVisible(true);
				}
			}
		});

		panel.add(comboBox);

		ckbxEqu = new JCheckBox("\u5747\u8861\u5316");
		ckbxEqu.setBounds(176, 9, 103, 23);
		panel.add(ckbxEqu);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "\u76F4\u65B9\u56FE",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 54, 285, 153);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		histogramAll = new JLabel("");
		histogramAll.setBounds(14, 20, 256, 100);
		panel_1.add(histogramAll);
		histogramAll.setMinimumSize(new Dimension(256, 100));

		histogramRed = new JLabel("");
		histogramRed.setLocation(14, 20);
		histogramRed.setMinimumSize(new Dimension(256, 100));
		histogramRed.setSize(new Dimension(256, 100));
		// 默认显示混合通道
		histogramRed.setVisible(false);
		panel_1.add(histogramRed);

		histogramGreen = new JLabel("");
		histogramGreen.setSize(new Dimension(256, 100));
		histogramGreen.setMinimumSize(new Dimension(256, 100));
		histogramGreen.setBounds(14, 20, 256, 100);
		// 默认显示混合通道
		histogramGreen.setVisible(false);
		panel_1.add(histogramGreen);

		histogramBlue = new JLabel("");
		histogramBlue.setSize(new Dimension(256, 100));
		histogramBlue.setMinimumSize(new Dimension(256, 100));
		histogramBlue.setBounds(14, 20, 256, 100);
		// 默认显示混合通道
		histogramBlue.setVisible(false);
		panel_1.add(histogramBlue);

		label_1 = new JLabel("0");
		label_1.setBounds(14, 130, 54, 15);
		panel_1.add(label_1);

		label_2 = new JLabel("255");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(221, 130, 54, 15);
		panel_1.add(label_2);
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
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						cancel();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	/**
	 * 绘制直方图
	 */
	private void paint() {
		// 绘制综合通道直方图
		histogramAll.setIcon(new ImageIcon(paintAChannel(allHistogramData,
				Channel.ALL)));
		// 绘制红色直方图
		histogramRed.setIcon(new ImageIcon(paintAChannel(redHistogramData,
				Channel.RED)));
		// 绘制绿色直方图
		histogramGreen.setIcon(new ImageIcon(paintAChannel(greenHistogramData,
				Channel.GREEN)));
		// 绘制蓝色直方图
		histogramBlue.setIcon(new ImageIcon(paintAChannel(blueHistogramData,
				Channel.BLUE)));

	}

	/**
	 * 绘制一个通道的直方图。
	 * 
	 * @param channelData
	 *            这个通道的直方图数据，是一个256长度的数组，每个数值不大于1.0。
	 * @param channel
	 *            进行绘制的通道，可选Channel类中All, Red, Green、Blue四种。
	 * @return 这个通道绘制完成的直方图。
	 */
	private Image paintAChannel(double[] channelData, int channel) {
		double max = 0.0;
		// 首先寻找这个通道中直方图数据最大的值，然后等比例进行绘制。
		// 显然这个最大值最终不应该是0.
		for (int i = 0; i < 256; i++) {
			max = channelData[i] > max ? channelData[i] : max;
		}
		// 创建一个直方图图像，高度为100，长度为256
		BufferedImage histogram = new BufferedImage(256, 100,
				BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = histogram.getGraphics();
		// 根据通道选择颜色
		if (channel == Channel.ALL) {
			graphics.setColor(new Color(0xFF000000, true));
		} else if (channel == Channel.RED) {
			graphics.setColor(new Color(0xFFFF0000, true));
		} else if (channel == Channel.GREEN) {
			graphics.setColor(new Color(0xFF00FF00, true));
		} else if (channel == Channel.BLUE) {
			graphics.setColor(new Color(0xFF0000FF, true));
		}

		// 进行绘制
		for (int i = 0; i < 256; i++) {
			graphics.drawLine(i, 99, i, (int) (100 * (1.0 - channelData[i]
					/ max)));
		}
		return histogram;
	}

	/**
	 * 展现对话框并返回用户输入。
	 * 
	 * @return 返回一个对象数组。<br>
	 *         0 - (boolean)用户是否进行了选择，是返回true，否返回false;<br>
	 *         1 - (boolean)用户是否进行直方图均衡化，是返回true，否返回false;<br>
	 */
	@Override
	public Object[] showDialog() {
		// 绘制直方图
		paint();
		super.showDialog();
		return new Object[] { isEntered(), isEqulizaion };
	}

	@Override
	public void ok() {
		isEqulizaion = ckbxEqu.isSelected();
		super.ok();
	};

	@Override
	public void cancel() {
		isEqulizaion = false;
		super.cancel();
	}

	/**
	 * 用户选择的通道，分别为红色、绿色、蓝色。如果选择了多个通道，采用“与”预算进行复合。<br>
	 */
	public static final class Channel {
		public static final int RED = 0x000000001, GREEN = 0x00000002,
				BLUE = 0x00000004, ALL = 0x00000007;
	}
}
