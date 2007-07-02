package tester.graphcomparison;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.util.Sort;
import prefuse.util.collections.IntIterator;

/**
 * 
 * @author mwlinnem
 *
 */
public class DefaultGraphComparer implements GraphComparer {

	public ComparisonResult compare(Graph g1, Graph g2, boolean idsPreserved) {
		if (g1 == null || g2 == null) {
			return new ComparisonResult(false, "At least one of the provided" +
					" graphs was null.");
		}
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
		if (idsPreserved) {
			//tests for when graph IDs are preserved across the conversion	
			if (! areEqual(g1, g2,  true)) 
				return new ComparisonResult(false, "Graphs do not have the " +
						"same contents.");		
		} else {
			//tests for when graph IDs are NOT preserved across the conversion
			if (! nodeDegreeFrequenciesEqual(g1, g2))
				return new ComparisonResult(false, "The number of nodes" +
						"with a certain number of edges is not the same in" +
						"both graphs.");		
			
			/*
			 * TODO: we could really use a graph isomorphism comparison right
			 * here. nodeDegreeFrequencies will catch some errors, but lets
			 * a lot through.
			 */	
			
			if (! haveSameNodeAttributes(g1, g2))
				return new ComparisonResult(false, "Node attributes are not " +
						"the same in both graphs.");
			
			if (! haveSameEdgeAttributes(g1, g2)) 
				return new ComparisonResult(false, "Edge attributes are not " +
						"the same in both graphs.");
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
	
	/*
	 * Helper method for nodeDegreeFrequenciesEqual
	 */
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
		Set<Entry<Integer,Integer>> nodeFrequencyPairs 
			= nodeDegreeFrequencies.entrySet();
		
		return nodeFrequencyPairs;
	}

	
	private boolean haveSameNodeAttributes(Graph g1, Graph g2) {
		Table t1 = getStrippedNodeTable(g1);
		Table t2 = getStrippedNodeTable(g2);
		boolean result = areEqualWhenSorted(t1, t2);
		return result;
	}

	/*
	 * Determines whether the two graphs have the same edge attributes.
	 * That is, for every edge in table A there is an edge in table B with
	 * the exactly the same attribute values, and vice versa. Has no regard 
	 * for source and target IDs, or the order the edgesappear in the edge 
	 * tables.
	 */
	private boolean haveSameEdgeAttributes(Graph g1, Graph g2) {
		//remove the IDs
		Table t1 = getStrippedEdgeTable(g1.getEdgeTable());
		Table t2 = getStrippedEdgeTable(g2.getEdgeTable());
				
		boolean result = areEqualWhenSorted(t1, t2);
		return result;
	}
	
	
	
	/**
	 * Removes source and target columns from a copied version of the table.
	 * 
	 * Helper method for haveSameEdgeAttributes
	 * 
	 * @param t the original table
	 * @return a stripped copy of the original table
	 */
	private Table getStrippedEdgeTable(Table t) {
		Table tCopy = copyTable(t);
		tCopy.removeColumn(Graph.DEFAULT_SOURCE_KEY);
		tCopy.removeColumn(Graph.DEFAULT_TARGET_KEY);
		return tCopy;
	}
	
	private Table getStrippedNodeTable(Graph g) {
		Table tCopy = copyTable(g.getNodeTable());
		String nodeKeyField = g.getNodeKeyField();
		if (nodeKeyField != null) {
			tCopy.removeColumn(nodeKeyField);
		}
		return tCopy;
	}
		
	/*
	 * These methods do what .equals() should do for their respective objects:
	 * Actually compare the contents to see if they are .equals() to each
	 * other. The default methods instead appear to be doing a memory 
	 * location comparison.
	 */

	private boolean areEqual(Graph g1, Graph g2, boolean sort) {
		Table nodeTable1 = g1.getNodeTable();
		Table nodeTable2 = g2.getNodeTable();
		
		if (sort) {
			if (! areEqualWhenSorted(nodeTable1, nodeTable2))
				return false;
		} else {
			if (! areEqual(nodeTable1, nodeTable2))
				return false;
		}
		
		Table edgeTable1 = g1.getEdgeTable();
		Table edgeTable2 = g2.getEdgeTable();
		
		if (sort) {
			if (! areEqualWhenSorted(edgeTable1, edgeTable2)) 
				return false;
		} else {
			if (! areEqual(edgeTable1, edgeTable2)) 
				return false;
		}
		
		return true;
	}
	
	/*
	 * Cares about the order of nodes and edges as well.
	 */
	private boolean areEqual(Table t1, Table t2) {
		Iterator tuplesIterator1 = t1.tuples();
		Iterator tuplesIterator2 = t2.tuples();
		
		while (tuplesIterator1.hasNext()) {
			Tuple tuple1 = (Tuple) tuplesIterator1.next();
			Tuple tuple2 = (Tuple) tuplesIterator2.next();
			
			if (! areEqual(tuple1, tuple2)) {
				return false;
			}
		}
		
		return true;
	}
	
	private boolean areEqual(Tuple tu1, Tuple tu2) {
		if (tu1.getColumnCount() != tu2.getColumnCount()) 
			return false;
		
		for (int ii = 0; ii < tu1.getColumnCount(); ii++) {
			Object columnContents1 = tu1.get(ii);
			Object columnContents2 = tu2.get(ii);
			
			if (columnContents1 == null && columnContents2 == null) {
				//nulls are equal to each other!
				continue;
			} else if (columnContents1 == null) {
				//one is null and the other is not.
				return false;
			} else if (columnContents2 == null) {
				//one is null and the other is not.
				return false;
			} else if (! tu1.get(ii).equals(tu2.get(ii))){
				//neither are null, but they are still not equal.
				return false;
			}
		}
		
		//all column contents are equal.
		return true;
	}
	
	private boolean areEqualWhenSorted(Table t1, Table t2) {	
		Sort t1Sort = new Sort(getColumnNames(t1));
		/*
		 * Predicates here are always true, because we don't want to remove 
		 * any rows when we select. We only want to sort them.
		 */
		Table sortedT1 = t1.select(ExpressionParser.predicate("TRUE"), t1Sort);
		
		Sort t2Sort = new Sort(getColumnNames(t2));
		Table sortedT2 = t2.select(ExpressionParser.predicate("TRUE"), t2Sort);
		
		IntIterator t1Iter = sortedT1.rows();
		IntIterator t2Iter = sortedT2.rows();
		
		while (t1Iter.hasNext()) {
			int t1Index = t1Iter.nextInt();
			Tuple t1Tuple = t1.getTuple(t1Index);
			
			int t2Index = t2Iter.nextInt();
			Tuple t2Tuple = t2.getTuple(t2Index);
			if (! areEqual(t1Tuple, t2Tuple)) {
				return false;
			}

		}
		//every tuple has an identical tuple in the other table.
		return true;
	}
	
	//utility methods 
	
	private String[] getColumnNames(Table t) {
		String[] columnNames = new String[t.getColumnCount()];
		for (int ii = 0; ii < t.getColumnCount(); ii++) {
			columnNames[ii] = t.getColumnName(ii);
		}
		return columnNames;
	}
		
	private Table copyTable(Table t) {
		Table tCopy = new Table();
		tCopy.addColumns(t.getSchema());
		
		for (Iterator ii = t.tuples(); ii.hasNext();) {
			Tuple tuple = (Tuple) ii.next();
			tCopy.addTuple(tuple);
		}
		return tCopy;
	}
	
	private void printTable(Table t) {
		Iterator ii = t.tuples();
		while (ii.hasNext()) {
			System.out.println((Tuple) ii.next());
		}
	}
	
	
}
