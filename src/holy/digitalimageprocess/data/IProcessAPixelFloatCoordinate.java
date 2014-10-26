package holy.digitalimageprocess.data;

/**
 * ��һ�����صĽ��д���ĺ����ӿڣ����ص���������Ϊdouble�͡�<br>
 * ��һ�����صĽ��д������շ��س�һ�����ص�rgb������
 */
public interface IProcessAPixelFloatCoordinate {
	/**
	 * ��һ������λ�õĽ��������������
	 * @param rgbs ���������ص�ARGB����
	 * @param width ͼ��Ŀ��
	 * @param height ͼ��ĸ߶�
	 * @param x Ҫ�����������ͼ���е�λ�ã�������һ������λ�á�
	 * @param y Ҫ�����������ͼ���е�λ�ã�������һ������λ�á�
	 * @return �����²���������
	 */
	int process(int rgbs[], int width, int height, double x, double y);
}
