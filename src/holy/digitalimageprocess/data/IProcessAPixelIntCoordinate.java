package holy.digitalimageprocess.data;

/**
 * ��һ�����صĽ��д���ĺ����ӿڣ����ص��С�������Ϊint�͡�<br>
 * ��һ�����صĽ��д������շ��س�һ�����ص�rgb������
 */
public interface IProcessAPixelIntCoordinate {
	/**
	 * ��һ������λ�õĽ��������������
	 * @param rgbs ���������ص�ARGB����
	 * @param index ���������argbs�е�λ��
	 * @return �����²���������
	 */
	int process(int argbs[], int index);
}
