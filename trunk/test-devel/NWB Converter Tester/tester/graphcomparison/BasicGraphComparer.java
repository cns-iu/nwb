package tester.graphcomparison;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.tuple.TupleSet;
import prefuse.util.collections.IntIterator;

public class BasicGraphComparer implements GraphComparer {
		
	private PrintStream logger;
	
	public BasicGraphComparer() {
		setLogger(System.out);
	}
	
	public void setLogger(PrintStream log) {
		this.logger = logger;
	}
	
	public boolean compare(Graph originalGraph, Graph convertedGraph,
			boolean IdsPreserved) {
		//basic tests
		if (! numNodesEqual(originalGraph, convertedGraph)) 
			return false;
		if (! numEdgesEqual(originalGraph, convertedGraph)) 
			return false;
//		
//		if (IdsPreserved) {
//			//tests for when graph IDs are preserved across the conversion
//		} else {
//			//tests for when graph IDs are NOT preserved across the conversion
//			if (! edgeFrequenciesEqual(originalGraph, convertedGraph))
//				return false;
//			
//		}
		//all tests passed
		return true;
	}
	
	private boolean numNodesEqual(Graph g1, Graph g2) {
		return g1.getNodeCount() == g2.getNodeCount();
	}
	
	private boolean numEdgesEqual(Graph g1, Graph g2) {
		return g1.getEdgeCount() == g2.getEdgeCount();
	}
	
	/*
	 * Tests whether there are an equal numbers of nodes with the same 
	 * number of edges in each graph, e.g. 5 nodes with 1 edge, 12 nodes
	 * with 2 edges etc.. .
	 * 
	 * Possibly useful when graph IDs are modified by the conversion.
	 */
	private boolean edgeFrequenciesEqual(Graph g1, Graph g2) {
		Set e1 = getGraphEdgeNumberFrequencies(g1);
		Set e2 = getGraphEdgeNumberFrequencies(g2);
		
		//If they both contai each other they are equal
		boolean result = e1.containsAll(e2) && e2.containsAll(e1); 
		return result;
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
	private boolean idsAndEdgesAgree(Graph g1, Graph g2) {
		Table table1 = g1.getNodeTable();
		Table table2 = g2.getNodeTable();
		
		return true;
		
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
	}
	
	private Set<Entry<Integer,Integer>> getGraphEdgeNumberFrequencies(Graph g) {
		Map<Integer, Integer> edgeNumberFrequencies 
			= new HashMap<Integer, Integer>();
		
		/*
		 * (might be able to shortcut all of this by counting from 0 to 
		 * numberOfNodes)
		 */
		Table nodeTable = g.getNodeTable();
		for (IntIterator ii = nodeTable.rows(); ii.hasNext();) {
			int nodeID = ii.nextInt();
			Node node = g.getNode(nodeID);
			
			int edges = g.getInDegree(node) + g.getOutDegree(node);
			
			Integer currentFrequency = edgeNumberFrequencies.get(edges);
			if (currentFrequency == null) { 
				/*
				 * A node with this number of edges has not been recorded yet,
				 * so we set the number of occurences to one.
				 */
				edgeNumberFrequencies.put(edges, 1);
			} else {
				/*
				 * A node with this number of edges has been recorded, so
				 * we increment the number of occurences by one.
				 */
				edgeNumberFrequencies.put(edges, currentFrequency++);
			}
		}
		//Now our edgeNumberFrequencies map is filled
		//Time to convert it into a more usable form
		
		Set<Entry<Integer,Integer>> occuranceFrequencyPairs = edgeNumberFrequencies.entrySet();
		
		return occuranceFrequencyPairs;
	}
}
