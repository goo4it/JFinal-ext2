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

/**
 * @author BruceZCQ
 * 自定义文件名称
 */
public class CustomNameFileRenamePolicy extends FileRenamePolicyWrapper {
	
	private String customName = null;

	public CustomNameFileRenamePolicy(String customName) {
		this.customName = customName;
	}

	@Override
	public File nameProcess(File f, String name, String ext) {
		if (null == this.customName) {
			throw new IllegalArgumentException("Please Set Custom File Name!");
		}

		// add "/" postfix
		StringBuilder path = new StringBuilder(f.getParent());
		String _path = path.toString();
		this.setSaveDirectory(_path);
		
		String fileName = this.customName + ext;
		
		return (new File(_path, fileName));
	}

}
