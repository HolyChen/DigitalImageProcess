/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
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
 * ������File�˵���Ĵ������࣬������MNBase��ʹ��ǰ�����õ����� Щ��������ͼ frame����ʼ��context��<br>
 * ����Open File, Save As, Exit.
 */
public class MenuFileController extends IController {

	// ���ļ�ѡ�����Ļ�·��
	String basePath = ".";

	/**
	 * �������󣬲����������frame��Ϊ�����ġ�
	 * 
	 * @param frame
	 *            ��������˵���������Frame
	 */
	public MenuFileController(Frame frame) {
		super(frame);
	}

	/**
	 * ʵ�ִ��ļ����ܣ������ļ�����ΪͼƬ
	 * 
	 * @param imageData
	 *            ��Ҫ����ͼ����ļ�
	 */
	public void openFile(ImageData imageData) {
		// ���ȹر�����ͼ��
		// ����û�ȡ���رգ��򷵻ء�
		if (!close(imageData)) {
			return;
		}

		// ����ļ�ѡ��Ի���
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(basePath));
		fileChooser.setAcceptAllFileFilterUsed(false);
		// �����ļ���������Ҫ�������ļ���������ΪͼƬ
		for (FileFilter filter : FILTERS) {
			fileChooser.setFileFilter(filter);
		}
		int success = fileChooser.showDialog(context, null);
		// ѡ��ͼƬ�󣬽�ͼƬ���뵽imageData��
		if (success == JFileChooser.APPROVE_OPTION) {
			imageData.setImage(fileChooser.getSelectedFile());
		}
		// �����·���������û������ļ�
		if (fileChooser.getSelectedFile() != null) {
			basePath = fileChooser.getSelectedFile().getParent();	
		}
	}

	/**
	 * ʵ�֡��رա�����
	 * 
	 * @param imageData
	 *            �ر�ʱ�漰������Ϊ�����ͼ��ӦΪ������ͼ��
	 * @return �û������������ر���ͼ���򷵻�true����֮false.
	 */
	public boolean close(ImageData imageData) {
		boolean continueClose = true;
		if (imageData.isImageChanged()) {
			int opt = JOptionPane.showConfirmDialog(context, "�ļ��Ѿ��������ģ��Ƿ񱣴棿",
					"�ر�", JOptionPane.YES_NO_CANCEL_OPTION);

			// �û�ѡ��ȡ����ֹͣ�ر�
			if (opt == JOptionPane.CANCEL_OPTION) {
				continueClose = false;
			}
			// �û�ѡ����б��棬���ñ��湦��
			else if (opt == JOptionPane.YES_OPTION) {
				/*
				 * �ڹ����У�continueClose�ᷢ���ı� ���ȡ�����棬ͬ��ȡ���ر�
				 */
				continueClose = saveAs(imageData);
			}
			// �û�ѡ��ֱ�ӹرգ������б���
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
	 * �����ļ��Ĵ�������<br>
	 * Postcondition: ����ڱ��������ȡ����continueClose״̬���Ϊfalse��
	 * 
	 * @param imageData
	 *            �ر�ʱ�漰������Ϊ�����ͼ��ӦΪ������ͼ��
	 * @return �û������˱�����߷��������򷵻�true��ȡ���˱��淵��false
	 */
	public boolean saveAs(ImageData imageData) {
		// ����ļ�ѡ��Ի���
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setCurrentDirectory(new File(basePath));
		fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
		fileChooser.setAcceptAllFileFilterUsed(false);
		// �����ļ���������Ҫ�������ļ���������ΪͼƬ
		for (FileFilter filter : FILTERS) {
			fileChooser.setFileFilter(filter);
		}
		int success = fileChooser.showDialog(context, null);
		// �����ļ�·��
		if (success == JFileChooser.APPROVE_OPTION) {
			String description = fileChooser.getFileFilter().getDescription();
			File selectedFile = fileChooser.getSelectedFile();
			for (ImageFileFilter fileFilter : FILTERS) {
				if (description == fileFilter.getDescription()) {
					// Ϊ�ļ������չ��
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
						Logger.getGlobal().log(Level.INFO, "�ⲻ��һ��ͼƬ���������ߴ���ʧ��");
					}
				}
			}
		}
		return true;
	}

	// ------------ �˳����� ------------
	/**
	 * �˳�����
	 * 
	 * @param imageData
	 *            �ر�ʱ�漰������Ϊ�����ͼ��ӦΪ������ͼ��
	 * @return �û����湤���������˳����򷵻�true����֮false��
	 */
	public boolean exit(ImageData imageData) {
		// ���ȵ��ùرգ����⵱ǰ��Ϊ����Ĺ����������û��Ƿ�ر�ͼ��
		return close(imageData);
	}

	// ------------ �ļ������б� ------------
	static final List<ImageFileFilter> FILTERS = new ArrayList<ImageFileFilter>();
	static {
		// BMP
		FILTERS.add(new ImageFileFilter(new String[] { ".BMP" }, "BMP (*.BMP)",
				"bmp"));
		// PNG
		FILTERS.add(new ImageFileFilter(new String[] { ".PNG" },
				"PNG Image ��*.png)", "png"));
		// GIF
		FILTERS.add(new ImageFileFilter(new String[] { ".GIF", }, "GIF (*.GIF)",
				"gif"));
		// JPEG
		FILTERS.add(new ImageFileFilter(new String[] { ".JPG", ".JPE", ".JPEG" },
				"JPEG (*.JPG,;*.JPE;*.JPEG)", "jpg"));
	}
}
