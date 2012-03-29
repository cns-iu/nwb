package edu.iu.sci2.filtering.topnpercent;

import java.util.Dictionary;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.ColumnNotFoundException;

import prefuse.data.Table;
import edu.iu.sci2.filtering.topncommon.TopNUtilities;

public class TopNPercentAlgorithm implements Algorithm {
	private Data inputData;
    private float topNPercent;
    private String columnToSortBy;
    private boolean isDescending;
    
    public TopNPercentAlgorithm(
    		Data inputData, float topNPercent, String columnToSortBy, boolean isDescending) {
    	this.inputData = inputData;
		this.topNPercent = topNPercent;
		this.columnToSortBy = columnToSortBy;
		this.isDescending = isDescending;
		
		// Make sure topNPercent is between 0.0f and 1.0f.
		this.topNPercent = Math.min(1.0f, this.topNPercent);
		this.topNPercent = Math.max(0.0f, this.topNPercent);
    }
    
    public Data[] execute() throws AlgorithmExecutionException {
    	Table table = (Table) this.inputData.getData();
    	int numTableRows = table.getRowCount();
    	
    	/* This is the raw number of rows the user specified with the given percentage
    	 * (percentage * numRows).  There's no such thing as a fraction of a row, though, so we
    	 * have to properly round this number.
    	 */
    	double calculatedTopNRows = (double) this.topNPercent * numTableRows;
    	
    	/* If there is at least one row and the user-specified percentage is greater than 0, at
    	 * least one row should be returned.  To make sure this happens, we can add 0.5 to
    	 * calculatedTopNRows.  However, if calculatedTopNRows ends up being an exact number of
    	 * rows, we DON'T want to do that.
    	 */
		/* TODO cast to int actually rounds toward zero, so don't call this "floored"
		 * use Math.ceiling and Math.max to verify you don't overindex the table
		 */
    	int flooredCalculatedTopNRows = (int) calculatedTopNRows;
    	int finalTopN = flooredCalculatedTopNRows;
    	
    	if ((double) flooredCalculatedTopNRows != calculatedTopNRows) {
    		finalTopN = (int) Math.round(calculatedTopNRows + 0.5);
    	}
    	
    	Table sortedTable = null;
    	
    	try {
    		sortedTable = TopNUtilities.sortTableWithOnlyTopN(
    			table, this.columnToSortBy, this.isDescending, finalTopN);
    	} catch (ColumnNotFoundException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}
    	
    	Data[] outData = prepareOutData(sortedTable, this.inputData);

    	return outData;
    }
    
    private Data[] prepareOutData(Table outTable, Data inData) {
		Data outData = new BasicData(outTable, outTable.getClass().getName());
		Dictionary<String, Object> outMetadata = outData.getMetadata();
		Dictionary<String, Object> inMetadata = inData.getMetadata();

		int topNPercentAsInteger = (int) (this.topNPercent * 100);
		String sortingLabelString = null;
		
		if (this.isDescending) {
			sortingLabelString = "descending order";
		} else {
			sortingLabelString = "ascending order";
		}

		String label = String.format(
			"Top %d%% (%d rows(s)) (%s) (based on %s) of %s",
			topNPercentAsInteger,
			outTable.getRowCount(),
			sortingLabelString,
			this.columnToSortBy,
			inMetadata.get(DataProperty.LABEL)); // TODO remove?
		outMetadata.put(DataProperty.LABEL, label);
		outMetadata.put(DataProperty.PARENT, inData);
		outMetadata.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[] { outData };
	}
}