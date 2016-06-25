package cn.jlook.jfinal.ext3.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

public class ApiInterceptor implements Interceptor {
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        if ("OPTIONS".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase())
                || "POST".equalsIgnoreCase(controller.getRequest().getMethod().toUpperCase()))
            inv.invoke();
        else
            controller.renderError(404);
    }
}
