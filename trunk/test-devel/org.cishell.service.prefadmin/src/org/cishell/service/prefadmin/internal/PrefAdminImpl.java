package org.cishell.service.prefadmin.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.List;

import org.cishell.service.prefadmin.PrefAdmin;
import org.cishell.service.prefadmin.PrefPage;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationEvent;
import org.osgi.service.cm.ConfigurationListener;
import org.osgi.service.cm.ConfigurationPlugin;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeService;

public class PrefAdminImpl implements PrefAdmin, ConfigurationPlugin, ConfigurationListener {

	
	private LogService log;
	private MetaTypeService mts;
	private ConfigurationAdmin ca;
	
	private PrefReferenceProcessor prefProcessor;
	private List prefReferencesToBeProcessed = new ArrayList();
	
	private List prefHolderReferences = new ArrayList();
	
	private boolean hasBeenActivated = false;

	public PrefPage[] getLocalPrefPages() {
		if (this.prefProcessor != null) {
		return this.prefProcessor.getLocalPrefPages();
		} else {
			return new PrefPage[0];
		}
	}
	
	public PrefPage[] getGlobalPrefPages() {
		if (this.prefProcessor != null) {
			return this.prefProcessor.getGlobalPrefPages();
			} else {
				return new PrefPage[0];
		}
	}
	
    protected void activate(ComponentContext ctxt) {
    	
    	this.log = (LogService) ctxt.locateService("LOG");
    	this.mts = (MetaTypeService) ctxt.locateService("MTS");
    	this.ca = (ConfigurationAdmin) ctxt.locateService("CS");
    	
    	this.prefProcessor = new PrefReferenceProcessor(log, mts, ca);
    	
    	this.hasBeenActivated = true;
    	
    	//takes care of any prefHolders that may have been registered
    	//before the rest of these services were registered.
    	this.prefProcessor.processPrefReferences(
    			(ServiceReference[]) prefReferencesToBeProcessed.toArray(new ServiceReference[0]));
    	this.prefReferencesToBeProcessed.clear();
    }
    
    protected void deactivate(ComponentContext ctxt) {
    }
    
    protected void prefHolderRegistered(ServiceReference prefHolder) {
    	System.out.println("ServiceReference " + prefHolder.getProperty("service.pid") + " beginning registration");
    	this.prefReferencesToBeProcessed.add(prefHolder);
    	
    	if (this.hasBeenActivated == true) {
    		this.prefProcessor.processPrefReferences((ServiceReference[]) this.prefReferencesToBeProcessed.toArray(new ServiceReference[0]));
    		this.prefReferencesToBeProcessed.clear();
    	}
    	
    	this.prefHolderReferences.add(prefHolder);
    }
    
    protected void prefHolderUnregistered(ServiceReference prefHolder) {
    	//ignore for now
    }

	public void modifyConfiguration(ServiceReference reference,
			Dictionary properties) {
		
		System.out.println("Injecting global preferences into " + reference.getProperty("service.pid"));

		//inject global preferences into configuration objects headed for ManagedServices
		PrefPage[] globalPrefPages = getGlobalPrefPages();
		for (int ii = 0; ii < globalPrefPages.length; ii++) {
			PrefPage globalPrefPage = globalPrefPages[ii];
			Configuration globalPrefConf = globalPrefPage.getPrefConf();
			
			System.out.println("  Injecting " + globalPrefConf.getPid());
			
			String namespace = globalPrefConf.getPid();
			AttributeDefinition d;
			
			Dictionary globalPrefDict = globalPrefConf.getProperties();
			
			//the keys of each dictionary are the ids of global preference OCDs.
			Enumeration ids = globalPrefDict.keys();
			while (ids.hasMoreElements()) {
				String id = (String) ids.nextElement();
				String value = (String) globalPrefDict.get(id);
			
				String keyForConfiguration = namespace + "." + id;
				
				properties.put(keyForConfiguration, value);
			}
		}
	}

	public void configurationEvent(ConfigurationEvent event) {
		if (event.getType() == event.CM_UPDATED) {
			System.out.println("UPDATED event received for " + event.getPid());
			if (isGlobalConf(event.getPid())) {
				System.out.println("  Sending global preferences");
				sendGlobalPreferences();
			} else {
				System.out.println("  NOT sending global preferences");
			}
		} else if (event.getType() == event.CM_DELETED) {
		}
	}
	
	/**
	 * Call when a global preference object is created or updated.
	 * Necessary because changes to global preference do not
	 * cause an update event for every ManagedService.
	 */
	public void sendGlobalPreferences() {
		try {
		for (int ii = 0; ii < this.prefHolderReferences.size(); ii++) {
			ServiceReference prefHolder = (ServiceReference) this.prefHolderReferences.get(ii);
			Configuration localPrefConf = ca.getConfiguration((String) prefHolder.getProperty("service.pid"));
			try {
			localPrefConf.update();
			} catch (IOException e) {
				this.log.log(LogService.LOG_ERROR, "Unable to update configuration for " + localPrefConf.getPid(), e);
			}
		}
		} catch (IOException e) {
			this.log.log(LogService.LOG_ERROR, "Unable to obtain all configuration objects", e);
		}
	}
	
	private boolean isGlobalConf(String pid) {
		return (pid.substring(0, pid.length() - 1).endsWith("global_prefs")
				|| pid.endsWith("global_prefs"));
	}
}