/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.util;

import java.awt.Frame;

/**
 * ����Menu�˵��ĸ��ࡣ�������в˵��Ľ����ܡ�
 * ���еĲ˵������඼Ӧ�����ڴ��ࡣ
 */
public abstract class IController {
	protected Frame context;
	
	/**
	 * ����һ��MNBase�࣬�����������frame��Ϊ�����ġ� 
	 * @param frame ����MNBase���frame
	 */
	public IController(Frame frame) {
		this.context = frame;
	}
}
