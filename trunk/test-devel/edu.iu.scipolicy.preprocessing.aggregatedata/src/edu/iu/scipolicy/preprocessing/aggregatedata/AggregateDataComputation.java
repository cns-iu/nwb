package edu.iu.scipolicy.preprocessing.aggregatedata;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

/**
 * 
 * After getting the input file following algorithm is implemented,
 * 1. Get the address for each row & begin parsing for zip codes in following manner,
 * 		a.  Save all groups of digits along with their start position, end position &
 * 			length of the group of digits.
 * 		b.	Since the zip code, presumably, will be in the later portion of the address 
 * 			string, traverse the collected zip code candidates in the reverse fashion.
 * 		c.	If there is a 5 digit group then,
 * 			(i) 	Consider it as the primary zip code
 * 			(ii) 	The extension of the ZIP code follows the primary zip code, so check
 * 					if the previous group has length 4 and if so, then check if its
 * 					distance from primary zip is less than or equal to 2. If yes, than
 * 					consider this as the extension of the zip code.
 * 			(iii)	If there is no 4 digit group satisfying the above conditions then return 
 * 					null as the extension value.
 * 		d. If there is no 5 digit group then return null for the primary zip code value. In
 *		   this case, display a warning to the user that no ZIP code was found for this 
 *		   particular address string.
 *	 			  
 * @author cdtank
 *
 */
public class AggregateDataComputation {
	
	private static final int PRIMARY_TO_EXTENSION_DISTANCE_TOLERANCE_VALUE = 2;
	private static final int EXTENSION_ZIPCODE_LENGTH = 4;
	private static final int PRIMARY_ZIPCODE_LENGTH = 5;
	
	
	private int totalAddressesConsidered = 0, totalZipcodesExtracted = 0;

	private LogService logger;

	private static final String[] UNIQUE_RECORDS_COUNT_COLUMN_NAME_SUGGESTIONS = {"Count", "Aggregated_Count"};
	
	private String aggregatedOnColumnName, delimiter;
	private Table originalTable, outputTable;

	private String outputTableCountColumnName;

	public AggregateDataComputation(String delimiter, String aggregatedOnColumnName, Table table,
			LogService logger) {
		this.delimiter = delimiter;
		this.aggregatedOnColumnName = aggregatedOnColumnName;
		this.originalTable = table;
		this.logger = logger;
		
		processTable();
	
	}

	private void processTable() {
		
		/*
		 * Create Blank new output table using the schema from the original table.
		 * */
//		outputTable = originalTable.getSchema().instantiate();
		outputTableCountColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), UNIQUE_RECORDS_COUNT_COLUMN_NAME_SUGGESTIONS);
		
		outputTable = new Table();
		
		for (int columnIndex = 0; columnIndex < originalTable.getColumnCount(); columnIndex++) {
			outputTable.addColumn(originalTable.getColumnName(columnIndex), String.class);
		}
		
		outputTable.addColumn(outputTableCountColumnName, int.class);
		
		logger.log(LogService.LOG_INFO, "ZIP code added to \""
										+ outputTableCountColumnName + "\" column.");
		
		Iterator aggregatedOnColumnIterator = originalTable.iterator();
		
		int aggregatedOnColumnNumber = originalTable.getColumnNumber(aggregatedOnColumnName);
		int countColumnNumber = outputTable.getColumnNumber(outputTableCountColumnName);
		
		Set<String> uniqueAggregatedValues = new HashSet<String>();
		Map<String, List> uniqueAggregatedValueToRowNumbers = new LinkedHashMap<String, List>();
		
		while (aggregatedOnColumnIterator.hasNext()) {
			int currentRowNumber = Integer.parseInt(aggregatedOnColumnIterator.next().toString());
			String currentAggregatedValue = originalTable
												.get(currentRowNumber, aggregatedOnColumnNumber).toString(); 
			String lowerCaseCurrentAggregatedValue = currentAggregatedValue.toLowerCase();
			
			if (uniqueAggregatedValues.contains(lowerCaseCurrentAggregatedValue)) {
				List<Integer> aggregatedValueMappedRowNumbers = 
					uniqueAggregatedValueToRowNumbers.get(lowerCaseCurrentAggregatedValue);
				aggregatedValueMappedRowNumbers.add(currentRowNumber);
				
			} else {
				uniqueAggregatedValues.add(lowerCaseCurrentAggregatedValue);
				List<Integer> rowNumbers = new ArrayList<Integer>();
				rowNumbers.add(currentRowNumber);
				uniqueAggregatedValueToRowNumbers.put(lowerCaseCurrentAggregatedValue, rowNumbers);
			}
		}
		Iterator outputAggregatedValueMapIterator = uniqueAggregatedValueToRowNumbers.values().iterator();
		
		int outputTableRowCount = 0;
		while (outputAggregatedValueMapIterator.hasNext()) {
			
			List<Integer> aggregatedValueRowNumbers = (List<Integer>) outputAggregatedValueMapIterator.next();

			Iterator<Integer> rowNumberIterator = aggregatedValueRowNumbers.iterator();
			StringBuffer[] outputColumnContents = new StringBuffer[originalTable.getColumnCount()];
			StringColumnAggregator[] outputStringAggregates = new StringColumnAggregator[originalTable.getColumnCount()];
			NumberColumnAggregator[] outputIntegerAggregates = new NumberColumnAggregator[originalTable.getColumnCount()];
			BooleanColumnAggregator[] outputBooleanAggregates = new BooleanColumnAggregator[originalTable.getColumnCount()];
		
			String outputTableAggregatedValue = null;
			
			while (rowNumberIterator.hasNext()) {
				int originalTableRowNumber = rowNumberIterator.next();

				if (outputTableAggregatedValue == null) {
					outputTableAggregatedValue = originalTable.get(originalTableRowNumber, aggregatedOnColumnNumber).toString();
				}
				
				for (int ii = 0; ii < originalTable.getColumnCount(); ii++) {
					
					if (originalTable.getColumnType(ii) == Number.class 
							&& outputIntegerAggregates[ii] == null) {
						outputIntegerAggregates[ii] = new NumberColumnAggregator<Number>();
					} else if (originalTable.getColumnType(ii) == boolean.class 
							&& outputBooleanAggregates[ii] == null) {
						outputBooleanAggregates[ii] = new BooleanColumnAggregator<Boolean>();
					} else if (outputStringAggregates[ii] == null) {
						outputStringAggregates[ii] = new StringColumnAggregator<String>();
					}
				}
				
				for (int ii = 0; ii < originalTable.getColumnCount(); ii++) {
//					if (outputColumnContents[ii] == null) {
//						outputColumnContents[ii] = new StringBuffer();
//					}
//					
//					if (outputStringAggregates[ii] == null) {
//						outputStringAggregates[ii] = new StringColumnAggregator<String>();
//					}
//					
//					if (outputIntegerAggregates[ii] == null && originalTable.getColumnType(ii) == int.class) {
//						System.out.println("int found + ====");
//						outputIntegerAggregates[ii] = new NumberColumnAggregator<Integer>();
//					}
//					
					if (ii != aggregatedOnColumnNumber) {
						String currentCellContent = originalTable.getString(originalTableRowNumber, ii);
						
						if (!currentCellContent.equals("")) {
							outputColumnContents[ii].append(currentCellContent);
							outputColumnContents[ii].append(delimiter);
							
							outputStringAggregates[ii].aggregateValue(currentCellContent);
							outputStringAggregates[ii].aggregateValue(delimiter);
						}
						
						if (originalTable.getColumnType(ii) == Number.class) {
							outputIntegerAggregates[ii].aggregateValue(originalTable.get(originalTableRowNumber, ii));
						}
					}
				}
			}
			
			outputTable.addRow();
			for (int ii = 0; ii < originalTable.getColumnCount(); ii++) { 
				if (ii != aggregatedOnColumnNumber) {
					int currentCellContentLength = outputColumnContents[ii].length();
					
					if (currentCellContentLength > 0) {
						outputColumnContents[ii] = outputColumnContents[ii]
						                                .delete(currentCellContentLength - delimiter.length(), 
						                                		currentCellContentLength);
					}
					
					System.out.println("-- " + outputStringAggregates[ii].getValue());
					
					if (originalTable.getColumnType(ii) == int.class) {
						System.out.println(" +++++++ " + outputIntegerAggregates[ii].getValue());
					}
					
					outputTable.set(outputTableRowCount, ii, outputColumnContents[ii].toString());
				} else {
					outputTable.set(outputTableRowCount, ii, outputTableAggregatedValue);
				}
				
			}
			
			outputTable.set(outputTableRowCount, countColumnNumber, aggregatedValueRowNumbers.size());
			outputTableRowCount++;
		}
		
		
	}

	/**
	 * @return the outputTable
	 */
	public Table getOutputTable() {
		return outputTable;
	}

	/**
	 * @return the totalAddressesConsidered
	 */
	public int getTotalAddressesConsidered() {
		return totalAddressesConsidered;
	}

	/**
	 * @return the totalZipcodesExtracted
	 */
	public int getTotalZipcodesExtracted() {
		return totalZipcodesExtracted;
	}
	
}
