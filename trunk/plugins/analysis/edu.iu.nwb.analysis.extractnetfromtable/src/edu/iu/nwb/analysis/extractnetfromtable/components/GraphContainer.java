package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.ProgressMonitor;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunctionNames;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.column.Column;

public class GraphContainer {
	
	private Graph graph;
	private Table table;
	private AggregateFunctionMappings nodeMap;
	private AggregateFunctionMappings edgeMap;
	private ProgressMonitor progMonitor = null;

	public GraphContainer(Graph g, Table t, AggregateFunctionMappings nodeFunctionMap, AggregateFunctionMappings edgeFunctionMap){
		this(g, t, nodeFunctionMap, edgeFunctionMap, null);
	}

	public GraphContainer(Graph g, Table t, AggregateFunctionMappings nodeFunctionMap, AggregateFunctionMappings edgeFunctionMap, ProgressMonitor pm){
		this.graph = g;
		this.table = t;
		this.nodeMap = nodeFunctionMap;
		this.edgeMap = edgeFunctionMap;
		this.progMonitor = pm;
	}

	public Graph buildGraph(
			String sourceColumnName,
			String targetColumnName,
			String delimiter,
			LogService log){
		String[] targetColumnNames = targetColumnName.split("\\,");
		
		if(this.graph.isDirected()) {
			return buildDirectedGraph(
					sourceColumnName, targetColumnNames, delimiter,	log);
		} else {
			return buildUndirectedGraph(
					targetColumnNames, delimiter, log);
		}
	}

	private Graph buildUndirectedGraph(
			String[] targetColumnNames,
			String delimiter,
			LogService log) {
		boolean dupValues = false;
		final HashMap dupValuesErrorMessages = new HashMap();
		int numTotalRows = this.table.getRowCount();
		int numRowsProcessedSoFar = 0;

		if(this.progMonitor != null) {
			this.progMonitor.start(ProgressMonitor.WORK_TRACKABLE, numTotalRows);
		}

		for (Iterator rowIt = this.table.rows(); rowIt.hasNext();){
			int row = ((Integer)rowIt.next()).intValue();
			
			Node node1 = null;
			Node node2 = null;
			
			final String targetString =
				buildRowTargetStringFromColumnNames(
						row, targetColumnNames, this.table, delimiter);

			Set seenObject = new HashSet();

			if(targetString != null) { //no values to extract from
				final Pattern splitPattern = Pattern.compile("\\Q" + delimiter + "\\E");
				final String[] splitTargetStringArray = splitPattern.split(targetString);
				
				// Trim each target.
				for (int ii = 0; ii < splitTargetStringArray.length; ii++) {
					splitTargetStringArray[ii] =
						splitTargetStringArray[ii].trim();
				}

				for (int ii = splitTargetStringArray.length - 1; ii >= 0; ii--) {
					if(seenObject.add(splitTargetStringArray[ii])) { // No duplicate nodes.
						node1 = NodeUtilities.mutateNode(splitTargetStringArray[ii], this.graph,
							this.table, row, this.nodeMap, AggregateFunctionMappings.SOURCEANDTARGET);
					}
					
					node1 = this.graph.getNode(this.nodeMap.getFunctionRow(splitTargetStringArray[ii]).getRowNumber());
					
					for (int jj = 0; jj < ii; jj++) {
						if(!splitTargetStringArray[jj].equals(splitTargetStringArray[ii])) {
							if(seenObject.add(splitTargetStringArray[jj])) { //No duplicate nodes.
								node2 = NodeUtilities.mutateNode(splitTargetStringArray[jj],
									this.graph, this.table, row, this.nodeMap,
									AggregateFunctionMappings.SOURCEANDTARGET);

							}
							//create or modify an edge as necessary
							node2 = this.graph.getNode(this.nodeMap.getFunctionRow(splitTargetStringArray[jj]).getRowNumber());
							EdgeContainer.mutateEdge(node1, node2, this.graph, this.table, row, this.edgeMap);
						}
						else {
							dupValues = true; //detected a self-loop
						}
					}
				}
				if(dupValues){
					//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
					//String title = "unknown";
					//ExtractNetworkFromTable.addDuplicateValueErrorMessage(title, columnName, dupValuesErrorMessages);
					//dupValues = false;
				}
			}
			else{
				//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
				//String title = "unknown";
				//ExtractNetworkFromTable.printNoValueToExtractError(title, columnName, this.log);
			}
			numRowsProcessedSoFar = numRowsProcessedSoFar+1;
			if(this.progMonitor != null)
				this.progMonitor.worked(numRowsProcessedSoFar);

		}
		for(Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();){
			log.log(LogService.LOG_WARNING, (String)dupValuesErrorMessages.get(dupIter.next()));
		}

		return this.graph;
	}

	private Graph buildDirectedGraph(
			String sourceColumnName,
			String[] targetColumnNames,
			String delimiter,
			LogService log){
		final Pattern splitPattern = Pattern.compile("\\Q" + delimiter + "\\E");
		final HashMap dupValuesErrorMessages = new HashMap();
		Column sourceColumn = this.table.getColumn(sourceColumnName);
		Node node1;
		Node node2;

		int numTotalRows = this.table.getRowCount();
		int numRowsProcessedSoFar = 0;

		if(this.progMonitor != null){
			this.progMonitor.start(ProgressMonitor.WORK_TRACKABLE, numTotalRows);
		}
			
		for (Iterator rowIt = this.table.rows(); rowIt.hasNext();) {
			int row = ((Integer)rowIt.next()).intValue();
			final String sourceString = (String) sourceColumn.get(row);
			
			final String targetString =
				buildRowTargetStringFromColumnNames(
						row, targetColumnNames, this.table, delimiter);

			Set seenSource = new HashSet();
			Set seenTarget;// = seenSource;www
			if (sourceString != null && targetString != null) { //ensure we have values to extract
				final String[] sources = splitPattern.split(sourceString);
				final String[] targets = splitPattern.split(targetString);

				for (int ii = 0; ii < sources.length; ii++) {
					String trimmedSource = sources[ii].trim();
					if("".equals(trimmedSource)) { continue; }
					if (seenSource.add(trimmedSource)) { 
						node1 = NodeUtilities.mutateNode(
								trimmedSource,
								this.graph,
								this.table,
								row,
								this.nodeMap,
								AggregateFunctionMappings.SOURCE);
							
						seenTarget = new HashSet();

						for (int jj = 0; jj < targets.length; jj++) {
							String trimmedTarget = targets[jj].trim();
							if("".equals(trimmedTarget)) { continue; }
							if(seenTarget.add(trimmedTarget)) {
								node2 = NodeUtilities.mutateNode(
										trimmedTarget,
										this.graph,
										this.table,
										row,
										this.nodeMap,
										AggregateFunctionMappings.TARGET);

								EdgeContainer.mutateEdge(
										node1,
										node2,
										this.graph,
										this.table,
										row,
										this.edgeMap);
							}
						}
					}
				}
			} else {
				// String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
				//String title = "unknown";
				// ExtractNetworkFromTable.printNoValueToExtractError(title, sourceColumnName, log);
			}

			numRowsProcessedSoFar = numRowsProcessedSoFar + 1;

			if(this.progMonitor != null)
				this.progMonitor.worked(numRowsProcessedSoFar);
		}
				
		for(Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();){
			log.log(LogService.LOG_WARNING, (String)dupValuesErrorMessages.get(dupIter.next()));
		}

		return this.graph;
	}

	public static GraphContainer initializeGraph(Table pdt,String sourceColumnName, String targetColumnName, boolean isDirected,Properties p, LogService log) throws InvalidColumnNameException{
		return initializeGraph(pdt, sourceColumnName, targetColumnName, isDirected, p, log, null);
	}
	
	public static GraphContainer initializeGraph(Table inputTable, String sourceColumnName,
			String targetColumnName, boolean isDirected, Properties functions,
			LogService log, ProgressMonitor pm)
	throws InvalidColumnNameException {

		final Schema inputSchema = inputTable.getSchema();

		if(inputSchema.getColumnIndex(sourceColumnName) < 0)
			throw new InvalidColumnNameException(sourceColumnName + " was not a column in this table.\n");
		
		// Get all of the target column names.
		String[] targetColumnNameArray = targetColumnName.split("\\,");
		
		// Make sure the one or more column name(s) is/are valid.
		
		if ((targetColumnNameArray == null) || (targetColumnNameArray.length == 0))
			throw new InvalidColumnNameException(targetColumnName + " was not a column in this table.\n");
		else
		{
			for (int ii = 0; ii < targetColumnNameArray.length; ii++)
			{
				if (inputSchema.getColumnIndex(targetColumnNameArray[ii]) < 0)
				{
					throw new InvalidColumnNameException(targetColumnNameArray[ii] +
						" was not a column in this table.\n");
				}
			}
		}

		Schema nodeSchema = createNodeSchema();
		Schema edgeSchema = createEdgeSchema();

		AggregateFunctionMappings nodeAggregateFunctionMap = new AggregateFunctionMappings();
		AggregateFunctionMappings edgeAggregateFunctionMap = new AggregateFunctionMappings();

		AggregateFunctionMappings.parseProperties(inputSchema, nodeSchema, edgeSchema, functions, 
				nodeAggregateFunctionMap, edgeAggregateFunctionMap, log);
		
		if (isPerformingCooccurrenceExtraction(sourceColumnName, targetColumnName)) {
			/*
			 * (For now we only add default edge weights for co-occurrence extractions).
			 * (What this operation would mean for non-co-occurrence extractions
			 *  is not yet understood (by me at least)).
			 */
			//If we haven't already prepared to add an edge weight column...
			if (edgeSchema.getColumnIndex(AggregateFunctionMappings.DEFAULT_WEIGHT_NAME) == -1) {
				AggregateFunctionMappings.addDefaultEdgeWeightColumn(
						inputSchema, edgeSchema, edgeAggregateFunctionMap, sourceColumnName);
			}
		}

		Graph outputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), isDirected);

		return new GraphContainer(outputGraph, inputTable, nodeAggregateFunctionMap, edgeAggregateFunctionMap, pm);

	}

	private static boolean isPerformingCooccurrenceExtraction(String sourceColumnName,
			String targetColumnName) {
		return sourceColumnName.equals(targetColumnName);
	}
	

	private static Schema createNodeSchema() {
		Schema nodeSchema = new Schema();
		nodeSchema.addColumn("label", String.class);
		return nodeSchema;
	}

	private static Schema createEdgeSchema() {
		Schema edgeSchema = new Schema();
		edgeSchema.addColumn("source", int.class);
		edgeSchema.addColumn("target", int.class);
		return edgeSchema;
	}
	
	private static String buildRowTargetStringFromColumnNames(
			int row, String[] targetColumnNames, Table table, String delimiter) {
		String targetString = "";
		
		// This is outside of the proceeding for loop because that loop appends the delimiter first.
		Column targetColumn = table.getColumn(targetColumnNames[0]);
		targetString += targetColumn.getString(row);
		
		for (int iColumn = 1; iColumn < targetColumnNames.length; iColumn++) {
			targetColumn = table.getColumn(targetColumnNames[iColumn]);
			targetString += (delimiter);
			targetString += targetColumn.getString(row);
		}
		
		return targetString;
	}
	
}
