/**
 * ActionExtentionHandler
 */
package com.jfinal.ext2.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.handler.Handler;

/**
 * @author BruceZCQ
 */
public class ActionExtentionHandler extends Handler {

	// 伪静态处理
	public static final String htmlExt = ".html";
	public static final String htmExt = ".htm";
	public static final String jsonExt = ".json";
	
	private int len = ActionExtentionHandler.htmlExt.length();
	private String actionExt = ActionExtentionHandler.htmlExt;
	
	public ActionExtentionHandler(){
		this(ActionExtentionHandler.htmExt);
	}
	
	public ActionExtentionHandler(String actionExt){
		this.actionExt = actionExt;
		this.len = actionExt.length();
	}
	
	@Override
	public void handle(String target, HttpServletRequest request,
			HttpServletResponse response, boolean[] isHandled) {
		if (target.endsWith(this.actionExt))
            target = target.substring(0, target.length() - this.len);
        nextHandler.handle(target, request, response, isHandled);
	}

}
