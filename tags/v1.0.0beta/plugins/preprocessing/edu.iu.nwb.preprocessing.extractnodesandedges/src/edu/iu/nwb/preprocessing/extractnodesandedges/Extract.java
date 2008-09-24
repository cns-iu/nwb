package edu.iu.nwb.preprocessing.extractnodesandedges;

import java.util.ArrayList;
import java.util.List;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.AbstractPredicate;
import prefuse.data.expression.Predicate;
import prefuse.data.expression.parser.ExpressionParser;
import prefuse.data.util.Sort;
import prefuse.util.collections.IntIterator;

public class Extract {
	
	private static final String EXPLICIT_ID_COLUMN_NAME = "SpecialID";

	public static Graph extractOrKeepTopEdges(Graph g, int numTopNodes, String sortAttribute, boolean ascending, boolean keep) {
		System.out.println("Original Graph");
		GraphUtil.printGraph(g);
		Graph gCopy = GraphUtil.copySparseGraph(g);
		System.out.println("Graph Copy");
		GraphUtil.printGraph(gCopy);
		addExplicitNodeKeyField(gCopy.getEdgeTable(), EXPLICIT_ID_COLUMN_NAME);
		System.out.println("Added explicit edge fields");
		GraphUtil.printGraph(gCopy);
		Table edgeTableAttributeSorted = GraphUtil.sortCopyByColumn(gCopy.getEdgeTable(), sortAttribute, ascending);
		System.out.println("Sorted node table");
		GraphUtil.printTable(edgeTableAttributeSorted);
		//remove top 'numTopNodes' nodes, or keep top 'numNodes' nodes, depending on 'keepTop'
		IntIterator nodeRowIt = edgeTableAttributeSorted.rows();
		//(process first 'numTopNodes' nodes)
		for (int numToProcessFirst = 0; numToProcessFirst < numTopNodes && nodeRowIt.hasNext(); numToProcessFirst++) {
			int nodeRow = nodeRowIt.nextInt();
			if (!keep) { //(if we are keeping the bottom, remove the top (here))
				int rowInOriginalTable;
				rowInOriginalTable = edgeTableAttributeSorted.getInt(nodeRow, EXPLICIT_ID_COLUMN_NAME);
				gCopy.removeEdge(rowInOriginalTable);
			}
		}
		//(process the rest)
		while (nodeRowIt.hasNext()) {
			int nodeRow = nodeRowIt.nextInt();
			if (keep ) { //(if we are keeping the top, remove the bottom (here))
				int rowInOriginalTable;
				rowInOriginalTable = edgeTableAttributeSorted.getInt(nodeRow, EXPLICIT_ID_COLUMN_NAME);
			gCopy.removeEdge(rowInOriginalTable);
			}
		}
		System.out.println("Removed unneeded nodes");
		GraphUtil.printGraph(gCopy);
		removeExplicitNodeKeyField(gCopy.getEdgeTable(), EXPLICIT_ID_COLUMN_NAME);
		System.out.println("Removed explicit node key field");
		GraphUtil.printGraph(gCopy);
		Table edgeTableCopy = GraphUtil.copyTable(gCopy.getEdgeTable());
		Graph returnGraph = new Graph(gCopy.getNodeTable(), edgeTableCopy, g.isDirected(),
				g.getNodeKeyField(), g.getEdgeSourceField(), g.getEdgeTargetField());
		return returnGraph;
	}
	
	public static Graph extractOrKeepTopNodes(Graph g, int numTopNodes, String sortAttribute, boolean ascending, boolean keep) {
		System.out.println("Original Graph");
		GraphUtil.printGraph(g);
		Graph gCopy = GraphUtil.copySparseGraph(g);
		System.out.println("Graph Copy");
		GraphUtil.printGraph(gCopy);
		boolean addedExplicitNodeKeyField = addExplicitNodeKeyFieldIfNecessary(gCopy);
		System.out.println("Added explicit node fields");
		GraphUtil.printGraph(gCopy);
		Table nodeTableAttributeSorted = GraphUtil.sortCopyByColumn(gCopy.getNodeTable(), sortAttribute, ascending);
		System.out.println("Sorted node table");
		GraphUtil.printTable(nodeTableAttributeSorted);
		//remove top 'numTopNodes' nodes, or keep top 'numNodes' nodes, depending on 'keepTop'
		IntIterator nodeRowIt = nodeTableAttributeSorted.rows();
		//(process first 'numTopNodes' nodes)
		for (int numToProcessFirst = 0; numToProcessFirst < numTopNodes && nodeRowIt.hasNext(); numToProcessFirst++) {
			int nodeRow = nodeRowIt.nextInt();
			if (!keep) { //(if we are keeping the bottom, remove the top (here))
				int rowInOriginalTable;
				if (addedExplicitNodeKeyField) {
					rowInOriginalTable = nodeTableAttributeSorted.getInt(nodeRow, EXPLICIT_ID_COLUMN_NAME);
				} else {
					rowInOriginalTable = nodeTableAttributeSorted.getInt(nodeRow, gCopy.getNodeKeyField());
				}
				gCopy.removeNode(rowInOriginalTable);
			}
		}
		//(process the rest)
		while (nodeRowIt.hasNext()) {
			int nodeRow = nodeRowIt.nextInt();
			if (keep ) { //(if we are keeping the top, remove the bottom (here))
				int rowInOriginalTable;
				if (addedExplicitNodeKeyField) {
					rowInOriginalTable = nodeTableAttributeSorted.getInt(nodeRow, EXPLICIT_ID_COLUMN_NAME);
				} else {
					rowInOriginalTable = nodeTableAttributeSorted.getInt(nodeRow, gCopy.getNodeKeyField());
				}
			gCopy.removeNode(rowInOriginalTable);
			}
		}
		System.out.println("Removed unneeded nodes");
		GraphUtil.printGraph(gCopy);
		if (addedExplicitNodeKeyField) {
			removeExplicitNodeKeyField(gCopy.getNodeTable(), EXPLICIT_ID_COLUMN_NAME);
		}
		System.out.println("Removed explicit node key field");
		GraphUtil.printGraph(gCopy);
		
		return gCopy;
	}
	
	private static void addExplicitNodeKeyField(Table t, String columnName) {
		t.addColumn(columnName, int.class);
		for (IntIterator nodeRowIt = t.rows(); nodeRowIt.hasNext();) {
			int nodeRow = nodeRowIt.nextInt();
			t.setInt(nodeRow, columnName, nodeRow);
		}
	}
	
	private static void removeExplicitNodeKeyField(Table t, String columnName) {
		t.removeColumn(columnName);
	}
	
	
	private static boolean addExplicitNodeKeyFieldIfNecessary(Graph g) {
		boolean addedExplicitNodeKeyField;
		if (g.getNodeKeyField() == null) {
			addedExplicitNodeKeyField = true;
			addExplicitNodeKeyField(g.getNodeTable(), EXPLICIT_ID_COLUMN_NAME);
		} else {
			System.out.println("Node key field is " + g.getNodeKeyField());
			addedExplicitNodeKeyField = false;
		}
		System.out.println("Node key field is " + g.getNodeKeyField());
		return addedExplicitNodeKeyField;
	}
	
	public static Graph Nodes(Table nodeTable, Table edgeTable, int numNodes, String sortAttribute, boolean ascend, boolean directed) {
		//sort the table by the user-specified attribute
		Sort sortByAttribute = new Sort(new String[]{sortAttribute}, new boolean[]{ascend});
		Table sortedNodeTableCopy = nodeTable.select(ExpressionParser.predicate("TRUE"), sortByAttribute);
		Table edgeTableCopy = GraphUtil.copyTable(edgeTable);
		//copy the top 'numNodes' nodes to a second table
		List nodesToRemove = new ArrayList();
		Predicate filter = new IsInTopX(numNodes);
		for (IntIterator nodeIt = sortedNodeTableCopy.rows(); nodeIt.hasNext();) {
			int nodeRow = nodeIt.nextInt();
			if (! filter.getBoolean(sortedNodeTableCopy.getTuple(nodeRow)))	{
				nodesToRemove.add(new Integer(nodeRow));
			}
		}
		
		Graph newGraph = new Graph(sortedNodeTableCopy, edgeTableCopy, directed);
		for (int ii = 0; ii < nodesToRemove.size(); ii++) {
			newGraph.removeNode(((Integer) nodesToRemove.get(ii)).intValue()); 
		}
		
		//Table extractedTable = table.select(filter, sortByAttribute);
		//return the second table
		return newGraph;
		
	}
	
	public static Table extractTopEdges(Table table, int numNodes, String sortAttribute, boolean ascend) {
		//sort the table by the user-specified attribute
		Sort sortByAttribute = new Sort(new String[]{sortAttribute}, new boolean[]{ascend});
		//copy the top 'numNodes' nodes to a second table
		Predicate filter = new IsInTopX(numNodes);
		Table extractedTable = table.select(filter, sortByAttribute);
		//return the second table
		return extractedTable;
	}
	
	public static Table extractAboveOrBelowEdges(Table table, double partitionNumber, String numericAttribute, boolean takeAbove) {
		Sort doNotSort = new Sort(new String[0]);
		//copy all nodes above/below the specified number (partition) to a second table
		Predicate filter = new IsAboveBelow(partitionNumber, numericAttribute, takeAbove);
		Table extractedTable = table.select(filter, doNotSort);
		return extractedTable;
	}
	
	static class IsInTopX extends AbstractPredicate {
		
		private int x;
		
		public IsInTopX(int x) {
			this.x = x;
		}
		public boolean getBoolean(Tuple t) {
			return t.getRow() <= x;
		}
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
