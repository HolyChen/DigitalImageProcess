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
 * ��MainWindow����Ԫ�ء����г�Ա����Ϊ���п��Ƶ��ࡣ<br>
 * ӵ�ж�MainWindow��Ԫ�ص���ȫ����Ȩ�ޡ�Ϊ���ṩ���ݺͿ��ơ�<br>
 */
public class MainWindowViewModel {
	// ���ƵĴ��ڶ���
	private MainWindow view = null;
	
	// ͼƬ����
	ImageData imageData;
	// ���ļ����˵�������
	MenuFileController mncFile;
	// ���༭���˵�������
	MenuEditController mncEdit;
	// ��ͼ�񡱲˵�������
	MenuImageController mncImage;
	// ���˾����˵�������
	MenuFilterController mncFilter;
	
	/**
	 * ʹ��Ҫ������MainWindow�����ʼ�����������
	 * @param mainWindow Ҫ�����Ƶ�MainWindow����
	 */
	public MainWindowViewModel(MainWindow mainWindow) {
		this.view = mainWindow;
		// ��ʼ��������Ա����
		imageData = new ImageData();
		// --------- ��ʼ���˵�ѡ����� ---------
		// �ļ��˵�������
		mncFile = new MenuFileController(view.frame);
		// �༭�˵�������
		mncEdit = new MenuEditController(view.frame);
		// ͼ��˵�������
		mncImage = new MenuImageController(view.frame);
		// �˾��Ű�������
		mncFilter = new MenuFilterController(view.frame);
		// ---------- ��ʼ���¼��� -----------
		initializeEventHandler();
		// ---------- ��ʼ�����ݸı�ʱ�Ĵ������ ----------
		initializeDataDelegate();
		// ---------- ��ʼ������ ----------
		setUIEnable(false);
	}

	/**
	 * ���ڹر�ʱ��ȡ����Ϊ�������ȵ��ò˵��еĹرպ�����
	 */
	public void windowClose() {
		// �û����������˹����������˳�
		if (mncFile.exit(imageData)) {
			view.frame.dispose();
		}
	}
	
	
	/**
	 * ��ʼ������ģ�͸ı�ʱ������ͼ�Ĵ������
	 */
	private void initializeDataDelegate() {
		// ��ͼƬ����Ϊ��ǰͼƬ�󣬽��и���
		imageData.addUpdateEventHandler((e) -> {
			if (imageData.getDisplayImage() != null) {
				view.lbImage.setIcon(new ImageIcon(imageData.getDisplayImage()));
			} else {
				view.lbImage.setIcon(null);
			}
			// �Գ�����������ť���п���
			setRedoAndUndo();
			view.frame.validate();
			view.frame.repaint();
		}, getClass().getName());
	}

	
	/**
	 * ��ʼ��������¼���������
	 */
	private void initializeEventHandler() {
		// ���ļ����˵�
		{
			// ���ļ�
			view.mntmOpenFile.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.openFile(imageData);
					if (imageData.getImage() != null) {
						// ����UI�еİ�ťԪ��
						setUIEnable(true);
					} else {
						setUIEnable(false);
					}
				}
			});

			// �ر��ļ�
			view.mntmClose.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.close(imageData);
					setUIEnable(false);
				}
			});

			// ���Ϊ
			view.mntmSaveAs.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncFile.saveAs(imageData);
				}
			});

			// �˳�
			view.mntmExit.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					windowClose();
				}
			});
		}
		// ���༭���˵�
		{
			// ����
			view.mntmUndo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncEdit.undo(imageData);
					setRedoAndUndo();
				}

			});
			// ����
			view.mntmRedo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncEdit.redo(imageData);
					setRedoAndUndo();
				}
			});
		}

		// ��ͼ�񡱲˵�
		{
			// �����˵�
			{
				// �Ҷ�ͼ��
				view.mntmBlackWhite.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						mncImage.adjust_ToBlackWhite(imageData);
					}
				});
				// ֱ��ͼ
				view.mntmHistogram.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncImage.histogram(imageData);
					}
				});
			}

			// ͼ���С
			view.mntmSize.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					mncImage.resizeImage(imageData);
				}
			});
			// �ü�
			view.mntmPatch.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncImage.patch(imageData);
				}
			});
		}
		
		// ���˾����˵�
		{
			// ͼ�����
			view.mntmOverlay.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncFilter.imageOverlay(imageData);
				}
			});
			// ģ��
			view.mntmBlur.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					mncFilter.blur(imageData);
				}
			});
			// ��
			{
				// ������˹������
				view.mntmLaplaceSharpen.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncFilter.sharpen(imageData);
					}
				});
				// Sobel�����˲�
				view.mntmSobelHorizontal.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						mncFilter.sobelHorizontal(imageData);
					}
				});
				// Sobel�����˲�
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
	 * ��������UIԪ�أ����������״̬����ص�Ԫ���⡣
	 * @param act �Ƿ񼤻trueδ���falseΪ�����
	 */
	protected void setUIEnable(boolean act) {
		if (!act) {
			{
				// ���Ϊ����
				view.mntmSaveAs.setEnabled(false);
				// �رս���
				view.mntmClose.setEnabled(false);
			}
			// ͼ�񡢱༭�˵�����
			view.mnImage.setEnabled(false);
			view.mnEdit.setEnabled(false);
			view.mnFilter.setEnabled(false);
		} else {
			// ���Ϊ����
			view.mntmSaveAs.setEnabled(true);
			// �ر�����
			view.mntmClose.setEnabled(true);
			// �˵�����
			view.mnImage.setEnabled(true);
			view.mnEdit.setEnabled(true);
			view.mnFilter.setEnabled(true);
			setRedoAndUndo();
		}
	}
	
	/**
	 * ��ͼ���Ƿ��ܷ���г����������Ĳ˵���ť��״̬�������á�
	 */
	private void setRedoAndUndo() {
		// ���������˵�
		view.mntmUndo.setEnabled(imageData.canUndo());
		view.mntmRedo.setEnabled(imageData.canRedo());
	}
}

