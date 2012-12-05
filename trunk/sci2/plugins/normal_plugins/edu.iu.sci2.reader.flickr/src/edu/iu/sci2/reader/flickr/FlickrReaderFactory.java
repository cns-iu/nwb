package edu.iu.sci2.reader.flickr;

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

public class FlickrReaderFactory implements AlgorithmFactory, ParameterMutator {
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary<String, Object> parameters,
    								 CIShellContext ciShellContext) {
        return new FlickrReader(data, parameters, ciShellContext);
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
            } else {
                /* None edit field */
                newParameters.addAttributeDefinition(
                    ObjectClassDefinition.REQUIRED, oldAttributeDefinition);
            }
        }
        return newParameters;
	}
}