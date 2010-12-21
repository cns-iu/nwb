package edu.iu.scipolicy.filtering.topnpercent;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.ColumnNotFoundException;

import prefuse.data.Table;
import edu.iu.scipolicy.filtering.topncommon.TopNUtilities;

public class TopNPercentAlgorithm implements Algorithm {
	private Data[] data;
    
    private float topNPercent;
    private String columnToSortBy;
    private boolean isDescending;
    
    public TopNPercentAlgorithm(Data[] data,
    							Dictionary parameters,
    							CIShellContext context)
    {
    	this.data = data;
    	
        // Unpack parameters that user specified.
    	
    	Integer topNPercentInteger =
    		(Integer)parameters.get(TopNUtilities.TOP_N_PERCENT_ID);
    	
		this.topNPercent = ((float)topNPercentInteger.intValue() / 100.0f);
		
		this.columnToSortBy =
			((String)parameters.get(TopNUtilities.COLUMN_TO_SORT_BY_ID));
		
		this.isDescending =
			((Boolean)parameters.get(TopNUtilities.IS_DESCENDING_ID)).booleanValue();
		
		// Make sure topNPercent is between 0.0f and 1.0f.
		this.topNPercent = Math.min(1.0f, this.topNPercent);
		this.topNPercent = Math.max(0.0f, this.topNPercent);
    }
    
    public Data[] execute() throws AlgorithmExecutionException {
    	Data inData = this.data[0];
    	Table table = (Table)inData.getData();
    	final int numTableRows = table.getRowCount();
    	
    	// This is the raw number of rows the user specified with the given
    	// percentage (percentage * numRows).  There's no such thing as a fraction
    	// of a row, though, so we have to properly round this number.
    	final double calculatedTopNRows = (double)this.topNPercent * numTableRows;
    	
    	// If there is at least one row and the user-specified percentage is greater
    	// than 0, at least one row should be returned.  To make sure this happens,
    	// we can add 0.5 to calculatedTopNRows.  However, if calculatedTopNRows
    	// ends up being an exact number of rows, we DON'T want to do that.
    	final int flooredCalculatedTopNRows = (int)calculatedTopNRows;
    	int finalTopN = flooredCalculatedTopNRows;
    	
    	// Casting a double to an int has the same effect as using floor on the
    	// double.  If the floor of calculatedTopNRows, casted BACK to double, is
    	// the same as the original calculatedTopNRows, the user specified a
    	// percentage that resulted in an exact number of rows, in which case we
    	// DON'T want to add 0.5 when rounding (to make sure there is at least one
    	// row in the result set).
    	if ((double)flooredCalculatedTopNRows != calculatedTopNRows)
    		finalTopN = (int)Math.round(calculatedTopNRows + 0.5);
    	
    	Table sortedTable = null;
    	
    	try {
    		sortedTable = TopNUtilities.sortTableWithOnlyTopN(table,
    														  this.columnToSortBy,
    														  this.isDescending,
    														  finalTopN);
    	}
    	catch (ColumnNotFoundException e) {
    		throw new AlgorithmExecutionException(e);
    	}
    	
    	Data[] outData = prepareOutData(sortedTable, inData);
    	
    	return outData;
    }
    
    private Data[] prepareOutData(Table outTable, Data inData) {
		Data outData = new BasicData(outTable, outTable.getClass().getName());
		Dictionary outMetaData = outData.getMetadata();
		Dictionary inMetaData = inData.getMetadata();
		
		final String baseLabelString = "Top " + this.topNPercent + "% (";
		
		String sortingLabelString = null;
		
		if (this.isDescending)
			sortingLabelString = "descending order";
		else
			sortingLabelString = "ascending order";
		
		outMetaData.put(DataProperty.LABEL,
						baseLabelString + outTable.getRowCount() + " row(s)) (" +
						sortingLabelString + ") (based on " + this.columnToSortBy +
						") of " + inMetaData.get(DataProperty.LABEL));
		
		outMetaData.put(DataProperty.PARENT, inData);
		outMetaData.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[] { outData };
	}
}