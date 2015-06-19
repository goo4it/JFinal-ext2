/**
 * 
 */
package com.jfinal2.ext.config;

import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.wall.WallFilter;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.druid.DruidPlugin;

/**
 * @author BruceZCQ
 *
 */
public abstract class JFinalExtConfig extends com.jfinal.config.JFinalConfig {
	
	private final static String cfg = "cfg.txt";
	private String webAppName = null;

	public String getWebAppName(){
		if (StrKit.notBlank(webAppName)) {
			return webAppName;
		}
		
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		webAppName = this.getProperty("webappname");
		if (StrKit.isBlank(webAppName)) {
			throw new IllegalArgumentException("Please Set Your WebApp Name in Your cfg file");
		}
		return webAppName;
	}
	
	/**
	 * 获取File Save Directory
	 * "/var/upload/webappname"
	 * @return
	 */
	public String getSaveDiretory(){
		return (new StringBuilder("/var/upload/").append(this.getWebAppName()).toString());
	}
	
	/**
	 * DruidPlugin
	 * @param prop ： property
	 * @return
	 */
	public DruidPlugin getDruidPlugin() {
		if (this.prop == null) {
			this.loadPropertyFile(cfg);
		}
		DruidPlugin dp = new DruidPlugin("jdbc:mysql://"+this.getProperty("dbUrl"),
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
}
