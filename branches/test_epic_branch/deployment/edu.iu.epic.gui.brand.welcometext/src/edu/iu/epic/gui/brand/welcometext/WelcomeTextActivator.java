package edu.iu.epic.gui.brand.welcometext;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;

public class WelcomeTextActivator {
	public static final String PLUGIN_ID =
		"edu.iu.epic.gui.brand.welcometext";
	
	private static WelcomeTextActivator plugin;

	public WelcomeTextActivator() {
		plugin = this;
	}
	
	protected void activate(ComponentContext componentContext) {
		if (componentContext != null) {
        	String greeting = null;
        	Properties props = new Properties();
        	BundleContext bundleContext = componentContext.getBundleContext();

        	try {
        		Bundle bundle = bundleContext.getBundle();
        		URL pluginPropertiesEntry =
        			bundle.getEntry("/plugin.properties");
            	props.load(pluginPropertiesEntry.openStream());                
        	}
        	catch (IOException ioException) {
        		ioException.printStackTrace();
        	}
        
        	greeting = props.getProperty("greeting", null);
        	
        	LogService logService =
        		(LogService)componentContext.locateService("LOG");
        	
        	logService.log(LogService.LOG_INFO, greeting);
		}
	}
	
	protected void deactivate(ComponentContext componentContext) {
	}
	
	public static WelcomeTextActivator getDefault() {
		return plugin;
	}
}
