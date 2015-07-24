/**
 * The MIT License (MIT)

Copyright (c) 2015 BruceZCQ

https://github.com/BruceZCQ/jfinal-ext2
http://zhucongqi.cn

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */
package com.jfinal.ext2.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.ext2.handler.ActionExtentionHandler;
import com.jfinal.ext2.interceptor.NotFoundActionInterceptor;
import com.jfinal.ext2.kit.PageViewKit;
import com.jfinal.ext2.upload.OreillyCosExt;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

/**
 * @author BruceZCQ
 *
 */
public abstract class JFinalConfigExt extends com.jfinal.config.JFinalConfig {
	
	private final static String cfg = "cfg.txt";
	
	public static String WEB_APP_NAME = null;
	public static Boolean DEV_MODE = false;
	
	/**
	 * Config other More constant
	 */
	public abstract void configMoreConstants(Constants me);
	
	/**
	 * Config other more route
	 */
	public abstract void configMoreRoutes(Routes me);
	
	/**
	 * Config other more plugin
	 */
	public abstract void configMorePlugins(Plugins me);
	
	/**
	 * Config other Tables Mapping
	 */
	public abstract void configTablesMapping(String configName, ActiveRecordPlugin arp);
	
	/**
	 * Config other more interceptor applied to all actions.
	 */
	public abstract void configMoreInterceptors(Interceptors me);
	
	/**
	 * Config other more handler
	 */
	public abstract void configMoreHandlers(Handlers me);

	/**
	 * After JFinalStarted
	 */
	public abstract void afterJFinalStarted();
	
	/**
	 * Config constant
	 * 
	 * Default <br/>
	 * ViewType: JSP <br/>
	 * Encoding: UTF-8 <br/>
	 * ErrorPages: <br/>
	 * 404 : /WEB-INF/errorpages/404.html <br/>
	 * 500 : /WEB-INF/errorpages/500.html <br/>
	 * 403 : /WEB-INF/errorpages/403.html <br/>
	 * UploadedFileSaveDirectory : cfg basedir + WebappName <br/>
	 */
	public void configConstant(Constants me) {
		me.setViewType(ViewType.JSP);
		me.setDevMode(this.getAppDevMode());
		me.setEncoding("UTF-8");
		me.setError404View(PageViewKit.get404PageView());
		me.setError500View(PageViewKit.get500PageView());
		me.setError403View(PageViewKit.get403PageView());
		//file save dir
		me.setUploadedFileSaveDirectory(this.getSaveDiretory());
		
		JFinalConfigExt.WEB_APP_NAME = this.getWebAppName();
		JFinalConfigExt.DEV_MODE = this.getAppDevMode();
		
		// config others
		configMoreConstants(me);
		this.initOreillyCosExt(me);
	}
	
	/**
	 * Config route
	 * Config the AutoBindRoutes
	 * 自动bindRoute。controller命名为xxController。<br/>
	 * AutoBindRoutes自动取xxController对应的class的Controller之前的xx作为controllerKey(path)<br/>
	 * 如：MyUserController => myuser; UserController => user; UseradminController => useradmin<br/>
	 */
	public void configRoute(Routes me) {
		me.add(new AutoBindRoutes());
		
		// config others
		configMoreRoutes(me);
	}
	
	/**
	 * Config plugin
	 */
	public void configPlugin(Plugins me) {
		
		if (this.getDbActiveState()) {
			for (Integer index = 1; index <= this.getDataSourceCount(); index++) {
				DruidPlugin drp = this.getDruidPlugin(index);
				String configName = this.getDataSouceConfigNameAtIndex(index);
				me.add(drp);
				if (this.getDataSouceAutoTableBindStateAtIndex(index)) {
					AutoTableBindPlugin atb = new AutoTableBindPlugin(configName, drp);
					me.add(atb);
				}else{
					ActiveRecordPlugin arp = this.getActiveRecordPlugin(configName, drp);
					me.add(arp);
					configTablesMapping(configName, arp);
				}
			}
		}
		
		// config others
		configMorePlugins(me);
	}
	
	/**
	 * Config interceptor applied to all actions.
	 */
	public void configInterceptor(Interceptors me) {
		// when action not found fire 404 error
		me.add(new NotFoundActionInterceptor());

		// config others
		configMoreInterceptors(me);
	}
	
	/**
	 * Config handler
	 */
	public void configHandler(Handlers me) {
		// add extension handler
		me.add(new ActionExtentionHandler());
		
		// config others
		configMoreHandlers(me);
	}
	
	public void afterJFinalStart() {
		super.afterJFinalStart();
		this.afterJFinalStarted();
	}
	
	/**
	 * Init OreillyCosExt
	 */
	private void initOreillyCosExt(Constants me) {
		OreillyCosExt.init(me.getUploadedFileSaveDirectory(), 
				me.getMaxPostSize(), me.getEncoding());
	}
	
	/**
	 * 获取File Save Directory
	 * "/var/upload/webappname"
	 * @return
	 */
	private String getSaveDiretory(){
		String app = this.getWebAppName();
		String baseDir = this.getProperty("savebasedir");
		
		if (!baseDir.endsWith("/")) {
			baseDir += "/";
		}
		
		if (!baseDir.endsWith("uploads")) {
			baseDir += "uploads/";
		}
		return (new StringBuilder(baseDir).append(app).toString());
	}
	
	/**
	 * 获取app的dev mode
	 * @return
	 */
	private Boolean getAppDevMode(){
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		return this.getPropertyToBoolean("dev");
	}

	/**
	 * 获取 WebAppName
	 * @return
	 */
	private String getWebAppName() {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		
		String webAppName = this.getProperty("webappname");
		if (StrKit.isBlank(webAppName)) {
			throw new IllegalArgumentException("Please Set Your WebApp Name in Your cfg file");
		}
		return webAppName;
	}
	
	/**
	 * 获取是否打开数据库状态
	 * @return
	 */
	private Boolean getDbActiveState(){
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		
		return this.getPropertyToBoolean("dbactive");
	}
	
	/**
	 * 是否开启AutoTableBind
	 * @return
	 */
	private Boolean getDataSouceAutoTableBindStateAtIndex(Integer index) {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		return this.getPropertyToBoolean("autotablebind"+index);
	}
	
	/**
	 * 获取数据源数量
	 * @return
	 */
	private Integer getDataSourceCount() {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		return this.getPropertyToInt("datasourcecnt");
	}
	
	/**
	 * 获取数据源configname
	 * @param index
	 * @return
	 */
	private String getDataSouceConfigNameAtIndex(Integer index) {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		return this.getProperty("configname"+index);
	}
	
	/**
	 * DruidPlugin
	 * @param prop ： property
	 * @return
	 */
	private DruidPlugin getDruidPlugin(Integer index) {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		
		DruidPlugin dp = new DruidPlugin("jdbc:mysql://"+this.getProperty("dbUrl"+index),
				this.getProperty("user"),
				this.getProperty("password"));
		dp.setInitialSize(this.getPropertyToInt("initsize"));
		dp.setMaxActive(this.getPropertyToInt("maxactive"));
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType(this.getProperty("dbtype"));
		dp.addFilter(wall);
		return dp;
	}
	
	/**
	 * 获取ActiveRecordPlugin 
	 * @param dp DruidPlugin
	 * @return
	 */
	private ActiveRecordPlugin getActiveRecordPlugin(String cfgName, DruidPlugin dp){
		if (null == dp) {
			throw new IllegalArgumentException("Please Call `getDruidPlugin` first");
		}
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cfgName, dp);
		arp.setShowSql(this.getPropertyToBoolean("showSql"));
		return arp;
	}

}
