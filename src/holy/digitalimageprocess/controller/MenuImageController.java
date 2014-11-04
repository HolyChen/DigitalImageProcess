/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ProcessAdjust;
import holy.digitalimageprocess.data.ImageData;
import holy.digitalimageprocess.dialogs.DImage_Adjust_GrayLevel;
import holy.digitalimageprocess.dialogs.DImage_Adjust_Histogram;
import holy.digitalimageprocess.dialogs.DImage_Patch;
import holy.digitalimageprocess.dialogs.DImage_Size;
import holy.util.IController;
import holy.util.IOKorCancelDialog;

import java.awt.Frame;
import java.awt.Image;

/**
 * 处理分发“图像”菜单功能的类
 */
public class MenuImageController extends IController {
	// 对图像处理类的实例
	ProcessAdjust imageAdjust;

	public MenuImageController(Frame frame) {
		super(frame);
		this.imageAdjust = ProcessAdjust.getInstance();
	}

	/**
	 * 将图像按照灰度阶数level变为灰度图像
	 * 
	 * @param imageData
	 *            需要进行改变的图像
	 */
	public void adjust_ToBlackWhite(ImageData imageData) {
		// 使用异步方法，防止阻塞主线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 灰度阶数
				Object[] returns = new DImage_Adjust_GrayLevel(context)
						.showDialog();
				try {
					if (returns != null
							&& (int) returns[0] == IOKorCancelDialog.STATUS_ENTERED) {
						int level = (int) returns[1];
						Image image = imageAdjust.toBlackWhite(
								imageData.getImage(), level);
						imageData.setImage(image);
					}
				} catch (Exception e) {
					System.out.println("转化灰度图像出错");
					return;
				}

			}
		}).start();
	}

	/**
	 * 改变图像大小。
	 * 
	 * @param imageData
	 *            要改变图像大小的图像。
	 */
	public void resizeImage(ImageData imageData) {
		// 使用异步方法，防止阻塞主线程
		new Thread(new Runnable() {
			@Override
			public void run() {
				Image image = imageData.getImage();

				// 返回类型参见DImageSize.showDialog文档
				Object[] returns = new DImage_Size(context, image
						.getWidth(null), image.getHeight(null), UNIT_PIXEL)
						.showDialog();

				// 如果用户选择了新的图像大小才进行操作
				if (returns != null
						&& (int) returns[0] == IOKorCancelDialog.STATUS_ENTERED) {
					Image newImage = null;
					// 用户选择了邻近方式
					if (((String) returns[3]).equals(MODE_NEAREST)) {
						newImage = ProcessAdjust.getInstance()
								.resizeImageNearest(imageData.getImage(),
										(int) returns[1], (int) returns[2]);
					}
					// 用户选择了两次线性方式
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

	/**
	 * 计算一张图片的直方图，并以图形化的方式显示。<br>
	 * 同时也询问用户是否进行直方图均衡化，并对此进行操作。
	 * 
	 * @param imageData
	 *            要计算直方图的图片
	 */
	public void histogram(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				double[][] statisticResult = ProcessAdjust.getInstance()
						.statisticHistogram(imageData.getImage());
				Object[] results = new DImage_Adjust_Histogram(context,
						statisticResult[0], statisticResult[1],
						statisticResult[2]).showDialog();
				if ((boolean) results[0] && (boolean) results[1]) {
					Image newImage = ProcessAdjust.getInstance()
							.histogramEqulization(imageData.getImage(),
									statisticResult[0], statisticResult[1],
									statisticResult[2]);
					imageData.setImage(newImage);
				}
			}
		}).start();
	}

	/**
	 * 打开裁剪对话框，由用户输入裁剪数值，对图像进行裁剪。
	 * 
	 * @param imageData
	 *            进行裁剪的图像
	 */
	public void patch(ImageData imageData) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				int srcWidth = imageData.getImage().getWidth(null);
				int srcHeight = imageData.getImage().getHeight(null);
				// 打开对话框
				Object[] results = new DImage_Patch(context, srcWidth,
						srcHeight).showDialog();
				if ((boolean) results[0]) {
					int originX = (int) results[1], originY = (int) results[2], endX = (int) results[3], endY = (int) results[4];
					imageData.setImage(ProcessAdjust.getInstance().patch(
							imageData.getImage(), originX, originY, endX, endY));
				}
			}
		}).start();
	}

	// ------------------- 处理时用到的常量 -------------------

	// 放缩模式
	public static final String MODE_NEAREST = "邻近（保留硬边缘）",
			MODE_BILINEAR = "两次线性", MODE_BICUBIC = "两次立方（自动）";
	// 单位
	public static final String UNIT_PIXEL = "像素";
}
