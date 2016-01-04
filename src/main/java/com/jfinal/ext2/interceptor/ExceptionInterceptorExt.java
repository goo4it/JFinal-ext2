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
import com.jfinal.ext2.config.JFinalConfigExt;
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
				ControllerExt controller = inv.getTarget();;
				controller.onExceptionError(e);
				if (JFinalConfigExt.DEV_MODE) {
					e.printStackTrace();
				}
				if (e instanceof ActionException) {
					controller.renderError(((ActionException)e).getErrorCode());
				}else{
					controller.renderError(500);	
				}
			}
		}
	}

}
