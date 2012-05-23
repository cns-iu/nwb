package edu.iu.nwb.analysis.extractnetfromtable.algorithms;

import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.ExtractNetworkFromTable;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer.PropertyParsingException;

public class ExtractNetFromTableAlgorithm implements Algorithm {
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private LogService logger;

	
	public ExtractNetFromTableAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.logger =
			(LogService) context.getService(LogService.class.getName());
	}
	
		
	public Data[] execute() throws AlgorithmExecutionException {
		final Table dataTable = (Table) this.data[0].getData();

		String delimiter =
			(String) this.parameters.get(
					ExtractNetFromTableAlgorithmFactory.DELIMITER_PARAMETER_ID);
		String extractColumn =
			(String) this.parameters.get(
					ExtractNetFromTableAlgorithmFactory.COLUMN_NAME_PARAMETER_ID);
		Properties properties = null;

		Object aggregationFunctionFilePath =
			this.parameters.get(
				ExtractNetFromTableAlgorithmFactory.AGGREGATION_FUNCTION_FILE_PARAMETER_ID);
		if (aggregationFunctionFilePath != null) {
			properties =
				PropertyHandler.getProperties(
						(String) aggregationFunctionFilePath,
						this.logger);
		}

		Graph graph = getGraph(dataTable, delimiter, extractColumn, properties);

		Table mergeTable = ExtractNetworkFromTable.constructTable(graph);

		Data outGraphData = createOutGraphData(extractColumn, graph);
		Data outTableData = createOutTableData(extractColumn, mergeTable);

		return new Data[] { outGraphData, outTableData };	
	}


	private Graph getGraph(final Table dataTable, String delimiter,
			String extractColumn, Properties properties) throws AlgorithmExecutionException {
		try {
			GraphContainer graphContainer =
				GraphContainer.initializeGraph(
						dataTable,
						extractColumn,
						extractColumn,
						false,
						properties,
						this.logger);
			Graph graph =
				graphContainer.buildGraph(extractColumn, extractColumn, delimiter, false, this.logger);
			return graph;
		} catch (InvalidColumnNameException e) {
			String message = "Invalid column name: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		} catch (PropertyParsingException e) {
			throw new AlgorithmExecutionException(e);
		}
	}
	
	
	private Data createOutGraphData(String extractColumn, Graph outputGraph) {
		Data outData = DataFactory.withClassNameAsFormat(outputGraph,
				DataProperty.NETWORK_TYPE, this.data[0],
				"Extracted Network on Column " + extractColumn);
		outData.getMetadata().put(DataProperty.MODIFIED, Boolean.valueOf(true));
		
		return outData;
	}

	private Data createOutTableData(String extractColumn, Table table) {
		Data outData = DataFactory.withClassNameAsFormat(table,
				DataProperty.MATRIX_TYPE, this.data[0],
				"Merge Table: based on " + extractColumn);
		outData.getMetadata().put(DataProperty.MODIFIED, Boolean.valueOf(true));
		
		
		return outData;
	}
}