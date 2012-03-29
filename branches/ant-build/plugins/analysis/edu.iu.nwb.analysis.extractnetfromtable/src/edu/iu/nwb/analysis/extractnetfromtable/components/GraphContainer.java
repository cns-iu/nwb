package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.column.Column;

public class GraphContainer {
	public static final String TARGET_COLUMN_NAME = "target";
	public static final String SOURCE_COLUMN_NAME = "source";
	public static final String LABEL_COLUMN_NAME = "label";

	private Graph graph;
	private Table table;
	private AggregateFunctionMappings nodeMap;
	private AggregateFunctionMappings edgeMap;
	private ProgressMonitor progMonitor = null;

	public GraphContainer(Graph g, Table t, AggregateFunctionMappings nodeFunctionMap,
			AggregateFunctionMappings edgeFunctionMap) {
		this(g, t, nodeFunctionMap, edgeFunctionMap, null);
	}

	public GraphContainer(Graph g, Table t, AggregateFunctionMappings nodeFunctionMap,
			AggregateFunctionMappings edgeFunctionMap, ProgressMonitor pm) {
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
			boolean requestBipartite,
			LogService logger) {
		String[] targetColumnNames = targetColumnName.split("\\,");

		if (this.graph.isDirected()) {
			return buildDirectedGraph(
				sourceColumnName, targetColumnNames, delimiter, requestBipartite, logger);
		} else {
			return buildUndirectedGraph(targetColumnNames, delimiter, logger);
		}
	}

	private Graph buildUndirectedGraph(
			String[] targetColumnNames, String delimiter, LogService logger) {
		boolean duplicateValues = false;
		final HashMap dupValuesErrorMessages = new HashMap();
		int numTotalRows = this.table.getRowCount();
		int numRowsProcessedSoFar = 0;

		NodeMaintainer nodeMaintainer = new NodeMaintainer();

		if (this.progMonitor != null) {
			this.progMonitor.start(ProgressMonitor.WORK_TRACKABLE, numTotalRows);
		}

		for (Iterator rowIt = this.table.rows(); rowIt.hasNext();) {
			int row = ((Integer) rowIt.next()).intValue();

			Node node1 = null;
			Node node2 = null;

			final String targetString =
				buildRowTargetStringFromColumnNames(row, targetColumnNames, this.table, delimiter);

			Set seenObject = new HashSet();

			if (targetString != null) { // no values to extract from
				final Pattern splitPattern = Pattern.compile("\\Q" + delimiter + "\\E");
				final String[] splitTargetStringArray =
					splitIfDelimiterIsValid(targetString, delimiter, splitPattern);

				// Trim each target.
				for (int ii = 0; ii < splitTargetStringArray.length; ii++) {
					splitTargetStringArray[ii] = normalizeLabel(splitTargetStringArray[ii]);
				}

				for (int ii = splitTargetStringArray.length - 1; ii >= 0; ii--) {
					if ("".equals(splitTargetStringArray[ii])) {
						continue;
					}

					if (seenObject.add(splitTargetStringArray[ii])) { // No
						// duplicate
						// nodes.
						node1 = nodeMaintainer.mutateNode(
							splitTargetStringArray[ii],
							null,
							this.graph,
							this.table,
							row,
							this.nodeMap,
							AggregateFunctionMappings.SOURCE_AND_TARGET);
					}

					node1 = this.graph.getNode(this.nodeMap.getFunctionRow(
							new NodeID(splitTargetStringArray[ii], null)).getRowNumber());

					for (int jj = 0; jj < ii; jj++) {
						if ("".equals(splitTargetStringArray[jj])) {
							continue;
						}

						if (!splitTargetStringArray[jj].equals(splitTargetStringArray[ii])) {
							if (seenObject.add(splitTargetStringArray[jj])) { // No
								// duplicate
								// nodes.
								node2 = nodeMaintainer.mutateNode(
									splitTargetStringArray[jj],
									null,
									this.graph,
									this.table,
									row,
									this.nodeMap,
									AggregateFunctionMappings.SOURCE_AND_TARGET);

							}
							// Create or modify an edge as necessary.
							node2 = this.graph.getNode(this.nodeMap.getFunctionRow(
								new NodeID(splitTargetStringArray[jj], null)).getRowNumber());
							EdgeContainer.mutateEdge(
								node1, node2, this.graph, this.table, row, this.edgeMap);
						} else {
							duplicateValues = true; // Detected a self-loop.
						}
					}
				}

				if (duplicateValues) {
					// String title =
					// (String)pdt.get(row,pdt.getColumnNumber("TI"));
					// String title = "unknown";
					// ExtractNetworkFromTable.addDuplicateValueErrorMessage(title,
					// columnName, dupValuesErrorMessages);
					// dupValues = false;
				}
			} else {
				// String title =
				// (String)pdt.get(row,pdt.getColumnNumber("TI"));
				// String title = "unknown";
				// ExtractNetworkFromTable.printNoValueToExtractError(title,
				// columnName, this.log);
			}
			numRowsProcessedSoFar = numRowsProcessedSoFar + 1;

			if (this.progMonitor != null) {
				this.progMonitor.worked(numRowsProcessedSoFar);
			}
		}

		for (Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();) {
			logger.log(LogService.LOG_WARNING, (String) dupValuesErrorMessages.get(dupIter.next()));
		}

		return this.graph;
	}

	private static String normalizeLabel(String label) {
		String[] parts = label.trim().split("\\s+");
		for(int ii = 0; ii < parts.length; ii++) {
			parts[ii] = capitalize(parts[ii]);
		}
		return StringUtilities.implodeStringArray(parts, " ");
	}

	private static String capitalize(String s) {
		if(s.length() <= 1) {
			return s.toUpperCase();
		}
		return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
	}

	private Graph buildDirectedGraph(
			String sourceColumnName,
			String[] targetColumnNames,
			String delimiter,
			boolean requestBipartite,
			LogService log) {
		final Pattern splitPattern = Pattern.compile("\\Q" + delimiter + "\\E");
		final HashMap dupValuesErrorMessages = new HashMap();
		Column sourceColumn = this.table.getColumn(sourceColumnName);

		NodeMaintainer nodeMaintainer = new NodeMaintainer();

		String sourceBipartiteType = null;
		String targetBipartiteType = null;

		if (requestBipartite) {
			sourceBipartiteType = sourceColumnName;
			targetBipartiteType = separate(targetColumnNames, " OR ");
		}

		int numTotalRows = this.table.getRowCount();
		int numRowsProcessedSoFar = 0;

		if (this.progMonitor != null) {
			this.progMonitor.start(ProgressMonitor.WORK_TRACKABLE, numTotalRows);
		}

		for (Iterator rows = this.table.rows(); rows.hasNext();) {
			int row = ((Integer) rows.next()).intValue();

			final String sourceString = sourceColumn.getString(row);
			final String targetString =
				buildRowTargetStringFromColumnNames(row, targetColumnNames, table, delimiter);

			if (sourceString != null && targetString != null) {
				// Split, trim, remove duplicates.
				final String[] sources =
					splitIfDelimiterIsValid(sourceString, delimiter, splitPattern);
				Set cleanSourceNames = clean(sources);
				final String[] targets =
					splitIfDelimiterIsValid(targetString, delimiter, splitPattern);
				Set cleanTargetNames = clean(targets);
				
				// Update nodes.
				Set updatedSources = updateNodes(nodeMaintainer, row, cleanSourceNames,
						sourceBipartiteType, AggregateFunctionMappings.SOURCE);

				Set updatedTargets = updateNodes(nodeMaintainer, row, cleanTargetNames,
						targetBipartiteType, AggregateFunctionMappings.TARGET);

				// Update edges.
				for (Iterator updatedSourcesIt = updatedSources.iterator();
						updatedSourcesIt.hasNext();) {
					Node updatedSource = (Node) updatedSourcesIt.next();
					
					for (Iterator updatedTargetsIt = updatedTargets.iterator();
							updatedTargetsIt.hasNext();) {
						Node updatedTarget = (Node) updatedTargetsIt.next();

						EdgeContainer.mutateEdge(updatedSource, updatedTarget, graph, table, row,
							edgeMap);
					}
				}
			}

			numRowsProcessedSoFar = numRowsProcessedSoFar + 1;

			if (progMonitor != null) {
				progMonitor.worked(numRowsProcessedSoFar);
			}
		}

		for (Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();) {
			log.log(LogService.LOG_WARNING, (String) dupValuesErrorMessages.get(dupIter.next()));
		}

		return graph;
	}
	
	private static String separate(Object[] array, String delimiter) {
		StringBuffer buffer = new StringBuffer();
		
		if (array.length > 0) {
			buffer.append(array[0]);
			
			for (int ii = 1; ii < array.length; ii++) {
				buffer.append(delimiter);
				buffer.append(array[ii]);
			}
		}
		
		return buffer.toString();
	}

	private Set updateNodes(NodeMaintainer nodeMaintainer, int rowIndex, final Set cleanNames,
			String bipartiteType, int aggregateFunctionMappingType) {
		Set updatedNodes = new HashSet();

		for (Iterator cleanNamesIt = cleanNames.iterator(); cleanNamesIt.hasNext();) {
			String cleanName = (String) cleanNamesIt.next();

			Node updatedNode = nodeMaintainer.mutateNode(cleanName, bipartiteType,
					graph, table, rowIndex, nodeMap, aggregateFunctionMappingType);

			updatedNodes.add(updatedNode);
		}

		return updatedNodes;
	}

	// Trim (discarding the empty string) and take only unique entries.
	private static Set clean(final String[] strings) {
		Set cleanedStrings = new HashSet();

		for (int ii = 0; ii < strings.length; ii++) {
			String rawString = strings[ii];
			String trimmedString = normalizeLabel(rawString);

			if (!"".equals(trimmedString)) {
				cleanedStrings.add(trimmedString);
			}
		}

		return cleanedStrings;
	}

	public static GraphContainer initializeGraph(Table pdt, String sourceColumnName,
			String targetColumnName, boolean isDirected, Properties p, LogService log)
			throws InvalidColumnNameException {
		return initializeGraph(pdt, sourceColumnName, targetColumnName, isDirected, p, log,
				null);
	}

	public static GraphContainer initializeGraph(Table inputTable, String sourceColumnName,
			String targetColumnName, boolean isDirected, Properties functions, LogService log, ProgressMonitor progressMonitor)
			throws InvalidColumnNameException {
		final Schema inputSchema = inputTable.getSchema();

		if (inputSchema.getColumnIndex(sourceColumnName) < 0) {
			throw new InvalidColumnNameException(sourceColumnName
					+ " was not a column in this table.\n");
		}

		// Get all of the target column names.
		String[] targetColumnNameArray = targetColumnName.split("\\,");

		// Make sure the one or more column name(s) is/are valid.

		if ((targetColumnNameArray == null) || (targetColumnNameArray.length == 0)) {
			throw new InvalidColumnNameException(targetColumnName
					+ " was not a column in this table.\n");
		} else {
			for (int ii = 0; ii < targetColumnNameArray.length; ii++) {
				if (inputSchema.getColumnIndex(targetColumnNameArray[ii]) < 0) {
					throw new InvalidColumnNameException(targetColumnNameArray[ii]
							+ " was not a column in this table.\n");
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
			 * (For now we only add default edge weights for co-occurrence
			 * extractions). (What this operation would mean for
			 * non-co-occurrence extractions is not yet understood (by me at
			 * least)).
			 */
			// If we haven't already prepared to add an edge weight column...
			if (edgeSchema.getColumnIndex(AggregateFunctionMappings.DEFAULT_WEIGHT_NAME) == -1) {
				AggregateFunctionMappings.addDefaultEdgeWeightColumn(inputSchema, edgeSchema,
						edgeAggregateFunctionMap, sourceColumnName);
			}
		}

		Graph outputGraph = new Graph(nodeSchema.instantiate(), edgeSchema.instantiate(),
				isDirected);

		return new GraphContainer(outputGraph, inputTable, nodeAggregateFunctionMap,
				edgeAggregateFunctionMap, progressMonitor);

	}

	private static boolean isPerformingCooccurrenceExtraction(String sourceColumnName,
			String targetColumnName) {
		return sourceColumnName.equals(targetColumnName);
	}

	private static Schema createNodeSchema() {
		Schema nodeSchema = new Schema();
		nodeSchema.addColumn(LABEL_COLUMN_NAME, String.class);

		return nodeSchema;
	}

	private static Schema createEdgeSchema() {
		Schema edgeSchema = new Schema();
		edgeSchema.addColumn(SOURCE_COLUMN_NAME, int.class);
		edgeSchema.addColumn(TARGET_COLUMN_NAME, int.class);
		return edgeSchema;
	}

	private static String buildRowTargetStringFromColumnNames(
			int row, String[] targetColumnNames, Table table, String delimiter) {
		String targetString = "";

		// This is outside of the proceeding for loop because that loop appends
		// the delimiter first.
		Column targetColumn = table.getColumn(targetColumnNames[0]);
		targetString += targetColumn.getString(row);

		for (int iColumn = 1; iColumn < targetColumnNames.length; iColumn++) {
			targetColumn = table.getColumn(targetColumnNames[iColumn]);
			targetString += (delimiter);
			targetString += targetColumn.getString(row);
		}

		return targetString;
	}

	private static String[] splitIfDelimiterIsValid(
			String toSplit, String delimiter, Pattern splitPattern) {
		if (!StringUtilities.isNull_Empty_OrWhitespace(delimiter)) {
			return splitPattern.split(toSplit);
		} else {
			return new String[] { toSplit };
		}
	}
}
