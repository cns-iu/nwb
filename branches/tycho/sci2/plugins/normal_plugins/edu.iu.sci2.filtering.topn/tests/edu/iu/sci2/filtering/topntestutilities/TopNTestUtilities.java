package edu.iu.sci2.filtering.topntestutilities;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import prefuse.data.Table;
import edu.iu.sci2.filtering.topncommon.TopNUtilities;

public class TopNTestUtilities {
	public static Dictionary formUserParametersForTopN(int topN,
													   String columnToSortBy,
													   boolean isDescending) {
		Hashtable userParameters = new Hashtable();
		
		userParameters.put(TopNUtilities.TOP_N_ID, new Integer(topN));
		userParameters.put(TopNUtilities.COLUMN_TO_SORT_BY_ID, columnToSortBy);
		userParameters.put(TopNUtilities.IS_DESCENDING_ID,
						   new Boolean(isDescending));
		
		return userParameters;
	}
	
	public static Dictionary formUserParametersForTopNPercent
			(int topNPercent, String columnToSortBy, boolean isDescending) {
		Hashtable userParameters = new Hashtable();
		
		userParameters.put(TopNUtilities.TOP_N_PERCENT_ID, new Integer(topNPercent));
		userParameters.put(TopNUtilities.COLUMN_TO_SORT_BY_ID, columnToSortBy);
		userParameters.put(TopNUtilities.IS_DESCENDING_ID,
						   new Boolean(isDescending));
		
		return userParameters;
	}
	
	public static Table formTestTable(String columnName) {
		Random randomNumberGenerator = new Random();
		Table table = new Table();
		final int numRows = randomNumberGenerator.nextInt(50);
		
		table.addColumn(columnName, Integer.class);
		table.addRows(numRows);
		
		for (int ii = 0; ii < numRows; ii++) {
			table.set(ii, 0, new Integer(randomNumberGenerator.nextInt
								(Integer.MAX_VALUE)));
		}
		
		return table;
	}
	
	public static Data formTestData(String columnName) {
		Table testTable = formTestTable(columnName);
		Data testTableData = new BasicData(testTable, testTable.getClass().getName());
		
		return testTableData;
	}
}