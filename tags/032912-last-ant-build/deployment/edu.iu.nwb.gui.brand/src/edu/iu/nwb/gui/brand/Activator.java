package edu.iu.nwb.gui.brand;

import java.io.IOException;
import java.util.Properties;

import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin implements IStartup{

	// The plug-in ID
	public static final String PLUGIN_ID = "edu.iu.nwb.gui.brand";
	// The shared instance
	private static Activator plugin;	
	private BundleContext bContext;
	private boolean alreadyLogged;
	
	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
		alreadyLogged = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		this.bContext = context;
        if (!alreadyLogged) {
            earlyStartup();
        }

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	public void earlyStartup(){

		if (bContext != null) {
            String greeting = null;
            Properties props = new Properties();

            try {
                props.load(bContext.getBundle().getEntry("/plugin.properties").openStream());                
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            greeting = props.getProperty("greeting", null);
            
            ServiceReference ref = bContext.getServiceReference(LogService.class.getName());
                
            if (ref != null && greeting != null) {
                alreadyLogged = true;
            }
        }        
	}
}
