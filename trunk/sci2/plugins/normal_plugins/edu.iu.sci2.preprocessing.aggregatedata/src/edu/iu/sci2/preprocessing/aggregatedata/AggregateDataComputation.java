package edu.iu.sci2.preprocessing.aggregatedata;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.DoubleAverageAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.DoubleDifferenceAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.DoubleSumAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.FloatAverageAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.FloatDifferenceAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.FloatSumAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.IntegerAverageAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.IntegerDifferenceAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.IntegerSumAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.MaxAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.MinAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.NoneNumericalAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.NoneStringAggregator;
import edu.iu.sci2.preprocessing.aggregatedata.aggregators.StringAggregator;

/**
 * 
 * After getting the input file & parameters following algorithm is implemented,
 * 1. 	First identify numerical & string columns in the input table.
 * 2. 	Consider the column on the basis of which other columns have to be aggregated 
 * 		as "Grouped On" column.
 * 3. 	Parse this column and for each unique value associate the respective row number 
 * 		to it. So if there are multiple rows that have the same field value for a column 
 * 		then that particular value will have multiple row numbers associated to it.
 * 4. 	Now iterate over all the rows belonging to a particular column for a specific 
 * 		"Grouped on" field value. While iterating over each row perform appropriate 
 * 		aggregations (numerical or string).
 * 5. 	Populate the output table with the "merged" value for each column.
 * 6. 	For those "Grouped on" field values that have only one row associated with it
 * 		skip the aggregation process since we would not require it & directly populate
 * 		that row in the new/output table.
 * 7. 	Repeat steps from 4 through 6 for each column.
 * 9. 	In the end, delete all the columns which were assigned "NONE" aggregation type or 
 * 		"" text delimiter.
 * 10. 	After completing population of the output table make it available to the user via
 * 		Data Manager.          
 * 
 * @author cdtank
 * 
 */
public class AggregateDataComputation {

	private LogService logger;

	private static final String[] UNIQUE_RECORDS_COUNT_COLUMN_NAME_SUGGESTIONS = {
			"Count", "Aggregated_Count" };

	private String groupedOnColumnName;
	private Table originalTable, outputTable;

	private String outputTableCountColumnName;
	private Map<Integer, String> columnNumberToAggregationType;
	private Map<Integer, SingleFunctionAggregator<?>> columnNumberToAggregationFunction;
	
	private List<Integer> stringAggregationColumnNumbers;
	private List<Integer> numericalAggregationColumnNumbers;

	public AggregateDataComputation(String groupedOnColumnName, 
									Map<Integer, String> columnNumberToAggregationType, 
									Table table, 
									List<Integer> tableColumnNumericalParameterIDs, 
									List<Integer> tableColumnStringParameterIDs, 
									LogService logger) {
		this.groupedOnColumnName = groupedOnColumnName;
		this.columnNumberToAggregationType = columnNumberToAggregationType;
		this.originalTable = table;
		this.numericalAggregationColumnNumbers = tableColumnNumericalParameterIDs;
		this.stringAggregationColumnNumbers = tableColumnStringParameterIDs;
		this.logger = logger;

		processTable();

	}

	/*
	 * The user chooses which column should have its values "grouped together". Let's call this the 
	 * "grouped" column.
	 * If two or more rows have the same value in the grouped column, we consider these rows to be 
	 * duplicates of each other.
	 * Rows with duplicate values in the grouped column will be merged together to form one row.
	 * When we merge duplicate rows together, the values for each column will be aggregated together
	 * according to some function that the user selects (sum, count, etc...) .
     * The resulting merged row will have the original value in its "grouped" column, and 
     * aggregated values for all its other columns.
	 */

	private void processTable() {
		Set<String> columnNumbersToBeRemovedFromOutput = new HashSet<String>();
		
		/*
		 * Create Blank new output table using the schema from the original
		 * table. 
		 */
		outputTable = originalTable.getSchema().instantiate();
		outputTableCountColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), UNIQUE_RECORDS_COUNT_COLUMN_NAME_SUGGESTIONS);
		outputTable.addColumn(outputTableCountColumnName, int.class);

		int groupedOnColumnNumber = originalTable.getColumnNumber(groupedOnColumnName);
		int countColumnNumber = outputTable.getColumnNumber(outputTableCountColumnName);
		
		columnNumberToAggregationFunction =
			createColumnNumberToAggregationFunctionMap(groupedOnColumnNumber);
		
		/*
		 * Group together all rows with the same value in their "grouped" column.
		 * */
		 Map<String, List<Integer>> uniqueAggregatedValueToRowNumbers = 
			 createAggregatedOnValueToRowNumbersMappings(groupedOnColumnNumber);
		
		 int outputTableRowCount = 0;
		 
		 
		/*
		 * Used to filter out those columns on which no aggregation will be performed
		 * as indicated by the user by selection of "NONE" numerical aggregation type
		 * or "" Text delimiter & also the "Grouped On" column.
		 * */
		
		/*
		 * Side-effects,
		 * 		1. columnNumbersToBeRemovedFromOutput - used to track columns to be
		 * 			removed from the output table.
		 * */
		List<Integer> finalNumericalAggregationColumnNumbers = 
			setupNumericalColumnsToBeProcessed(columnNumbersToBeRemovedFromOutput,
											   groupedOnColumnNumber);
		
		/*
		 * Side-effects,
		 * 		1. columnNumbersToBeRemovedFromOutput - used to track columns to be
		 * 			removed from the output table.
		 * */
		List<Integer> finalStringAggregationColumnNumbers = setupStringColumnsToBeProcessed(
			columnNumbersToBeRemovedFromOutput, groupedOnColumnNumber);
		
		/*
		 * for each group of rows...
		 * */
		for (List<Integer> groupedOnValueRowNumbers : uniqueAggregatedValueToRowNumbers.values()) {
			outputTable.addRow();
			/*
			 * Only process those rows which are supposed to be aggregated. i.e. Ignore
			 * "Grouped On" values that have only one row assigned to it.
			 * */
			if (groupedOnValueRowNumbers.size() > 1) {
				/*
				 * "outputTableGroupedOnValue" data type has to be Object since we dont know
				 * in advance if it is going to be a String or Number etc.
				 * */
				Object outputTableGroupedOnValue =
					originalTable.get(groupedOnValueRowNumbers.get(0), groupedOnColumnNumber);
				
				/*
				 * Create a new "merged" row in the output table
				 * Populate the output table. Set the value of each cell in the output table.
				 * */
				populateColumnFields(
					outputTableGroupedOnValue,
					outputTableRowCount,
					finalNumericalAggregationColumnNumbers,
					finalStringAggregationColumnNumbers,
					groupedOnValueRowNumbers);
				
				/*
				 * Set the value of the cell on which table aggregation was based off.  
				 * */
				outputTable.set(outputTableRowCount, 
								groupedOnColumnNumber, 
								outputTableGroupedOnValue);
			} else {
				
				/*
				 * To handle the case where there was no aggregation possible hence directly 
				 * copy off the original table contents.
				 * */
				TableUtilities.copyTableRow(outputTableRowCount, 
											groupedOnValueRowNumbers.get(0), 
											outputTable, 
											originalTable);
			}
			
			/*
			 * Set the value of the Count Column 
			 * */
			outputTable.set(outputTableRowCount, countColumnNumber,
					groupedOnValueRowNumbers.size());
			outputTableRowCount++;
			
		}
		
		logger.log(LogService.LOG_INFO, "Frequency of unique \"" 
										+ groupedOnColumnName 
										+ "\" values added to \""
										+ outputTableCountColumnName + "\" column.");
		
		/*
		 * Side-effects,
		 * 		1. Output table - Columns which had their aggregation type as "None"
		 * 			or their text delimiters as "" are deleted from the output table.
		 * */
		removeNoneAggregationColumns(columnNumbersToBeRemovedFromOutput);
	}

	/**
	 * @param outputTableRowCount
	 * @param finalNumericalAggregationColumnNumbers
	 * @param finalStringAggregationColumnNumbers
	 * @param groupedOnValueRowNumbers
	 */
	private void populateColumnFields(Object groupedOnValue,
			int outputTableRowCount,
			List<Integer> finalNumericalAggregationColumnNumbers,
			List<Integer> finalStringAggregationColumnNumbers,
			List<Integer> groupedOnValueRowNumbers) {
		/*
		 * Side-effects,
		 * 		1. Output table - Only numerical columns for which aggregation types are
		 * 			specified & those that do not belong to "Grouped On" column are 
		 * 			populated.
		 * */
		populateNumericalColumnFields(groupedOnValue,
				outputTableRowCount,
				groupedOnValueRowNumbers,
				finalNumericalAggregationColumnNumbers);
		
		/*
		 * Side-effects,
		 * 		1. Output table - Only string columns for which aggregation types/ text
		 * 			delimiters are specified & those that do not belong to "Grouped On" 
		 * 			column.
		 * */
		populateStringColumnFields(groupedOnValue,
				outputTableRowCount,
				groupedOnValueRowNumbers,
				finalStringAggregationColumnNumbers);
	}

	/**
	 * @param outputTableRowCount
	 * @param groupedOnValueRowNumbers
	 * @param finalStringAggregationColumnNumbers
	 */
	private void populateStringColumnFields(Object groupedOnValue,
			int outputTableRowCount,
			List<Integer> groupedOnValueRowNumbers,
			List<Integer> finalStringAggregationColumnNumbers) {
		for (Integer currentColumnNumber : finalStringAggregationColumnNumbers) {
			
			List<String> cellValuesToBeAggregated = new ArrayList<String>();
			int nullCount = 0;
			
			for (Integer currentRowNumber : groupedOnValueRowNumbers) {
				
				String currentCellContent = originalTable.getString(currentRowNumber, 
																	currentColumnNumber);
				
				
				
				/*
				 * There are cases when the Prefuse CSV reader returns a null value for
				 * empty cells. So to handle this null checking is done. 
				 * */
				if (currentCellContent == null) {
					currentCellContent = "";
				}
				
				if (!currentCellContent.equals("")) {
					cellValuesToBeAggregated.add(currentCellContent);
				} else {
					nullCount++;
				}
			}
			
			@SuppressWarnings("unchecked")
			SingleFunctionAggregator<String> currentColumnAggregatorFunction = 
				(SingleFunctionAggregator<String>)
					columnNumberToAggregationFunction.get(currentColumnNumber);

			// aggregate all the column values into a single value
			StringBuffer outputStringAggegatedValue = new StringBuffer();
			outputStringAggegatedValue.append(
				currentColumnAggregatorFunction.aggregateValue(cellValuesToBeAggregated));
			
			String aggregationType = 
				columnNumberToAggregationType.get(currentColumnNumber);
			
			int currentCellContentLength = outputStringAggegatedValue.length();
			
			//log the error to user
			if (nullCount > 0) {
				if (nullCount == groupedOnValueRowNumbers.size()) {
					/* All rows were skipped */
					logSkippedAll(groupedOnValue, outputTable.getColumnName(currentColumnNumber));
				} else {
					/* Some of the rows were skipped */
					logSkipped(groupedOnValue, outputTable.getColumnName(currentColumnNumber),
							nullCount);
				}
			}
			
			/*
			 * Try to delete trailing text delimiters only if there was any
			 * content in the original table cell to begin with.
			 * */
			if (currentCellContentLength > 0) {
				outputStringAggegatedValue = outputStringAggegatedValue
						.delete(currentCellContentLength
								- aggregationType.length(),
								currentCellContentLength);
			}
			
		    // set the value of our "merged" row for this column to the aggregated value
			outputTable.set(outputTableRowCount, 
							currentColumnNumber,
							outputStringAggegatedValue.toString()); 
			
		}
	}

	/**
	 * @param outputTableRowCount
	 * @param groupedOnValueRowNumbers
	 * @param finalNumericalAggregationColumnNumbers
	 */
	private void populateNumericalColumnFields(
			Object groupedOnValue,
			int outputTableRowCount,
			List<Integer> groupedOnValueRowNumbers,
			List<Integer> finalNumericalAggregationColumnNumbers) {
		for (Integer currentColumnNumber : finalNumericalAggregationColumnNumbers) {
			
			List<Number> cellValuesToBeAggregated = new ArrayList<Number>();
			int nullCount = 0;
			int invalidCount = 0;
			
			for (Integer currentRowNumber : groupedOnValueRowNumbers) {
				try {
					Object cell = originalTable.get(currentRowNumber, currentColumnNumber);
					Number number = NumberUtilities.interpretObjectAsNumber(cell);

					if (number != null) {
						cellValuesToBeAggregated.add(number);
					} else {
						nullCount++;
					}
				} catch (NumberFormatException e) {
					invalidCount++;
				} catch (ParseException e) {
					invalidCount++;
				}
			}
			
			// Log the error to user.
			if (nullCount > 0) {
				if (nullCount == groupedOnValueRowNumbers.size()) {
					/* All rows were skipped */
					logSkippedAll(groupedOnValue, outputTable.getColumnName(currentColumnNumber));
				} else {
					/* Some of the rows were skipped */
					logSkipped(
						groupedOnValue, outputTable.getColumnName(currentColumnNumber), nullCount);
				}
			}

			if (invalidCount > 0) {
				if (invalidCount == groupedOnValueRowNumbers.size()) {
					/* All rows were skipped */
					logSkippedAll(groupedOnValue, outputTable.getColumnName(currentColumnNumber));
				} else {
					/* Some of the rows were skipped */
					logSkipped(
						groupedOnValue,
						outputTable.getColumnName(currentColumnNumber),
						invalidCount);
				}
			}
			
			// aggregate all the column values into a single value
			@SuppressWarnings("unchecked")
			SingleFunctionAggregator<Number> currentColumnAggregatorFunction = 
				(SingleFunctionAggregator<Number>) 
					columnNumberToAggregationFunction.get(currentColumnNumber);
			
		    // set the value of our "merged" row for this column to the aggregated value
			if (!cellValuesToBeAggregated.isEmpty()) {
				Object aggregatedValue =
					currentColumnAggregatorFunction.aggregateValue(cellValuesToBeAggregated);

				if (outputTable.getColumnType(currentColumnNumber).isArray()) {
					if (aggregatedValue != null) {
						Object array = Array.newInstance(
							outputTable.getColumnType(currentColumnNumber).getComponentType(), 1);
						Array.set(array, 0, aggregatedValue);

						outputTable.set(
							outputTableRowCount, currentColumnNumber, array);
					} else {
						Object array = Array.newInstance(
							outputTable.getColumnType(currentColumnNumber).getComponentType(), 0);

						outputTable.set(
							outputTableRowCount, currentColumnNumber, array);
					}
				} else {
					outputTable.set(
						outputTableRowCount, currentColumnNumber, aggregatedValue);
				}
			}
			
		}
	}

	/**
	 * @param groupedOnColumnNumber 
	 * @param columnNumberToAggregationFunction
	 */
	private Map<Integer, SingleFunctionAggregator<?>> createColumnNumberToAggregationFunctionMap(
			int groupedOnColumnNumber) {
		Map<Integer, SingleFunctionAggregator<?>> columnNumberToAggregationFunction = 
			new HashMap<Integer, SingleFunctionAggregator<?>>();

		for (Integer currentColumnNumber : numericalAggregationColumnNumbers) {
			String aggregationType = columnNumberToAggregationType.get(currentColumnNumber);
			Class<?> currentColumnClass = originalTable.getColumnType(currentColumnNumber);

			if (currentColumnNumber.intValue() == groupedOnColumnNumber) {
				columnNumberToAggregationFunction.put(
					currentColumnNumber, new NoneNumericalAggregator());
			} else if (GlobalConstants.NONE_NUMERICAL_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
					aggregationType)) {
				columnNumberToAggregationFunction.put(
					currentColumnNumber, new NoneNumericalAggregator());
			} else if (GlobalConstants.MIN_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
					aggregationType)) {
				columnNumberToAggregationFunction.put(currentColumnNumber, new MinAggregator());
			} else if (GlobalConstants.MAX_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
					aggregationType)) {
				columnNumberToAggregationFunction.put(currentColumnNumber, new MaxAggregator());
			} else if (GlobalConstants.INTEGER_CLASS_TYPES.contains(currentColumnClass)) {
				/*
				 * Handle the cases when the aggregation is to be performed on an integer column.
				 */

				if (GlobalConstants.SUM_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new IntegerSumAggregator());
				} else if (GlobalConstants.DIFFERENCE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new IntegerDifferenceAggregator());
				} else if (GlobalConstants.AVERAGE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new IntegerAverageAggregator());
				} 
			} else if (GlobalConstants.FLOAT_CLASS_TYPES.contains(currentColumnClass)) {
				/*
				 * Handle the cases when the aggregation is to be performed on an float column. 
				 */

				if (GlobalConstants.SUM_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new FloatSumAggregator());
				} else if (GlobalConstants.DIFFERENCE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new FloatDifferenceAggregator());
				} else if (GlobalConstants.AVERAGE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new FloatAverageAggregator());
				}
			} else if (GlobalConstants.DOUBLE_CLASS_TYPES.contains(currentColumnClass)) {
				/*
				 * Handle the cases when the aggregation is to be performed on an double column. 
				 */

				if (GlobalConstants.SUM_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new DoubleSumAggregator());
				} else if (GlobalConstants.DIFFERENCE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new DoubleDifferenceAggregator());
				} else if (GlobalConstants.AVERAGE_AGGREGATION_TYPE_VALUE.equalsIgnoreCase(
						aggregationType)) {
					columnNumberToAggregationFunction.put(
						currentColumnNumber, new DoubleAverageAggregator());
				} 
			}				
		}
		
		/*
		 * For String Columns
		 * */
		for (Integer currentColumnNumber : stringAggregationColumnNumbers) {
			String aggregationType = columnNumberToAggregationType.get(currentColumnNumber);

			if (currentColumnNumber.intValue() == groupedOnColumnNumber) {
				columnNumberToAggregationFunction.put(
					currentColumnNumber, new NoneStringAggregator());
			} else if (GlobalConstants.NONE_AGGREGATION_TEXT_DELIMITER.equalsIgnoreCase(
					aggregationType)) {
				columnNumberToAggregationFunction.put(
					currentColumnNumber, new NoneStringAggregator());
			} else {
				columnNumberToAggregationFunction.put(
					currentColumnNumber, new StringAggregator(aggregationType));
			}
		}

		return columnNumberToAggregationFunction;
	}

	/**
	 * @param columnNumbersToBeRemovedFromOutput
	 */
	private void removeNoneAggregationColumns(
			Set<String> columnNumbersToBeRemovedFromOutput) {
		for (Iterator<?> columnNumbersToBeRemovedFromOutputIterator 
				= columnNumbersToBeRemovedFromOutput.iterator();
				columnNumbersToBeRemovedFromOutputIterator.hasNext();) {
			String currentColumnName = (String) columnNumbersToBeRemovedFromOutputIterator.next();
			if (!groupedOnColumnName.equalsIgnoreCase(currentColumnName)) {
				outputTable.removeColumn(currentColumnName);
				logger.log(LogService.LOG_INFO, "\"" 
							+ currentColumnName + "\" column has been deleted" 
							+ " from the output. Since No aggregation was mentioned for it.");
			}
		}
	}

	/**
	 * @param columnNumbersToBeRemovedFromOutput
	 * @param groupedOnColumnNumber
	 * @param finalStringAggregationColumnNumbers
	 */
	private List<Integer> setupStringColumnsToBeProcessed(
			Set<String> columnNumbersToBeRemovedFromOutput,
			int groupedOnColumnNumber) {
		
		List<Integer> finalStringAggregationColumnNumbers = new ArrayList<Integer>();
		/*
		 * Assign appropriate String Aggregators per column.
		 * */
		for (Integer currentColumnNumber : stringAggregationColumnNumbers) {
			
			String textDelimiter = 
				columnNumberToAggregationType.get(currentColumnNumber);
			
			/*
			 * Condition 1: The current column should not be the "Grouped On" column.
			 * Condition 2: Aggregation Type or the Text delimiter for the current 
			 * 				column should not be "" 
			 * */
			if (currentColumnNumber.intValue() != groupedOnColumnNumber
						&& !GlobalConstants.NONE_AGGREGATION_TEXT_DELIMITER
								.equalsIgnoreCase(textDelimiter)) {
				finalStringAggregationColumnNumbers.add(currentColumnNumber);
			}
			
			/*
			 * In order to remove the columns from the output table for which
			 * the aggregation type was selected was "" by the user.
			 * */
			if (GlobalConstants.NONE_AGGREGATION_TEXT_DELIMITER
					.equalsIgnoreCase(textDelimiter)) {
				columnNumbersToBeRemovedFromOutput.add(
						originalTable.getColumnName(currentColumnNumber));
			}
		}
		return finalStringAggregationColumnNumbers; 
	}

	/**
	 * @param columnNumbersToBeRemovedFromOutput
	 * @param groupedOnColumnNumber
	 * @param finalNumericalAggregationColumnNumbers
	 */
	private List<Integer> setupNumericalColumnsToBeProcessed(
			Set<String> columnNumbersToBeRemovedFromOutput,
			int groupedOnColumnNumber) {
		
		List<Integer> finalNumericalAggregationColumnNumbers = new ArrayList<Integer>();
		/*
		 * Assign/Initialize appropriate Numerical Aggregators per column.
		 * */
			
		for (Integer currentColumnNumber : numericalAggregationColumnNumbers) {	
			
			String aggregationType = 
				columnNumberToAggregationType.get(currentColumnNumber);

			/*
			 * Condition 1: The current column should not be the "Grouped On" column.
			 * Condition 2: Aggregation Type for the current column should not be 
			 * 				"NONE". 
			 * */
			if (currentColumnNumber != groupedOnColumnNumber
					&& !GlobalConstants.NONE_NUMERICAL_AGGREGATION_TYPE_VALUE
							.equalsIgnoreCase(aggregationType)) {
				finalNumericalAggregationColumnNumbers.add(currentColumnNumber);
			}
			
			/*
			 * In order to remove the columns from the output table for which
			 * the aggregation type was selected was "NONE" by the user.
			 * */
			if (GlobalConstants.NONE_NUMERICAL_AGGREGATION_TYPE_VALUE
					.equalsIgnoreCase(aggregationType)) {
				columnNumbersToBeRemovedFromOutput.add(
						originalTable.getColumnName(currentColumnNumber));
			}
			
		}
		return finalNumericalAggregationColumnNumbers; 
	}

	/**
	 * @param groupedOnColumnNumber
	 */
	private Map<String, List<Integer>> createAggregatedOnValueToRowNumbersMappings(
			int groupedOnColumnNumber) {
		
		Map<String, List<Integer>> uniqueGroupedValueToRowNumbers = 
			new LinkedHashMap<String, List<Integer>>();
		Set<String> uniqueGroupedValues = new HashSet<String>();
		Iterator<?> groupedOnColumnIterator = originalTable.iterator();
		
		while (groupedOnColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(groupedOnColumnIterator.next().toString());
			
			
			String currentAggregatedValue = originalTable.getString(currentRowNumber,
																	groupedOnColumnNumber);
			
			/*
			 * There are cases when the Prefuse CSV reader returns a null value for
			 * empty cells. So to handle this null checking is done. 
			 * */
			if (currentAggregatedValue == null) {
				currentAggregatedValue = "";
			}
			
			
			String lowerCaseCurrentGroupedValue = currentAggregatedValue.toLowerCase();

			if (uniqueGroupedValues
					.contains(lowerCaseCurrentGroupedValue)) {
				List<Integer> groupedValueMappedRowNumbers = uniqueGroupedValueToRowNumbers
						.get(lowerCaseCurrentGroupedValue);
				groupedValueMappedRowNumbers.add(currentRowNumber);

			} else {
				uniqueGroupedValues.add(lowerCaseCurrentGroupedValue);
				List<Integer> rowNumbers = new ArrayList<Integer>();
				rowNumbers.add(currentRowNumber);
				uniqueGroupedValueToRowNumbers.put(
						lowerCaseCurrentGroupedValue, rowNumbers);
			}
		}
		return uniqueGroupedValueToRowNumbers;
	}

	/**
	 * @return the outputTable
	 */
	public Table getOutputTable() {
		//return the output table
		return outputTable;
	}
	
	/**
	 * Log warning message to user regarding all rows were skipped due
	 * to null values.
	 */
	private void logSkippedAll(Object groupedOnValue, String columnName) {
		String groupedOnValueString = "";
		if (groupedOnValue != null) {
			groupedOnValueString = groupedOnValue.toString();
		}
		
		logger.log(LogService.LOG_WARNING, String.format(
			"Aggregated by \'%s\': All rows of %s column were skipped"
			+ " due to no non-null, non-empty values.", 
			groupedOnValueString, columnName));
	}
	
	/**
	 * Log warning message to user regarding rows were skipped due to
	 * null values.
	 */
	private void logSkipped(Object groupedOnValue, String columnName, int totalSkipped) {
		String groupedOnValueString = "";

		if (groupedOnValue != null) {
			groupedOnValueString = groupedOnValue.toString();
		}

		String format =
			"Aggregated by \'%s\': %d row(s) of %s column were skipped " +
			"due to non-existent values.";
		logger.log(LogService.LOG_WARNING, String.format(
			format, groupedOnValueString, totalSkipped, columnName));
	}
}
