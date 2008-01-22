package org.cishell.testalgorithm2;

import java.util.Dictionary;
import java.util.Enumeration;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;
import org.osgi.service.metatype.ObjectClassDefinition;


public class TestAlgorithm2Factory implements AlgorithmFactory, ManagedService, MetaTypeProvider {
    private MetaTypeProvider provider;

    private BundleContext bContext;
    private ConfigurationAdmin configAdmin;
    private Dictionary properties;
    
    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle()); 
        
        System.out.println("TestAlgorithm2Factory activate method:");
        System.out.println("  Keys and values in ComponentContext properties dictionary:");
        
        Dictionary properties = ctxt.getProperties();
        
        Enumeration propertiesKeys = properties.keys();
		
		while (propertiesKeys.hasMoreElements()) {
			String propertiesKey = (String) propertiesKeys.nextElement();
			
			Object propertiesValue = properties.get(propertiesKey);
			
			System.out.println("    " + propertiesKey + ": " + propertiesValue.toString());
		}
		
		this.bContext = ctxt.getBundleContext();
		this.configAdmin = (ConfigurationAdmin) ctxt.locateService("CM");
    }
    
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
    	System.out.println("TestAlgorithm2Factory createAlgorithm method:");
    	System.out.println("  Keys and values in parameters dictionary:");
        
    	//not renaming everything parametersX for sake of ease
    	
    	Enumeration propertiesKeys = parameters.keys();
		
		while (propertiesKeys.hasMoreElements()) {
			String propertiesKey = (String) propertiesKeys.nextElement();
			
			Object propertiesValue = parameters.get(propertiesKey);
			
			System.out.println("    " + propertiesKey + ": " + propertiesValue.toString());
		}
		
		System.out.println("Here's the lowdown on the properties while we're at it");
		
		printProperties();
    	
    	return new TestAlgorithm2(data, parameters, context, this.bContext, this.configAdmin);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
	public void updated(Dictionary properties) throws ConfigurationException {
		System.out.println("^^^^TestAlgorithm2Factory updated method^^^^:");
		
	
		
		this.properties = properties;
		
		printProperties();
	}
	
	private void printProperties() {
	System.out.println("  Keys and values in ConfigurationAdmin properties dictionary:");
		
		if (properties == null) {
			System.out.println("    properties was null");
		} else {
			Enumeration propertiesKeys = properties.keys();
			
			while (propertiesKeys.hasMoreElements()) {
				String propertiesKey = (String) propertiesKeys.nextElement();
				
				Object propertiesValue = properties.get(propertiesKey);
				
				System.out.println("    " + propertiesKey + ": " + propertiesValue.toString());
			}
		}
	}

	public String[] getLocales() {
		// TODO Auto-generated method stub
		return null;
	}

	public ObjectClassDefinition getObjectClassDefinition(String id,
			String locale) {
		ObjectClassDefinition ocd = provider.getObjectClassDefinition("org.cishell.testalgorithm2.TestAlgorithm2.prefs", null);
		return ocd;
	}
}