/*
 * ����һ�������ͼ����д����СӦ�ó���
 * ���߳ºƴ���
 * ����ʱ��2014��10�¡�
 * ��ѭX/MITЭ�顣����Զ�Դ������н��в���˵�������á�
 */
package holy.digitalimageprocess.controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * ��ͼƬ���ļ����й��˵Ĺ�����������jpg��bmp�ļ���
 */
class ImageFileFilter extends FileFilter {

	String[] postfix;
	String description;
	String saveFormatName;

	/**
	 * ����һ��ͼƬ��������ͨ�����ƺ�׺���б��������
	 * 
	 * @param strings
	 *            �ļ���׺��Ӧ����postfix�б�������һ�����ļ��У��ű����֡�
	 * @param description
	 *            �Ա����˵��ļ����ͽ���������������
	 * @param saveFormatName
	 * 			  ����ʱ��ʹ�õ���չ��
	 */
	public ImageFileFilter(String[] strings, String description, String saveFormatName) {
		this.postfix = strings;
		this.description = description;
		this.saveFormatName = saveFormatName;
	}

	/**
	 * �����ļ���׺������description������Ϊ�ļ��С�
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) {
			return true;
		}
		boolean returnValue = false;
		for (String aPostfix : postfix) {
			if (f.getName().toUpperCase().endsWith(aPostfix)) {
				returnValue = true;
				break;
			}
		}
		return returnValue;
	}

	/**
	 * ���ضԹ�������������
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * ���ش���ʱ���õ���չ��
	 * @return ����ʹ�õ���չ��
	 */
	public String getSaveFormatName() {
		return saveFormatName;
	}
}