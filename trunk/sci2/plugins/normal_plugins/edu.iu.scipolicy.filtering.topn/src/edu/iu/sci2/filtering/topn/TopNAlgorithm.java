package edu.iu.sci2.filtering.topn;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.ColumnNotFoundException;

import prefuse.data.Table;
import edu.iu.sci2.filtering.topncommon.TopNUtilities;

public class TopNAlgorithm implements Algorithm {
	private Data[] data;
    private Dictionary parameters;
    private CIShellContext context;
    
    private int topN;
    private String columnToSortBy;
    private boolean isDescending;
    
    public TopNAlgorithm(Data[] data,
    					 Dictionary parameters,
    					 CIShellContext context)
    {
    	this.data = data;
    	this.parameters = parameters;
    	this.context = context;
    	
        // Unpack parameters that user specified.
		this.topN = ((Integer)parameters.get(TopNUtilities.TOP_N_ID)).intValue();
		
		this.columnToSortBy =
			((String)parameters.get(TopNUtilities.COLUMN_TO_SORT_BY_ID));
		
		this.isDescending =
			((Boolean)parameters.get(TopNUtilities.IS_DESCENDING_ID)).booleanValue();
    }
    
    public Data[] execute() throws AlgorithmExecutionException {
    	Data inData = this.data[0];
    	Table table = (Table)inData.getData();
    	
    	Table sortedTable = null;
    	
    	try {
    		sortedTable = TopNUtilities.sortTableWithOnlyTopN(table,
    														  this.columnToSortBy,
    														  this.isDescending,
    														  this.topN);
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
		
		final String baseLabelString =
			"Top " + outTable.getRowCount() + " row(s) (";
		
		String sortingLabelString = null;
		
		if (this.isDescending)
			sortingLabelString = "descending order";
		else
			sortingLabelString = "ascending order";
		
		outMetaData.put(DataProperty.LABEL,
						baseLabelString + sortingLabelString +
						") (based on " + this.columnToSortBy + ") of " +
						inMetaData.get(DataProperty.LABEL));
		
		outMetaData.put(DataProperty.PARENT, inData);
		outMetaData.put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		
		return new Data[] { outData };
	}
}