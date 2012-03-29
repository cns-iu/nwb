package edu.iu.cns.database.extract.generic.service;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.cns.database.extract.generic.ExtractGraph;
import edu.iu.cns.database.extract.generic.ExtractGraphFactory;
import edu.iu.cns.database.extract.generic.ExtractTable;
import edu.iu.cns.database.extract.generic.ExtractTableFactory;

public class ExtractionService {
	
	public static Data[] extractTable(Data[] dataWithDB, 
			String tableQuery,
			String label,
			CIShellContext context) throws AlgorithmExecutionException {
		
		Dictionary parameters = new Hashtable();
		parameters.put(ExtractTableFactory.QUERY_KEY, tableQuery);
		parameters.put(ExtractTableFactory.LABEL_KEY, label);
		
		ExtractTable extractTable = new ExtractTable(dataWithDB, parameters, context);
		Data[] tableData = extractTable.execute();
		return tableData;
	}
	
	public static Data[] extractGraph(Data[] dataWithDB,
			String nodeQuery,
			String edgeQuery,
			String idColumn,
			String sourceColumn,
			String targetColumn,
			Boolean directed,
			String label, 
			CIShellContext context) 
			throws AlgorithmExecutionException {
			
		
		Dictionary parameters = new Hashtable();
		parameters.put(ExtractGraphFactory.NODE_QUERY_KEY, nodeQuery);
		parameters.put(ExtractGraphFactory.EDGE_QUERY_KEY, edgeQuery);
		parameters.put(ExtractGraphFactory.ID_COLUMN_KEY, idColumn);
		parameters.put(ExtractGraphFactory.SOURCE_COLUMN_KEY, sourceColumn);
		parameters.put(ExtractGraphFactory.TARGET_COLUMN_KEY, targetColumn);
		parameters.put(ExtractGraphFactory.DIRECTED_KEY, directed);
		parameters.put(ExtractGraphFactory.LABEL_KEY, label);
		
		ExtractGraph extractGraph = new ExtractGraph(dataWithDB, parameters, context);
		Data[] graphData = extractGraph.execute();
		return graphData;
	}
			
}
