/**
 * 
 */
package com.jfinal.ext2.config;

import java.util.List;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.plugin.IPlugin;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * @author BruceZCQ
 */
public class StandaloneDbConfig {
	
	private static StandaloneDbConfig instance = null;
	private Config cfg = null;
	
	private StandaloneDbConfig() {
		cfg = new Config();
		cfg.start();
	}
	
	public static StandaloneDbConfig start() {
		if (instance == null) {
			instance = new StandaloneDbConfig();
		}
		return instance;
	}
	
	public static void stop() {
		if (instance != null) {
			instance.cfg.stop();
		}
	}
	
	private static class Config {
		
		private static final Plugins plugins = new Plugins();
		
		public Config() {
			JFinalConfig config = new JFinalConfig();
			config.configPlugin(plugins);
		}
		
		public void start() {
			this.startPlugins();
		}
		
		public void stop() {
			this.stopPlugins();
		}
		
		private void startPlugins() {
			List<IPlugin> pluginList = plugins.getPluginList();
			if (pluginList == null)
				return ;
			
			for (IPlugin plugin : pluginList) {
				try {
					if (plugin.start() == false) {
						String message = "Plugin start error: " + plugin.getClass().getName();
						throw new RuntimeException(message);
					}
				}
				catch (Exception e) {
					String message = "Plugin start error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
					throw new RuntimeException(message, e);
				}
			}
		}
		
		private void stopPlugins() {
			List<IPlugin> pluginList = plugins.getPluginList();
			if (pluginList == null)
				return ;
			
			for (IPlugin plugin : pluginList) {
				try {
					if (plugin.stop() == false) {
						String message = "Plugin stop error: " + plugin.getClass().getName();
						throw new RuntimeException(message);
					}
				}
				catch (Exception e) {
					String message = "Plugin stop error: " + plugin.getClass().getName() + ". \n" + e.getMessage();
					throw new RuntimeException(message, e);
				}
			}
		}
	}

	private static class JFinalConfig extends JFinalConfigExt {

		@Override
		public void configMoreConstants(Constants me) {
			
		}

		@Override
		public void configMoreRoutes(Routes me) {
			
		}

		@Override
		public void configMorePlugins(Plugins me) {
			
		}

		@Override
		public void configTablesMapping(String configName, ActiveRecordPlugin arp) {
			
		}

		@Override
		public void configMoreInterceptors(Interceptors me) {
			
		}

		@Override
		public void configMoreHandlers(Handlers me) {
			
		}

		@Override
		public void afterJFinalStarted() {
			
		}
	}
	
}
