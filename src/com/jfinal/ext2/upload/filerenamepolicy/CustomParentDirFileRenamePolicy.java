/**
 * 
 */
package com.jfinal.ext2.upload.filerenamepolicy;

import java.io.File;

import com.jfinal.ext2.kit.RandomKit;

/**
 * @author BruceZCQ
 * 自定义资源父目录名称
 *
 */
public class CustomParentDirFileRenamePolicy extends
		FileRenamePolicyWrapper {

	private String parentDir = null;
	private NamePolicy namepolicy = NamePolicy.ORIGINAL_NAME;
	
	public CustomParentDirFileRenamePolicy(String parentDir, NamePolicy namepolicy) {
		this.parentDir = parentDir;
		this.namepolicy = namepolicy;
	}
	
	@Override
	public File nameProcess(File f, String name, String ext) {
		if (null == this.parentDir) {
			throw new IllegalArgumentException("Please Set Custom ParentDir Name!");
		}
		
		String path = f.getParent();
		
		// add "/" postfix
		path = this.appendFileSeparator(path);
		
		path += this.parentDir;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		this.setSaveDirectory(path);
		
		if (this.namepolicy == NamePolicy.RANDOM_NAME) {
			name = RandomKit.randomMD5Str();
		} 
		return (new File(path,name+ext));
	}

}
