package org.cishell.testalgorithm2;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

public class TestAlgorithm2 implements Algorithm {
	Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private Dictionary preferences;
    
    public TestAlgorithm2(Data[] data, Dictionary parameters, CIShellContext context, Dictionary preferences) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.preferences = preferences;
    }

    public Data[] execute() {
    	printPreferences(this.preferences);
        return null;
    }
    
    private void printPreferences(Dictionary preferences) {
		System.out.println("  Preferences are as follows:");
		if (preferences == null) {
			System.out.println("    Dictionary is null!");
		} else {
			Enumeration propertiesKeys = preferences.keys();
			
			while (propertiesKeys.hasMoreElements()) {
				String propertiesKey = (String) propertiesKeys.nextElement();
				
				Object propertiesValue = preferences.get(propertiesKey);
				System.out.println("    " + propertiesKey + ":" + propertiesValue);
			}
		}
}
}