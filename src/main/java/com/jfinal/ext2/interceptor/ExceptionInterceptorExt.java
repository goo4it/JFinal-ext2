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
package com.jfinal.ext2.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.ActionException;
import com.jfinal.core.Controller;
import com.jfinal.ext2.core.ControllerExt;
import com.jfinal.log.Log;

/**
 * ExceptionInterceptor
 * @author BruceZCQ
 */
public class ExceptionInterceptorExt implements Interceptor {

	private Log log = Log.getLog(this.getClass());
	
	@Override
	public void intercept(Invocation inv) {
		try {
			inv.invoke();
		} catch (Exception e) {
			int errorCode = 500;
			if (e instanceof ActionException) {
				errorCode = ((ActionException)e).getErrorCode();
			}
			this.log.error(e.getLocalizedMessage());
			Object target = inv.getTarget();
			if (inv.getTarget() instanceof ControllerExt) {
				((ControllerExt)target).onExceptionError(e);
			}
			((Controller)target).renderError(errorCode);
		}
	}
}
