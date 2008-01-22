package org.cishell.service.prefadmin.internal;

import org.cishell.service.prefadmin.PrefHolder;
import org.cishell.service.prefadmin.PrefPage;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;


public class PrefHolderImpl implements PrefHolder {
	    private String servicePID; //servicePID of pref holding service
	    	
	    private PrefPage localPrefPage;
	    private PrefPage[] globalPrefPages;	  
	   
//	    public PrefHolderImpl(final ConfigurationAdmin ca, ServiceReference prefHolderReference) {
//	    	this.localPrefPage = fillLocalPrefPage(ca, prefHolderReference);
//	    	this.globalPrefPages = fillGlobalPrefPages(ca, prefHolderReference);
//	    }
//	    
//	    private PrefPage fillLocalPrefPage(final ConfigurationAdmin ca, ServiceReference prefHolderReference) {
//	    	//Configuration PID for local preferences is the same as the services service.pid 
//	    	//this means that the configuration object for the local preferences
//	    	//will be sent to that service.
//	    	
//	    	if (prefHolderReference.getProperty("service.pid") != null) {
//	    		String configurationPID = (String) prefHolderReference.getProperty("service.pid");
//	    		ca.getConfiguration(configurationPID, null);
//	    	} else {
//	    		
//	    	}
//	    	
//	    }
	    
	    public String getServicePID() {
	    	return this.servicePID;
	    }

	    public PrefPage getLocalPrefPage() {
	    	return this.localPrefPage;
	    }
	    
	    public PrefPage[] getGlobalPrefPages() {
	    	return this.globalPrefPages; 
	    }
}
