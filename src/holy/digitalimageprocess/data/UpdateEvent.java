/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.data;

/**
 * ImageData��ͼƬ�����¼�����������ͼƬ��·����Ϣ��
 */
public class UpdateEvent {
	String path;

	/**
	 * ͨ�����µ�ͼƬ��·����Ϣ����ʼ���¼���
	 * 
	 * @param path
	 *            ����ͼƬ��·����
	 */
	public UpdateEvent(String path) {
		this.path = path;
	}
	
	/**
	 * �����¼���������ͼƬ·��
	 * @return path
	 */
	public String getPath() {
		return path;
	}
}
