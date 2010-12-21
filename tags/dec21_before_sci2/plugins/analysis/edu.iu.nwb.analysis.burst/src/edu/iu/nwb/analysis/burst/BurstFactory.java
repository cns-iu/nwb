package edu.iu.nwb.analysis.burst;

import java.io.IOException;
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

import prefuse.data.Schema;
import prefuse.data.Table;

public class BurstFactory implements AlgorithmFactory, ParameterMutator {
    protected static final String NO_DOCUMENT_COLUMN = "No document column.";

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        return new Burst(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table t = (Table) data[0].getData();

		ObjectClassDefinition oldDefinition = parameters;

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), oldDefinition.getIcon(16));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(oldDefinition.getID(), oldDefinition.getName(), oldDefinition.getDescription(), null);
		}

		String[] columnNames = createKeyArray(t.getSchema());
		String[] documentColumnNames = addNoColumnOption(columnNames, NO_DOCUMENT_COLUMN);
		

		AttributeDefinition[] definitions = oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for(int ii = 0; ii < definitions.length; ii++) {
			
			AttributeDefinition attribute = definitions[ii];
			String id = attribute.getID();
			if("date".equals(id)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("date", attribute.getName(), attribute.getDescription(), AttributeDefinition.STRING, columnNames, columnNames));
			} else if("text".equals(id)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("text", attribute.getName(), attribute.getDescription(), AttributeDefinition.STRING, columnNames, columnNames));
			} else if("document".equals(id)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition("document", attribute.getName(), attribute.getDescription(), AttributeDefinition.STRING, documentColumnNames, documentColumnNames));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
			}
		}

		
		return definition;
	}
	
	private String[] addNoColumnOption(String[] columnNames, String noColumnDescriptor) {
		String[] withNoColumn = new String[columnNames.length + 1];
		withNoColumn[0] = noColumnDescriptor;
		for(int ii = 0; ii < columnNames.length; ii++) {
			withNoColumn[ii+1] = columnNames[ii];
		}
		return withNoColumn;
	}

	private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount()];

		for(int ii = 0; ii < schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii);
		}

		return keys;
	}
}