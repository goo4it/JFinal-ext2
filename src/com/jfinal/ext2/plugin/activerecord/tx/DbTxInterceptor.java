/**
 * 
 */
package com.jfinal.ext2.plugin.activerecord.tx;

import java.sql.SQLException;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.ext2.core.ControllerExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 事务处理Interceptor
 * @author BruceZCQ
 */
public class DbTxInterceptor implements Interceptor {

	@Override
	public void intercept(final Invocation inv) {
		Db.tx(new IAtom() {
			@Override
			public boolean run() throws SQLException {
				try {
					inv.invoke();
				} catch (Exception e) {
					if (inv.getTarget() instanceof ControllerExt) {
						ControllerExt controller = inv.getTarget();
						controller.onExceptionError(e);
					}
					return false;
				}
				return true;
			}
		});
	}

}
