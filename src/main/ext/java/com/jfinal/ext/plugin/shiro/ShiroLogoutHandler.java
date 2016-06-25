package com.jfinal.ext.plugin.shiro;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

public class ShiroLogoutHandler extends Handler {

    @Override
    public void handle(String target, HttpServletRequest request, HttpServletResponse response, boolean[] isHandled) {
        if (target.indexOf("/logout") >= 0) {
            next.handle("/auth/logout", request, response, isHandled);
        } else
            next.handle(target, request, response, isHandled);
    }

}
