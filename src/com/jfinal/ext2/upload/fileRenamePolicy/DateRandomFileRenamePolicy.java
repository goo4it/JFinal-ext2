/**
 * 
 */
package com.jfinal.ext2.upload.fileRenamePolicy;

import java.io.File;

import com.jfinal.ext2.kit.DateTimeKit;
import com.jfinal.ext2.kit.RandomKit;

/**
 * @author BruceZCQ
 *  baseSaveDir/2015/6/22/zzzz.jpg
 */
public class DateRandomFileRenamePolicy extends FileRenamePolicyWrapper {

	@Override
	public File nameProcess(File f, String name, String ext) {
		String rename = RandomKit.randomMD5Str();
		String path = f.getParent();
		if (!path.endsWith("/")) {
			path += "/";
		}
		//add year month day
		path += DateTimeKit.formatNowToStyle("yyyy/M/d");
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.setSaveDirectory(path);
		return (new File(path,rename+ext));
	}

}
