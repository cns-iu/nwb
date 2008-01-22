package org.cishell.testalgorithm4;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;


public class TestAlgorithm4Factory implements AlgorithmFactory, ManagedService {
    private MetaTypeProvider provider;
    private ConfigurationAdmin ca;
    private Dictionary properties;
    private LogService log;
    String pid;

    protected void activate(ComponentContext ctxt) {
        //You may delete all references to metatype service if 
        //your algorithm does not require parameters and return
        //null in the createParameters() method
    	System.out.println("Algorithm 4 started activating");
        MetaTypeService mts = (MetaTypeService)ctxt.locateService("MTS");
        
        this.log = (LogService) ctxt.locateService("LOG");
        this.ca = (ConfigurationAdmin) ctxt.locateService("CA");
        provider = mts.getMetaTypeInformation(ctxt.getBundleContext().getBundle());       
        this.pid = (String) ctxt.getProperties().get("service.pid");
        System.out.println("Algorithm 4 done activating");
    }
    protected void deactivate(ComponentContext ctxt) {
        provider = null;
    }

    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
//    	this.log.log(LogService.LOG_WARNING, "We are probably fucked!");
//    	try {
//    	Configuration conf = ca.getConfiguration(pid, null);
//    	Dictionary dict = conf.getProperties();
//    	if (dict == null) {
//    		dict = new Hashtable();
//    	}
//    	dict.put("YEE", "HAW");
//    	this.properties = dict;
//    	conf.update(dict);
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    		System.out.println("BLURG!");
//    	}
        return new TestAlgorithm4(data, parameters, context);
    }
    public MetaTypeProvider createParameters(Data[] data) {
        return provider;
    }
	public void updated(Dictionary properties) throws ConfigurationException {
		System.out.println("TestAlgorithm4 updated!");
		
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
}