package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ImageData;
import holy.digitalimageprocess.data.ProcessAdjust;
import holy.digitalimageprocess.data.ProcessFilter;
import holy.digitalimageprocess.dialogs.DFilter_Overlay;
import holy.util.IController;
import holy.util.IOKorCancelDialog;

import java.awt.Frame;
import java.awt.Image;

public class MenuFilterController extends IController {

	/**
	 * �������󣬲����������frame��Ϊ�����ġ�
	 * 
	 * @param frame
	 *            ��������˵���������Frame
	 */
	public MenuFilterController(Frame frame) {
		super(frame);
	}

	/**
	 * ������ͼ����һ���������е��ӹ��ܡ�
	 * 
	 * @param imageData
	 *            ԭͼ��
	 */
	public void imageOverlay(ImageData imageData) {
		DFilter_Overlay dialog = new DFilter_Overlay(context);
		Image srcImage = imageData.getImage();
		// �첽�򿪵��Ӵ��ڣ���ֹ�����̡߳�
		new Thread(new Runnable() {

			@Override
			public void run() {
				dialog.showDialog();
			}
		}).start();

		// �첽��ȡ�û����룬�����д�����ֹ�߳�����
		new Thread(new Runnable() {

			@Override
			public void run() {
				Object[] results;
				// ������һ�λ�ȡ��ͼƬ�����ⲻ��Ҫ�Ĵ���
				Image lastImage = null;
				// ������һ�λ�ȡ�ĵ��ӱ��������ⲻ��Ҫ�Ĵ���
				double lastRadio = 0.0;

				Image image = null;
				double radio = 0.0;
				int status = IOKorCancelDialog.STATUS_UNDECIDED;
				// ��ȡ��ͼ��Ҫ�����ĵ���imageData��ͬ��С�����ｫ�仺��
				// �Լ�����������
				Image resizedImage = null;
				Image overlayImage = srcImage; 

				while (true) {
					results = dialog.getResult();
					status = (int) results[0];
					image = (Image) results[1];
					radio = (double) results[2];

					if (status == IOKorCancelDialog.STATUS_CANCELED) {
						break;
					} else if (status == IOKorCancelDialog.STATUS_ENTERED) {
						// �û�ȷ������
						imageData.setImage(overlayImage);
						break;
					} else {
						// �û�����ʵʱ�ı�������
						if (image != null) {
							// ͼƬû�иı�
							if (image == lastImage) {
								// ����Ҳû�иı䣬�����д���
								if (radio == lastRadio) {
									continue;
								} else {
									// �������ı䣬�������ɵ��Ӻ�ͼ��
									lastRadio = radio;
									overlayImage = ProcessFilter.getInstance().imageOverlay(
											srcImage, resizedImage, radio);
								}
							} else {
								// �������ñ���
								lastRadio = radio;
								// ���¶�ͼ����������С
								lastImage = image;
								if (image.getWidth(null) == srcImage
										.getWidth(null)
										&& image.getHeight(null) == srcImage
												.getHeight(null)) {
									// ͼ���Сһ�£�ֱ�ӵ���
									resizedImage = image;
								} else {
									// ͼ���С��һ�£����������С��ʹ�ö�������
									resizedImage = ProcessAdjust.getInstance()
											.resizeImageBilinear(image,
													srcImage.getWidth(null),
													srcImage.getHeight(null));
								}
								overlayImage = ProcessFilter.getInstance().imageOverlay(
										srcImage, resizedImage, radio);
							}
							imageData.setDisplayImage(overlayImage);
						} else {
							imageData.setDisplayImage(srcImage);
						}
					}
				}
			}
		}).start();
	}

}
