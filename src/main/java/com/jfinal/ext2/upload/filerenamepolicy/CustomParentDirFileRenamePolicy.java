/**
 * Copyright (c) 2015-2016, BruceZCQ (zcq@zhucongqi.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
		this.parentDir = this.appendFileSeparator(parentDir);
		this.namepolicy = namepolicy;
	}
	
	@Override
	public File nameProcess(File f, String name, String ext) {
		if (null == this.parentDir) {
			throw new IllegalArgumentException("Please Set Custom ParentDir Name!");
		}
		
		// add "/" postfix
		StringBuilder path = new StringBuilder(f.getParent());
		
		path.append(this.parentDir);
		
		String _path = path.toString();
		this.setSaveDirectory(_path);

		if (this.namepolicy == NamePolicy.RANDOM_NAME) {
			name = RandomKit.randomMD5Str();
		} 
		
		String fileName = name + ext;
		
		File file = new File(_path);
		if (!file.exists()) {
			file.mkdirs();
		}
		
		return (new File(_path, fileName));
	}
}
