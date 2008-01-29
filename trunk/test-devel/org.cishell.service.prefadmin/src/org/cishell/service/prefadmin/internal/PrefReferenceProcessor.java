package org.cishell.service.prefadmin.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.service.prefadmin.PrefPage;
import org.cishell.service.prefadmin.PreferenceAD;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.cishell.service.prefadmin.shouldbeelsewhere.PreferenceOCDImpl;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeInformation;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PrefReferenceProcessor {
	
	
	private static final String LOCAL_PREFS_SUFFIX = ".prefs";
	private static final String GLOBAL_PREFS_SUFFIX = ".global_prefs"; //with or without digit ( >= 2) on end
	
	private LogService log;
	private MetaTypeService mts;
	private ConfigurationAdmin ca;
	
	private List localPrefPages = new ArrayList();
	private List globalPrefPages = new ArrayList();
	
	
	//level 0 methods
	
	public PrefReferenceProcessor(LogService log, MetaTypeService mts, ConfigurationAdmin ca) {
		this.log = log;
		this.mts = mts;
		this.ca = ca;
	}
	
    public void processPrefReferences(ServiceReference[] prefReferences) {

    	//for each service that purports to hold preference information...
    	for (int ii = 0; ii < prefReferences.length; ii++) {
    		ServiceReference prefReference = prefReferences[ii];
    		System.out.println("Processing service reference " + prefReference.getProperty("service.pid"));
    		//get all the OCD PIDs belonging to this service (from the bundle's METADATA.xml file)
    		String[] ocdPIDs = getOCDPIDs(prefReference);
    		
    		PrefPage localPrefPage = getLocalPrefPage(prefReference, ocdPIDs);
    		if (localPrefPage != null) {
    			System.out.println("  Found local preference page");
    			initializeConfiguration(localPrefPage);
    			this.localPrefPages.add(localPrefPage);
    		}
    		
    		PrefPage[] globalPrefPages = getGlobalPrefPages(prefReference, ocdPIDs);
    		if (globalPrefPages != null) {
    			System.out.println("  Found global preference pages");
    			initializeConfigurations(globalPrefPages);
    			Collections.addAll(this.globalPrefPages, globalPrefPages);
    		}
    	}	
    }
    
    public PrefPage[] getLocalPrefPages() {
    	return (PrefPage[]) this.localPrefPages.toArray(new PrefPage[0]);
    }
    
    public PrefPage[] getGlobalPrefPages() {
    	return (PrefPage[]) this.globalPrefPages.toArray(new PrefPage[0]);
    }
    
    //level 1 methods
   
    private String[] getOCDPIDs(ServiceReference prefReference) {
    	Bundle bundle = prefReference.getBundle();
    	MetaTypeInformation metaTypeInfo = mts.getMetaTypeInformation(bundle);
    	String[] allPIDs = metaTypeInfo.getPids();
    	
    	String servicePID = (String) prefReference.getProperty("service.pid");
    	
    	List pidsBelongingToThisService = new ArrayList();
    	for (int ii = 0; ii < allPIDs.length; ii++) {
    		String pid = allPIDs[ii];
    		
    		if (pid.startsWith(servicePID)) {
    			pidsBelongingToThisService.add(pid);
    		}
    	}
    	
    	String[] pidsBelongingToThisServiceArray = (String[]) pidsBelongingToThisService.toArray(new String[0]);
    	return pidsBelongingToThisServiceArray;
    }
    
    
    private PrefPage getLocalPrefPage(ServiceReference prefReference,
			String[] ocdPIDs) {
		// collect all the PIDs that represent local preferences.
		String[] localPIDs = filterLocalPIDs(ocdPIDs);

		String localPID = null;

		// if there is exactly one local preference PID
		if (localPIDs.length == 1) {
			// this is the expected case. Keep track of it.
			localPID = localPIDs[0];
		}
		// if there is more than local preference PID...
		else if (localPIDs.length > 1) {
			// this should not happen, but we will deal with it
			// choose the first one, and write a warning to the console
			localPID = localPIDs[0];
			this.log
					.log(
							LogService.LOG_WARNING,
							"More than one local preference provided by service "
									+ prefReference.getProperty("service.pid")
									+ ". Choosing the first local preference, and ignoring the others.");
		} // if there are no preference PIDs...
		else if (localPIDs.length == 0) {
			// this is okay. This service probably has global preferences, and
			// is thus not wasting our time.
		}

		PrefPage localPrefPage = null;
		// if we now have a single preference PID...
		if (localPID != null) {
			// get the corresponding OCD object
			PreferenceOCD prefOCD = getLocalPrefOCD(prefReference);
			// get the corresponding Configuration object
			Configuration prefConf = getLocalPrefConf(prefReference);
			// make a PreferencePage out of these two
			if (prefConf != null) {
				localPrefPage = new PrefPageImpl(prefConf, prefOCD);
			}
		} // if we have no preference PIDs...
		else {
			// leave our reference as null
			localPrefPage = null;
		}

		return localPrefPage;
	}
    
    private void initializeConfiguration(PrefPage prefPage) {
    	Configuration prefConf = prefPage.getPrefConf();
    	Dictionary prefDict = prefConf.getProperties();
    	PreferenceOCD prefOCD = prefPage.getPrefOCD();

    	
		//if there are no properties defined for this prefPages configuration...
    	if (prefDict == null) {

    		//create configuration properties for this prePage based on the default values in its OCD.
    		prefDict = new Hashtable();
    		
    		PreferenceAD[] prefADs = prefOCD.getPreferenceAttributeDefinitions(ObjectClassDefinition.ALL);
    		for (int ii = 0; ii < prefADs.length; ii++) {
    			AttributeDefinition prefAD = prefADs[ii];
    			
    			
    			String id = prefAD.getID();
    			String val = prefAD.getDefaultValue()[0];
    			
    			try {
    			prefDict.put(id, val);
    			} catch (Throwable e) {;
    				e.printStackTrace();
    			}
    			
    		}
    		
    		try {
    		prefConf.update(prefDict);
    		} catch (IOException e) {
    			this.log.log(LogService.LOG_ERROR, "Unable to update configuration with PID " + prefConf.getPid(), e);
    		}
    	} else {
    		//update it anyway, because if it is a global conf it needs to be propogated.
    		try {
    			System.out.println("Updating configuration for " + prefConf.getPid() + ", even though nothing changed");
        		prefConf.update(prefDict);
        		} catch (IOException e) {
        			this.log.log(LogService.LOG_ERROR, "Unable to update configuration with PID " + prefConf.getPid(), e);
        		}
    	}
    	
    	//TODO: does not worry about old version of bundles for now
    }
    
    private PrefPage[] getGlobalPrefPages(ServiceReference prefReference, String[] ocdPIDs) {
    	
    	//collect all the PIDs that represent global preferences.
		String[] globalPIDs = filterGlobalPIDs(ocdPIDs);
		
		if (globalPIDs.length > 0) {
			//if there is one or more global preference PID
			//Keep track of them.
		} //if there are no global preference PIDs
		else {
			//this is okay
			//but if there were no local preference PIDs,
			//this service is wasting our time purporting to have preference stuff
				//write a warning
			return null;
		}
		
		List globalPrefPageList = new ArrayList();
		for (int ii = 0; ii < globalPIDs.length; ii++) {
			String globalPID = globalPIDs[ii];
			PreferenceOCD prefOCD = getGlobalPrefOCD(prefReference, globalPID);
			//get the corresponding Configuration object
			Configuration prefConf = getGlobalPrefConf(globalPID);
			//make a PreferencePage out of these two
			globalPrefPageList.add(new PrefPageImpl(prefConf, prefOCD));
		}
		
		PrefPage[] globalPrefPages = (PrefPage[]) globalPrefPageList.toArray(new PrefPage[0]);
		
		return globalPrefPages;
    }
    
    private void initializeConfigurations(PrefPage[] prefPages) 
    {
    	for (int ii = 0; ii < prefPages.length; ii++) {
    		PrefPage prefPage = prefPages[ii];
    		initializeConfiguration(prefPage);
    	}
    }

    //level 2 methods
	   
	    
    private String[] filterLocalPIDs(String[] allPIDs) {
		List localPIDList = new ArrayList();
		for (int ii = 0; ii < allPIDs.length; ii++) {
			String pid = allPIDs[ii];

			if (pid.endsWith(LOCAL_PREFS_SUFFIX)
					&& (!pid.substring(0, pid.length() - 1).endsWith(
							GLOBAL_PREFS_SUFFIX))) {
				System.out.println("    " + pid + " is a local preference PID");
				localPIDList.add(pid);
			}
		}

		String[] localPIDs = (String[]) localPIDList.toArray(new String[0]);
		return localPIDs;
	}

	private PreferenceOCD getLocalPrefOCD(ServiceReference prefReference) {
		Bundle bundle = prefReference.getBundle();
		MetaTypeInformation metatypeInfo = this.mts
				.getMetaTypeInformation(bundle);
		Object result = prefReference.getProperty("service.pid");
		String servicePID = (String) result;
		ObjectClassDefinition ocd = metatypeInfo.getObjectClassDefinition(
				servicePID + LOCAL_PREFS_SUFFIX, null);
		PreferenceOCD prefOCD = new PreferenceOCDImpl(this.log, ocd);
		return prefOCD;
	}

	private Configuration getLocalPrefConf(ServiceReference prefReference) {
		String servicePID = (String) prefReference.getProperty("service.pid");
		try {
			return ca.getConfiguration(servicePID, null);
		} catch (IOException e) {
			this.log.log(LogService.LOG_ERROR,
					"Error occurred while trying to get configuration object for "
							+ servicePID, e);
			return null;
		}
	}
	    
	    
	    
	private String[] filterGlobalPIDs(String[] allPIDs) {
		List globalPIDList = new ArrayList();
		for (int ii = 0; ii < allPIDs.length; ii++) {
			String pid = allPIDs[ii];

			if (pid.substring(0, pid.length() - 1)
					.endsWith(GLOBAL_PREFS_SUFFIX)
					|| pid.endsWith(GLOBAL_PREFS_SUFFIX)) {
				System.out.println("    " + pid + " is a global preference PID");
				globalPIDList.add(pid);
			}
		}

		String[] globalPIDs = (String[]) globalPIDList.toArray(new String[0]);
		return globalPIDs;
	}

	private PreferenceOCD getGlobalPrefOCD(
			ServiceReference prefReference, String globalPID) {
		return new PreferenceOCDImpl(this.log, this.mts.getMetaTypeInformation(prefReference.getBundle())
				.getObjectClassDefinition(globalPID, null));
	}

	private Configuration getGlobalPrefConf(String globalPID) {
		try {
			return ca.getConfiguration(globalPID);
		} catch (IOException e) {
			this.log.log(LogService.LOG_ERROR,
					"Error occurred while trying to get global configuration object "
							+ globalPID, e);
			return null;
		}
	}
}
