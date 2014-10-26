package holy.component;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * һ��ֻ�������������ı��༭��
 */
public class IntegerTextField extends JTextField {
	
	/**
	 * ֻ�������ı����ĵ�
	 */
	class IntegerDocument extends PlainDocument {
		/**
		 * ��һ���汾
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * ��������м�⣬��������к��з����֣������޳�
		 */
		@Override
		public void insertString(int offs, String str, AttributeSet a)
				throws BadLocationException {
			String filtedText = "";
			for (int i = 0; i < str.length(); i++) {
				char c = str.charAt(i);
				if ('0' <= c && c <= '9') {
					filtedText += c;
				}
			}
			super.insertString(offs, filtedText, a);
		}
	}
	
	/**
	 * ��һ���汾
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * ����һ��ֻ�������������ı���
	 */
	public IntegerTextField() {
		// �����ĵ�Ϊֻ�������������ĵ�
		setDocument(new IntegerDocument());
	}
	
	/**
	 * ��ȡ���ı�������е��ַ���������ֵ
	 * @return ����������ʽ�����ı�������е��ַ�����ֵ
	 */
	public int getInteger() {
		return Integer.valueOf(getText().length() == 0 ? "0" : getText());
	}
	
	/**
	 * ���ý�������Ϊ�ı����õ��ı�����
	 * @param i Ҫ���õ�����ֵ
	 */
	public void setInteger(int i) {
		setText(Integer.toString(i));
	}
}
