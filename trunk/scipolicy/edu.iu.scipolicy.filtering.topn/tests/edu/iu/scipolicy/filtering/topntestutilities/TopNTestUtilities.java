package edu.iu.scipolicy.filtering.topntestutilities;

import static org.junit.Assert.fail;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Random;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;

import edu.iu.scipolicy.filtering.topn.TopNAlgorithm;
import edu.iu.scipolicy.filtering.topncommon.TopNUtilities;

import prefuse.data.Table;

public class TopNTestUtilities {
	public static Dictionary formUserParameters(int topN,
												String columnToSortBy,
												boolean isDescending)
	{
		Hashtable userParameters = new Hashtable();
		
		userParameters.put(TopNUtilities.TOP_N_ID, new Integer(topN));
		userParameters.put(TopNUtilities.COLUMN_TO_SORT_BY_ID, columnToSortBy);
		userParameters.put(TopNUtilities.IS_DESCENDING_ID,
						   new Boolean(isDescending));
		
		return userParameters;
	}
	
	public static Dictionary formUserParameters(float topNPercent,
												String columnToSortBy,
												boolean isDescending)
	{
		Hashtable userParameters = new Hashtable();
		
		userParameters.put(TopNUtilities.TOP_N_PERCENT_ID, new Float(topNPercent));
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
	
	public static Data[] formTestData(String columnName) {
		Table testTable = formTestTable(columnName);
		Data testTableData = new BasicData(testTable,
										   testTable.getClass().getName());
		
		return new Data[] { testTableData };
	}
}