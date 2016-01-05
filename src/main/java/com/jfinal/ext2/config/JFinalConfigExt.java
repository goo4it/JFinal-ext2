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
package com.jfinal.ext2.config;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.interceptor.POST;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.ext2.handler.ActionExtentionHandler;
import com.jfinal.ext2.interceptor.ExceptionInterceptorExt;
import com.jfinal.ext2.interceptor.NotFoundActionInterceptor;
import com.jfinal.ext2.kit.PageViewKit;
import com.jfinal.ext2.plugin.druid.DruidEncryptPlugin;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;

/**
 * @author BruceZCQ
 *
 */
public abstract class JFinalConfigExt extends com.jfinal.config.JFinalConfig {
	
	private final static String cfg = "cfg.txt";
	
	public static String APP_NAME = null;
	public static String UPLOAD_SAVE_DIR = null;
	public static Boolean DEV_MODE = false;
	
	private boolean geRuned = false;
	
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
	 * 404 : /WEB-INF/errorpages/404.jsp <br/>
	 * 500 : /WEB-INF/errorpages/500.jsp <br/>
	 * 403 : /WEB-INF/errorpages/403.jsp <br/>
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
		String path = this.getSaveDiretory();
		me.setBaseUploadPath(path);
		
		JFinalConfigExt.APP_NAME = this.getAppName();
		JFinalConfigExt.DEV_MODE = this.getAppDevMode();
		JFinalConfigExt.UPLOAD_SAVE_DIR = path;
		
		// config others
		configMoreConstants(me);
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
	 * TODO 自动 mapping
	 */
	public void configPlugin(Plugins me) {
		if (this.getDbActiveState()) {
			Integer cnt = this.getDataSourceCount();
			for (Integer index = 1; index <= cnt; index++) {
				DruidEncryptPlugin drp = this.getDruidPlugin(index);
				String configName = this.getDataSouceConfigNameAtIndex(index);
				me.add(drp);
				ActiveRecordPlugin arp = this.getActiveRecordPlugin(configName, drp);
				me.add(arp);
				configTablesMapping(configName, arp);
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
		// add excetion interceptor
		me.add(new ExceptionInterceptorExt());
		if (this.getHttpPostMethod()) {
			me.add(new POST());
		}
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

	private void loadPropertyFile() {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
	}
	
	private boolean getHttpPostMethod() {
		this.loadPropertyFile();
		return this.getPropertyToBoolean("app.post",false);
	}
	
	/**
	 * 获取File Save Directory
	 * "/var/uploads/appname"
	 * @return
	 */
	private String getSaveDiretory(){
		this.loadPropertyFile();
		String app = this.getAppName();
		String baseDir = this.getProperty("app.upload.basedir");
		
		if (baseDir.endsWith("/")) {
			if (!baseDir.endsWith("uploads/")) {
				baseDir += "uploads/";	
			}
		}else{
			if (!baseDir.endsWith("uploads")) {
				baseDir += "/uploads/";
			}else{
				baseDir += "/";
			}
		}
		
		return (new StringBuilder(baseDir).append(app).toString());
	}
	
	/**
	 * 获取app的dev mode
	 * @return
	 */
	private Boolean getAppDevMode(){
		this.loadPropertyFile();
		return this.getPropertyToBoolean("app.dev");
	}

	/**
	 * 获取 AppName
	 * @return
	 */
	private String getAppName() {
		this.loadPropertyFile();
		String appName = this.getProperty("app.name");
		if (StrKit.isBlank(appName)) {
			throw new IllegalArgumentException("Please Set Your App Name in Your cfg file");
		}
		return appName;
	}
	
	/**
	 * 获取是否打开数据库状态
	 * @return
	 */
	private Boolean getDbActiveState(){
		this.loadPropertyFile();
		return this.getPropertyToBoolean("db.active");
	}
	
	/**
	 * 获取数据源数量
	 * @return
	 */
	private Integer getDataSourceCount() {
		this.loadPropertyFile();
		return this.getPropertyToInt("db.dscnt");
	}
	
	/**
	 * 获取数据源configname
	 * @param index
	 * @return
	 */
	private String getDataSouceConfigNameAtIndex(Integer index) {
		this.loadPropertyFile();
		return this.getProperty("db.cfgname"+index);
	}
	
	/**
	 * DruidPlugin
	 * @param prop ： property
	 * @return
	 */
	private DruidEncryptPlugin getDruidPlugin(Integer index) {
		this.loadPropertyFile();
		DruidEncryptPlugin dp = new DruidEncryptPlugin("jdbc:mysql://"+this.getProperty("db.url"+index),
				this.getProperty("db.user"),
				this.getProperty("db.password"));
		dp.setInitialSize(this.getPropertyToInt("db.initsize"));
		dp.setMaxActive(this.getPropertyToInt("db.maxactive"));
		dp.addFilter(new StatFilter());
		WallFilter wall = new WallFilter();
		wall.setDbType("mysql");
		dp.addFilter(wall);
		
		if (this.getGeRun() && !this.geRuned) {
			dp.start();
			Generator ge = new Generator(dp.getDataSource(), this.getBaseModelPackage(), this.getBaseModelOutDir(), this.getModelPackage(), this.getModelOutDir());
			ge.generate();
			this.geRuned = this.getDataSourceCount() == 1 ? true : false;
		}
		
		return dp;
	}
	
	/**
	 * 获取ActiveRecordPlugin 
	 * @param dp DruidPlugin
	 * @return
	 */
	private ActiveRecordPlugin getActiveRecordPlugin(String cfgName, DruidPlugin dp){
		this.loadPropertyFile();
		ActiveRecordPlugin arp = new ActiveRecordPlugin(cfgName, dp);
		arp.setShowSql(this.getPropertyToBoolean("db.showsql"));
		arp.setDialect(new MysqlDialect());
		// mapping
		try {
			Class<?> clazz = Class.forName(this.getModelPackage()+"._MappingKit");
			Method mapping = clazz.getMethod("mapping", ActiveRecordPlugin.class);
			mapping.invoke(clazz, arp);
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
		}
		return arp;
	}
	
	private boolean getGeRun() {
		this.loadPropertyFile();
		return this.getPropertyToBoolean("ge.run");
	}
	
	private String modelPackage = null;
	private String modelOutDir = null;
	private String baseModelPackage = null;
	private String baseModelOutDir = null;
	
	private String getBaseModelOutDir() {
		this.loadPropertyFile();
		if (this.baseModelOutDir == null) {
			this.baseModelOutDir = this.getProperty("ge.base.model.outdir");
		}
		return this.baseModelOutDir;
	}
	
	private String getBaseModelPackage() {
		this.loadPropertyFile();
		if (this.baseModelPackage == null) {
			this.baseModelPackage = this.getProperty("ge.base.model.package");
		}
		return this.baseModelPackage;
	}
	
	private String getModelOutDir() {
		this.loadPropertyFile();
		if (this.modelOutDir == null) {
			this.modelOutDir = this.getProperty("ge.model.outdir");
		}
		return this.modelOutDir;
	}
	
	private String getModelPackage() {
		this.loadPropertyFile();
		if (this.modelPackage == null) {
			this.modelPackage =  this.getProperty("ge.model.package");
		}
		return this.modelPackage;
	}
}
