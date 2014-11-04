package holy.digitalimageprocess.windows;

import holy.digitalimageprocess.controller.MenuEditController;
import holy.digitalimageprocess.controller.MenuFileController;
import holy.digitalimageprocess.controller.MenuFilterController;
import holy.digitalimageprocess.controller.MenuImageController;
import holy.digitalimageprocess.data.ImageData;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

/**
 * 对MainWindow界面元素、所有成员、行为进行控制的类。<br>
 * 拥有对MainWindow中元素的完全访问权限。为其提供数据和控制。<br>
 */
public class MainWindowViewModel {
	// 控制的窗口对象
	private MainWindow view = null;
	
	// 图片数据
	ImageData imageData;
	// “文件”菜单处理类
	MenuFileController mncFile;
	// “编辑”菜单处理类
	MenuEditController mncEdit;
	// “图像”菜单处理类
	MenuImageController mncImage;
	// “滤镜”菜单处理类
	MenuFilterController mncFilter;
	
	/**
	 * 使用要被控制MainWindow对象初始化其控制器。
	 * @param mainWindow 要被控制的MainWindow对象。
	 */
	public MainWindowViewModel(MainWindow mainWindow) {
		this.view = mainWindow;
		// 初始化其他成员变量
		imageData = new ImageData();
		// --------- 初始化菜单选项处理类 ---------
		// 文件菜单处理类
		mncFile = new MenuFileController(view.frame);
		// 编辑菜单处理类
		mncEdit = new MenuEditController(view.frame);
		// 图像菜单处理类
		mncImage = new MenuImageController(view.frame);
		// 滤镜才按处理类
		mncFilter = new MenuFilterController(view.frame);
		// ---------- 初始化事件绑定 -----------
		initializeEventHandler();
		// ---------- 初始化数据改变时的代理服务 ----------
		initializeDataDelegate();
		// ---------- 初始化界面 ----------
		setUIEnable(false);
	}

	/**
	 * 窗口关闭时采取的行为。即首先调用菜单中的关闭函数。
	 */
	public void windowClose() {
		// 用户保存或放弃了工作，进行退出
		if (mncFile.exit(imageData)) {
			view.frame.dispose();
		}
	}
	
	
	/**
	 * 初始化数据模型改变时更新视图的代理服务
	 */
	private void initializeDataDelegate() {
		// 将图片设置为当前图片后，进行更新
		imageData.addUpdateEventHandler((e) -> {
			if (imageData.getDisplayImage() != null) {
				view.lbImage.setIcon(new ImageIcon(imageData.getDisplayImage()));
			} else {
				view.lbImage.setIcon(null);
			}
			// 对撤销和重做按钮进行控制
			setRedoAndUndo();
			view.frame.validate();
			view.frame.repaint();
		}, getClass().getName());
	}

	
	/**
	 * 初始化组件的事件处理器绑定
	 */
	private void initializeEventHandler() {
		// “文件”菜单
		{
			// 打开文件
			view.mntmOpenFile.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.openFile(imageData);
					if (imageData.getImage() != null) {
						// 激活UI中的按钮元素
						setUIEnable(true);
					} else {
						setUIEnable(false);
					}
				}
			});

			// 关闭文件
			view.mntmClose.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.close(imageData);
					setUIEnable(false);
				}
			});

			// 另存为
			view.mntmSaveAs.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.saveAs(imageData);
				}
			});

			// 退出
			view.mntmExit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					windowClose();
				}
			});
		}
		// “编辑”菜单
		{
			// 撤销
			view.mntmUndo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncEdit.undo(imageData);
					setRedoAndUndo();
				}

			});
			// 重做
			view.mntmRedo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncEdit.redo(imageData);
					setRedoAndUndo();
				}
			});
		}

		// “图像”菜单
		{
			// 调整菜单
			{
				// 灰度图像
				view.mntmBlackWhite.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mncImage.adjust_ToBlackWhite(imageData);
					}
				});
				// 直方图
				view.mntmHistogram.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncImage.histogram(imageData);
					}
				});
			}

			// 图像大小
			view.mntmSize.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncImage.resizeImage(imageData);
				}
			});
			// 裁剪
			view.mntmPatch.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncImage.patch(imageData);
				}
			});
		}
		
		// “滤镜”菜单
		{
			// 图像叠加
			view.mntmOverlay.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncFilter.imageOverlay(imageData);
				}
			});
			// 模糊
			view.mntmBlur.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncFilter.blur(imageData);
				}
			});
			// 锐化
			{
				// 拉普拉斯二阶锐化
				view.mntmLaplaceSharpen.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncFilter.sharpen(imageData);
					}
				});
				// Sobel横向滤波
				view.mntmSobelHorizontal.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncFilter.sobelHorizontal(imageData);
					}
				});
				// Sobel纵向滤波
				view.mntmSobelVertical.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncFilter.sobelVertical(imageData);
					}
				});
			}			
		}
	}
	
	/**
	 * 激活所有UI元素，除了与操作状态的相关的元素外。
	 * @param act 是否激活。true未激活，false为不激活。
	 */
	protected void setUIEnable(boolean act) {
		if (!act) {
			{
				// 另存为禁用
				view.mntmSaveAs.setEnabled(false);
				// 关闭禁用
				view.mntmClose.setEnabled(false);
			}
			// 图像、编辑菜单禁用
			view.mnImage.setEnabled(false);
			view.mnEdit.setEnabled(false);
			view.mnFilter.setEnabled(false);
		} else {
			// 另存为启用
			view.mntmSaveAs.setEnabled(true);
			// 关闭启用
			view.mntmClose.setEnabled(true);
			// 菜单启用
			view.mnImage.setEnabled(true);
			view.mnEdit.setEnabled(true);
			view.mnFilter.setEnabled(true);
			setRedoAndUndo();
		}
	}
	
	/**
	 * 对图像是否能否进行撤销和重做的菜单按钮的状态进行设置。
	 */
	private void setRedoAndUndo() {
		// 撤销重做菜单
		view.mntmUndo.setEnabled(imageData.canUndo());
		view.mntmRedo.setEnabled(imageData.canRedo());
	}
}

