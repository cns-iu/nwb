package edu.iu.cns.database.extract.generic;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DatabaseUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.io.DataIOException;
import prefuse.data.io.sql.ConnectionFactory;
import prefuse.data.io.sql.DatabaseDataSource;

public class ExtractGraph implements Algorithm {
	private Data parentData;
	private LogService logger;
	
	private String nodeQuery;
	private String edgeQuery;
	private String idColumn;
	private String sourceColumn;
	private String targetColumn;
	private boolean directed;
	
	private String label = "Extracted graph";
	
	private static final String internalId = "_x_nwb_id";
	private static final String internalSource = "_x_nwb_source";
	private static final String internalTarget = "_x_nwb_target";
	private boolean custom;

	public ExtractGraph(Data[] data, Dictionary parameters, CIShellContext context) {
		this.parentData = data[0];
		this.logger = (LogService) context.getService(LogService.class.getName());

		this.nodeQuery = (String) parameters.get(ExtractGraphFactory.NODE_QUERY_KEY);
		this.edgeQuery = (String) parameters.get(ExtractGraphFactory.EDGE_QUERY_KEY);
		this.idColumn = (String) parameters.get(ExtractGraphFactory.ID_COLUMN_KEY);
		this.sourceColumn = (String) parameters.get(ExtractGraphFactory.SOURCE_COLUMN_KEY);
		this.targetColumn = (String) parameters.get(ExtractGraphFactory.TARGET_COLUMN_KEY);
		this.directed = (Boolean) parameters.get(ExtractGraphFactory.DIRECTED_KEY);
		if(parameters.get(ExtractGraphFactory.LABEL_KEY) != null) {
			this.label = (String) parameters.get(ExtractGraphFactory.LABEL_KEY);
		}
		this.custom = (parameters.get(ExtractGraphFactory.CUSTOM_KEY) == null);
	}

	public Data[] execute() throws AlgorithmExecutionException {
		if(this.custom) {
			this.logger.log(LogService.LOG_INFO, "If you see unexpected results with the resulting network from analysis algorithms or " +
				"inconsistencies between the Network Analysis Toolkit and GUESS, this is probably due to the queries including " +
				"duplicates of nodes or edges. To verify your queries are producing exactly what you expect, " +
				"try running each of them separately as table extractions, and looking over the results manually.");
		}

		Database database = (Database) this.parentData.getData();
		Connection connection = DatabaseUtilities.connect(database, "Unable to communicate with the database.");
		try {
			Graph graph = extractGraph(connection);
			Data outputData = wrapWithMetadata(graph);
			return new Data[] { outputData };
		} catch (SQLException e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException("Unable to communicate with the selected database.", e);
		} catch (DataIOException e) {
			e.printStackTrace();
			throw new AlgorithmExecutionException("There was a problem executing a query: " + e.getMessage(), e);
		} catch (RuntimeException e) {
			e.printStackTrace(); //really, CIShell should be doing this
			throw e;
		} finally {
			DatabaseUtilities.closeConnectionQuietly(connection);
		}

	}

	/*
	 * Prefuse only accepts integers for node/source/target IDs. Our data might have those as any type.
	 * As such, we need to generate new 'internal' columns that have integers based on the original IDs.
	 * That's most of the work in this method.
	 */
	private Graph extractGraph(Connection connection) throws SQLException,
					DataIOException, AlgorithmExecutionException {
		DatabaseDataSource tableSource = ConnectionFactory.getDatabaseConnection(connection);
		
		Table nodes = tableSource.getData(this.nodeQuery);
		
		Table edges = tableSource.getData(this.edgeQuery);
		
		addInternalColumns(nodes, edges);

		int nodeCount = nodes.getRowCount();
		Map<Object, Integer> newIds = new HashMap<Object, Integer>(nodeCount);

		int currentId = 0;

		for(int row = 0; row < nodeCount; row++) {
			Tuple node = nodes.getTuple(row);
			try {
				Object originalId = node.get(this.idColumn);
				newIds.put(originalId, currentId);
				node.setInt(internalId, currentId);
				currentId++;
			} catch(ArrayIndexOutOfBoundsException e) {
				throw new AlgorithmExecutionException("The node query does not include a column named " + this.idColumn);
			}

		}

		int edgeCount = edges.getRowCount();
		for(int row = 0; row < edgeCount; row++) {
			Tuple edge = edges.getTuple(row);
			internalizeId(edge, this.sourceColumn, internalSource, newIds);
			internalizeId(edge, this.targetColumn, internalTarget, newIds);
		}

		Graph graph = new Graph(nodes, edges, this.directed, internalId, internalSource, internalTarget);
		return graph;
	}

	private static void internalizeId(Tuple edge, String column, String internal, Map<Object, Integer> newIds)
			throws AlgorithmExecutionException {
		try {
			Object originalSource = edge.get(column);
			Integer newSource = newIds.get(originalSource);
			if(newSource == null) {
				throw new AlgorithmExecutionException("Problem constructing graph. The edge source '" 
						+ originalSource + "' could not be found among the node ids.");
			}
			edge.setInt(internal, newSource);
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new AlgorithmExecutionException("The edge query does not include a column named " + column);
		}
	}

	private static void addInternalColumns(Table nodes, Table edges) {
		nodes.addColumn(internalId, int.class);
		edges.addColumn(internalSource, int.class);
		edges.addColumn(internalTarget, int.class);
	}

	private Data wrapWithMetadata(Graph extractedGraph) {
		Data outputData = new BasicData(extractedGraph, Graph.class.getName());
		Dictionary<String, Object> metadata = outputData.getMetadata();
		metadata.put(DataProperty.LABEL, this.label);
		metadata.put(DataProperty.PARENT, this.parentData);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		return outputData;
	}
}