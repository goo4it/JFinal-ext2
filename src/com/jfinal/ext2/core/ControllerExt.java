/**
 * 
 */
package com.jfinal.ext2.core;

import java.lang.reflect.Field;
import java.math.BigInteger;

import com.jfinal.ext.kit.Reflect;

/**
 * @author BruceZCQ Jun 22, 20154:15:48 PM
 */
public class ControllerExt extends com.jfinal.core.Controller {

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
