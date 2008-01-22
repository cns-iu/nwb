package org.cishell.testalgorithm2;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class TestAlgorithm2 implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    private BundleContext bContext;
    private ConfigurationAdmin configAdmin;
    
    
    public TestAlgorithm2(Data[] data, Dictionary parameters, CIShellContext context,
    		BundleContext bContext, ConfigurationAdmin configAdmin) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.bContext = bContext;
        this.configAdmin = configAdmin;
    }

    public Data[] execute() {
//    	try {
//        Configuration config = 
//        	configAdmin.getConfiguration("org.cishell.testalgorithm2.TestAlgorithm2");
//        
//        Dictionary originalDict = config.getProperties();
//        System.out.println("ORIGINALDICT: " + originalDict.toString());
//        
//        Dictionary newDict = new Hashtable();
//        System.out.println("NEWDICT: " + newDict.toString());
//        newDict.put("New key", "New valuuuuue");
//        config.update(newDict);
//        
//        System.out.println("WHAT COMES OUT!?!?: " + config.getProperties().toString());
//        System.out.println("WHAT COMES OUT VALUES!!!!?: " + config.getProperties().get("New key").toString());
//        
//    	} catch (IOException e) {
//    		e.printStackTrace();
//    	}
//    	
    	return null;        
    }
}