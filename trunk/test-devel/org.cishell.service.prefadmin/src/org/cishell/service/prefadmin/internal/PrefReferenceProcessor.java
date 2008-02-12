package org.cishell.service.prefadmin.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.framework.preference.PreferenceProperty;
import org.cishell.service.prefadmin.PrefPage;
import org.cishell.service.prefadmin.PreferenceAD;
import org.cishell.service.prefadmin.PreferenceOCD;
import org.cishell.service.prefadmin.PrefsByService;
import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;

public class PrefReferenceProcessor{

	public static final String BUNDLE_VERSION_KEY  = "Bundle-Version";
	private LogService log;
	
	private PrefInfoGrabber prefInfoGrabber;
	
	private List allLocalPrefPages   = new ArrayList();
	private List allGlobalPrefPages = new ArrayList();
	private List allParamPrefPages = new ArrayList();
	
	private List allPrefsByService = new ArrayList();
	
	public PrefReferenceProcessor(LogService log, PrefInfoGrabber prefInfoGrabber) {
		this.log = log;
		
		this.prefInfoGrabber = prefInfoGrabber;
	}
	
    public void processPrefReferences(ServiceReference[] prefReferences) {

    	//for each service that purports to hold preference information...
    	for (int ii = 0; ii < prefReferences.length; ii++) {
    		ServiceReference prefReference = prefReferences[ii];
    		
        	//get all preference pages from this service by type, and save them by type
    		
        	PrefPage[] localPrefPages = null;
        	if(isTurnedOn(prefReference, PreferenceProperty.PUBLISH_LOCAL_PREF_VALUE)) {
        		localPrefPages = getLocalPrefPages(prefReference);
        		initializeConfigurations(localPrefPages);
        		Collections.addAll(this.allLocalPrefPages, localPrefPages);
        	}
        	
        	PrefPage[] globalPrefPages = null;
        	if (isTurnedOn(prefReference, PreferenceProperty.PUBLISH_LOCAL_PREF_VALUE)) {
        	    globalPrefPages = getGlobalPrefPages(prefReference);
        		initializeConfigurations(globalPrefPages);
        		Collections.addAll(this.allGlobalPrefPages, globalPrefPages);
        	}
        	
        	PrefPage[] paramPrefPages = null;
        	if (isTurnedOn(prefReference,PreferenceProperty.PUBLISH_LOCAL_PREF_VALUE)) {
        		paramPrefPages = getParamPrefPages(prefReference);
        		initializeConfigurations(paramPrefPages);
        		Collections.addAll(this.allParamPrefPages, paramPrefPages);
        	}
        	
        	//save all the preferences by service as well
        	
        	PrefsByService allPrefsForThisService = new PrefsByService(prefReference, localPrefPages, globalPrefPages, paramPrefPages);
        	this.allPrefsByService.add(allPrefsForThisService);
        	
        	//make sure that preferences are sent to this service if it wants to see them.
        	
        	if (isTurnedOn(prefReference, PreferenceProperty.RECEIVE_PREFS_KEY)) {
        		prefInfoGrabber.ensurePrefsCanBeSentTo(prefReference);
        	}
    	}
    }
    
    public PrefPage[] getAllLocalPrefPages() {
    	return getPrefPages(allLocalPrefPages);
    }
    
    public PrefPage[] getAllGlobalPrefPages() {
    	return getPrefPages(allGlobalPrefPages);
    }
    
    public PrefPage[] getAllParamPrefPages() {
    	return getPrefPages(allParamPrefPages);
    }
    
    public PrefsByService[] getAllPrefsByService() {
    	return (PrefsByService[]) allPrefsByService.toArray(new PrefsByService[allPrefsByService.size()]);
    }
    
    private PrefPage[] getLocalPrefPages(ServiceReference prefHolder) {
    	PreferenceOCD[] localPrefOCDs = prefInfoGrabber.getLocalPrefOCDs(prefHolder);
    	Configuration[] localPrefConfs = prefInfoGrabber.getLocalPrefConfs(prefHolder, localPrefOCDs);
    	PrefPage[] localPrefPages = composePrefPages(prefHolder, localPrefOCDs, localPrefConfs, PrefPage.LOCAL);
    	return localPrefPages;
    }
    
    private PrefPage[] getGlobalPrefPages(ServiceReference prefHolder) {
    	PreferenceOCD[] globalPrefOCDs = prefInfoGrabber.getGlobalPrefOCDs(prefHolder);
    	Configuration[] globalPrefConfs = prefInfoGrabber.getGlobalPrefConfs(prefHolder, globalPrefOCDs);
    	PrefPage[] globalPrefPages = composePrefPages(prefHolder, globalPrefOCDs, globalPrefConfs, PrefPage.GLOBAL);
    	return globalPrefPages;
    }
    
    private PrefPage[] getParamPrefPages(ServiceReference prefHolder) {
    	PreferenceOCD[] paramPrefOCDs = prefInfoGrabber.getParamPrefOCDs(prefHolder);
    	Configuration[] paramPrefConfs = prefInfoGrabber.getParamPrefConfs(prefHolder, paramPrefOCDs);
    	PrefPage[] paramPrefPages = composePrefPages(prefHolder, paramPrefOCDs, paramPrefConfs, PrefPage.PARAM);
    	return paramPrefPages;
    }
    
    //assumes prefOCD.length == prefConfs.length
    private PrefPage[] composePrefPages(ServiceReference prefHolder, PreferenceOCD[] prefOCDs, Configuration[] prefConfs, int type) {
    	List composedPrefPageList = new ArrayList(prefOCDs.length);
    	for (int ii = 0; ii < prefOCDs.length; ii++) {
    		PrefPage composedPrefPage = new PrefPageImpl(prefHolder, prefOCDs[ii], prefConfs[ii], type);
    		composedPrefPageList.add(composedPrefPage);
    	}
    	
    	return (PrefPage[]) composedPrefPageList.toArray(new PrefPage[composedPrefPageList.size()]);
    }
    
    private void initializeConfiguration(PrefPage prefPage) {
    	Configuration prefConf = prefPage.getPrefConf();
    	Dictionary prefDict = prefConf.getProperties();
    	PreferenceOCD prefOCD = prefPage.getPrefOCD();

		//if there are no properties defined for this prefPages configuration...
    	if (prefDict == null || bundleHasBeenUpdated(prefPage)) {

    		//create configuration properties for this pref based on the default values in its OCD.
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
    		
    		prefDict.put(BUNDLE_VERSION_KEY, getBundleVersion(prefPage));
    		
    		try {
    		prefConf.update(prefDict);
    		} catch (IOException e) {
    			this.log.log(LogService.LOG_ERROR, "Unable to update configuration with PID " + prefConf.getPid(), e);
    		}
    	} else {
    		//update it anyway, because if it is a global conf it needs to be propogated.
    		//(if it is a global preference, this update will be caught by our ConfigurationListener,
    		//and the global preference data will be propagated.
    		try {
        		prefConf.update(prefDict);
        		} catch (IOException e) {
        			this.log.log(LogService.LOG_ERROR, "Unable to update configuration with PID " + prefConf.getPid(), e);
        		}
    	}
    	
    	//TODO: does not worry about old version of bundles for now
    }
    
    private void initializeConfigurations(PrefPage[] prefPages) 
    {
    	for (int ii = 0; ii < prefPages.length; ii++) {
    		PrefPage prefPage = prefPages[ii];
    		initializeConfiguration(prefPage);
    	}
    }
    
    //only supports 3 publish keys and receive_prefs key
    private boolean isTurnedOn(ServiceReference prefReference, String processingKey) {
    	if (processingKey.equals(PreferenceProperty.RECEIVE_PREFS_KEY)) {
    		String receivePrefsValue = (String) prefReference.getProperty(PreferenceProperty.RECEIVE_PREFS_KEY);
    		return receivePrefsValue != null && receivePrefsValue.equals("true");
    	} else {
    		String unparsedPublishedPrefsValues = (String) prefReference.getProperty(PreferenceProperty.PREFS_PUBLISHED_KEY);
    		String[] publishedPrefsValues = unparsedPublishedPrefsValues.split(",");
    		for (int ii = 0; ii < publishedPrefsValues.length; ii++) {
    			if (publishedPrefsValues.equals(processingKey)) {
    				return true;
    			}
    		}
    		String value = (String) prefReference.getProperty(processingKey);
    		return (value != null && value.equals("true"));
    	}
    }
    
    private boolean bundleHasBeenUpdated(PrefPage prefPage) {
    	Bundle b = prefPage.getServiceReference().getBundle();
    	
    	String currentBundleVersion = getBundleVersion(prefPage);
    	
    	Dictionary confDict = prefPage.getPrefConf().getProperties();
    	if (confDict == null) {
    		logBundleWasUpdated(prefPage);
    		return true; //maybe should be false
    	}
    	String savedBundleVersion = (String) b.getHeaders().get(BUNDLE_VERSION_KEY);
    	if (savedBundleVersion == null) {
    		logBundleWasUpdated(prefPage);
    		return true;
    	} 
    	
    	if (currentBundleVersion.equals(savedBundleVersion)) {
    		return false;
    	} else {
    		logBundleWasUpdated(prefPage);
    		return true;
    	}
    }

    private String getBundleVersion(PrefPage prefPage) {
    	Bundle b = prefPage.getServiceReference().getBundle();	
    	String currentBundleVersion = (String) b.getHeaders().get(BUNDLE_VERSION_KEY);
    	return currentBundleVersion;
    }
    
    private void logBundleWasUpdated(PrefPage prefPage) {
    	String servicePID = (String) prefPage.getServiceReference().getProperty(Constants.SERVICE_PID);
    	String prefOCDName = prefPage.getPrefOCD().getName();
    	this.log.log(LogService.LOG_WARNING, "The Bundle for the service " + servicePID +
    			" was updated, so the preferences for " + prefOCDName +
    			" will be set to the new defaults.");
    }
    
    private PrefPage[] getPrefPages(List prefPageList) {
    	return (PrefPage[]) prefPageList.toArray(new PrefPage[prefPageList.size()]);
     }
}
