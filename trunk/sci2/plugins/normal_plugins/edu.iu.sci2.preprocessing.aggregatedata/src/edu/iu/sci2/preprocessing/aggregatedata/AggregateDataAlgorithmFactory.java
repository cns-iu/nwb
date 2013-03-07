package edu.iu.sci2.preprocessing.aggregatedata;

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
import org.cishell.utilities.TableUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class AggregateDataAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	
	private List<Integer> inputNumericalParameterIDs, inputStringParameterIDs;
	private static final String DEFAULT_TEXT_DELIMITER = "";

	private static final String[] NUMERICAL_AGGREGATION_TYPE_NAMES = {
		"None",
		"Sum",
		"Difference",
		"Average",
		"Min",
		"Max"
	};
	
	private static final String[] NUMERICAL_AGGREGATION_TYPE_VALUES = {
		GlobalConstants.NONE_NUMERICAL_AGGREGATION_TYPE_VALUE,
		GlobalConstants.SUM_AGGREGATION_TYPE_VALUE,
		GlobalConstants.DIFFERENCE_AGGREGATION_TYPE_VALUE,
		GlobalConstants.AVERAGE_AGGREGATION_TYPE_VALUE,
		GlobalConstants.MIN_AGGREGATION_TYPE_VALUE,
		GlobalConstants.MAX_AGGREGATION_TYPE_VALUE
	};
	
	
	public Algorithm createAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		return new AggregateDataAlgorithm(data, 
										  parameters, 
										  context, 
										  inputNumericalParameterIDs, 
										  inputStringParameterIDs);
	}

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		/*
		 * 
		 * 	Fill the 'Aggregated On' parameter drop-down box with column names 
		 * 	from the input table, so the user can choose which column they want 
		 * 	to be considered as the bases for aggregation.
		 *	
		 *	Also assign aggregation types for all the columns (Numerical or otherwise).
		 *	If "NONE" is selected then delete those columns in the final output table.  
		 *  
		 */
		
		Data inData = data[0];
		Table table = (Table) inData.getData();
		
		BasicObjectClassDefinition newParameters;

		try {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters
					.getName(), oldParameters.getDescription(), oldParameters.getIcon(16));
		} catch (IOException e) {
			newParameters = new BasicObjectClassDefinition(oldParameters.getID(), oldParameters
					.getName(), oldParameters.getDescription(), null);
		}

		AttributeDefinition[] oldAttributeDefinitions = oldParameters
				.getAttributeDefinitions(ObjectClassDefinition.ALL);
		
		List<String> allColumnNames = TableUtilities.getAllColumnNames(table.getSchema());
		String[] currentDatasetColumns = allColumnNames.toArray(new String[allColumnNames.size()]);

		/*
		 * Make the drop down boxes appear for Address.
		 * */
		for (AttributeDefinition oldAttributeDefinition : oldAttributeDefinitions) {
			String oldAttributeDefinitionID = oldAttributeDefinition.getID();
			
			/*
			 * For "Aggregated On" column name.
			 * */
			if (oldAttributeDefinitionID.equals(AggregateDataAlgorithm.AGGREGATE_ON_COLUMN)) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(oldAttributeDefinition.getID(), 
								oldAttributeDefinition.getName(), 
								oldAttributeDefinition.getDescription(), 
								oldAttributeDefinition.getType(), 
								currentDatasetColumns, 
								currentDatasetColumns));
			}
		}
		
		
		
		inputNumericalParameterIDs = new ArrayList<Integer>();
		/*
		 * Handle Numerical Columns
		 * */
		String[] numericalColumnNames = TableUtilities.getValidNumberColumnNamesInTable(table);
		for (String currentColumnName : numericalColumnNames) {
			int parameterColumnNumber = table.getColumnNumber(currentColumnName);
			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
					new BasicAttributeDefinition(String.valueOf(parameterColumnNumber), 
												 currentColumnName, 
												 "Aggregate numerical column " 
												 + currentColumnName 
												 + " using following functions.", 
												 AttributeDefinition.STRING, 
												 NUMERICAL_AGGREGATION_TYPE_NAMES, 
												 NUMERICAL_AGGREGATION_TYPE_VALUES));
			inputNumericalParameterIDs.add(parameterColumnNumber);
		}
		
		
		inputStringParameterIDs = new ArrayList<Integer>();
		/*
		 * Handle Non-numerical Columns
		 * */
		for (String currentColumnName : currentDatasetColumns) {
			
			/*
			 * Used to filter out all the numerical columns by checking the column type against
			 * a pre-built set of all the Numerical class types. 
			 * */
			if (!GlobalConstants.NUMBER_CLASS_TYPES
					.contains(table.getColumnType(currentColumnName))) {
				int parameterColumnNumber = table.getColumnNumber(currentColumnName);
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(String.valueOf(parameterColumnNumber), 
													 "Delimiter for " + currentColumnName, 
													 "Aggregate string column " 
													 + currentColumnName 
													 + " using following Text Delimiter", 
													 AttributeDefinition.STRING, 
													 DEFAULT_TEXT_DELIMITER));
				inputStringParameterIDs.add(parameterColumnNumber);
			}
		}
		return newParameters;
	}

}