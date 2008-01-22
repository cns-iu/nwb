package org.cishell.service.prefadmin.internal;

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
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.cm.ConfigurationPlugin;

public class PrefAdminImpl implements PrefAdmin, ConfigurationPlugin {

	
	private LogService log;
	private MetaTypeService mts;
	private ConfigurationAdmin ca;
	
	private PrefReferenceProcessor prefProcessor;
	private List prefReferencesToBeProcessed = new ArrayList();
	
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
    	System.out.println("PrefAdminImpl activated");
    	
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
    	System.out.println("PrefAdminImpl deactivated");
    }
    
    protected void prefHolderRegistered(ServiceReference prefHolder) {
    	System.out.println("PrefAdminImpl prefHolderRegistered called, with service " + prefHolder.getProperty("service.pid").toString());
    	
    	this.prefReferencesToBeProcessed.add(prefHolder);
    	
    	if (this.hasBeenActivated == true) {
    		this.prefProcessor.processPrefReferences((ServiceReference[]) this.prefReferencesToBeProcessed.toArray(new ServiceReference[0]));
    		this.prefReferencesToBeProcessed.clear();
    	}
    }
    
    protected void prefHolderUnregistered(ServiceReference prefHolder) {
    	//ignore for now
    }

	public void modifyConfiguration(ServiceReference reference,
			Dictionary properties) {
		System.out.println("My my! the Config Dictionary for " + reference.getProperty("service.pid") + "has landed upon my doorstep!");
		//inject global preferences into configuration objects headed for ManagedServices
		System.out.println("  Modifying a configuration, adding global stuff");
		PrefPage[] globalPrefPages = getGlobalPrefPages();
		for (int ii = 0; ii < globalPrefPages.length; ii++) {
			PrefPage globalPrefPage = globalPrefPages[ii];
			Configuration globalPrefConf = globalPrefPage.getPrefConf();
			
			String namespace = globalPrefConf.getPid();
			
			Dictionary globalPrefDict = globalPrefConf.getProperties();
			
			//the keys of each dictionary are the ids of global preference OCDs.
			Enumeration ids = globalPrefDict.keys();
			while (ids.hasMoreElements()) {
				String id = (String) ids.nextElement();
				String value = (String) globalPrefDict.get(id);
			
				String keyForConfiguration = namespace + "." + id;
				
				System.out.println("Adding " + keyForConfiguration + ": " + value);
				properties.put(keyForConfiguration, value);
			}
		}
	}

	public void configurationEvent(ConfigurationEvent event) {
		System.out.println("EVENT: " + event.toString());
		if (event.getType() == event.CM_UPDATED) {
			System.out.println("Updated!");
		} else if (event.getType() == event.CM_DELETED) {
			System.out.println("Deleted!");
		}
		
		System.out.println("PID is: " + event.getPid());
	}
}