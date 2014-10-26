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
 * ��ʾһ���Ի������û�ѡ��һ��ͼƬ��Ȼ�������ͼƬ����͸���ȵ��ӡ�<br>
 * ע�⣺����һ��ͼ������û�������ʱ�仯�ĶԻ����ڣ���Ҫ��getResult��ȡ�����
 */
public class DFilter_Overlay extends IJDialogWithImageInstanceChange {

	private static final long serialVersionUID = -9088111910809678528L;

	// �򿪵�Ҫ���н�����ӵ���Ƭ
	ImageData imageData = new ImageData();
	/*
	 * ������ӵı�����0Ϊԭͼ��100Ϊ��ͼ�� ���ù�ʽ���£� [f(x,y)] = (1-radio)[f0(x, y)] + radio[f1(x,
	 * y)].
	 */
	double radio = 0.0;
	// ���ļ����п��ƴ�ȡ����
	MenuFileController mnpFile;

	private final JPanel contentPanel = new JPanel();
	private JSlider slider;
	private JButton btnOpen;
	private JPanel panel;
	private JLabel lblValue;
	private JLabel lblTips;

	/**
	 * ���ԶԻ���
	 * 
	 * @param args
	 *            ����
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
	 * ʹ�ô򿪶Ի����֡��ʼ�����˾�-ͼ����ӡ��Ի���
	 * 
	 * @param owner
	 *            ������Ի����֡
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

		// ��ʾ���������label
		lblValue = new JLabel("0");
		lblValue.setEnabled(false);
		lblValue.setBounds(284, 10, 27, 26);
		panel.add(lblValue);

		// ���ƽ�������Ļ�����
		slider = new JSlider();
		slider.setToolTipText("\u201C\u8BF7\u5148\u9009\u62E9\u56FE\u7247\"");
		slider.setEnabled(false);
		slider.setBounds(74, 10, 200, 26);
		panel.add(slider);
		slider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// ����ֵ��ʾ��label��
				lblValue.setText(Integer.toString(slider.getValue()));
				// ����ֵ����radio��
				radio = (double) slider.getValue()
						/ (double) slider.getMaximum();
			}
		});

		slider.setValue(0);

		// ��Ҫ���н�����ӵ�ͼƬ
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
				JButton okButton = new JButton("ȷ��");
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
	 * ��ȡ�û�����������<br>
	 * ����Ϊ���ؽ��˳��<br>
	 * 0-(int)�û�������״̬��ΪIJDialogWithImageChange�е�STATUS_����,<br>
	 * 1-(Image)�û�ѡ��Ҫ���е��ӵ�ͼƬ,<br>
	 * 2-(double)�û�ѡ��Ľ��������
	 */
	@Override
	public Object[] getResult() {
		return new Object[] { getStatus(), imageData.getImage(), radio };
	}
}
