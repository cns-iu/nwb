package edu.iu.nwb.preprocessing.duplicatenodedetector;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import uk.ac.shef.wit.simmetrics.similaritymetrics.AbstractStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import edu.iu.nwb.preprocessing.duplicatenodedetector.util.GraphSearchAlgorithms;
import edu.iu.nwb.preprocessing.duplicatenodedetector.util.ListMap;

public class DuplicateNodeDetectorAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private static final String SIMILARITY_COLUMN_NAME = "similarity";
    private static final String UNIQUE_INDEX_COLUMN_NAME = "uniqueIndex";
    private static final String COMBINE_VALUES_COLUMN_NAME = "combineValues";
    private final AbstractStringMetric similarityChecker = new JaroWinkler();
    private String compareAttributeName;
    private float mergeOnSimilarity;
    private float makeNoteOnSimilarity;
    private int numPrefixLetters;
    
    public DuplicateNodeDetectorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        //unpack parameters
        this.compareAttributeName = ((String) parameters.get("compareAttribute"));
        this.mergeOnSimilarity = ((Float)parameters.get("mergeOnSimilarity")).floatValue();
        this.makeNoteOnSimilarity = ((Float)parameters.get("makeNoteOnSimilarity")).floatValue();
        this.numPrefixLetters = ((Integer) parameters.get("numPrefixLetters")).intValue();
    }

    public Data[] execute() {
    	//take our input graph graph
    	Graph inputGraph = (Graph) data[0].getData();
    	//make copy of its node table, with added unique index column
    	Table nodeTable = constructAlteredNodeTable(inputGraph);
    	//keep track of nodes we intend to merge, in a graph containing the original input nodes
    	Graph mergeGraph = makeMergeGraph(nodeTable);
    	//keep track of noteworthy similarities between nodes that we find
    	List notableNodeSimilarities = new ArrayList();
    	//for each group of nodes with a common attribute prefix... (with row #)
    	ListMap groupedNodes = sortNodesByAttributePrefix(nodeTable, this.compareAttributeName, this.numPrefixLetters);
    	for (Iterator groupIt = groupedNodes.values().iterator(); groupIt.hasNext();) {
    		List nodeGroup = (List) groupIt.next();
    		//for each pair of nodes in the group...
    			for (int i = 0; i < nodeGroup.size(); i++) {
    				Integer firstNodeIndex = (Integer) nodeGroup.get(i);
    				for (int j = i; j < nodeGroup.size(); j++) {
    					Integer secondNodeIndex = (Integer) nodeGroup.get(j);
    					//test how similar the two nodes are
    					float similarity = compareNodesBy(this.compareAttributeName, firstNodeIndex, secondNodeIndex, nodeTable);
    	    			//if their similarity is noteworthy...
    					if (similarity >= this.makeNoteOnSimilarity) {
    	    				//record it.
    						//DO IT!
    					}
    					//if their similarity is uncanny (or something) ...
    	    			if (similarity >= this.mergeOnSimilarity) {
    	    				//link the nodes in the merge graph  (nodes have node table row #, edges have similarities)
    	    				mergeGraph.addEdge(firstNodeIndex.intValue(), secondNodeIndex.intValue());
    	    			}
    				}
    			}
    	}
  		//extract clusters from the merge graph
    	List clusters = extractWeakComponentClusters(mergeGraph);
		//for each cluster...
    	for (Iterator clusterIt = clusters.iterator(); clusterIt.hasNext();) {
    		LinkedHashSet cluster = (LinkedHashSet) clusterIt.next();
    		//mark that we will merge every node into a single node (using row #'s)
    		//(this step could be made smarter, but is ok for now. Will need user intervention in some cases)
    		Integer[] eachNodeInCluster = (Integer[]) cluster.toArray(new Integer[cluster.size()]);
    		Integer firstNode = null;
    		for (int ii = 0; ii < eachNodeInCluster.length; ii++) {
    			Integer node  = eachNodeInCluster[ii];
    			if (firstNode == null) {
    				//(we arbitrarily choose the first node as the node to merge other nodes into)
    				firstNode = node;
    			} else {
    				Integer nodeBeyondFirst = node;
    				//(we merge nodes beyond the first into the first node)
    				//(off by one, because unique indices are 1-based instead of 0-based, but otherwise correlate with row number.
    				nodeTable.setInt(nodeBeyondFirst.intValue(), UNIQUE_INDEX_COLUMN_NAME, firstNode.intValue() + 1);
    				String iAmNotThePrimaryNode = "";
    				nodeTable.setString(nodeBeyondFirst.intValue(), COMBINE_VALUES_COLUMN_NAME, iAmNotThePrimaryNode);
    			}
    		}
    		
    		//record our actions in the log
    	}

    	final prefuse.data.Table outputTable = nodeTable;
		final Data outputData2 = new BasicData(outputTable,
				prefuse.data.Table.class.getName());	
		final Dictionary tableAttributes = outputData2.getMetadata();
		tableAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		tableAttributes.put(DataProperty.PARENT, data[0]);
		tableAttributes.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		tableAttributes.put(DataProperty.LABEL, "Unique Values from Column "+ this.compareAttributeName);
		
		return new Data[] {outputData2};

    	//return node table with merges and unique index column
    	//AND return log that looks like... (needs clusters and table for name lookup)
    	/*
    	 * for "Andrew W.K"...
    	 * 	merging in "Andow W.K"
    	 * 	merging in "Andrew W."
    	 * 	merging in "Andrew WK"
    	 * .
    	 * .
    	 * .
    	 * 
    	 * Other noteworthy similarities:
    	 * 	NOT merging "Captain Kirk" and "Captain Korp" (needs special report list)
    	 * 	NOT merging "Admiral Ackbar" and "Anvil Ackbar"
    	 * 	.
    	 * 	.
    	 * 	.
    	 */
    }
    
    private float compareNodesBy(String attributeColumn, Integer nodeOneIndex, Integer nodeTwoIndex, Table nodeTable) {
    	String nodeOneAttribute = (String) nodeTable.getString(nodeOneIndex.intValue(), attributeColumn);
     	String nodeTwoAttribute = (String) nodeTable.getString(nodeTwoIndex.intValue(), attributeColumn);
     	float similarity = this.similarityChecker.getSimilarity(nodeOneAttribute, nodeTwoAttribute);
     	return similarity;
    }
    
    private Graph makeMergeGraph(Table nodeTable) {
    	//same nodes as original graph, but we will build different edges to show which nodes we intend to merge
    	
    	Table edgeTable = new Table();
    	edgeTable.addColumn(Graph.DEFAULT_SOURCE_KEY, Integer.TYPE);
    	edgeTable.addColumn(Graph.DEFAULT_TARGET_KEY, Integer.TYPE);
    	edgeTable.addColumn(SIMILARITY_COLUMN_NAME, Float.TYPE);

    	
    	boolean isDirected = false;
    	Graph mergeGraph = new Graph(nodeTable, edgeTable, isDirected);
    	
    	return mergeGraph;
    }
    
	private Table constructAlteredNodeTable(prefuse.data.Graph graph) {
		Table outputTable = new Table();
		outputTable = createTableSchema(graph.getNodeTable().getSchema(),
				outputTable);
		outputTable = populateTable(outputTable, graph);
		return outputTable;
	}
    
//    public Data[] execute() {
//    	//get the node information from the input table 
//    	Graph inputGraph = (Graph) data[0].getData();
//    	Table nodeTable = inputGraph.getNodeTable();
//    	//sort the nodes by the first 'numPrefixLetters' letters in specified attribute
//    	ListMap nodesByAttrPrefix = sortNodesByAttributePrefix(nodeTable, this.compareAttributeName, this.numPrefixLetters);
//        AbstractStringMetric similarityChecker = new JaroWinkler();
//    	MergesAndMergeNotes allMergesAndMergeNotes = new MergesAndMergeNotes();
//    	//for each group of attributes sorted by prefix...
//    	for (Iterator nodesByAttrIt = nodesByAttrPrefix.keySet().iterator(); nodesByAttrIt.hasNext();) {
//    		//compare each node's attribute with everyone other node's attribute
//    		String nodeKey = (String) nodesByAttrIt.next();
//    		List nodesWithCommonAttrPrefix = nodesByAttrPrefix.get(nodeKey);
//    		
//    		for (int ii = 0; ii < nodesWithCommonAttrPrefix.size(); ii++) {
//    			Integer node = (Integer) nodesWithCommonAttrPrefix.get(ii);
//    			MergesAndMergeNotes mergesAndMergeNotes = recordSimilarNodes(similarityChecker, nodeTable, node, nodesWithCommonAttrPrefix);
//    			allMergesAndMergeNotes.addAll(mergesAndMergeNotes);
//    		}	
//    		
//    		List merges = allMergesAndMergeNotes.merges;
//    		Table mergeTable = makeMergeTable(nodeTable, merges);
//    	}
//    	
//    	//Make a note in our output log, if two are above the "makeNoteOnSimilarity" threshold.
//    	
//        return null;
//    }
    
//    private Table makeMergeTable(Table nodeTable, List merges) {
//    	Graph mergeGraph = makeMergeGraph(merges);
//    	List clusters = extractWeakComponentClusters(mergeGraph);
//    	Table mergeTable = new Table();
//    	for (int ii = 0; ii < clusters.size(); ii++) {
//    		LinkedHashSet cluster = (LinkedHashSet) clusters.get(ii);
//    		for (int jj = 0; jj < cluster.size(); jj++) {
//    			
//    		}
//    	}
//    }
    
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
    
    private Graph makeMergeGraph(List merges) {
    	Graph mergeGraph = new Graph();
    	Map idToRow = new HashMap();
    	for (int ii = 0; ii < merges.size(); ii++) {
    		SimilarityInfo mergeLink = (SimilarityInfo) merges.get(ii);
    		Integer startNodeRow = (Integer) idToRow.get(new Integer(mergeLink.node1)); 
    		if (startNodeRow == null) {
    			//isn't in the graph already
    			startNodeRow = new Integer(mergeGraph.addNodeRow());
    		
    			idToRow.put(new Integer(mergeLink.node1), startNodeRow);
    		}
    		
    		Integer endNodeRow = (Integer) idToRow.get(new Integer(mergeLink.node2));
    		if (endNodeRow == null) {
    			//isn't in the graph already
    			endNodeRow = new Integer(mergeGraph.addNodeRow());
    			idToRow.put(new Integer(mergeLink.node2), endNodeRow);
    		}
    		
    		int existingEdge = mergeGraph.getEdge(startNodeRow.intValue(), endNodeRow.intValue());
    		if (existingEdge == -1) {
    			mergeGraph.addEdge(startNodeRow.intValue(), endNodeRow.intValue());
    		}
    	}
    	
    	return mergeGraph;
    }
    
	public List extractWeakComponentClusters(final Graph grph){
		List clusters = new ArrayList();
		
		HashSet seenNodes = new HashSet();
		for(Iterator it = grph.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			Integer i = new Integer(n.getRow());
			if(!seenNodes.contains(i)){

				LinkedHashSet cluster = GraphSearchAlgorithms.undirectedDepthFirstSearch(grph, i);
				seenNodes.addAll(cluster);
				clusters.add(cluster);
				}			
		}
		return clusters;
	}
    
    private MergesAndMergeNotes recordSimilarNodes(
    		AbstractStringMetric similarityChecker, Table nodeTable, Integer node, List nodesWithCommonPrefix) {
    	List merges = new ArrayList();
    	List mergeNotes = new ArrayList();
    	
    	String nodeLabel = (String) nodeTable.get(node.intValue(), compareAttributeName);
    	for (int ii = 0; ii < nodesWithCommonPrefix.size(); ii++) {
    		Integer otherNode = (Integer) nodesWithCommonPrefix.get(ii);
    		if (node == otherNode) continue;
    		String otherNodeLabel = (String) nodeTable.get(otherNode.intValue(), compareAttributeName);
    		float similarity = similarityChecker.getSimilarity(nodeLabel, otherNodeLabel);
    		if (similarity > mergeOnSimilarity) {
    			System.out.println("MERGING: (" + similarity + ")");
    			System.out.println("  " + nodeLabel);
    			System.out.println("  " + otherNodeLabel);
    			SimilarityInfo similarityInfo = new SimilarityInfo(node.intValue(), otherNode.intValue(), similarity);
    			merges.add(similarityInfo);
    			mergeNotes.add(similarityInfo);
    		} else if (similarity > makeNoteOnSimilarity) {
    			System.out.println("NOTE: (" + similarity + ")");
    			System.out.println("  " + nodeLabel);
    			System.out.println("  " + otherNodeLabel);
    			mergeNotes.add(new SimilarityInfo(node.intValue(), otherNode.intValue(), similarity));
    		}
    	}
    	
    	return new MergesAndMergeNotes(merges, mergeNotes);
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
	
	//sad result of not having support for tuples or multiple return values
	
	private class MergesAndMergeNotes {
		//lists of merge
		public List merges;
		public List notes;
		
		public MergesAndMergeNotes() {
			this.merges = new ArrayList();
			this.notes = new ArrayList();
		}
		
		public MergesAndMergeNotes(List merges, List notes) {
			this.merges = merges;
			this.notes = notes;
		}
		
		public void addAll(MergesAndMergeNotes otherOne) {
			merges.addAll(otherOne.merges);
			notes.addAll(otherOne.notes);
		}
	}
	
	private class  SimilarityInfo {
    	public int node1;
    	public int node2;
    	public float similarity;
    	
    	public SimilarityInfo(int node1, int node2, float similarity) {
    		this.node1 = node1;
    		this.node2 = node2;
    		this.similarity = similarity;
    	}
    }
}