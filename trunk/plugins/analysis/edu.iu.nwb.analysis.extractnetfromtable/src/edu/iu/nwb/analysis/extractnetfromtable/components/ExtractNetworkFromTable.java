package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.Iterator;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;

public class ExtractNetworkFromTable {

	/***
	 * 
	 * Creates a network from a the provided table. If the value in
	 * the supplied column name contains the split string, break the value
	 * on the split string and create an edge between those values.
	 * <p>
	 * After construction, the Object contains a Graph consisting of unique
	 * values and edges between those values, if any exist. It also contains 
	 * a Table of the unique values.
	 * 
	 * @param pdt A Table
	 * @param columnName The name of a column within the table.
	 * @param split The string used to divide values within the provided column
	 * @param metaData A set of properties for operating on the columns in the Table.
	 * @param isDirected Whether or not the created edges should be treated as directed or undirected.
	 * 
	 * 
	 */

//	private static void addDuplicateValueErrorMessage(String title, String col, HashMap errorMessages){
//		//This needs to be generalized.
//		String error = "The work:"+
//		System.getProperty("line.separator")+
//		"\t"+title+
//		System.getProperty("line.separator")+
//		"contains duplicate values in column: " + col +
//		System.getProperty("line.separator")+
//		"The work has been added with duplicates considered as a single value."+
//		System.getProperty("line.separator")+
//		"This may affect the accuracy of your data."+
//		System.getProperty("line.separator")+
//		System.getProperty("line.separator");;
//		errorMessages.put(title, error);
//	}
//
//	private static void printNoValueToExtractError(String title, String col, LogService ls){
//		//This needs to be generalized.
//		String error = "The work:"+
//		System.getProperty("line.separator")+
//		"\t"+title+
//		System.getProperty("line.separator")+
//		"contains no values in column: "+ col +
//		System.getProperty("line.separator")+
//		"The work has not been added."+
//		System.getProperty("line.separator")+
//		System.getProperty("line.separator");
//		ls.log(LogService.LOG_WARNING, error);
//	}

	/***
	 * 
	 * A table containing the nodes and node attributes found in the provided
	 * Graph.
	 * 
	 * @param graph
	 * @return
	 * 
	 */

	public static prefuse.data.Table constructTable(prefuse.data.Graph graph) {
		Table outputTable = new Table();
		outputTable = createTableSchema(graph.getNodeTable().getSchema(),
				outputTable);
		outputTable = populateTable(outputTable, graph);
		return outputTable;
	}


	private static Table createTableSchema(Schema graphSchema, Table t) {
		for (int i = 0; i < graphSchema.getColumnCount(); i++) {
			t.addColumn(graphSchema.getColumnName(i), graphSchema.getColumnType(i));
		}
		
		t.addColumn("uniqueIndex", Integer.class);
		t.addColumn("combineValues", String.class, "*");

		return t;
	}

	private static Table populateTable(Table t, Graph g) {
		for (final Iterator it = g.nodes(); it.hasNext();) {
			final Node n = (Node) it.next();
			t.addRow();
			for (int i = 0; i < n.getColumnCount(); i++) {
				t.set(t.getRowCount() - 1, i, n.get(i));
			}
			t.set(t.getRowCount() - 1, "uniqueIndex", new Integer(t
					.getRowCount()));
		}
		return t;
	}
}
