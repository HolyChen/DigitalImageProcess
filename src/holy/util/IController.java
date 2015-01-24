/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.util;

import java.awt.Frame;

/**
 * 所有Menu菜单的父类。包含含有菜单的界面框架。
 * 所有的菜单处理类都应派生于此类。
 */
public abstract class IController {
	protected Frame context;
	
	/**
	 * 构造一个MNBase类，并将包含其的frame作为上下文。 
	 * @param frame 包含MNBase类的frame
	 */
	public IController(Frame frame) {
		this.context = frame;
	}
}
