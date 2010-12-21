package edu.iu.nwb.preprocessing.extractnodesandedges;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.tuple.TupleManager;
import prefuse.util.collections.IntIterator;

public class NewExtract {
	//Table.rowsSortedBy(field, ascend)
	//Table.rows(filter)
	//Graph.getKey(int id). row # - > node
	//Graph.removeNode(int node) 
	
	public static Graph extractNodesAbove(Graph origG, int aboveWhat, String field, boolean above) {
		//copy the graph
		Graph outG = GraphUtil.copySparseGraph(origG);
		//keep track of nodes we intend to delete
		List nodesToDelete = new ArrayList();
		//for each node in the graph below a certain value...
		Predicate isBelow = new IsAboveBelow(aboveWhat, field, !above);
		Table nodeTable = outG.getNodeTable();
		for (IntIterator filteredRowIt = nodeTable.rows(isBelow); filteredRowIt.hasNext();) {
			int rowID = filteredRowIt.nextInt();
			long nodeID = outG.getKey(rowID);
			//mark the node for deletion
			nodesToDelete.add(new Long(nodeID));
		}
		//for each node marked for deletion...
		for (Iterator it = nodesToDelete.iterator(); it.hasNext();) {
			Long nodeIDObj = ((Long) it.next());
			int nodeID = nodeIDObj.intValue(); //prefuse sucks
			//delete the node
			outG.removeNode(nodeID);
		}
		return outG;
	}
	
	public static Graph extractEdgesAbove(Graph origG, int aboveWhat, String field, boolean above) {
			//copy the graph
		Graph outG = GraphUtil.copySparseGraph(origG);
		//keep track of edges we intend to delete
		List edgesToDelete = new ArrayList();
			//for each edge in the graph below a certain value...
		Predicate isBelow = new IsAboveBelow(aboveWhat, field, !above);
		Table edgeTable = outG.getEdgeTable();
		for (IntIterator filteredRowIt = edgeTable.rows(isBelow); filteredRowIt.hasNext();) {
			int rowID = filteredRowIt.nextInt();
			long edgeID = outG.getKey(rowID);
				//mark the edge for deletion
			edgesToDelete.add(new Long(edgeID));
		}
			//for each edge marked for deletion...
		for (Iterator it = edgesToDelete.iterator(); it.hasNext();) {
			Long edgeIDObj = ((Long) it.next());
			int edgeID = edgeIDObj.intValue(); //prefuse sucks
				//delete the edge
			outG.removeEdge(edgeID);
		}
		return outG;
	}
	
	public static Graph extractTopNodes(Graph origG, int numTopNodes, String sortAttribute, boolean ascending) {
		//copy the graph
		Graph outG = GraphUtil.copySparseGraph(origG);
		//keep track of nodes we intend to delete
		List nodesToDelete = new ArrayList();

		//for each node in the graph, sorted by a field...
		Table nodeTable = outG.getNodeTable();
		int count = 0;
		for (IntIterator sortedRowIt = nodeTable.rowsSortedBy(sortAttribute, ascending); sortedRowIt.hasNext();) {
			//if node is in within the top...
			if (count < numTopNodes) {
				//skip over it
				count++;
				continue;
			} 	//othewise...
			else {
				//mark this node for deletion
				int rowID = sortedRowIt.nextInt();
				long nodeID = outG.getKey(rowID);
				nodesToDelete.add(new Long(nodeID));
				count++;
			}
		}
		
		//for each node marked for deletion...
		for (Iterator it = nodesToDelete.iterator(); it.hasNext();) {
			Long nodeIDObj = ((Long) it.next());
			int nodeID = nodeIDObj.intValue(); //prefuse sucks
			//delete the node
			outG.removeNode(nodeID);
		}
		return outG;
	}
	
	public static Graph extractTopEdges(Graph origG, int numTopEdges, String sortAttribute, boolean ascending) {
		//copy the graph
		Graph outG = GraphUtil.copySparseGraph(origG);
		//keep track of edges we intend to delete
		List edgesToDelete = new ArrayList();
		//for each edge in the graph, sorted by a field...
		Table edgeTable = outG.getEdgeTable();
		int count = 0;
		for (IntIterator sortedRowIt = edgeTable.rowsSortedBy(sortAttribute, ascending); sortedRowIt.hasNext();) {
			//if edge is in within the top...
			if (count < numTopEdges) {
				//skip over it
				count++;
				continue;
			} 	//othewise...
			else {
				//mark this edge for deletion
				int rowID = sortedRowIt.nextInt();
				long edgeID = outG.getKey(rowID);
				edgesToDelete.add(new Long(edgeID));
				count++;
			}
		}
		
		//for each edge marked for deletion...
		for (Iterator it = edgesToDelete.iterator(); it.hasNext();) {
			Long edgeIDObj = ((Long) it.next());
			int edgeID = edgeIDObj.intValue(); //prefuse sucks
			//delete the edge
			outG.removeEdge(edgeID);
		}
		return outG;
	}
	
static class IsAboveBelow extends AbstractPredicate {
		
		private double partitionNumber;
		private String numericAttribute;
		private boolean takeAbove;
		
		public IsAboveBelow(double partitionNumber, String numericAttribute, boolean takeAbove) {
			this.partitionNumber = partitionNumber;
			this.numericAttribute = numericAttribute;
			this.takeAbove = takeAbove;
		}
		
		public boolean getBoolean(Tuple t) {
			double val = t.getDouble(numericAttribute);
			if (takeAbove) {
				return val > this.partitionNumber;
			} else {
				return val < this.partitionNumber;
			}
		}

}
}
