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
	 * ���ɵ����к�
	 */
	private static final long serialVersionUID = 8807302585030547754L;

	// ԭͼ����
	private int width = 0;
	// ԭͼ��߶�
	private int height = 0;
	// �ü����ε���ʼ�������
	private int oriX = 0;
	// �ü����ε���ʼ��������
	private int oriY = 0;
	// �ü����ε���ֹ�������
	private int endX = 0;
	// �ü����ε���ֹ��������
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
	 * ����һ����ȡ�û��ü�ͼ���������ĶԻ���
	 * 
	 * @param owner
	 *            ��������Ի���Ĵ��ڡ�
	 * @param width
	 *            ԭͼ��Ŀ��
	 * @param height
	 *            ԭͼ��߶�
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
	 * ���ֶԻ��򣬲������û����롣
	 * 
	 * @return ����һ���������飬�����û����롣<br>
	 *         0 - �û��Ƿ�ѡ����ȷ����trueΪѡ��falseΪδѡ��<br>
	 *         1 - ��ȡ������ʼ���x����<br>
	 *         2 - ��ȡ������ʼ���y����<br>
	 *         3 - ��ȡ������ֹ���x����<br>
	 *         4 - ��ȡ������ֹ���y����<br>
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
		// ��������ǲ��Ϸ��ģ���ֹȷ���������������Ի���
		if (!checkValid()) {
			return;
		}
		super.ok();
	}

	/**
	 * ����û�����Ĳ����Ƿ�������ģ�������󵯳��Ի�����ʾ��
	 * 
	 * @return ����û��������ȷ����true����֮����false.
	 */
	private boolean checkValid() {
		boolean isValid = true;
		// ���Ͻǵ�ֵ��������½ǵ�ֵС�������Ƕ���ͼ��֮��
		if (endX > width - 1 || endY > height - 1 || oriX >= endX
				|| oriY >= endY) {
			JOptionPane.showMessageDialog(this, "�����������");
			isValid = false;
		}
		return isValid;
	}
}
