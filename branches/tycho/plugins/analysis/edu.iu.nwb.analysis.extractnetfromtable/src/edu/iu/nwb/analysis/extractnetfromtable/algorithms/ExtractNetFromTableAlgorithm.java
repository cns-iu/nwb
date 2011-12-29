package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;

public class ExtractNetFromTableAlgorithm implements Algorithm {
	private Data[] data;
	private Dictionary parameters;
	private LogService logger;

	
	public ExtractNetFromTableAlgorithm(
			Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.logger =
			(LogService) context.getService(LogService.class.getName());
	}
	
		
	public Data[] execute() throws AlgorithmExecutionException {
		final Table dataTable = (Table) data[0].getData();

		String delimiter =
			(String) parameters.get(
					ExtractNetFromTableAlgorithmFactory.DELIMITER_PARAMETER_ID);
		String extractColumn =
			(String) parameters.get(
					ExtractNetFromTableAlgorithmFactory.COLUMN_NAME_PARAMETER_ID);
		Properties properties = null;

		Object aggregationFunctionFilePath =
			parameters.get(
				ExtractNetFromTableAlgorithmFactory.AGGREGATION_FUNCTION_FILE_PARAMETER_ID);
		if (aggregationFunctionFilePath != null) {
			properties =
				PropertyHandler.getProperties(
						(String) aggregationFunctionFilePath,
						logger);
		}

		try {
			GraphContainer gc =
				GraphContainer.initializeGraph(
						dataTable,
						extractColumn,
						extractColumn,
						false,
						properties,
						logger);
			Graph outputGraph =
				gc.buildGraph(extractColumn, extractColumn, delimiter, false, logger);//enft.getGraph();
			Data outGraphData = createOutGraphData(extractColumn, outputGraph);	
			
			Table outputTable =
				ExtractNetworkFromTable.constructTable(outputGraph);
			Data outTableData = createOutTableData(extractColumn, outputTable);
	
			return new Data[]{ outGraphData, outTableData };		
		} catch (InvalidColumnNameException e) {
			String message = "Invalid column name: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}
	
	
	private Data createOutGraphData(String extractColumn, Graph outputGraph) {
		Data outGraphData =	new BasicData(outputGraph, Graph.class.getName());
		
		Dictionary graphAttributes = outGraphData.getMetadata();		
		graphAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		graphAttributes.put(DataProperty.PARENT, data[0]);
		graphAttributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		graphAttributes.put(DataProperty.LABEL,
				"Extracted Network on Column " + extractColumn);
		
		return outGraphData;
	}

	private Data createOutTableData(String extractColumn, Table table) {
		Data outTableData =	new BasicData(table, Table.class.getName());
		
		Dictionary tableAttributes = outTableData.getMetadata();		
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(
				DataProperty.LABEL,
				"Merge Table: based on " + extractColumn);
		
		return outTableData;
	}
}