/**
 * 
 */
package com.jfinal.ext2.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.ext2.core.ControllerExt;

/**
 * ExceptionInterceptor
 * @author BruceZCQ
 */
public class ExceptionInterceptorExt implements Interceptor {

	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
		} catch (Exception e) {
			if (inv.getTarget() instanceof ControllerExt) {
				ControllerExt controller = inv.getTarget();
				controller.onExceptionError(e);
			}
		}
	}

}
