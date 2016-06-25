package cn.jlook.jfinal.ext3;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;
import com.jfinal.kit.StrKit;

import cn.jlook.jfinal.ext3.kit.SecondDomainKit;

public class SecondDomainHandler extends Handler {
    private String domain;

    public SecondDomainHandler(String domain) {
        this.domain = domain;
    }

    private String getDomain(HttpServletRequest request) {
        if (request.getServerName().endsWith(domain)) {
            return SecondDomainKit.contains(
                    request.getServerName().substring(0, request.getServerName().length() - (domain.length() + 1)));
        } else
            return null;
    }

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        String domain = getDomain(request);
        if (StrKit.notNull(domain)) {
            target = "/" + domain + target;
        }
        next.handle(target, request, response, isHandled);
    }

}
