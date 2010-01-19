package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;

public class ExtractTableWithQueryFactory implements AlgorithmFactory {
	
	private static final String QUERY_PROPERTY = "query";
	private static final String LABEL_PROPERTY = "output_label";

	private static AlgorithmFactory extractFactory = new ExtractTableFactory();
    
    private Map constructedParameters =  new HashMap();
	
	ComponentContext ctxt;
	
	protected void activate(ComponentContext ctxt) {
		this.ctxt = ctxt;
	}
	
	
    public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		Dictionary propertiesFromFile = ctxt.getProperties();
    	Dictionary constructedParameters = constructParameters(propertiesFromFile);
        return extractFactory.createAlgorithm(data, constructedParameters, context);
    }
	
    protected String query() {
    	return null;
    }
    protected String query(String queryFromPropertiesFile) {
    	String query = query();
    	if (query == null) {
    		return queryFromPropertiesFile;
    	} else {
    		return query;
    	}
    }
    
    protected String label() {
    	return null;
    }
    protected String label(String labelFromPropertiesFile) {
    	String label = label();
    	if (label == null) {
    		return labelFromPropertiesFile;
    	} else {
    		return label;
    	}
    }
    
    /**
     * Allows programmers to either define extraction properties using the .properties file or java methods.
     * Programmers can define properties entirely in the .properties file, entirely programmatically, or using a combination.
     * Programmers can define a property in the .properties file, and then modify it through the Java method.
     * 
     * (partly based on the CIShell MutateParameters pattern.)
     * @param propertiesFromFile a dictionary of query properties specified in the .properties file (may contain null entries)
     * @return the final extraction properties for our query which will be passed to the algorithm.
     */
    private Dictionary constructParameters(Dictionary propertiesFromFile) {
    	Hashtable parameters = new Hashtable();
    	
    	addIfNotNull(parameters, ExtractTableFactory.QUERY_KEY, query((String) propertiesFromFile.get(QUERY_PROPERTY)));
    	addIfNotNull(parameters, ExtractTableFactory.LABEL_KEY, label((String) propertiesFromFile.get(LABEL_PROPERTY)));
    
    	return parameters;
    }
    
    private void addIfNotNull(Dictionary parameters, Object key, Object value) {
    	if (value != null) {
    		parameters.put(key, value);
    	} else {
    		throw new RuntimeException("Programmer error: you must provide a value for '" 
    				+ key + " either in your algorithm's .properties file or by " +
    						"overriding the corresponding method in the AlgorithmFactory");
    	}
    }
}