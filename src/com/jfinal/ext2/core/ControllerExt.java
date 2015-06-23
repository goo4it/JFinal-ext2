/**
 * 
 */
package com.jfinal.ext2.core;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.List;

import com.jfinal.ext.kit.Reflect;
import com.jfinal.ext2.upload.MultipartRequestExt;
import com.jfinal.ext2.upload.OreillyCosExt;
import com.jfinal.upload.MultipartRequest;
import com.jfinal.upload.UploadFile;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * @author BruceZCQ Jun 22, 20154:15:48 PM
 */
public class ControllerExt extends com.jfinal.core.Controller {

	private static final FileRenamePolicy fileRenamePolicy = new DefaultFileRenamePolicy();
	private MultipartRequestExt multipartRequest = null;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ControllerExt() {
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			Class clazz = field.getType();
			if (Service.class.isAssignableFrom(clazz) && clazz != Service.class) {
				try {
					((Field) Reflect.accessible(field)).set(this,
							Service.getInstance(clazz, this));
				} catch (IllegalAccessException e) {
					throw new RuntimeException();
				}
			}
		}
	}

	// --------
	/**
	 * Get upload file from multipart request.
	 */
	
	/**
	 * call Super
	 */
	public List<UploadFile> getFiles(String saveDirectory, Integer maxPostSize,
			String encoding, FileRenamePolicy fileRenamePolicy) {
		OreillyCosExt.setFileRenamePolicy(fileRenamePolicy);
		if (this.getRequest() instanceof MultipartRequest == false)
			multipartRequest = new MultipartRequestExt(this.getRequest(), saveDirectory, maxPostSize, encoding);
		return multipartRequest.getFiles();
	}
	
	public List<UploadFile> getFiles(String saveDirectory, int maxPostSize,
			FileRenamePolicy fileRenamePolicy) {
		OreillyCosExt.setFileRenamePolicy(fileRenamePolicy);
		if (this.getRequest() instanceof MultipartRequest == false)
			multipartRequest = new MultipartRequestExt(this.getRequest(), saveDirectory, maxPostSize);
		return multipartRequest.getFiles();
	}

	public List<UploadFile> getFiles(String saveDirectory,
			FileRenamePolicy fileRenamePolicy) {
		OreillyCosExt.setFileRenamePolicy(fileRenamePolicy);
		if (this.getRequest() instanceof MultipartRequest == false)
			multipartRequest = new MultipartRequestExt(this.getRequest(), saveDirectory);
		return multipartRequest.getFiles();
	}
	
	public List<UploadFile> getFiles(FileRenamePolicy fileRenamePolicy) {
		OreillyCosExt.setFileRenamePolicy(fileRenamePolicy);
		if (this.getRequest() instanceof MultipartRequest == false)
			multipartRequest = new MultipartRequestExt(this.getRequest());
		return multipartRequest.getFiles();
	}
	
	/**
	 * call self
	 */
	
	/**
	 * File
	 */
	public UploadFile getFile(String parameterName, String saveDirectory,
			Integer maxPostSize, String encoding) {
		return this.getFile(parameterName, saveDirectory, maxPostSize, encoding,
				fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName, String saveDirectory,
			Integer maxPostSize, String encoding,
			FileRenamePolicy fileRenamePolicy) {
		this.getFiles(saveDirectory, maxPostSize, encoding, fileRenamePolicy);
		return this.getFile(parameterName, fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName, String saveDirectory,
			int maxPostSize) {
		return this.getFile(parameterName, saveDirectory, maxPostSize,
				fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName, String saveDirectory,
			int maxPostSize, FileRenamePolicy fileRenamePolicy) {
		this.getFiles(saveDirectory, maxPostSize, fileRenamePolicy);
		return this.getFile(parameterName, fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName, String saveDirectory) {
		return this.getFile(parameterName, saveDirectory, fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName, String saveDirectory,
			FileRenamePolicy fileRenamePolicy) {
		this.getFiles(saveDirectory, fileRenamePolicy);
		return this.getFile(parameterName, fileRenamePolicy);
	}

	public UploadFile getFile() {
		return this.getFile(fileRenamePolicy);
	}

	public UploadFile getFile(FileRenamePolicy fileRenamePolicy) {
		List<UploadFile> uploadFiles = this.getFiles(fileRenamePolicy);
		return uploadFiles.size() > 0 ? uploadFiles.get(0) : null;
	}

	public UploadFile getFile(String parameterName) {
		return this.getFile(parameterName, fileRenamePolicy);
	}

	public UploadFile getFile(String parameterName,
			FileRenamePolicy fileRenamePolicy) {
		List<UploadFile> uploadFiles = this.getFiles(fileRenamePolicy);
		for (UploadFile uploadFile : uploadFiles) {
			if (uploadFile.getParameterName().equals(parameterName)) {
				return uploadFile;
			}
		}
		return null;
	}


	/**
	 * Files
	 */
	public List<UploadFile> getFiles(String saveDirectory, Integer maxPostSize,
			String encoding) {
		return this.getFiles(saveDirectory, maxPostSize, encoding, fileRenamePolicy);
	}

	public List<UploadFile> getFiles(String saveDirectory, int maxPostSize) {
		return this.getFiles(saveDirectory, maxPostSize, fileRenamePolicy);
	}

	public List<UploadFile> getFiles(String saveDirectory) {
		return this.getFiles(saveDirectory, fileRenamePolicy);
	}

	/**
	 * Returns the value of a request parameter and convert to BigInteger.
	 * @param name a String specifying the name of the parameter
	 * @return a BigInteger representing the single value of the parameter
	 */
	public BigInteger getParaToBigInteger(String name){
		return this.toBigInteger(getPara(name), null);
	}
	
	/**
	 * Returns the value of a request parameter and convert to BigInteger with a default value if it is null.
	 * @param name a String specifying the name of the parameter
	 * @return a BigInteger representing the single value of the parameter
	 */
	public BigInteger getParaToBigInteger(String name,BigInteger defaultValue){
		return this.toBigInteger(getPara(name), defaultValue);
	}
	
	private BigInteger toBigInteger(String value, BigInteger defaultValue) {
		if (value == null || "".equals(value.trim()))
			return defaultValue;
		return (new BigInteger(value));
	}
	
}
