package edu.iu.nwb.preprocessing.tablefilter;

import java.util.Collection;
import java.util.Dictionary;
import java.util.regex.Pattern;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;


public class FilterFactory implements AlgorithmFactory, ParameterMutator {
	public static final String SEPARATOR_KEY = "separator";
	public static final String COLUMN_KEY = "column";
	public static final String CUTOFF_KEY = "cutoff";

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inputData = data[0];
    	Table inputTable = (Table) inputData.getData();
    	String separator = (String) parameters.get(SEPARATOR_KEY);
    	Pattern pattern = Pattern.compile("\\Q" + separator + "\\E");
    	String column = (String) parameters.get(COLUMN_KEY);
    	int cutoff = ((Integer) parameters.get(CUTOFF_KEY)).intValue();

        return new Filter(inputData, inputTable, pattern, column, cutoff);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		Data inputData = data[0];
		Table inputTable = (Table) inputData.getData();
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);

		for (AttributeDefinition attribute :
				oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL)) {
			if (COLUMN_KEY.equals(attribute.getID())) {
				Collection<String> columnNames =
					TableUtilities.getAllColumnNames(inputTable.getSchema());
				String[] columnNameOptions = columnNames.toArray(new String[0]);
				AttributeDefinition columnAttribute = new BasicAttributeDefinition(
					COLUMN_KEY,
					attribute.getName(),
					attribute.getDescription(),
					attribute.getType(),
					columnNameOptions,
					columnNameOptions);
				newParameters.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, columnAttribute);
			} else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
			}
		}

		return newParameters;
	}
}