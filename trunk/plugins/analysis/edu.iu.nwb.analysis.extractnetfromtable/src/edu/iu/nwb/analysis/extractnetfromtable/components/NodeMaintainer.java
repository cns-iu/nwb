package edu.iu.nwb.analysis.extractnetfromtable.components;

import org.cishell.utilities.TableUtilities;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;

public class NodeMaintainer {
	public static final String BIPARTITE_TYPE_SUGGESTED_COLUMN_NAME = "bipartiteType";

	/* The name of the new column to add for recording each node's bipartite type is lazily
	 * initialized because some algorithms enter this plug-in through static methods
	 * (see the initialize methods in GraphContainer).  We cannot fix a constant String for the
	 * column name as it may already exist in the table.
	 */
	private String bipartiteTypeColumnName = null;

	protected Node createNode(String label, String bipartiteType, Graph graph, Table table,
			int rowNumber, AggregateFunctionMappings nodeFunctionMappings, int nodeType) {
		int nodeNumber = graph.addNodeRow();
		Node n = graph.getNode(nodeNumber);
		n.set(GraphContainer.LABEL_COLUMN_NAME, label);
		
		// Are we in bipartite mode?
		if (bipartiteType != null) {
			// Have we not created a node table column for the bipartite type yet?
			if (bipartiteTypeColumnName == null) {
				this.bipartiteTypeColumnName = 
				TableUtilities.formNonConflictingNewColumnName(
					graph.getNodeTable().getSchema(), BIPARTITE_TYPE_SUGGESTED_COLUMN_NAME);
				graph.getNodeTable().addColumn(bipartiteTypeColumnName, String.class);
			}
			
			n.set(bipartiteTypeColumnName, bipartiteType);
		}
		ValueAttributes va = new ValueAttributes(nodeNumber);
		va = FunctionContainer.mutateFunctions(n, table, rowNumber, va, nodeFunctionMappings,
				nodeType);
		nodeFunctionMappings.addFunctionRow(new NodeID(label, bipartiteType), va);
		return n;
	}

	protected Node mutateNode(String tableValue, String bipartiteType, Graph graph,
			Table table, int rowNumber, AggregateFunctionMappings afm, int nodeType) {
		ValueAttributes va = afm.getFunctionRow(new NodeID(tableValue, bipartiteType));
		Node n;

		/* If we don't find a ValueAttributes object,
		 * we haven't seen this node before; create a new one.
		 */
		if (va == null) {
			n = createNode(tableValue, bipartiteType, graph, table, rowNumber, afm, nodeType);
		} else {
			int nodeNumber = va.getRowNumber();
			n = graph.getNode(nodeNumber);
			FunctionContainer.mutateFunctions(n, table, rowNumber, va, afm, nodeType);
		}

		return n;
	}
}
