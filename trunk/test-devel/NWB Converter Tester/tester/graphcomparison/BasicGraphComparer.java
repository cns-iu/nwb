package tester.graphcomparison;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.tuple.TableEdge;
import prefuse.util.collections.IntIterator;

/**
 * 
 * @author mwlinnem
 *
 */
public class BasicGraphComparer implements GraphComparer {

	public ComparisonResult compare(Graph g1, Graph g2, boolean IdsPreserved) {
		//basic tests
		
		if (! isSameDirectedness(g1, g2)) {
			return new ComparisonResult(false, "Directedness not of the " +
					"same type.");
		} else if (! isEqualNodeCount(g1, g2)) {
			return new ComparisonResult(false, "Node counts not equal.");
		} else if (! isEqualEdgeCount(g1, g2)) {
			return new ComparisonResult(false, "Edge counts not equal.");
		}
		
		//complex tests
		
		if (IdsPreserved) {
			//tests for when graph IDs are preserved across the conversion
			if (! nodesHaveSameNeighbors(g1, g2)) 
				return new ComparisonResult(false, "Nodes do not connect to" +
						" the same nodes in both graphs.");
		} else {
			//tests for when graph IDs are NOT preserved across the conversion
			if (! nodeDegreeFrequenciesEqual(g1, g2))
				return new ComparisonResult(false, "The number of nodes" +
						"with a certain number of edges is not the same in" +
						"both graphs.");
		}
		
		//all tests passed
		return new ComparisonResult(true, "All tests succeeded.");
	}
	
	private boolean isSameDirectedness(Graph g1, Graph g2) {
		boolean result = g1.isDirected() == g2.isDirected();
		return result;
	}
	
	private boolean isEqualNodeCount(Graph g1, Graph g2) {
		boolean result =  g1.getNodeCount() == g2.getNodeCount();
		return result;
	}
	
	private boolean isEqualEdgeCount(Graph g1, Graph g2) {
		boolean result =  g1.getEdgeCount() == g2.getEdgeCount();
		return result;
	}
	
	/*
	 * Tests whether there are an equal numbers of nodes with the same 
	 * number of edges in each graph, e.g. 5 nodes with 1 edge, 12 nodes
	 * with 2 edges etc.. .
	 * 
	 * Possibly useful when graph IDs are modified by the conversion.
	 */
	private boolean nodeDegreeFrequenciesEqual(Graph g1, Graph g2) {
		Set e1 = getNodeDegreeFrequencies(g1);
		Set e2 = getNodeDegreeFrequencies(g2);
		
		boolean result = e1.equals(e2);
		return result;
	}
	
	private Set<Entry<Integer,Integer>> getNodeDegreeFrequencies(Graph g) {
		Map<Integer, Integer> nodeDegreeFrequencies 
			= new HashMap<Integer, Integer>();
		
		/*
		 * TODO: (might want to shortcut all of this by counting from 0 to 
		 * numberOfNodes)
		 */
		Table nodeTable = g.getNodeTable();
		for (IntIterator ii = nodeTable.rows(); ii.hasNext();) {
			int nodeID = ii.nextInt();
			Node node = g.getNode(nodeID);
			
			int numEdges = g.getInDegree(node) + g.getOutDegree(node);
			
			Integer currentFrequency = nodeDegreeFrequencies.get(numEdges);
			if (currentFrequency == null) { 
				/*
				 * A node with this number of edges has not been recorded yet,
				 * so we set the number of occurrences to one.
				 */
				nodeDegreeFrequencies.put(numEdges, 1);
			} else {
				/*
				 * A node with this number of edges has been recorded, so
				 * we increment the number of occurrences by one.
				 */
				nodeDegreeFrequencies.put(numEdges, currentFrequency++);
			}
		}

		//convert the result to a more usable format.
		Set<Entry<Integer,Integer>> nodeFrequencyPairs = nodeDegreeFrequencies.entrySet();
		
		return nodeFrequencyPairs;
	}
	
	private boolean nodesHaveSameNeighbors(Graph g1, Graph g2) {
		Map map1 = generateNodeEdgeMap(g1);
		Map map2 = generateNodeEdgeMap(g2);
		
		boolean result = map1.equals(map2);
		return result;
	}
	
	private Map generateNodeEdgeMap (Graph g) {
		String nodeKeyField = g.getNodeKeyField();
		if (nodeKeyField != null) {
			//can't support this yet. Maybe throw exception or something later
			return null;
		}
		
		Map<Integer, Set<Integer>> nodeEdgeMap 
			= new HashMap<Integer, Set<Integer>>();
		
		for (int nodeID = 0; nodeID < g.getNodeCount(); nodeID++) {
			Node node = g.getNode(nodeID);
			
			Set<Integer> edgeNodes = new HashSet<Integer>();
			for (Iterator ii = node.outNeighbors(); ii.hasNext();) {
				Node edgeNode = (Node) ii.next();
				edgeNodes.add(edgeNode.getRow());
			}
			
			nodeEdgeMap.put(node.getRow(), edgeNodes);
		}
		
		return nodeEdgeMap;
	}
	
	/*
	 * Compares nodes in each graph with the same ID to see whether they have
	 * same number of edges. Returns false if IDs do not match, or if number
	 * of edges is not the same for nodes whose IDs do match.
	 * 
	 * For now this assumes that the conversion preserved the order of the IDs, 
	 * i.e. If the conversion worked then if the first node in g2 has an id of 
	 * 5 then so will the first node of g2.
	 */
//	private boolean idsAndEdgesAgreeOrig(Graph g1, Graph g2) {
//		Table table1 = g1.getNodeTable();
//		Table table2 = g2.getNodeTable();
//		
//		return true;
		
		//UNDER CONSTRUCTION
		
		//-----------
//		TupleSet nodeTuples = g.getNodes();
//		
//		/*
//		 * The name of the key field can vary. This ensures that the name of 
//		 * the field we use to get the key is correct.
//		 */
//		String nodeKeyField = g.getNodeKeyField();
//		
//		for (Iterator ii = nodeTuples.tuples(); ii.hasNext();) {
//			
//		}
//
//		Tuple nodeTuple = (Tuple) ii.next();
//		long nodeKey = nodeTuple.getLong(nodeKeyField);
//		//g.getNo
//	}
	
	public static void main(String[] args) {
		//setup
		GraphComparer comparer = new BasicGraphComparer();
		
		Schema edgeTableSchema = new Schema();
		edgeTableSchema.addColumn(Graph.DEFAULT_SOURCE_KEY, Integer.class);
		edgeTableSchema.addColumn(Graph.DEFAULT_TARGET_KEY, Integer.class);
		
		//test1
		Graph emptyGraph1 = new Graph();
		Graph emptyGraph2 = new Graph();
		
		ComparisonResult result1 = comparer.compare(emptyGraph1, 
				emptyGraph2, true);	
		System.out.println("Empty undirected graph test ... " + result1);
		
		//test2
		Graph directedGraph1 = new Graph(true);
		Graph directedGraph2 = new Graph(true);
		
		ComparisonResult result2 = comparer.compare(directedGraph1,
				directedGraph2, true);		
		System.out.println("Empty directed graph test ... " + result2);
		
		//test3
		Table nodeTable1 = new Table();
		nodeTable1.addRows(10);
		
		Table nodeTable2 = new Table();
		nodeTable2.addRows(10);
		
		Graph noEdgeGraph1 = new Graph(nodeTable1, true);
		Graph noEdgeGraph2 = new Graph(nodeTable2, true);
		
		ComparisonResult result3 = comparer.compare(noEdgeGraph1,
				noEdgeGraph2, true);
		System.out.println("No edge graph test ... " + result3);
		
		//test4 (should fail)
		Table nodeTable3 = new Table();
		nodeTable3.addRows(11);
		
		Graph noEdgeGraph3 = new Graph(nodeTable3, true);
		
		ComparisonResult result4 = comparer.compare(noEdgeGraph1,
				noEdgeGraph3, true);
		System.out.println("No edge graph test 2 (should fail) ... " + result4);
		
		//test5
//		Table nodeTable4 = new Table();
//		nodeTable1.addRows(4);
//		
//		Table edgeTable4 = new Table();
//		edgeTable4.addColumns(edgeTableSchema);
//		edgeTable4.addRows(4);
//		
//		//edgeTable4.set(0, Graph.DEFAULT_Source_KEY, arg2)
	}
//	
//	private static void addEdge(Table edgeTable, int sourceID, int targetID) {
//		TableEdge edge = new TableEdge(); 
//		edge.set
//	}

}
