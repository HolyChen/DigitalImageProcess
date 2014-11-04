/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.windows;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Ӧ�õ���ںͽ�����
 */
public class MainWindow {
	MainWindowViewModel viewModel;

	// ��֡
	JFrame frame;
	// �˵���
	JMenuBar menuBarMain;
	// �ļ��˵�
	JMenu mnFile;
	// ���ļ���ť
	JMenuItem mntmOpenFile;
	// �رհ�ť
	JMenuItem mntmClose;
	// ���Ϊ��ť
	JMenuItem mntmSaveAs;
	// �ָ���
	JSeparator separator;
	// �˳���ť
	JMenuItem mntmExit;
	// ͼ��˵�
	JMenu mnImage;
	// �����Ӳ˵�
	JMenu mnAdjust;
	// �ڰ�ͼ��
	JMenuItem mntmBlackWhite;
	JScrollPane spImage;
	JLabel lbImage;
	JMenuItem mntmSize;
	JMenu mnEdit;
	JMenuItem mntmUndo;
	JMenuItem mntmRedo;
	// �˾��˵�
	JMenu mnFilter;
	// ͼ�����
	JMenuItem mntmOverlay;
	JMenuItem mntmHistogram;
	JMenuItem mntmPatch;
	JMenuItem mntmBlur;
	JMenuItem mntmLaplaceSharpen;
	JMenu mnShapen;
	JMenuItem mntmSobelHorizontal;
	JMenuItem mntmSobelVertical;

	/**
	 * ����Ӧ��
	 * 
	 * @param args
	 *            �����в���������
	 */
	public static void main(String[] args) {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		// ��ʼ������
		initialize();
		// ��ʼ��������
		viewModel = new MainWindowViewModel(this);
	}

	/**
	 * ��ʼ��frame�����ݣ���UI����
	 */
	@SuppressWarnings({ "serial" })
	private void initialize() {
		// ��ʼ����֡
		frame = new JFrame() {
			// ��JFrame�Ĺر��¼�תΪexit�˵�����
			@Override
			protected void processWindowEvent(WindowEvent e) {
				if (e.getID() == WindowEvent.WINDOW_CLOSING) {
					viewModel.windowClose();
				} else {
					super.processWindowEvent(e);
				}
			}

		};
		frame.setTitle("\u6570\u5B57\u56FE\u50CF\u5904\u7406\u2014\u2014\u9648\u6D69\u5DDD");
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// �˵���
		menuBarMain = new JMenuBar();
		frame.setJMenuBar(menuBarMain);
		{
			// �ļ��˵�
			mnFile = new JMenu("\u6587\u4EF6(F)");
			mnFile.setMnemonic('F');
			menuBarMain.add(mnFile);
			{

				// ���ļ�
				mntmOpenFile = new JMenuItem("\u6253\u5F00(O)...");
				mntmOpenFile
						.setIcon(new ImageIcon(
								MainWindow.class
										.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
				mntmOpenFile.setAccelerator(KeyStroke.getKeyStroke(
						KeyEvent.VK_O, InputEvent.CTRL_MASK));
				mntmOpenFile.setMnemonic('O');
				mnFile.add(mntmOpenFile);

				// �ر�
				mntmClose = new JMenuItem("\u5173\u95ED(C)");
				mntmClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
						InputEvent.CTRL_MASK));
				mntmClose.setMnemonic('C');
				mnFile.add(mntmClose);

				// ���Ϊ
				mntmSaveAs = new JMenuItem("\u53E6\u5B58\u4E3A(S)...");
				mntmSaveAs
						.setIcon(new ImageIcon(
								MainWindow.class
										.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
				mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
						InputEvent.CTRL_MASK));
				mntmSaveAs.setMnemonic('S');
				mnFile.add(mntmSaveAs);

				// �ָ���
				separator = new JSeparator();
				mnFile.add(separator);

				// �˳���ť
				mntmExit = new JMenuItem("\u9000\u51FA(X)");
				mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
						InputEvent.CTRL_MASK));
				mntmExit.setMnemonic('X');
				mnFile.add(mntmExit);
			}

			mnEdit = new JMenu("\u7F16\u8F91(E)");
			mnEdit.setMnemonic('E');
			menuBarMain.add(mnEdit);

			mntmUndo = new JMenuItem("\u64A4\u9500");
			mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
					InputEvent.CTRL_MASK));
			mnEdit.add(mntmUndo);

			mntmRedo = new JMenuItem("\u91CD\u505A");
			mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y,
					InputEvent.CTRL_MASK));
			mnEdit.add(mntmRedo);

			// ��ͼ�񡱲˵�
			mnImage = new JMenu("\u56FE\u50CF(I)");
			mnImage.setMnemonic('I');
			menuBarMain.add(mnImage);
			{
				// ���������˵�
				mnAdjust = new JMenu("\u8C03\u6574(J)");
				mnAdjust.setMnemonic('J');
				mnImage.add(mnAdjust);
				{
					// ���ڰ�ͼ��
					mntmBlackWhite = new JMenuItem("\u9ED1\u767D\u56FE\u50CF");
					mnAdjust.add(mntmBlackWhite);
				}

				mntmHistogram = new JMenuItem("\u76F4\u65B9\u56FE");
				mnAdjust.add(mntmHistogram);
			}

			mntmSize = new JMenuItem("\u56FE\u50CF\u5927\u5C0F");
			mnImage.add(mntmSize);

			mntmPatch = new JMenuItem("\u88C1\u526A");
			mnImage.add(mntmPatch);
		}

		mnFilter = new JMenu("\u6EE4\u955C(T)");
		mnFilter.setMnemonic('T');
		menuBarMain.add(mnFilter);

		mntmOverlay = new JMenuItem("\u56FE\u50CF\u53E0\u52A0");
		mnFilter.add(mntmOverlay);

		mntmBlur = new JMenuItem("\u6A21\u7CCA");
		mnFilter.add(mntmBlur);

		mnShapen = new JMenu("\u9510\u5316");
		mnFilter.add(mnShapen);

		mntmLaplaceSharpen = new JMenuItem("\u4E8C\u9636\u9510\u5316");
		mnShapen.add(mntmLaplaceSharpen);

		mntmSobelHorizontal = new JMenuItem("Sobel\u6A2A\u5411\u6EE4\u6CE2");
		mnShapen.add(mntmSobelHorizontal);

		mntmSobelVertical = new JMenuItem("Sobel\u7EB5\u5411\u6EE4\u6CE2");
		mnShapen.add(mntmSobelVertical);
		frame.getContentPane().setLayout(new GridLayout(1, 2, 0, 0));

		spImage = new JScrollPane();
		frame.getContentPane().add(spImage);

		lbImage = new JLabel("");
		lbImage.setHorizontalAlignment(SwingConstants.CENTER);
		spImage.setViewportView(lbImage);
	}

}
