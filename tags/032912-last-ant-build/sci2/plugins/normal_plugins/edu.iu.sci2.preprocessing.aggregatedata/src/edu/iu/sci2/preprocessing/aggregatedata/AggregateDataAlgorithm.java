package edu.iu.sci2.preprocessing.aggregatedata;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

/**
 * This algorithm aggregates/collapses the input table based off on values in a 
 * "Aggregated On" column provided by the user. The types of aggregation performed
 * on each column can be selected by the user from a drop-down box. 
 * Currently "Sum", "Difference", "Average", "Min", "Max" aggregations are available 
 * for numerical column types.
 * Non-numerical column types are treated as String and hence a user can select 
 * appropriate text delimiters for each. 
 * 
 * @author cdtank
 *
 */

public class AggregateDataAlgorithm implements Algorithm {
	
	private Data[] data;
    private Dictionary<String, Object> parameters;
	private LogService logger;
	private List<Integer> tableColumnNumericalParameterIDs, tableColumnStringParameterIDs;
	
    public static final String AGGREGATE_ON_COLUMN = "aggregateoncolumn";
    
    public AggregateDataAlgorithm(Data[] data, Dictionary<String, Object> parameters,
								  CIShellContext context, 
								  List<Integer> inputNumericalParameterIDs, 
								  List<Integer> inputStringParameterIDs) {
    	
        this.data = data;
        this.parameters = parameters;
        this.tableColumnNumericalParameterIDs = inputNumericalParameterIDs;
        this.tableColumnStringParameterIDs = inputStringParameterIDs;
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

    public Data[] execute() throws AlgorithmExecutionException {

		String aggregateOnColumnName = (String) parameters.get(AGGREGATE_ON_COLUMN);
		
		Table originalInputTable = (Table) this.data[0].getData();
		

		
		Map<Integer, String> columnNumberToAggregationType = new HashMap<Integer, String>();
		
		/*
		 * Side-effects columnNumberToAggregationType by handling all the numerical columns.
		 * */
		initializeNumericalAggregationParameters(columnNumberToAggregationType);
		
		/*
		 * Side-effects columnNumberToAggregationType by handling all the string columns.
		 * */
		initializeStringAggregationParameters(columnNumberToAggregationType);

		/*
		 * After getting the Prefuse Table data pass it for aggregation.
		 * */
		AggregateDataComputation aggregationComputation = 
			new AggregateDataComputation(aggregateOnColumnName,
										 columnNumberToAggregationType,
										 originalInputTable,
										 tableColumnNumericalParameterIDs,
										 tableColumnStringParameterIDs,
										 logger);
		
		/*
		 * After getting the output in table format make it available to the user.
		 * */
		Data output = new BasicData(aggregationComputation.getOutputTable(), Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "Aggregation performed using unique values in \'" 
										 + aggregateOnColumnName 
										 + "\' column.");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[]{output};
    }

	/**
	 * @param columnNumberToAggregationType
	 */
	private void initializeStringAggregationParameters(
			Map<Integer, String> columnNumberToAggregationType) {
		String aggregationType;
		/*
		 * associate each numeric column with the type of aggregation the user wants to perform
		 * on it (sum, count, etc...).
		 * */
		for (Integer currentColumnNumber : tableColumnStringParameterIDs) {
			aggregationType = (String) parameters.get(currentColumnNumber.toString());
			columnNumberToAggregationType.put(currentColumnNumber, aggregationType);
		}
	}

	/**
	 * @param columnNumberToAggregationType
	 */
	private void initializeNumericalAggregationParameters(
			Map<Integer, String> columnNumberToAggregationType) {
		String aggregationType;
		/*
		 * associate each numeric column with the type of aggregation the user wants to perform
		 * on it (sum, count, etc...).
		 */
		for (Integer currentColumnNumber : tableColumnNumericalParameterIDs) {
			aggregationType = (String) parameters.get(currentColumnNumber.toString());
			columnNumberToAggregationType.put(currentColumnNumber, aggregationType);
		}
	}
}