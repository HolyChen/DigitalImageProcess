/*
 * 这是一款对数字图像进行处理的小应用程序。
 * 作者陈浩川。
 * 创作时间2014年10月。
 * 遵循X/MIT协议。请勿对对源代码进行进行不加说明的引用。
 */
package holy.digitalimageprocess.controller;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * 对图片类文件进行过滤的过滤器。包括jpg，bmp文件。
 */
class ImageFileFilter extends FileFilter {

	String[] postfix;
	String description;
	String saveFormatName;

	/**
	 * 构造一个图片过滤器，通过限制后缀名列表和描述。
	 * 
	 * @param strings
	 *            文件后缀名应属于postfix列表中任意一个或文件夹，才被呈现。
	 * @param description
	 *            对被过滤的文件类型进行清晰的描述。
	 * @param saveFormatName
	 * 			  储存时所使用的扩展名
	 */
	public ImageFileFilter(String[] strings, String description, String saveFormatName) {
		this.postfix = strings;
		this.description = description;
		this.saveFormatName = saveFormatName;
	}

	/**
	 * 接受文件后缀名属于description，或者为文件夹。
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
	 * 返回对过滤器的描述。
	 */
	@Override
	public String getDescription() {
		return description;
	}
	
	/**
	 * 返回储存时所用的扩展名
	 * @return 储存使用的扩展名
	 */
	public String getSaveFormatName() {
		return saveFormatName;
	}
}