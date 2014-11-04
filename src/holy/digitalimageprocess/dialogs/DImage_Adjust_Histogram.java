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
 * ��ʾͼ���ֱ��ͼ��Ϣ����ѯ���û��Ƿ��ͼ�����ֱ��ͼ���⻯��<br>
 * ����һ��ͼ�����û������仯�ĶԻ���
 */
public class DImage_Adjust_Histogram extends IJDialogWithoutImageInstanceChange {

	/**
	 * ����ID
	 */
	private static final long serialVersionUID = -906444273378320175L;

	// ��ɫͨ����ֱ��ͼ����Ƶ��ֵ����һ������Ϊ256�����顣
	private double[] redHistogramData = null;
	// ��ɫͨ����ֱ��ͼ����Ƶ��ֵ����һ������Ϊ256�����顣
	private double[] greenHistogramData = null;
	// ��ɫͨ����ֱ��ͼ����Ƶ��ֵ����һ������Ϊ256�����顣
	private double[] blueHistogramData = null;
	// ȫ����ɫͨ���ϲ���ֱ��ͼ����Ƶ��ֵ����һ������Ϊ256�����顣
	private double[] allHistogramData = null;

	// �û��Ƿ�Ҫ���о��⻯
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
	 * ����һ��չʾͼ��ֱ��ͼ����ѯ���û��Ƿ����ֱ��ͼ���⻯�ĶԻ���
	 * 
	 * @param owner
	 *            ��������Ի���Ĵ��ڡ�
	 * @param redHistogramData
	 *            ��ɫͨ����ÿ��ǿ�ȵ�ֱ��ͼ���ݣ���һ������Ϊ256�ģ�ÿ����ֵ������1�����顣
	 * @param greenHistogramData
	 *            ��ɫͨ����ÿ��ǿ�ȵ�ֱ��ͼ���ݣ���һ������Ϊ256�ģ�ÿ����ֵ������1�����顣
	 * @param blueHistogramData
	 *            ��ɫͨ����ÿ��ǿ�ȵ�ֱ��ͼ���ݣ���һ������Ϊ256�ģ�ÿ����ֵ������1�����顣
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
		comboBox.addItem("���ͨ��");
		comboBox.addItem("��ɫͨ��");
		comboBox.addItem("��ɫͨ��");
		comboBox.addItem("��ɫͨ��");
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
		// Ĭ����ʾ���ͨ��
		histogramRed.setVisible(false);
		panel_1.add(histogramRed);

		histogramGreen = new JLabel("");
		histogramGreen.setSize(new Dimension(256, 100));
		histogramGreen.setMinimumSize(new Dimension(256, 100));
		histogramGreen.setBounds(14, 20, 256, 100);
		// Ĭ����ʾ���ͨ��
		histogramGreen.setVisible(false);
		panel_1.add(histogramGreen);

		histogramBlue = new JLabel("");
		histogramBlue.setSize(new Dimension(256, 100));
		histogramBlue.setMinimumSize(new Dimension(256, 100));
		histogramBlue.setBounds(14, 20, 256, 100);
		// Ĭ����ʾ���ͨ��
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
	 * ����ֱ��ͼ
	 */
	private void paint() {
		// �����ۺ�ͨ��ֱ��ͼ
		histogramAll.setIcon(new ImageIcon(paintAChannel(allHistogramData,
				Channel.ALL)));
		// ���ƺ�ɫֱ��ͼ
		histogramRed.setIcon(new ImageIcon(paintAChannel(redHistogramData,
				Channel.RED)));
		// ������ɫֱ��ͼ
		histogramGreen.setIcon(new ImageIcon(paintAChannel(greenHistogramData,
				Channel.GREEN)));
		// ������ɫֱ��ͼ
		histogramBlue.setIcon(new ImageIcon(paintAChannel(blueHistogramData,
				Channel.BLUE)));

	}

	/**
	 * ����һ��ͨ����ֱ��ͼ��
	 * 
	 * @param channelData
	 *            ���ͨ����ֱ��ͼ���ݣ���һ��256���ȵ����飬ÿ����ֵ������1.0��
	 * @param channel
	 *            ���л��Ƶ�ͨ������ѡChannel����All, Red, Green��Blue���֡�
	 * @return ���ͨ��������ɵ�ֱ��ͼ��
	 */
	private Image paintAChannel(double[] channelData, int channel) {
		double max = 0.0;
		// ����Ѱ�����ͨ����ֱ��ͼ��������ֵ��Ȼ��ȱ������л��ơ�
		// ��Ȼ������ֵ���ղ�Ӧ����0.
		for (int i = 0; i < 256; i++) {
			max = channelData[i] > max ? channelData[i] : max;
		}
		// ����һ��ֱ��ͼͼ�񣬸߶�Ϊ100������Ϊ256
		BufferedImage histogram = new BufferedImage(256, 100,
				BufferedImage.TYPE_INT_ARGB);
		Graphics graphics = histogram.getGraphics();
		// ����ͨ��ѡ����ɫ
		if (channel == Channel.ALL) {
			graphics.setColor(new Color(0xFF000000, true));
		} else if (channel == Channel.RED) {
			graphics.setColor(new Color(0xFFFF0000, true));
		} else if (channel == Channel.GREEN) {
			graphics.setColor(new Color(0xFF00FF00, true));
		} else if (channel == Channel.BLUE) {
			graphics.setColor(new Color(0xFF0000FF, true));
		}

		// ���л���
		for (int i = 0; i < 256; i++) {
			graphics.drawLine(i, 99, i, (int) (100 * (1.0 - channelData[i]
					/ max)));
		}
		return histogram;
	}

	/**
	 * չ�ֶԻ��򲢷����û����롣
	 * 
	 * @return ����һ���������顣<br>
	 *         0 - (boolean)�û��Ƿ������ѡ���Ƿ���true���񷵻�false;<br>
	 *         1 - (boolean)�û��Ƿ����ֱ��ͼ���⻯���Ƿ���true���񷵻�false;<br>
	 */
	@Override
	public Object[] showDialog() {
		// ����ֱ��ͼ
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
	 * �û�ѡ���ͨ�����ֱ�Ϊ��ɫ����ɫ����ɫ�����ѡ���˶��ͨ�������á��롱Ԥ����и��ϡ�<br>
	 */
	public static final class Channel {
		public static final int RED = 0x000000001, GREEN = 0x00000002,
				BLUE = 0x00000004, ALL = 0x00000007;
	}
}
