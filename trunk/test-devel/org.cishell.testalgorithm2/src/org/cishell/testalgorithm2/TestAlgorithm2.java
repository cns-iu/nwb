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
    private Dictionary prefs;
    
    
    public TestAlgorithm2(Data[] data, Dictionary parameters, CIShellContext context,
    		BundleContext bContext, ConfigurationAdmin configAdmin, Dictionary prefs) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.bContext = bContext;
        this.configAdmin = configAdmin;
        
        this.prefs = prefs;
    }

    public Data[] execute() {
    	System.out.println("stringID is " + prefs.get("stringID"));
    	return null;
    }
}