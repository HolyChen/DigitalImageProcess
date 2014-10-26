/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.controller;

import holy.digitalimageprocess.data.ImageData;
import holy.util.IController;

import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * 集成了File菜单项的处理函数类，派生于MNBase，使用前必须用调用这 些函数的视图 frame来初始化context。<br>
 * 包括Open File, Save As, Exit.
 */
public class MenuFileController extends IController {

	// 打开文件选择器的基路径
	String basePath = ".";

	/**
	 * 创建对象，并将包含其的frame作为上下文。
	 * 
	 * @param frame
	 *            包含这个菜单处理器的Frame
	 */
	public MenuFileController(Frame frame) {
		super(frame);
	}

	/**
	 * 实现打开文件功能，限制文件类型为图片
	 * 
	 * @param imageData
	 *            需要载入图像的文件
	 */
	public void openFile(ImageData imageData) {
		// 首先关闭已有图像，
		// 如果用户取消关闭，则返回。
		if (!close(imageData)) {
			return;
		}

		// 添加文件选择对话框
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(basePath));
		fileChooser.setAcceptAllFileFilterUsed(false);
		// 设置文件过滤器，要求限制文件类型限制为图片
		for (FileFilter filter : FILTERS) {
			fileChooser.setFileFilter(filter);
		}
		int success = fileChooser.showDialog(context, null);
		// 选择图片后，将图片读入到imageData中
		if (success == JFileChooser.APPROVE_OPTION) {
			imageData.setImage(fileChooser.getSelectedFile());
		}
		// 重设基路径，方便用户保存文件
		if (fileChooser.getSelectedFile() != null) {
			basePath = fileChooser.getSelectedFile().getParent();	
		}
	}

	/**
	 * 实现“关闭”功能
	 * 
	 * @param imageData
	 *            关闭时涉及到可能为保存的图像，应为处理后的图像
	 * @return 用户结束工作，关闭了图像，则返回true，反之false.
	 */
	public boolean close(ImageData imageData) {
		boolean continueClose = true;
		if (imageData.isImageChanged()) {
			int opt = JOptionPane.showConfirmDialog(context, "文件已经发生更改，是否保存？",
					"关闭", JOptionPane.YES_NO_CANCEL_OPTION);

			// 用户选择取消，停止关闭
			if (opt == JOptionPane.CANCEL_OPTION) {
				continueClose = false;
			}
			// 用户选择进行保存，调用保存功能
			else if (opt == JOptionPane.YES_OPTION) {
				/*
				 * 在过程中，continueClose会发生改变 如果取消保存，同样取消关闭
				 */
				continueClose = saveAs(imageData);
			}
			// 用户选择直接关闭，不进行保存
			else {
				continueClose = true;
			}
		}
		if (continueClose) {
			imageData.disposeImage();
		}
		return continueClose;
	}

	/**
	 * 保存文件的处理器。<br>
	 * Postcondition: 如果在保存过程中取消，continueClose状态会变为false。
	 * 
	 * @param imageData
	 *            关闭时涉及到可能为保存的图像，应为处理后的图像
	 * @return 用户进行了保存或者放弃更改则返回true，取消了保存返回false
	 */
	public boolean saveAs(ImageData imageData) {
		// 添加文件选择对话框
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(basePath));
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setAcceptAllFileFilterUsed(false);
		// 设置文件过滤器，要求限制文件类型限制为图片
		for (FileFilter filter : FILTERS) {
			fileChooser.setFileFilter(filter);
		}
		int success = fileChooser.showDialog(context, null);
		// 设置文件路径
		if (success == JFileChooser.APPROVE_OPTION) {
			String description = fileChooser.getFileFilter().getDescription();
			File selectedFile = fileChooser.getSelectedFile();
			for (ImageFileFilter fileFilter : FILTERS) {
				if (description == fileFilter.getDescription()) {
					// 为文件添加扩展名
					File saveFile;
					if (selectedFile.getName().endsWith(
							"." + fileFilter.getSaveFormatName())) {
						saveFile = selectedFile;
					} else {
						saveFile = new File(
								selectedFile.getPath()
										+ "."
										+ ((ImageFileFilter) fileFilter)
												.getSaveFormatName());
					}

					try {
						imageData.saveImage(saveFile,
								fileFilter.getSaveFormatName());
					} catch (Exception e) {
						Logger.getGlobal().log(Level.INFO, "这不是一个图片过滤器或者储存失败");
					}
				}
			}
		}
		return true;
	}

	// ------------ 退出功能 ------------
	/**
	 * 退出程序
	 * 
	 * @param imageData
	 *            关闭时涉及到可能为保存的图像，应为处理后的图像
	 * @return 用户保存工作并决定退出程序返回true，反之false。
	 */
	public boolean exit(ImageData imageData) {
		// 首先调用关闭，以免当前尤为保存的工作，返回用户是否关闭图像。
		return close(imageData);
	}

	// ------------ 文件过滤列表 ------------
	static final List<ImageFileFilter> FILTERS = new ArrayList<ImageFileFilter>();
	static {
		// BMP
		FILTERS.add(new ImageFileFilter(new String[] { ".BMP" }, "BMP (*.BMP)",
				"bmp"));
		// PNG
		FILTERS.add(new ImageFileFilter(new String[] { ".PNG" },
				"PNG Image （*.png)", "png"));
		// GIF
		FILTERS.add(new ImageFileFilter(new String[] { ".GIF", }, "GIF (*.GIF)",
				"gif"));
		// JPEG
		FILTERS.add(new ImageFileFilter(new String[] { ".JPG", ".JPE", ".JPEG" },
				"JPEG (*.JPG,;*.JPE;*.JPEG)", "jpg"));
	}
}
