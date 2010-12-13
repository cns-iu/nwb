package edu.iu.nwb.tools.mergenodes;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Graph;
import prefuse.data.Schema;
import prefuse.data.Table;


public class MergeNodesFactory implements AlgorithmFactory, ParameterMutator {
	public static final String AGGREGATE_FUNCTION_FILE_KEY = "aff";

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
    	String aggregateFunctionFileName = (String) parameters.get(AGGREGATE_FUNCTION_FILE_KEY);
    	Data inputNetworkData = findInputNetwork(data);
    	Graph inputNetwork = (Graph) inputNetworkData.getData();
    	Data inputMergeTableData = findInputMergeTable(data);
    	Table inputMergeTable = (Table) inputMergeTableData.getData();
    	LogService logger = (LogService) context.getService(LogService.class.getName());

    	validateNodeSchemaWithMergeTable(inputNetwork, inputMergeTable, logger);

        return new MergeNodes(
        	inputNetworkData, inputNetwork, inputMergeTable, logger, aggregateFunctionFileName);
    }
    
    public ObjectClassDefinition mutateParameters(
    		Data[] data, ObjectClassDefinition oldParameters) {
		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);

		AttributeDefinition[] oldAttributes =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		for (AttributeDefinition attribute : oldAttributes) {
			String id = attribute.getID();

			if (AGGREGATE_FUNCTION_FILE_KEY.equals(id)) {
				newParameters.addAttributeDefinition(ObjectClassDefinition.OPTIONAL, attribute);
			} else {
				newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, attribute);
			}
		}

		return newParameters;	
    }

    private Data findInputNetwork(Data[] data) {
		if (data[0].getData() instanceof Graph) {
			return data[0];
		} else {
			return data[1];
		}
	}

	private Data findInputMergeTable(Data[] data) {
		if (data[0].getData() instanceof Table) {
			return data[0];
		} else {
			return data[1];
		}
	}

	/*
	 * Except the last two columns in inputMergeTable, the column name and the column data type 
	 * of the first N-2 columns in inputMergeTable must match the node schema of inputNetwork. 
	 * No requirement on keeping the same order. 
	 * Note: that the schema in inputMergeTable could be a subset of the node schema of orgGrap.
	 * Store the column name of the first column in inputMergeTable.
	 * 
	 * If find any additional column in the first N-2 columns in inputMergeTable, 
	 * report errors and return false.
	 */
	private static void validateNodeSchemaWithMergeTable(
			Graph inputGraph, Table inputMergeTable, LogService logger)
			throws AlgorithmCreationFailedException {
		Table nodeTable = inputGraph.getNodeTable();
		Schema nodeSchema = nodeTable.getSchema();
		Schema mergeTableSchema = inputMergeTable.getSchema();

		validateMergeTableColumnCount(mergeTableSchema);
    	validateTheLastColumnAsStar(mergeTableSchema);
    	validateTheSecondToLastColumnAsUniqueID(mergeTableSchema);
    	validateThatSchemasAgree(nodeSchema, mergeTableSchema);
	}

	private static void validateMergeTableColumnCount(Schema mergeTableSchema)
			throws AlgorithmCreationFailedException {
		int mergeTableColumnCount = mergeTableSchema.getColumnCount();

		if (mergeTableColumnCount < 3) {
    		throw new AlgorithmCreationFailedException(
    			"Error, the node list table should contains at least three columns.");
    	}
	}

	/** Check the data type of the last column in the inputMergeTable. (It must be String.)
	 */
	private static void validateTheLastColumnAsStar(Schema mergeTableSchema)
			throws AlgorithmCreationFailedException {
		int mergeTableColumnCount = mergeTableSchema.getColumnCount();
    	String starColumnName =
    		mergeTableSchema.getColumnName(mergeTableColumnCount - 1);
    	String starColumnDataType =
    		mergeTableSchema.getColumnType(mergeTableColumnCount - 1).getName();

    	if (!starColumnDataType.equalsIgnoreCase(String.class.getName())) {
    		String format =
    			"Error, the data type of the last column - %s in the node list table is %s, " +
    			"however the algorithm expects String.";
    		String exceptionMessage = String.format(format, starColumnName);
    		throw new AlgorithmCreationFailedException(exceptionMessage);
    	}
	}

	/** Check the data type of the-second-to-last column in the inputMergeTable.
	 * (It must be Integer.)
	 */
	private static void validateTheSecondToLastColumnAsUniqueID(Schema mergeTableSchema)
			throws AlgorithmCreationFailedException {
		int mergeTableColumnCount = mergeTableSchema.getColumnCount();
    	String indexColumnName = mergeTableSchema.getColumnName(mergeTableColumnCount - 2); 
    	String indexColumnDataType =
    		mergeTableSchema. getColumnType(mergeTableColumnCount - 2).getName();

       	if (!indexColumnDataType.equalsIgnoreCase(Integer.class.getName()) &&
       			!indexColumnDataType.equalsIgnoreCase(int.class.getName())) {
       		String format =
       			"Error, the data type of the second last column - %s in the node list is %s, " +
				"however the algorithm expects int or java.lang.Integer.";
       		String exceptionMessage = String.format(format, indexColumnName, indexColumnDataType);
       		throw new AlgorithmCreationFailedException(exceptionMessage);
    	}
	}

	private static void validateThatSchemasAgree(Schema nodeSchema, Schema mergeTableSchema)
			throws AlgorithmCreationFailedException {
		Map<String, String> nodeAttributeNamesByType = mapColumnNamesToType(nodeSchema, 0);
		Map<String, String> mergeTableColumnNamesByType =
			mapColumnNamesToType(mergeTableSchema, 2);

		if (!nodeAttributeNamesByType.equals(mergeTableColumnNamesByType)) {
			String nodeAttributesForPrinting =
				StringUtilities.mapToString(nodeAttributeNamesByType, ": ", ", ");
			String mergeTableColumnNamesForPrinting =
				StringUtilities.mapToString(mergeTableColumnNamesByType, ": ", ", ");
		}

//		for (int ii = 0; ii < mergeTableColumnCount - 2; ii++) {			
//			String mergeTableColumnName = mergeTableSchema.getColumnName(ii); 
//			String graphLabel = mergeTableColumnName;
//			int colIndex = nodeSchema.getColumnIndex(mergeTableColumnName); 		
//
//			if (colIndex == -1) {
//				graphLabel = mergeTableColumnName.toLowerCase();
//				colIndex = nodeSchema.getColumnIndex(graphLabel);
//			}
//
//			if (colIndex >= 0) {
//				if (nodeSchema.getColumnType(graphLabel).getName().equalsIgnoreCase(
//						mergeTableSchema.getColumnType(mergeTableColumnName).getName())) {
//					if (ii == 0) {
//						// TODO:
////						nodeLabelField = theLabel;
//					}
//				} else {
//					String format =
//						"The data types of %1$s do not match.%n" +
//						"The data type of %1$s in the node list table is %2$s.%n" +
//						"But the data type of %1$s in the node schema of the original input " +
//						"graph is %3$s.%s";
//					String exceptionMessage = String.format(
//						format,
//						mergeTableColumnName,
//						mergeTableSchema.getColumnType(mergeTableColumnName).getName(),
//						nodeSchema.getColumnType(graphLabel).getName());
//					throw new AlgorithmCreationFailedException(exceptionMessage);
//				}
//			} else {
//				String exceptionMessage = String.format(
//					"%s does not exist in the node schema of the original input graph.%n",
//					mergeTableColumnName);
//				throw new AlgorithmCreationFailedException(exceptionMessage);
//			}
//		}
	}

	private static Map<String, String> mapColumnNamesToType(
			Schema schema, int endColumnsToIgnoreCount) {
		Map<String, String> columnNamesToType = new HashMap<String, String>();

		for (int ii = 0; ii < schema.getColumnCount() - endColumnsToIgnoreCount; ii++) {
			String columnName = schema.getColumnName(ii).toLowerCase();
			String columnDataType = schema.getColumnType(ii).getName().toLowerCase();
			columnNamesToType.put(columnName, columnDataType);
		}

		return columnNamesToType;
	}
}



	



    
    

    
   