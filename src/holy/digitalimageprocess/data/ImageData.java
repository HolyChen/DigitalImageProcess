/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
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
 * 保存、更新图像数据的数据模型。<br>
 * 为了使图像进行更新，需要更新图像的对象必须实现观察者的更新函数，
 * 并使用方法addUpdateEventHandler添加入updateEventHandlers中。
 */
public class ImageData {
	// 图像数据，并不直接用于显示
	private Image image;
	// 显示的图像数据，由图像放缩或其他结果得到
	private Image dispalyImage;
	// 标志图像是否发生改变
	private boolean imageChanged;
	// 进行撤销操作的栈
	private LinkedList<Image> undoStack;
	// 进行重做操作的栈
	private LinkedList<Image> redoStack;
	// 最大可撤销/重做的步数
	private static final int MAX_STEP = 5;

	Map<String, Consumer<UpdateEvent>> updateEventHandlers;

	/**
	 * 图像处理过程中错误代码
	 */
	public static final int
	// 成功
			SUCCESS = 0,
			// 参数为空
			NULL_ARGUMENT = 1,
			// IO错误
			IO_ERROR = 2;

	public ImageData() {
		// 因为处理器组需要经常遍历，因此使用树状映射
		this.updateEventHandlers = new TreeMap<String, Consumer<UpdateEvent>>();
		this.imageChanged = false;
		this.undoStack = new LinkedList<Image>();
		this.redoStack = new LinkedList<Image>();
	}

	/**
	 * 初始化数据
	 */
	public void initialize() {
		this.imageChanged = false;
		this.undoStack = new LinkedList<Image>();
		this.redoStack = new LinkedList<Image>();
	}

	/**
	 * image的访问器，获取图像。
	 * 
	 * @return 如果图像已经初始化，返回图像，否则返回null
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * 返回用于显示的图像。
	 * 
	 * @return 用于显示的图像，这个图像可能是由原图像叠加得到。
	 */
	public Image getDisplayImage() {
		return dispalyImage;
	}

	/**
	 * 设置新的显示图像，这个图像不会覆盖图像本身的数据。<br>
	 * 设置显示图像并不影响撤销重做栈。<br>
	 * 显示图像改变后，对界面进行更新。
	 * 
	 * @param displayImage
	 *            要显示的图像。
	 */
	public void setDisplayImage(Image displayImage) {
		this.dispalyImage = displayImage;
		// 调用图片事件更新处理器
		fireUpdateEvent("display");
	}

	/**
	 * 从文件中读取图像数据。
	 * 
	 * @param file
	 *            从该文件中读取图像。
	 * @return 根据文件读取状态返回错误代码表示。 读取成功，返回SUCCESS， 参数为NULL，返回NULL_ARGUMENT,
	 *         读取中发生IO错误，返回IO_ERROR.
	 */
	public int setImage(File file) {
		try {
			image = ImageIO.read(file);
			// 全部转化为ARGB图
			BufferedImage argbImage = new BufferedImage(image.getWidth(null),
					image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
			argbImage.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
					image.getHeight(null), null);
			image = argbImage;
			// 设置显示图像
			setDisplayImage(image);

			initialize();
			return SUCCESS;
		} catch (IllegalArgumentException e) {
			System.out.println("空指针错误");
			return NULL_ARGUMENT;
		} catch (IOException e) {
			System.out.println("IO错误");
			return IO_ERROR;
		}
	}

	/**
	 * 通过既有Image设置image。
	 * 
	 * @param bfImage
	 *            新的image
	 */
	public void setImage(Image bfImage) {
		setImageChanged(true);
		pushToUndoStack(this.image);
		redoStack.clear();
		this.image = bfImage;
		setDisplayImage(image);
	}

	/**
	 * 更新图片，但是不改变redo和undo栈。
	 * 
	 * @param bfImage
	 *            要更新的图片
	 */
	public void setImageWithoutStack(Image bfImage) {
		setImageChanged(true);
		this.image = bfImage;
		setDisplayImage(image);
	}

	/**
	 * 关闭图像
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
	 * 保存图像
	 * 
	 * @param file
	 *            储存图片的文件
	 * @param formatName
	 *            储存图片类型的格式名，如“jpg”， 与ImageIO.write所需要的formatName一致。<br>
	 *            具体支持的格式请查看ImageIO.getWriterFormat();
	 */
	public void saveImage(File file, String formatName) {
		try {

			if (!file.exists()) {
				file.createNewFile();
			}
			Image toWrite = null;
			// 根据格式的不同对Image色彩模式进行转换。
			if (formatName.equalsIgnoreCase("jpg") || formatName.equalsIgnoreCase("bmp")) {
				// JPG 或 BMP转化为RGB
				toWrite = new BufferedImage(image.getWidth(null),
						image.getHeight(null), BufferedImage.TYPE_INT_RGB);
				toWrite.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
						image.getHeight(null), null); 
			} else if (formatName.equalsIgnoreCase("png") || formatName.equalsIgnoreCase("gif")) {
				// png 或 gif 转化为ARGB
				toWrite = new BufferedImage(image.getWidth(null),
						image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
				toWrite.getGraphics().drawImage(image, 0, 0, image.getWidth(null),
						image.getHeight(null), null); 
			}
			ImageIO.write(((BufferedImage) toWrite), formatName, file);
			// 调用图片事件更新处理器
			fireUpdateEvent(file.getName());
			setImageChanged(false);
		} catch (Exception e) {
			// 取消保存
		}
	}

	/**
	 * 图像是否发生改变。
	 * 
	 * @param b
	 */
	private void setImageChanged(boolean b) {
		this.imageChanged = b;
	}

	/**
	 * 查询图像是否发生改变
	 * 
	 * @return 图像如果已经改变返回true，反之false
	 */
	public boolean isImageChanged() {
		return imageChanged;
	}

	/**
	 * 撤销操作
	 * 
	 * @return 剩余撤销的步骤
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
	 * 获取是否能够撤销
	 * 
	 * @return 如果能够撤销，返回true；反之false.
	 */
	public boolean canUndo() {
		return undoStack != null && !undoStack.isEmpty();
	}

	private void pushToRedoStack(Image image) {
		if (redoStack.size() == MAX_STEP) {
			// 删除最早压入的元素
			redoStack.pollFirst();
		}
		redoStack.push(image);
	}

	/**
	 * 重做操作
	 * 
	 * @return 剩余重做步骤
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
	 * 获取是否能够重做
	 * 
	 * @return 如果能够重做，返回true；反之false.
	 */
	public boolean canRedo() {
		return redoStack != null && !redoStack.isEmpty();
	}

	private void pushToUndoStack(Image image) {
		if (undoStack.size() == MAX_STEP) {
			// 删除最早压入元素
			undoStack.pollFirst();
		}
		undoStack.push(image);
	}

	/**
	 * 将一个图片更新事件处理器加入到处理器组中。
	 * 
	 * @param handler
	 *            需要添加的图片更新时间的处理器。
	 * @param name
	 *            这个处理器的名字，方便查询和删除
	 * @return 返回图片更新事件处理器组，便于连续添加
	 * @throws NullPointerException
	 *             当更新事件的处理器组为null时，抛出空指针异常
	 */
	public Map<String, Consumer<UpdateEvent>> addUpdateEventHandler(
			Consumer<UpdateEvent> handler, String name)
			throws NullPointerException {
		if (updateEventHandlers != null) {
			updateEventHandlers.put(name, handler);
		} else {
			throw new NullPointerException("updateEventHandlers未初始化");
		}
		return updateEventHandlers;
	}

	/**
	 * 调用updateEventHandlers中的处理器进行处理
	 * 
	 * @param name
	 *            更新事件的名称
	 */
	public void fireUpdateEvent(String name) {
		UpdateEvent e = new UpdateEvent(name);
		for (Consumer<UpdateEvent> h : updateEventHandlers.values()) {
			h.accept(e);
		}
	}

}
