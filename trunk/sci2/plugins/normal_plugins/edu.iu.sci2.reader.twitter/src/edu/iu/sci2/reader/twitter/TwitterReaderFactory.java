package edu.iu.sci2.reader.twitter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class TwitterReaderFactory implements AlgorithmFactory, ParameterMutator {
    public static final String DEFAULT_TWITTER_AUTH_FILE_NAME = "twitterAuth.PROPERTIES";

	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
				String filePath = (String) parameters.get("authData");
		return new TwitterReader(data, parameters, filePath, ciShellContext);
	}

	private String getTwitterAuthData(String defaultValue) {
		try {
			URL authDataFileURL = new URL(new URL(
					System.getProperty("osgi.configuration.area")),
					DEFAULT_TWITTER_AUTH_FILE_NAME);
			String filePath = authDataFileURL.getFile();
			
			if (filePath.startsWith("\\") || filePath.startsWith("/")) {
				filePath = filePath.substring(1);
			}

			return filePath;
		} catch (MalformedURLException e) {
			return defaultValue;
		}
	}
	
	
	@Override
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table table = (Table) data[0].getData();
        
        BasicObjectClassDefinition newParameters = new BasicObjectClassDefinition(
            parameters.getID(), parameters.getName(), parameters.getDescription(), null);
        AttributeDefinition[] oldAttributeDefinitions = parameters.getAttributeDefinitions(
            ObjectClassDefinition.ALL);
        String[] columnTitles = 
            TableUtilities.getAllColumnNames(table.getSchema()).toArray(new String[0]);
        for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
            /*
             * Append column titles into drop down boxes and assign to name parameter.
             */
            if (oldAttributeDefinition.getID().equals("uid")) {
                newParameters.addAttributeDefinition(
                        ObjectClassDefinition.REQUIRED,
                        new BasicAttributeDefinition(
                                oldAttributeDefinition.getID(), 
                                oldAttributeDefinition.getName(), 
                                oldAttributeDefinition.getDescription(), 
                                oldAttributeDefinition.getType(), 
                                columnTitles, 
                                columnTitles));
            } else if (oldAttributeDefinition.getID().equals("authData")) {
            	String defaultValue = "file:" + getTwitterAuthData(oldAttributeDefinition.getDefaultValue()[0]);
            		String id = oldAttributeDefinition.getID();
            		String name = oldAttributeDefinition.getName();
    				String description = oldAttributeDefinition.getDescription();
    				int type = oldAttributeDefinition.getType();
    				AttributeDefinition newAttributeDefinition = new BasicAttributeDefinition(
    					id, name, description, type, defaultValue);
    				newParameters.addAttributeDefinition(
    						ObjectClassDefinition.REQUIRED, newAttributeDefinition);    				
            } else {
                newParameters.addAttributeDefinition(
                    ObjectClassDefinition.REQUIRED, oldAttributeDefinition);
            }
        }
        return newParameters;
	}
}
