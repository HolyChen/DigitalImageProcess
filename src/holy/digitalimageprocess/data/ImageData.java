/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.data;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

import javax.imageio.ImageIO;

/**
 * ���桢����ͼ�����ݵ�����ģ�͡�<br>
 * Ϊ��ʹͼ����и��£���Ҫ����ͼ��Ķ������ʵ�ֹ۲��ߵĸ��º�����
 * ��ʹ�÷���addUpdateEventHandler�����updateEventHandlers�С�
 */
public class ImageData {
	// ͼ�����ݣ�����ֱ��������ʾ
	private Image image;
	// ��ʾ��ͼ�����ݣ���ͼ���������������õ�
	private Image dispalyImage;
	// ��־ͼ���Ƿ����ı�
	private boolean imageChanged;
	// ���г���������ջ
	private LinkedList<Image> undoStack;
	// ��������������ջ
	private LinkedList<Image> redoStack;
	// ���ɳ���/�����Ĳ���
	private static final int MAX_STEP = 5;

	Map<String, Consumer<UpdateEvent>> updateEventHandlers;

	/**
	 * ͼ��������д������
	 */
	public static final int
	// �ɹ�
			SUCCESS = 0,
			// ����Ϊ��
			NULL_ARGUMENT = 1,
			// IO����
			IO_ERROR = 2;

	public ImageData() {
		// ��Ϊ����������Ҫ�������������ʹ����״ӳ��
		this.updateEventHandlers = new TreeMap<String, Consumer<UpdateEvent>>();
		this.imageChanged = false;
		this.undoStack = new LinkedList<Image>();
		this.redoStack = new LinkedList<Image>();
	}

	/**
	 * ��ʼ������
	 */
	public void initialize() {
		this.imageChanged = false;
		this.undoStack = new LinkedList<Image>();
		this.redoStack = new LinkedList<Image>();
	}

	/**
	 * image�ķ���������ȡͼ��
	 * 
	 * @return ���ͼ���Ѿ���ʼ��������ͼ�񣬷��򷵻�null
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * ����������ʾ��ͼ��
	 * 
	 * @return ������ʾ��ͼ�����ͼ���������ԭͼ����ӵõ���
	 */
	public Image getDisplayImage() {
		return dispalyImage;
	}

	/**
	 * �����µ���ʾͼ�����ͼ�񲻻Ḳ��ͼ��������ݡ�<br>
	 * ������ʾͼ�񲢲�Ӱ�쳷������ջ��<br>
	 * ��ʾͼ��ı�󣬶Խ�����и��¡�
	 * 
	 * @param displayImage
	 *            Ҫ��ʾ��ͼ��
	 */
	public void setDisplayImage(Image displayImage) {
		this.dispalyImage = displayImage;
		// ����ͼƬ�¼����´�����
		fireUpdateEvent("display");
	}

	/**
	 * ���ļ��ж�ȡͼ�����ݡ�
	 * 
	 * @param file
	 *            �Ӹ��ļ��ж�ȡͼ��
	 * @return �����ļ���ȡ״̬���ش�������ʾ�� ��ȡ�ɹ�������SUCCESS�� ����ΪNULL������NULL_ARGUMENT,
	 *         ��ȡ�з���IO���󣬷���IO_ERROR.
	 */
	public int setImage(File file) {
		try {
			image = ImageIO.read(file);
			// ȫ��ת��ΪARGBͼ
			BufferedImage argbImage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			argbImage.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
					image.getHeight(null), null);
			image = argbImage;
			// ������ʾͼ��
			setDisplayImage(image);

			initialize();
			return SUCCESS;
		} catch (IllegalArgumentException e) {
			System.out.println("��ָ�����");
			return NULL_ARGUMENT;
		} catch (IOException e) {
			System.out.println("IO����");
			return IO_ERROR;
		}
	}

	/**
	 * ͨ������Image����image��
	 * 
	 * @param bfImage
	 *            �µ�image
	 */
	public void setImage(Image bfImage) {
		setImageChanged(true);
		pushToUndoStack(this.image);
		redoStack.clear();
		this.image = bfImage;
		setDisplayImage(image);
	}

	/**
	 * ����ͼƬ�����ǲ��ı�redo��undoջ��
	 * 
	 * @param bfImage
	 *            Ҫ���µ�ͼƬ
	 */
	public void setImageWithoutStack(Image bfImage) {
		setImageChanged(true);
		this.image = bfImage;
		setDisplayImage(image);
	}

	/**
	 * �ر�ͼ��
	 */
	public void disposeImage() {
		this.image = null;
		this.dispalyImage = null;
		this.imageChanged = false;
		this.redoStack = null;
		this.undoStack = null;
		fireUpdateEvent("NULL");
	}

	/**
	 * ����ͼ��
	 * 
	 * @param file
	 *            ����ͼƬ���ļ�
	 * @param formatName
	 *            ����ͼƬ���͵ĸ�ʽ�����硰jpg���� ��ImageIO.write����Ҫ��formatNameһ�¡�<br>
	 *            ����֧�ֵĸ�ʽ��鿴ImageIO.getWriterFormat();
	 */
	public void saveImage(File file, String formatName) {
		try {

			if (!file.exists()) {
				file.createNewFile();
			}
			Image toWrite = null;
			// ���ݸ�ʽ�Ĳ�ͬ��Imageɫ��ģʽ����ת����
			if (formatName.equalsIgnoreCase("jpg") || formatName.equalsIgnoreCase("bmp")) {
				// JPG �� BMPת��ΪRGB
				toWrite = new BufferedImage(image.getWidth(null),
						image.getHeight(null), BufferedImage.TYPE_INT_RGB);
				toWrite.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
						image.getHeight(null), null); 
			} else if (formatName.equalsIgnoreCase("png") || formatName.equalsIgnoreCase("gif")) {
				// png �� gif ת��ΪARGB
				toWrite = new BufferedImage(image.getWidth(null),
						image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				toWrite.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
						image.getHeight(null), null); 
			}
			ImageIO.write(((BufferedImage) toWrite), formatName, file);
			// ����ͼƬ�¼����´�����
			fireUpdateEvent(file.getName());
			setImageChanged(false);
		} catch (Exception e) {
			// ȡ������
		}
	}

	/**
	 * ͼ���Ƿ����ı䡣
	 * 
	 * @param b
	 */
	private void setImageChanged(boolean b) {
		this.imageChanged = b;
	}

	/**
	 * ��ѯͼ���Ƿ����ı�
	 * 
	 * @return ͼ������Ѿ��ı䷵��true����֮false
	 */
	public boolean isImageChanged() {
		return imageChanged;
	}

	/**
	 * ��������
	 * 
	 * @return ʣ�೷���Ĳ���
	 */
	public int undo() {
		if (!canUndo()) {
			return 0;
		} else {
			pushToRedoStack(this.image);
			setImageWithoutStack(undoStack.pop());
			return undoStack.size();
		}
	}

	/**
	 * ��ȡ�Ƿ��ܹ�����
	 * 
	 * @return ����ܹ�����������true����֮false.
	 */
	public boolean canUndo() {
		return undoStack != null && !undoStack.isEmpty();
	}

	private void pushToRedoStack(Image image) {
		if (redoStack.size() == MAX_STEP) {
			// ɾ������ѹ���Ԫ��
			redoStack.pollFirst();
		}
		redoStack.push(image);
	}

	/**
	 * ��������
	 * 
	 * @return ʣ����������
	 */
	public int redo() {
		if (!canRedo()) {
			return 0;
		} else {
			pushToUndoStack(this.image);
			setImageWithoutStack(redoStack.pop());
			return redoStack.size();
		}
	}

	/**
	 * ��ȡ�Ƿ��ܹ�����
	 * 
	 * @return ����ܹ�����������true����֮false.
	 */
	public boolean canRedo() {
		return redoStack != null && !redoStack.isEmpty();
	}

	private void pushToUndoStack(Image image) {
		if (undoStack.size() == MAX_STEP) {
			// ɾ������ѹ��Ԫ��
			undoStack.pollFirst();
		}
		undoStack.push(image);
	}

	/**
	 * ��һ��ͼƬ�����¼����������뵽���������С�
	 * 
	 * @param handler
	 *            ��Ҫ��ӵ�ͼƬ����ʱ��Ĵ�������
	 * @param name
	 *            ��������������֣������ѯ��ɾ��
	 * @return ����ͼƬ�����¼��������飬�����������
	 * @throws NullPointerException
	 *             �������¼��Ĵ�������Ϊnullʱ���׳���ָ���쳣
	 */
	public Map<String, Consumer<UpdateEvent>> addUpdateEventHandler(
			Consumer<UpdateEvent> handler, String name)
			throws NullPointerException {
		if (updateEventHandlers != null) {
			updateEventHandlers.put(name, handler);
		} else {
			throw new NullPointerException("updateEventHandlersδ��ʼ��");
		}
		return updateEventHandlers;
	}

	/**
	 * ����updateEventHandlers�еĴ��������д���
	 * 
	 * @param name
	 *            �����¼�������
	 */
	public void fireUpdateEvent(String name) {
		UpdateEvent e = new UpdateEvent(name);
		for (Consumer<UpdateEvent> h : updateEventHandlers.values()) {
			h.accept(e);
		}
	}

}
