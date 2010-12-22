package edu.iu.sci2.database.star.common.parameter;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.data.Data;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.common.StarDatabaseCSVDataValidationRules;
import edu.iu.sci2.database.star.common.utility.CSVReaderUtilities;

public class ColumnDescriptorFactory {
	public static Map<String, ColumnDescriptor> createColumnDescriptorsByHumanReadableName(
			Data data, Dictionary<String, Object> parameters)
			throws InvalidDerbyFieldTypeException, IOException {
		Map<String, ColumnDescriptor> columnDescriptors = new HashMap<String, ColumnDescriptor>();
		String[] header = CSVReaderUtilities.getHeader(data);

		for (int i = 0; i < header.length; i++) {
			columnDescriptors.put(header[i], createColumnDescriptor(header, i, parameters));
		}

		return columnDescriptors;
	}

	public static ColumnDescriptor createColumnDescriptor(
			String[] header, int columnIndex, Dictionary<String, Object> parameters)
			throws InvalidDerbyFieldTypeException {
		String columnName = header[columnIndex];
		String databaseName = StarDatabaseCSVDataValidationRules.normalizeName(columnName);

		String typeID = ParameterDescriptors.Type.id(columnName);
		DerbyFieldType type = DerbyFieldType.getFieldTypeByHumanReadableName(
			(String)parameters.get(typeID));

		String isMultiValuedID = ParameterDescriptors.SeparateEntity.id(columnName);
		Boolean isMultiValued =
			(Boolean)parameters.get(isMultiValuedID);

		String shouldMergeIdenticalValuesID =
			ParameterDescriptors.MergeIdentical.id(columnName);
		Boolean shouldMergeIdenticalValues =
			(Boolean)parameters.get(shouldMergeIdenticalValuesID);

		String separatorID = ParameterDescriptors.Separator.id(columnName);
		String separator = ((String) parameters.get(separatorID)).trim();

		return new ColumnDescriptor(
			columnIndex,
			columnName,
			databaseName,
			type,
			!isMultiValued.booleanValue(),
			isMultiValued.booleanValue(),
			shouldMergeIdenticalValues.booleanValue(),
			separator);
	}

	public static Map<String, ColumnDescriptor>
			mapColumnDescriptorHumanReadableNamesToColumnDescriptors(
    			Collection<ColumnDescriptor> columnDescriptors) {
    	Map<String, ColumnDescriptor> namesToDescriptors =
    		new HashMap<String, ColumnDescriptor>();

    	for (ColumnDescriptor columnDescriptor : columnDescriptors) {
    		namesToDescriptors.put(columnDescriptor.getName(), columnDescriptor);
    	}

    	return namesToDescriptors;
    }

	public static Map<String, ColumnDescriptor>
			mapColumnDescriptorDatabaseNamesToColumnDescriptors(
    			Collection<ColumnDescriptor> columnDescriptors) {
    	Map<String, ColumnDescriptor> namesToDescriptors =
    		new HashMap<String, ColumnDescriptor>();

    	for (ColumnDescriptor columnDescriptor : columnDescriptors) {
    		namesToDescriptors.put(columnDescriptor.getNameForDatabase(), columnDescriptor);
    	}

    	return namesToDescriptors;
    }
}