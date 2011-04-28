package edu.iu.nwb.preprocessing.timeslice;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class SliceFactory implements AlgorithmFactory, ParameterMutator {
    
    public Algorithm 
    createAlgorithm(Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new Slice(data, parameters, context);
    }
    
	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		BasicObjectClassDefinition ocd;
		ocd = new BasicObjectClassDefinition(parameters.getID(), 
											 parameters.getName(), 
											 parameters.getDescription(), 
											 null);
		
		AttributeDefinition[] attributes = 
			parameters.getAttributeDefinitions(ObjectClassDefinition.REQUIRED);
		
		for (int ii = 0; ii < attributes.length; ii++) {
			AttributeDefinition attribute = attributes[ii];
			if ("column".equals(attribute.getID())) {
				String[] columnNames = getColumnNames((Table) data[0].getData());
				ocd.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
										   new BasicAttributeDefinition(attribute.getID(), 
												   						attribute.getName(), 
												   						attribute.getDescription(), 
												   						attribute.getType(), 
												   						columnNames , 
												   						columnNames));
			} else {
				ocd.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
			}
		}
		return ocd;
	}

	private String[] getColumnNames(Table table) {
		String[] names = new String[table.getColumnCount()];
		for (int ii = 0; ii < names.length; ii++) {
			names[ii] = table.getColumnName(ii);
		}
		return names;
	}
}