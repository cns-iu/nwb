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
    protected static final String NO_DOCUMENT_COLUMN_VALUE = "No document column.";
    public static final int ICON_SIZE = 16;

	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
        return new Burst(data, parameters, context);
    }

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		Table t = (Table) data[0].getData();

		ObjectClassDefinition oldDefinition = parameters;

		BasicObjectClassDefinition definition;
		try {
			definition = new BasicObjectClassDefinition(
					oldDefinition.getID(), 
					oldDefinition.getName(), 
					oldDefinition.getDescription(), 
					oldDefinition.getIcon(ICON_SIZE));
		} catch (IOException e) {
			definition = new BasicObjectClassDefinition(
					oldDefinition.getID(), 
					oldDefinition.getName(), 
					oldDefinition.getDescription(), 
					null);
		}

		String[] columnValues = createKeyArray(t.getSchema());
		

		AttributeDefinition[] definitions = 
			oldDefinition.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (int ii = 0; ii < definitions.length; ii++) {
			
			AttributeDefinition attribute = definitions[ii];
			String id = attribute.getID();
			if (Constants.DATE_COLUMN.equals(id) || Constants.TEXT_COLUMN.equals(id)) {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								id, attribute.getName(), 
								attribute.getDescription(), 
								attribute.getType(), 
								columnValues, 
								columnValues));
			} else if (Constants.DOCUMENT_COLUMN.equals(id)) {
				columnValues = addNoColumnOption(columnValues, NO_DOCUMENT_COLUMN_VALUE);
				definition.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(
								id, 
								attribute.getName(), 
								attribute.getDescription(), 
								attribute.getType(), 
								columnValues, 
								columnValues));
			} else if (Constants.BATCH_BY_COLUMN.equals(id)) {
				definition.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(
								id, 
								attribute.getName(), 
								attribute.getDescription(), 
								attribute.getType(), 
								Constants.BATCHED_BY_OPTIONS, 
								Constants.BATCHED_BY_OPTIONS));
			} else {
				definition.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
			}
		}

		return definition;
	}
	
	private String[] addNoColumnOption(String[] columnNames, String noColumnDescriptor) {
		String[] withNoColumn = new String[columnNames.length + 1];
		withNoColumn[0] = noColumnDescriptor;
		for (int ii = 0; ii < columnNames.length; ii++) {
			withNoColumn[ii + 1] = columnNames[ii];
		}
		return withNoColumn;
	}

	private String[] createKeyArray(Schema schema) {
		String[] keys = new String[schema.getColumnCount()];

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			keys[ii] = schema.getColumnName(ii);
		}

		return keys;
	}
}