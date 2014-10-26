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
	 * ��һ���汾
	 */
	private static final long serialVersionUID = 1L;

	// ԭͼ���
	private int srcWidth = 1;
	// ԭͼ�߶�
	private int srcHeight = 1;

	// ���
	private int width = 0;
	// �߶�
	private int height = 0;
	// ʹ�õķ���ģʽ
	private String mode = null;
	// ������ֵ�ĵ�λ
	private String unit = null;

	// �Ƿ񱣳ֱ���
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
	 * ����һ���Ի������û�����ͼƬ�µĸ߶ȡ���ȡ�����ģʽ�����õ�λ��<br>
	 * �����û�����������Ϊһ��Object������з��أ�����ֵ˳���return��
	 * 
	 * @param owner
	 *            ������Ի����֡
	 * @param srcWidth
	 *            ԭͼ����
	 * @param srcHeight
	 *            ԭͼ��߶�
	 * @param unit
	 *            ԭͼ�����õ�λ������ʱʹ��DImageSize����UNIT_��ͷ������
	 * @return ����ֵ˳�����£�<br> 
	 * 	 	   0-(int)�û��Ĳ���״̬����Ӧ��IJMyDialog��STATUS_����;<br>
	 * 		   1-��ͼ����; <br>
	 *         2-��ͼ��߶�; 3-��ͼ�����÷���ģʽ; 4-��ͼ��ĵ�λ��
	 * 
	 */
	public Object[] showDialog() {
		try {
			super.showDialog();
			return new Object[] { getStatus(), width, height, mode, unit };
		} catch (Exception e) {
			// ����������ʱ��ȡ���Ҷ�ѡ��
			return null;
		}
	}

	/**
	 * �����Ի���
	 * 
	 * @param owner
	 *            ���ô����Ի���Ĵ���
	 * @param srcWidth
	 *            ԭͼ����
	 * @param srcHeight
	 *            ԭͼ��߶�
	 * @param unit
	 *            ԭͼ�����õ�λ
	 */
	public DImage_Size(Frame owner, int srcWidth, int srcHeight, String unit) {
		super(owner);

		// ��ʼ�����ݱ���
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
			// ��ʼֵ����Ϊԭͼ���С
			tfWidth.setInteger(srcWidth);
			panel.add(tfWidth);
			tfWidth.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// �������ּ���ɾ���Ž��п��Ʊ���
					int keycode = e.getKeyCode();
					if ((keycode >= KeyEvent.VK_0 && keycode <= KeyEvent.VK_9)
							|| (keycode >= KeyEvent.VK_NUMPAD0 && keycode <= KeyEvent.VK_NUMPAD9)
							|| (keycode == KeyEvent.VK_BACK_SPACE)
							|| (keycode == KeyEvent.VK_DELETE)) {
						// ���ֵ����Ϊ1
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
			// ��ʼֵ����Ϊԭͼ���С
			tfHeight.setInteger(srcHeight);
			tfHeight.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// �������ּ���ɾ���Ž��п��Ʊ���
					int keycode = e.getKeyCode();
					if ((keycode >= KeyEvent.VK_0 && keycode <= KeyEvent.VK_9)
							|| (keycode >= KeyEvent.VK_NUMPAD0 && keycode <= KeyEvent.VK_NUMPAD9)
							|| (keycode == KeyEvent.VK_BACK_SPACE)
							|| (keycode == KeyEvent.VK_DELETE)) {
						// �߶�ֵ����Ϊ1
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

			// ˮƽ��λ
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

		// �Ƿ����ر��ֱ�����ѡ����
		chkbIsHoldRatio = new JCheckBox("\u4FDD\u6301\u6BD4\u4F8B");
		chkbIsHoldRatio.setBounds(10, 96, 103, 23);
		// ����Ĭ��Ϊѡ��
		chkbIsHoldRatio.setSelected(true);
		chkbIsHoldRatio.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				holdRadio = chkbIsHoldRatio.isSelected();
				if (holdRadio) {
					// ��������������heigth
					adjustHeightByRadio();
				}
			}
		});
		contentPanel.add(chkbIsHoldRatio);

		// ����ģʽ
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

	// �������������ֵ
	private void adjustWidthByRadio() {
		// ������������ȣ�������С���Ϊ1
		tfWidth.setInteger(Math.max(
				(int) (((double) srcWidth / (double) srcHeight) * tfHeight
						.getInteger()), 1));
	}

	// �����������߶�ֵ
	private void adjustHeightByRadio() {
		// �����������߶ȣ�������С�߶�Ϊ1
		tfHeight.setInteger(Math.max(
				(int) (((double) srcHeight / (double) srcWidth) * tfWidth
						.getInteger()), 1));
	}

	/**
	 * ���ó��ȡ��߶ȡ�����ģʽ����λΪ�û����롣
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
	 * ���ó��ȡ��߶ȡ�����ģʽ����λΪ0��
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
