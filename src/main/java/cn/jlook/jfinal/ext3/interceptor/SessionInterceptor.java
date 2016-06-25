package cn.jlook.jfinal.ext3.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;

public class SessionInterceptor implements Interceptor {
    public static final String JSESSIONID        = "JSESSIONID";
    public static final String COOKIE_JSESSIONID = "cookie_key_jsessionid";

    @Override
    public void intercept(Invocation ai) {
        String _cookie_jsessionid = ai.getController().getCookie(COOKIE_JSESSIONID);
        if (StrKit.notBlank(_cookie_jsessionid)
                && !ai.getController().getSession().getId().equals(_cookie_jsessionid)) {
            System.out.println("COOKIE_JSESSIONID -> " + _cookie_jsessionid);
            ai.getController().setCookie(JSESSIONID, _cookie_jsessionid, 1 * 24 * 60 * 60);
        }
        ai.invoke();
    }

}
