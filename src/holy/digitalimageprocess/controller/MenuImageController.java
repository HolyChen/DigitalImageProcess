/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ProcessAdjust;
import holy.digitalimageprocess.data.ImageData;
import holy.digitalimageprocess.dialogs.DImage_Adjust_GrayLevel;
import holy.digitalimageprocess.dialogs.DImage_Size;
import holy.util.IController;
import holy.util.IOKorCancelDialog;

import java.awt.Frame;
import java.awt.Image;

/**
 * ����ַ���ͼ�񡱲˵����ܵ���
 */
public class MenuImageController extends IController {
	// ��ͼ�������ʵ��
	ProcessAdjust imageAdjust;

	public MenuImageController(Frame frame) {
		super(frame);
		this.imageAdjust = ProcessAdjust.getInstance();
	}

	/**
	 * ��ͼ���ջҶȽ���level��Ϊ�Ҷ�ͼ��
	 * 
	 * @param imageData
	 *            ��Ҫ���иı��ͼ��
	 */
	public void adjust_ToBlackWhite(ImageData imageData) {
		// ʹ���첽��������ֹ�������߳�
		new Thread(new Runnable() {
			@Override
			public void run() {
				// �ҶȽ���
				Object[] returns = new DImage_Adjust_GrayLevel(context).showDialog();
				try {
					if (returns != null && (int) returns[0] == IOKorCancelDialog.STATUS_ENTERED) {
						int level = (int) returns[1];
						Image image = imageAdjust.toBlackWhite(
								imageData.getImage(), level);
						imageData.setImage(image);
					}
				} catch (Exception e) {
					System.out.println("ת���Ҷ�ͼ�����");
					return;
				}

			}
		}).start();
	}

	/**
	 * �ı�ͼ���С��
	 * 
	 * @param imageData
	 *            Ҫ�ı�ͼ���С��ͼ��
	 */
	public void resizeImage(ImageData imageData) {
		// ʹ���첽��������ֹ�������߳�
		new Thread(new Runnable() {
			@Override
			public void run() {
				Image image = imageData.getImage();
				
				// �������Ͳμ�DImageSize.showDialog�ĵ�
				Object[] returns = new DImage_Size(context,
						image.getWidth(null), image.getHeight(null), UNIT_PIXEL)
						.showDialog();
				
				// ����û�ѡ�����µ�ͼ���С�Ž��в���
				if (returns != null && (int)returns[0] == IOKorCancelDialog.STATUS_ENTERED) {
					Image newImage = null;
					// �û�ѡ�����ڽ���ʽ
					if (((String) returns[3]).equals(MODE_NEAREST)) {
						newImage = ProcessAdjust.getInstance()
								.resizeImageNearest(imageData.getImage(),
										(int) returns[1], (int) returns[2]);
					}
					// �û�ѡ�����������Է�ʽ
					if (((String) returns[3]).equals(MODE_BILINEAR)) {
						newImage = ProcessAdjust.getInstance()
								.resizeImageBilinear(imageData.getImage(),
										(int) returns[1], (int) returns[2]);
					}
					imageData.setImage(newImage);
				}
			}
		}).start();
	}

	// ------------------- ����ʱ�õ��ĳ��� -------------------

	// ����ģʽ
	public static final String MODE_NEAREST = "�ڽ�������Ӳ��Ե��",
			MODE_BILINEAR = "��������", MODE_BICUBIC = "�����������Զ���";
	// ��λ
	public static final String UNIT_PIXEL = "����";
}
