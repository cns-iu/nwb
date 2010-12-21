package edu.iu.nwb.preprocessing.text.normalization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.net.URL;
import java.net.URLConnection;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;


public class StandardNormalyzerFactory implements AlgorithmFactory, ParameterMutator {

    protected static final String PREFIX = "column_";
    private static final String stopWordsFilePath = 
		"/edu/iu/nwb/preprocessing/text/normalization/stopwords.txt";
    
    private BundleContext bContext;
    private String[] stopWords = null;
  
    protected void activate(ComponentContext ctxt) {
    	bContext = ctxt.getBundleContext();
    }
    
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, 
			 CIShellContext context) {
		URL  filePath = bContext.getBundle().getResource(stopWordsFilePath);
    	stopWords = getStopWords (filePath);
		return new StandardNormalyzer(data, parameters, context, stopWords);
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table t = (Table) data[0].getData();

		ObjectClassDefinition oldDefinition = parameters;
		String[] columnNames = createKeyArray(t.getSchema());
		
		return addBooleanOptions(oldDefinition, columnNames, PREFIX);
	}

	private ObjectClassDefinition addBooleanOptions(
			ObjectClassDefinition oldDefinition, String[] columnNames, String prefix) {
		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}
		
		AttributeDefinition[] attributeDefinitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < attributeDefinitions.length; ii++) {
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attributeDefinitions[ii]);
		}

		for(int ii = 0; ii < columnNames.length; ii++) {
			
			String name = columnNames[ii];
			definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
				new BasicAttributeDefinition(prefix + name, name, "Normalize column " + name + "?", AttributeDefinition.BOOLEAN));
			
		}

		
		return definition;
	}
	
	private String[] createKeyArray(Schema schema) {
		List keys = new ArrayList();

		for(int ii = 0; ii < schema.getColumnCount(); ii++) {
			if(schema.getColumnType(ii).equals(String.class)) {
				keys.add(schema.getColumnName(ii));
			}
		}

		return (String[]) keys.toArray(new String[]{});
	}
	
    private String[] getStopWords(URL filePathURL) {
    	
    	InputStream inStream = null;
    	BufferedReader input = null;
    	String line;
    	ArrayList list = new ArrayList();
    	String[]stopWords = null;

    	try {
             URLConnection connection = filePathURL.openConnection();
             connection.setDoInput(true);
             inStream = connection.getInputStream();
             input = new BufferedReader(new InputStreamReader(inStream, "UTF-8"));
    		    		
    	    while (null != (line = input.readLine())) {
    	         list.add(line);
    	    }
        	stopWords = new String[list.size()];
        	for (int ii=0; ii<list.size(); ii++){
        		stopWords[ii] = (String) list.get(ii);
        		System.out.println(">>Debug: index = "+ii+", value = "+stopWords[ii]);
        	}
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			if (input != null) input.close();
    	        if (inStream != null) inStream.close();
    	    }
    	    catch (IOException e) {
    	        e.printStackTrace();
    	    }
    	}
    	return stopWords;
    	
    }  	

   	

}