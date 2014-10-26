package holy.component;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 一个只能输入整数的文本编辑栏
 */
public class IntegerTextField extends JTextField {
	
	/**
	 * 只能输入文本的文档
	 */
	class IntegerDocument extends PlainDocument {
		/**
		 * 第一个版本
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * 对输入进行检测，如果输入中含有非数字，进行剔除
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
	 * 第一个版本
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造一个只能输入整数的文本域
	 */
	public IntegerTextField() {
		// 限制文档为只能输入整数的文档
		setDocument(new IntegerDocument());
	}
	
	/**
	 * 获取回文本输入框中的字符串的整数值
	 * @return 以整数的形式返回文本输入框中的字符串的值
	 */
	public int getInteger() {
		return Integer.valueOf(getText().length() == 0 ? "0" : getText());
	}
	
	/**
	 * 设置将整数作为文本设置到文本框中
	 * @param i 要设置的整数值
	 */
	public void setInteger(int i) {
		setText(Integer.toString(i));
	}
}
