package edu.iu.cns.database.extract.generic;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.service.component.ComponentContext;

public class ExtractGraphWithQueriesFactory implements AlgorithmFactory {
	
	protected static final String NODE_QUERY_PROPERTY = "node_query";
    protected static final String EDGE_QUERY_PROPERTY = "edge_query";
    protected static final String ID_COLUMN_PROPERTY = "id_column";
    protected static final String SOURCE_COLUMN_PROPERTY = "source_column";
    protected static final String TARGET_COLUMN_PROPERTY = "target_column";
    protected static final Object DIRECTED_PROPERTY = "directed";
	private static final String LABEL_PROPERTY = "output_label";

	private static AlgorithmFactory extractFactory = new ExtractGraphFactory();
	
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
	
    protected String nodeQuery() {
    	return null;
    }
    protected String nodeQuery(String nodeQueryFromPropertiesFile) {
    	String nodeQuery = nodeQuery();
    	if (nodeQuery == null) {
    		return nodeQueryFromPropertiesFile;
    	} else {
    		return nodeQuery;
    	}
    }
    
    protected String edgeQuery() {
    	return null;
    }
    protected String edgeQuery(String edgeQueryFromPropertiesFile) {
    	String edgeQuery = edgeQuery();
    	if (edgeQuery == null) {
    		return edgeQueryFromPropertiesFile;
    	} else {
    		return edgeQuery;
    	}
    }
    
    protected String idColumn() {
    	return null;
    }
    protected String idColumn(String idColumnFromPropertiesFile) {
    	String idColumn = idColumn();
    	if (idColumn == null) {
    		return idColumnFromPropertiesFile;
    	} else {
    		return idColumn;
    	}
    }
    
    protected String sourceColumn() {
    	return null;
    }
    protected String sourceColumn(String idColumnFromPropertiesFile) {
    	String sourceColumn = sourceColumn();
    	if (sourceColumn == null) {
    		return idColumnFromPropertiesFile;
    	} else {
    		return sourceColumn;
    	}
    }
    
    protected String targetColumn() {
    	return null;
    }
    protected String targetColumn(String idColumnFromPropertiesFile) {
    	String targetColumn = targetColumn();
    	if (targetColumn == null) {
    		return idColumnFromPropertiesFile;
    	} else {
    		return targetColumn;
    	}
    }
    
    protected Boolean directed() {
    	return null;
    }
    protected Boolean directed(Boolean directedFromPropertiesFile) {
    	Boolean directed = directed();
    	if (directed == null) {
    		return directedFromPropertiesFile;
    	} else {
    		return directed;
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
    	
    	String isThisTheLabel = (String) propertiesFromFile.get(LABEL_PROPERTY);
    	
    	addIfNotNull(parameters, ExtractGraphFactory.NODE_QUERY_KEY, nodeQuery((String) propertiesFromFile.get(NODE_QUERY_PROPERTY)));
    	addIfNotNull(parameters, ExtractGraphFactory.EDGE_QUERY_KEY, edgeQuery((String) propertiesFromFile.get(EDGE_QUERY_PROPERTY)));
    	addIfNotNull(parameters, ExtractGraphFactory.ID_COLUMN_KEY, idColumn((String) propertiesFromFile.get(ID_COLUMN_PROPERTY)));
    	addIfNotNull(parameters, ExtractGraphFactory.SOURCE_COLUMN_KEY, sourceColumn((String) propertiesFromFile.get(SOURCE_COLUMN_PROPERTY)));
    	addIfNotNull(parameters, ExtractGraphFactory.TARGET_COLUMN_KEY, targetColumn((String) propertiesFromFile.get(TARGET_COLUMN_PROPERTY)));
    	addIfNotNull(parameters, ExtractGraphFactory.DIRECTED_KEY, directed((new Boolean((String)propertiesFromFile.get(DIRECTED_PROPERTY)))));
    	addIfNotNull(parameters, ExtractGraphFactory.LABEL_KEY, label((String) propertiesFromFile.get(LABEL_PROPERTY)));
    	parameters.put(ExtractGraphFactory.CUSTOM_KEY, "not custom"); //indicates this is not a custom graph (though the value is arbitrary)
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