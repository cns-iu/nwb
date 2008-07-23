package edu.iu.nwb.preprocessing.text.normalization;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Schema;
import prefuse.data.Table;


public class StandardNormalyzerFactory implements AlgorithmFactory, ParameterMutator {

    protected static final String PREFIX = "column_";

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new StandardNormalyzer(data, parameters, context);
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
}