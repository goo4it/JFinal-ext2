/**
 * 
 */
package com.jfinal.ext2.validate;

import com.jfinal.core.Controller;

/**
 * @author BruceZCQ
 *
 */
public class ValidatorExt extends com.jfinal.validate.Validator {
	
	@Override
	protected void validate(Controller c) {
		this.setShortCircuit(true);
	}
	
	@Override
	protected void handleError(Controller c) {
		c.renderError(403);
	}
	
//	/**
//	 * Validate long.
//	 */
//	protected void validateLong(String field, long min, long max, String errorKey, String errorMessage) {
//		try {
//			String value = controller.getPara(field);
//			long temp = Long.parseLong(value);
//			if (temp < min || temp > max)
//				addError(errorKey, errorMessage);
//		}
//		catch (Exception e) {
//			addError(errorKey, errorMessage);
//		}
//	}
//	
//	/**
//	 * Validate long.
//	 */
//	protected void validateLong(String field, String errorKey, String errorMessage) {
//		try {
//			String value = controller.getPara(field);
//			Long.parseLong(value);
//		}
//		catch (Exception e) {
//			addError(errorKey, errorMessage);
//		}
//	}
}
