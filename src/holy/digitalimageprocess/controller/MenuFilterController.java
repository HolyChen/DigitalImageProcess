package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ImageData;
import holy.digitalimageprocess.data.ProcessAdjust;
import holy.digitalimageprocess.data.ProcessFilter;
import holy.digitalimageprocess.dialogs.DFilter_Blur;
import holy.digitalimageprocess.dialogs.DFilter_Overlay;
import holy.util.IController;
import holy.util.IOKorCancelDialog;

import java.awt.Frame;
import java.awt.Image;

public class MenuFilterController extends IController {

	/**
	 * 创建对象，并将包含其的frame作为上下文。
	 * 
	 * @param frame
	 *            包含这个菜单处理器的Frame
	 */
	public MenuFilterController(Frame frame) {
		super(frame);
	}

	/**
	 * 对两个图像按照一定比例进行叠加功能。
	 * 
	 * @param imageData
	 *            原图像
	 */
	public void imageOverlay(ImageData imageData) {
		DFilter_Overlay dialog = new DFilter_Overlay(context);
		Image srcImage = imageData.getImage();
		// 异步打开叠加窗口，防止阻塞线程。
		new Thread(new Runnable() {

			@Override
			public void run() {
				dialog.showDialog();
			}
		}).start();

		// 异步获取用户输入，并进行处理，防止线程阻塞
		new Thread(new Runnable() {

			@Override
			public void run() {
				Object[] results;
				// 缓存上一次获取的图片，避免不必要的处理
				Image lastImage = null;
				// 缓存上一次获取的叠加比例，避免不必要的处理
				double lastRadio = 0.0;

				Image image = null;
				double radio = 0.0;
				int status = IOKorCancelDialog.STATUS_UNDECIDED;
				// 获取的图像要被更改到与imageData相同大小，这里将其缓存
				// 以减少运算需求。
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
						// 用户确定操作
						imageData.setImage(overlayImage);
						break;
					} else {
						// 用户正在实时改变着输入
						if (image != null) {
							// 图片没有改变
							if (image == lastImage) {
								// 比例也没有改变，不进行处理
								if (radio == lastRadio) {
									continue;
								} else {
									// 仅比例改变，重新生成叠加后图像
									lastRadio = radio;
									overlayImage = ProcessFilter.getInstance()
											.imageOverlay(srcImage,
													resizedImage, radio);
								}
							} else {
								// 重新设置比例
								lastRadio = radio;
								// 重新对图像进行重设大小
								lastImage = image;
								if (image.getWidth(null) == srcImage
										.getWidth(null)
										&& image.getHeight(null) == srcImage
												.getHeight(null)) {
									// 图像大小一致，直接叠加
									resizedImage = image;
								} else {
									// 图像大小不一致，首先重设大小，使用二次线性
									resizedImage = ProcessAdjust.getInstance()
											.resizeImageBilinear(image,
													srcImage.getWidth(null),
													srcImage.getHeight(null));
								}
								overlayImage = ProcessFilter.getInstance()
										.imageOverlay(srcImage, resizedImage,
												radio);
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

	/**
	 * 对图像进行模糊操作
	 * 
	 * @param imageData
	 *            要进行模糊的图像
	 */
	public void blur(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Object[] results = new DFilter_Blur(context).showDialog();
				if ((boolean) results[0]) {
					int intensity = (int) results[1];
					imageData.setImage(ProcessFilter.getInstance().blur(
							imageData.getImage(), intensity));
				}
			}
		}).start();
	}

	/**
	 * 对图像进行锐化操作
	 * 
	 * @param imageData
	 *            要进行锐化的图像
	 */
	public void sharpen(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				imageData.setImage(ProcessFilter.getInstance().sharpen(
						imageData.getImage()));
			}
		}).start();
	}

	/**
	 * 对图像进行Sobel横向滤波操作
	 * 
	 * @param imageData
	 *            要进行锐化的图像
	 */
	public void sobelHorizontal(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				imageData.setImage(ProcessFilter.getInstance().sobelHorizontal(
						imageData.getImage()));
			}
		}).start();
	}
	
	/**
	 * 对图像进行Sobel纵向滤波操作
	 * 
	 * @param imageData
	 *            要进行锐化的图像
	 */
	public void sobelVertical(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				imageData.setImage(ProcessFilter.getInstance().sobelVertical(
						imageData.getImage()));
			}
		}).start();
	}
	
	/**
	 * 对图像进行去雾操作。
     * @param imageData
     *            要进行去雾的图像
	 */
	public void dehaze(ImageData imageData) {
	    new Thread(new Runnable() {
            
            @Override
            public void run() {
                imageData.setImage(ProcessFilter.getInstance().dehaze(imageData.getImage()));
            }
        }).start();
	}
	
    /**
     * 获取图像的折射率。
     * @param imageData
     *            要获取折射率的图像
     */
    public void transsmision(ImageData imageData) {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                imageData.setImage(ProcessFilter.getInstance().transsmision(imageData.getImage()));
            }
        }).start();
    }

}
