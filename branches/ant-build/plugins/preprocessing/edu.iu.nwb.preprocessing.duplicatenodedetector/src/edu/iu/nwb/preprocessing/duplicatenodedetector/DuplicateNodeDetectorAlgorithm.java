package edu.iu.nwb.preprocessing.duplicatenodedetector;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.TableUtilities;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import edu.iu.nwb.preprocessing.duplicatenodedetector.util.GraphSearchAlgorithms;
import edu.iu.nwb.preprocessing.duplicatenodedetector.util.ListMap;


public class DuplicateNodeDetectorAlgorithm implements Algorithm {	
	private static final String TEXT_TYPE = "file:text/plain";
	public static final String SIMILARITY_COLUMN_NAME = "similarity";
	public static final String UNIQUE_INDEX_COLUMN_NAME = "uniqueIndex";
	public static final String COMBINE_VALUES_COLUMN_NAME = "combineValues";
	/* This value for a node's COMBINE_VALUE_COLUMN_NAME attribute
	 * means that it will be merged with another node (the primary one).
	 */
	public static final String NOT_THE_PRIMARY_NODE = "";
	
	private final AbstractStringMetric similarityChecker = new Jaro();
	
	private Data inputData;
	
	private String compareAttributeName;
	private float mergeOnSimilarity;
	private float makeNoteOnSimilarity;
	private int numPrefixLetters;

	public DuplicateNodeDetectorAlgorithm(Data[] data, Dictionary parameters) {
		this.inputData = data[0];

		// Unpack parameters.
		this.compareAttributeName = (String) parameters.get("compareAttribute");
		this.mergeOnSimilarity =
			((Float)parameters.get("mergeOnSimilarity")).floatValue();
		this.makeNoteOnSimilarity =
			((Float)parameters.get("makeNoteOnSimilarity")).floatValue();
		this.numPrefixLetters =
			((Integer) parameters.get("numPrefixLetters")).intValue();
	}

	public Data[] execute() throws AlgorithmExecutionException {
		// TODO: Clean these comments up.
		//get our input graph
		Graph inputGraph = (Graph) inputData.getData();
		//make copy of its node table, with added unique index column for merging purposes
		Table nodeTable = constructAlteredNodeTable(inputGraph);
		//keep human-readable log of noteworthy similarities between nodes that we find
		StringBuffer noteLog = new StringBuffer();
		//make a graph, with links between nodes we want to merge
		Graph mergeGraph = makeMergeGraph(nodeTable, noteLog);
		//keep track of which nodes we merged into which, in a human-readable format.
		StringBuffer mergeLog = new StringBuffer();
		//make our merge graph into a table that the "Merge Nodes" algorithm can use
		Table mergeTable = createTableWithMergeInfo(nodeTable, mergeGraph, mergeLog);
		//return the mergeTable and our logs, formatted nicely for the Data Manager
		Data[] mergeTableAndLogData = formatAsData(mergeTable, noteLog, mergeLog);
		
		return mergeTableAndLogData;
	}

	private Table constructAlteredNodeTable(Graph graph) {
		Table outputTable = new Table();
		outputTable = createTableSchema(graph.getNodeTable().getSchema(),
				outputTable);
		outputTable = populateTable(outputTable, graph);
		return outputTable;
	}

	private Graph makeMergeGraph(Table nodeTable, StringBuffer noteLog) {
		Graph mergeGraph = makeEmptyMergeGraph(nodeTable);
		//for each group of nodes with a common attribute prefix...
		ListMap groupedNodes =
			sortNodesByAttributePrefix(
					nodeTable, compareAttributeName, numPrefixLetters);
		
		List noteworthySimilarityReports = new ArrayList();
		
		for (Iterator groupIt = groupedNodes.values().iterator();
				groupIt.hasNext();) {
			List nodeGroup = (List) groupIt.next();
			//for each pair of nodes in the group...
			for (int ii = 0; ii < nodeGroup.size(); ii++) {
				Integer firstNodeIndex = (Integer) nodeGroup.get(ii);
				for (int jj = ii; jj < nodeGroup.size(); jj++) {
					Integer secondNodeIndex = (Integer) nodeGroup.get(jj);
					// Test how similar the two nodes are.
					float similarity =
						compareNodesCaseInsensitiveBy(
								compareAttributeName,
								firstNodeIndex,
								secondNodeIndex,
								nodeTable);

					// If their similarity is high enough to merge..
					if (similarity >= this.mergeOnSimilarity) {
						// Link the nodes in the merge graph
						mergeGraph.addEdge(
								firstNodeIndex.intValue(),
								secondNodeIndex.intValue());
					}
					// Else if their similarity is noteworthy..
					else if (similarity >= this.makeNoteOnSimilarity) {
						// Record it for logging.					
						String leftName =
							nodeTable.getString(
									firstNodeIndex.intValue(),
									compareAttributeName);
						String rightName =
							nodeTable.getString(
									secondNodeIndex.intValue(),
									compareAttributeName);
						
						noteworthySimilarityReports.add(
								new SimilarityReport(
										leftName, rightName, similarity));
					}
				}
			}
		}
		
		/* Report the similarities that were great enough to be noteworthy,
		 * but not great enough to warrant a merge.
		 */
		if (!(noteworthySimilarityReports.isEmpty())) {
			// Put into descending order by similarity.
			Collections.sort(noteworthySimilarityReports);
			Collections.reverse(noteworthySimilarityReports);
			
			for (Iterator noteworthyIt = noteworthySimilarityReports.iterator();
					noteworthyIt.hasNext();) {
				SimilarityReport report =
					(SimilarityReport) noteworthyIt.next();
				
				noteLog.append("" + report.similarity + " similar:" + "\r\n");
				noteLog.append("  \"" + report.leftName + "\"" + "\r\n");
				noteLog.append("  \"" + report.rightName + "\"" + "\r\n");
			}
		}

		return mergeGraph;
	}
	
	private static class SimilarityReport implements Comparable {
		protected String leftName;
		protected String rightName;
		protected double similarity;

		public SimilarityReport(
				String nameLeft, String nameRight, double similarity) {
			this.leftName = nameLeft;
			this.rightName = nameRight;
			this.similarity = similarity;
		}

		public int compareTo(Object other) {
			if (other instanceof SimilarityReport) {
				SimilarityReport that = (SimilarityReport) other;
				
				if (this.similarity < that.similarity) {
					return -1;
				} else if (this.similarity > that.similarity) {
					return +1;
				} else { // this.similarity == that.similarity, one would hope.
					return 0;
				}
			} else {
				throw new ClassCastException(
						"A SimilarityReport can only be compared " +
						"to other SimilarityReports.");
			}
		}
	}

	/* For each weak component cluster in mergeGraph,
	 * we pick from it a primary, representative node,
	 * and merge any other nodes in its cluster into that one.
	 * We set values on nodeTable to record merging information and report
	 * merges in the mergeLog.
	 */
	private Table createTableWithMergeInfo(
			Table oldNodeTable, Graph mergeGraph, StringBuffer mergeLog) {
		Table newNodeTable = TableUtilities.copyTable(oldNodeTable);
		
		/* Get all the clusters in our merge graph
		 * (where each cluster is a group of nodes to be merged).
		 */
		List clusters = extractWeakComponentClusters(mergeGraph);
		
		mergeLog.append("Merge report" + "\n");
		mergeLog.append(
				"Similarly named entities will be merged " +
				"into the one with the longest name." + "\n" + "\n");
		
		// Purely for formatting in the merge log.  1-indexed for users.
		int mergeReportIndex = 1;

		for (Iterator clusterIt = clusters.iterator(); clusterIt.hasNext();) {
			Collection cluster = (Collection) clusterIt.next();
			
			// When the cluster has only one node, there's nothing to merge.
			if (cluster.size() <= 1) {
				continue;
			}
			
			StringBuffer mergeLogPiece =
				setMergeInfoForCluster(newNodeTable, cluster, mergeReportIndex);
			mergeLog.append(mergeLogPiece);
			
			mergeReportIndex++;
		}
		
		mergeLog.append("End of merge report.\n");
		
		return newNodeTable;
	}
	
	private StringBuffer setMergeInfoForCluster(
			Table newNodeTable, Collection cluster, int mergeReportIndex) {
		StringBuffer mLog = new StringBuffer();
		
		mLog.append(
				"======== Merge " + mergeReportIndex + " ========" + "\n");
		
		
		Integer primaryNode =
			selectNodeWithLongestAttributeValue(
					newNodeTable, cluster, compareAttributeName);
		String primaryNodeName =
			newNodeTable.getString(
					primaryNode.intValue(),	compareAttributeName);
		/* Correct for that unique indices are 1-based rather than 0-based,
		 * but otherwise correlate with row index.
		 */
		int uniqueIndex = primaryNode.intValue() + 1;
		
		mLog.append(
				primaryNodeName + " will have the following merged in:" + "\n");
		
		for (Iterator nodeIt = cluster.iterator(); nodeIt.hasNext();) {
			Integer node = (Integer) nodeIt.next();
			
			/* Skip the primary node.
			 * We don't want to try to merge it with itself.
			 */
			if (primaryNode.equals(node)) {
				continue;
			}
			
			String name = setMergeInfoForNode(newNodeTable, node, uniqueIndex);
			
			mLog.append(name + "\n");
		}
		
		mLog.append("\n");
		
		return mLog;
	}

	private String setMergeInfoForNode(
			Table newNodeTable, Integer node, int uniqueIndex) {
		// Mark intent to merge in the table.
		newNodeTable.setInt(
				node.intValue(),
				UNIQUE_INDEX_COLUMN_NAME,
				uniqueIndex);				
		newNodeTable.setString(
				node.intValue(),
				COMBINE_VALUES_COLUMN_NAME,
				NOT_THE_PRIMARY_NODE);
		
		// Report intent to merge.
		String name =
			newNodeTable.getString(
					node.intValue(),
					compareAttributeName);
		
		return name;
	}
	
	/**
	 * Find a node index for which the (String) value
	 * of attributeKey in table has maximal length.
	 * @param table			A table of nodes into which nodeIndices indexes.
	 * @param nodeIndices	Competitors for the longest attribute value.
	 * @param attributeKey	Key of the attribute for comparison.
	 * @return null when nodeIndices is empty or
	 * table.canGetString(attributeKey) is false. 
	 */
	private Integer selectNodeWithLongestAttributeValue(
			Table table, Collection nodeIndices, String attributeKey) {
		if (nodeIndices.isEmpty()) {
			throw new IllegalArgumentException("Must give at least one node.");
		}
		
		int longestAttributeValue = Integer.MIN_VALUE;
		Integer winningNodeIndex = null;
		
		for (Iterator nodeIndexIt = nodeIndices.iterator();
				nodeIndexIt.hasNext();) {
			Integer nodeIndex = (Integer) nodeIndexIt.next();
			
			if (table.canGetString(attributeKey)) {
				String attributeValue =
					table.getString(nodeIndex.intValue(), attributeKey);
				
				/* Because this is ">" (and not ">="),
				 * the selected node index is the first encountered
				 * (in the iteration order) among any ties
				 *  for longest attribute value.
				 */
				if (attributeValue.length() > longestAttributeValue) {
					longestAttributeValue = attributeValue.length();
					winningNodeIndex = nodeIndex;
				}
			}
		}
		
		return winningNodeIndex;
	}

	private Data[] formatAsData(Table nodeTable, StringBuffer noteLog, StringBuffer mergeLog) throws AlgorithmExecutionException {
		//format nodeTable
		final Data nodeTableData = new BasicData(nodeTable,
				Table.class.getName());
		final Dictionary tableAttributes = nodeTableData.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, inputData);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Merge Table: based on "+ this.compareAttributeName);

		//format nodeLog
		final File nodeLogFile = this.stringToFile(noteLog.toString(), "nodeLog");
		final Data nodeLogData = new BasicData(nodeLogFile, TEXT_TYPE);
		final Dictionary nodeAttr = nodeLogData.getMetadata();
		nodeAttr.put(DataProperty.PARENT, inputData);
		nodeAttr.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		nodeAttr.put(DataProperty.LABEL, "Text Log: Noteworthy nodes that will NOT be merged");

		//format mergeLog
		final File mergeLogFile = this.stringToFile(mergeLog.toString(), "mergeLog");
		final Data mergeLogData = new BasicData(mergeLogFile, TEXT_TYPE);
		final Dictionary mergeAttr = mergeLogData.getMetadata();
		mergeAttr.put(DataProperty.PARENT, inputData);
		mergeAttr.put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
		mergeAttr.put(DataProperty.LABEL, "Text Log: Nodes that will be merged");


		return new Data[] {nodeTableData, mergeLogData, nodeLogData};

	}

	//    private float compareNodesCaseSensitiveBy(String attributeColumn, Integer nodeOneIndex, Integer nodeTwoIndex, Table nodeTable) {
	//    	String nodeOneAttribute = (String) nodeTable.getString(nodeOneIndex.intValue(), attributeColumn);
	//     	String nodeTwoAttribute = (String) nodeTable.getString(nodeTwoIndex.intValue(), attributeColumn);
	//     	float similarity = this.similarityChecker.getSimilarity(nodeOneAttribute, nodeTwoAttribute);
	//     	return similarity;
	//    }

	private float compareNodesCaseInsensitiveBy(String attributeColumn, Integer nodeOneIndex, Integer nodeTwoIndex, Table nodeTable) {
		String nodeOneAttribute = nodeTable.getString(nodeOneIndex.intValue(), attributeColumn);
		String nodeTwoAttribute = nodeTable.getString(nodeTwoIndex.intValue(), attributeColumn);
		float similarity = this.similarityChecker.getSimilarity(nodeOneAttribute.toLowerCase(), nodeTwoAttribute.toLowerCase());
		return similarity;
	}

	private Graph makeEmptyMergeGraph(Table nodeTable) {
		//same nodes as original graph, but we will build different edges to show which nodes we intend to merge

		Table edgeTable = new Table();
		edgeTable.addColumn(Graph.DEFAULT_SOURCE_KEY, Integer.TYPE);
		edgeTable.addColumn(Graph.DEFAULT_TARGET_KEY, Integer.TYPE);
		edgeTable.addColumn(SIMILARITY_COLUMN_NAME, Float.TYPE);


		boolean isDirected = false;
		Graph mergeGraph = new Graph(nodeTable, edgeTable, isDirected);

		return mergeGraph;
	}

	private Table createTableSchema(Schema graphSchema, Table t) {
		for (int i = 0; i < graphSchema.getColumnCount(); i++) {
			t.addColumn(graphSchema.getColumnName(i), graphSchema.getColumnType(i));
		}
		t.addColumn(UNIQUE_INDEX_COLUMN_NAME, int.class);
		t.addColumn(COMBINE_VALUES_COLUMN_NAME, String.class, "*");
		return t;
	}

	private Table populateTable(Table t, Graph g) {
		for (final Iterator it = g.nodes(); it.hasNext();) {
			final Node n = (Node) it.next();
			t.addRow();
			for (int i = 0; i < n.getColumnCount(); i++) {
				t.set(t.getRowCount() - 1, i, n.get(i));
			}
			t.set(t.getRowCount() - 1, UNIQUE_INDEX_COLUMN_NAME, new Integer(t
					.getRowCount()));
		}
		return t;
	}

	public List extractWeakComponentClusters(final Graph graph) {
		List clusters = new ArrayList();

		HashSet seenNodes = new HashSet();
		for (Iterator it = graph.nodes(); it.hasNext();) {
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if (!seenNodes.contains(i)) {
				LinkedHashSet cluster =
					GraphSearchAlgorithms.undirectedDepthFirstSearch(graph, i);
				seenNodes.addAll(cluster);
				clusters.add(cluster);
			}
		}
		
		return clusters;
	}


	private ListMap sortNodesByAttributePrefix(Table nodeTable, String compareAttributeName, int numPrefixLetters) {
		ListMap nodesByAttributePrefix = new ListMap();
		// for each node in the node table...
		for (IntIterator nodeIndexIt = nodeTable.rows(); nodeIndexIt.hasNext();) {
			int nodeIndex = nodeIndexIt.nextInt();
			Tuple row = nodeTable.getTuple(nodeIndex);
			// get the attribute contents
			String comparisonAttributeContents = row.getString(compareAttributeName);
			if (comparisonAttributeContents == null) continue;
			//add the node index to our list map, in the bin with a key made from a prefix of the attribute e.g. "do" for "donkey"
			String prefixKey = extractPrefixKey(comparisonAttributeContents, numPrefixLetters);
			nodesByAttributePrefix.put(prefixKey, new Integer(nodeIndex));
		}

		return nodesByAttributePrefix;
	}

	private String extractPrefixKey(String s, int prefixLength) {
		if (prefixLength <= s.length() && prefixLength >= 1) {
			return s.substring(0, prefixLength);
		} else if (prefixLength > s.length()) {
			return s;
		} else { //prefixLength <= 0
			return "";
		}
	}

	private File stringToFile(String s, String fileName) throws AlgorithmExecutionException {
		try {
			File outFile = File.createTempFile(fileName, "txt");
			FileWriter out = new FileWriter(outFile);
			out.write(s);
			out.close();
			return outFile;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Could not create file from string", e);
		}
	}
}