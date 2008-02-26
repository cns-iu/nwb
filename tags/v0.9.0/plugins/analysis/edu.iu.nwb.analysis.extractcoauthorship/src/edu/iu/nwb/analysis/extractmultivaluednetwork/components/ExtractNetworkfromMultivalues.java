package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;

public class ExtractNetworkfromMultivalues {
	private final Map metaEdgeColumnNameToFunctionMap = new HashMap();
	private final Map metaNodeColumnNameToFunctionMap = new HashMap();
	private final Map originalNodeColumnToFunctionMap = new HashMap();
	private final Map orginalEdgeColumnToFunctionMap = new HashMap();
	private final prefuse.data.Table objectTable;
	private final prefuse.data.Graph coObjectNetwork;
	private Properties functionDefinitions;
	private String splitString;

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

	public ExtractNetworkfromMultivalues(prefuse.data.Table pdt,
			String columnName, String split, Properties metaData,
			boolean isDirected) {
		this.functionDefinitions = metaData;
		this.splitString = split;
		coObjectNetwork = constructGraph(pdt, columnName,isDirected);
		objectTable = constructTable(coObjectNetwork);
	}

	/***
	 * 
	 * @param pdt A Table
	 * @param columnName The name of a column within the table.
	 * @param split The string used to divide values within the provided column
	 * @param metaData A set of properties for operating on the columns in the Table.
	 * @param isDirected Whether or not the created edges should be treated as directed or undirected.
	 * @return
	 * 
	 * Given a Table, create a new Graph with new Columns as defined by metaData.
	 */

	private prefuse.data.Graph constructGraph(prefuse.data.Table pdt,
			String columnName,boolean isDirected) {

		final Schema inputSchema = pdt.getSchema();

		//final Schema outputNodeSchema = createNodeSchema(inputNodeSchema,metaData);
		//final Schema outputEdgeSchema = createEdgeSchema(inputNodeSchema,metaData);

		final Schema nodeSchema = new Schema();
		final Schema edgeSchema = new Schema();

		createNodeAndEdgeSchema(inputSchema, nodeSchema,edgeSchema);

		final Graph outputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), isDirected);

		constructNodesandEdges(pdt, columnName, outputGraph, isDirected);

		return outputGraph;

	}

	private void constructNodesandEdges(prefuse.data.Table pdt,
			String columnName, prefuse.data.Graph g, boolean directed) {

		final HashMap values = new HashMap();
		final HashMap coValues = new HashMap();

		for (int row = 0; row < pdt.getRowCount(); row++) {
			final String s = (String) pdt.get(row, columnName);

			Set seenObject = new HashSet();

			// Here we ensure that we correctly capture regex metacharacters.
			// This means we can't split on regex, but I think that is
			// acceptable.

			final Pattern p = Pattern.compile("\\Q" + this.splitString + "\\E");
			final Object[] objects = p.split(s);


			for (int i = objects.length - 1; i >= 0; i--) {
				if(seenObject.add(objects[i])){

					constructAndModifyNode(objects[i],g,pdt,row,values);
				}
				// if there is more than one value, create unique edges between each
				// value.
				for (int j = 0; j < i; j++) {
					if(seenObject.add(objects[j])){
						constructAndModifyNode(objects[j],g,pdt,row,values);
					}
					//create or modify an edge as necesary
					constructAndModifyEdge(objects[j],objects[i],g,pdt,row,values,coValues,directed);
				}
			}
		}
	}

	private Node createNode(Object labelObject, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber, Map nodeValues){
		Node n = graph.addNode();
		n.set(0, labelObject);
		ValueAttributes va = new ValueAttributes(graph.getNodeCount()-1);
		createNodeFunctions(n,table,rowNumber,va);
		nodeValues.put(labelObject, va);
		return n;

	}

	/***
	 * 
	 * @param graph
	 * @return
	 * A table containing the nodes and node attributes found in the provided
	 * Graph.
	 */

	private prefuse.data.Table constructTable(prefuse.data.Graph graph) {
		Table outputTable = new Table();
		outputTable = createTableSchema(graph.getNodeTable().getSchema(),
				outputTable);
		outputTable = populateTable(outputTable, graph);
		return outputTable;
	}

	/***
	 * 
	 * @param newColumnName
	 * @param calculateColumnName
	 * @param function
	 * @param columnType
	 * @param newSchema
	 * 
	 * We add a new column to the 
	 */

	private void createColumn(String newColumnName, String calculateColumnName,
			String function, Class columnType, Schema newSchema) {
		Class finalType = null;
		if (function.equalsIgnoreCase("count")) {
			finalType = int.class;
		} else if (function.equalsIgnoreCase("sum")) {
			finalType = columnType;
		} else if (function.equalsIgnoreCase("arithmeticmean")) {
			if (columnType.equals(int.class) || columnType.equals(long.class)) {
				finalType = double.class; // we may want to change this to
				// allow more options, such as
				// float?
			} else {
				finalType = columnType;
			}
		} else if (function.equalsIgnoreCase("geometricmean")) {
			if (columnType.equals(int.class) || columnType.equals(long.class)) {
				finalType = float.class;
			} else {
				finalType = columnType;
			}
		}

		newSchema.addColumn(newColumnName, finalType);
	}

	private void createNodeAndEdgeSchema(Schema inputSchema, Schema nodeSchema, Schema edgeSchema){
		nodeSchema.addColumn("label", String.class);
		edgeSchema.addColumn("source", int.class);
		edgeSchema.addColumn("target",int.class);

		for (final Iterator it = this.functionDefinitions.keySet().iterator(); it.hasNext();) {

			final String key = (String) it.next();
			String sourceColumnName = this.functionDefinitions.getProperty(key);
			final int index = sourceColumnName.lastIndexOf(".");
			final String function = sourceColumnName.substring(index + 1);
			sourceColumnName = sourceColumnName.substring(0,
					index);
			final Class columnType = inputSchema.getColumnType(sourceColumnName);
			String newColumnName = key.substring(key.indexOf(".")+1);

			// need to check against existing column names.


			if (key.startsWith("edge.")) {
				createColumn(newColumnName,sourceColumnName, function, columnType, edgeSchema);
				metaEdgeColumnNameToFunctionMap.put(newColumnName, function);
				orginalEdgeColumnToFunctionMap.put(newColumnName,
						sourceColumnName);

				// output.addColumn(key.substring(key.indexOf(".")+1),
				// joinSchema.getColumnType(calculateColumnName));
			}

			if (key.startsWith("node.")) {
				createColumn(newColumnName, sourceColumnName, function, columnType,
						nodeSchema);
				metaNodeColumnNameToFunctionMap.put(newColumnName, function);
				originalNodeColumnToFunctionMap.put(newColumnName, sourceColumnName);

			}
		}


	}



	private ValueAttributes createFunction(ValueAttributes va,
			String functionName, Class type, int columnNumber) {

		final String name = functionName.toLowerCase();
		if (name.equals("count")) {
			va.addFunction(columnNumber, new Count());
		} else {

			if (name.equals("arithmeticmean")) {

				if (type.equals(int.class) || type.equals(Integer.class)) {
					va.addFunction(columnNumber, new DoubleArithmeticMean());
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					va.addFunction(columnNumber, new DoubleArithmeticMean());
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					va.addFunction(columnNumber, new FloatArithmeticMean());
				}
			} else if (name.equals("sum")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					va.addFunction(columnNumber, new IntegerSum());
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					va.addFunction(columnNumber, new DoubleSum());
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					va.addFunction(columnNumber, new FloatSum());
				}
			} else if (name.equals("max")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					va.addFunction(columnNumber, new IntegerMax());
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					va.addFunction(columnNumber, new DoubleMax());
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					va.addFunction(columnNumber, new FloatMax());
				}
			} else if (name.equals("min")) {
				if (type.equals(int.class) || type.equals(Integer.class)) {
					va.addFunction(columnNumber, new IntegerMin());
				}
				if (type.equals(double.class) || type.equals(Double.class)) {
					va.addFunction(columnNumber, new DoubleMin());
				}
				if (type.equals(float.class) || type.equals(Float.class)) {
					va.addFunction(columnNumber, new FloatMin());
				}
			}

		}

		return va;
	}

	private void createNodeFunctions(Node n, prefuse.data.Table t,
			int rowNumber, ValueAttributes va) {
		for (int k = 0; k < n.getColumnCount(); k++) {
			final String cn = n.getColumnName(k);

			if (metaNodeColumnNameToFunctionMap.get(cn) != null) {
				createFunction(va, (String) metaNodeColumnNameToFunctionMap.get(cn), n
						.getColumnType(cn), n.getColumnIndex(cn));
				final String operateColumn = (String) (originalNodeColumnToFunctionMap.get(cn));
				va.getFunction(n.getColumnIndex(cn)).operate(
						t.get(rowNumber, operateColumn));
				n.set(cn, va.getFunction(n.getColumnIndex(cn)).getResult());
			}
		}
	}


	private Table createTableSchema(Schema graphSchema, Table t) {
		// Schema outputSchema = new Schema();
		for (int i = 0; i < graphSchema.getColumnCount(); i++) {
			t.addColumn(graphSchema.getColumnName(i), graphSchema
					.getColumnType(i));
		}
		t.addColumn("uniqueIndex", Integer.class);
		t.addColumn("combineValues", String.class, "*");

		return t;
	}

	public Graph getGraph() {
		return coObjectNetwork;
	}

	public Table getTable() {
		return objectTable;
	}

	private void operateNodeFunctions(ValueAttributes va, prefuse.data.Graph g,
			prefuse.data.Table t, int rowNumber) {
		final Node n = g.getNode(va.getRowNumber());
		for (int k = 0; k < n.getColumnCount(); k++) {

			final UtilityFunction uf = va.getFunction(k);
			if (uf != null) {
				final String operateColumn = (String) (originalNodeColumnToFunctionMap.get((n
						.getColumnName(k))));
				uf.operate(t.get(rowNumber, operateColumn));
				n.set(k, uf.getResult());
			}
		}
	}

	private void operateEdgeFunctions(ValueAttributes va, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber){
		final Edge e = graph.getEdge(va.getRowNumber());
		for (int column = 0; column < e.getColumnCount(); column++) {
			final String cn = e.getColumnName(column);
			if (metaEdgeColumnNameToFunctionMap.get(cn) != null) {
				final int index = e.getColumnIndex(cn);
				final String operateColumn = (String) orginalEdgeColumnToFunctionMap
				.get(cn);
				va.getFunction(index).operate(table.get(rowNumber, operateColumn));

				e.set(index, va.getFunction(index).getResult());
			}
		}
	}

	private Table populateTable(Table t, Graph g) {

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

	private void constructAndModifyNode(Object tableValue, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber, HashMap objectToFunctionMap){
		ValueAttributes va = (ValueAttributes)objectToFunctionMap.get(tableValue);
		// If we don't find a ValueAttributes object, create one.
		if(va == null){
			createNode(tableValue,graph,table,rowNumber,objectToFunctionMap);
		}
		else{
			operateNodeFunctions(va,graph,table,rowNumber);
		}
	}

	private void constructAndModifyEdge(Object tableValue1, Object tableValue2, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber, HashMap objectToFunctionMap, HashMap coObjecttoFunctionMap, boolean isDirected){
		final CoValued v = new CoValued(tableValue1, tableValue2,
				isDirected);

		ValueAttributes va = (ValueAttributes) coObjecttoFunctionMap.get(v);

		if (va == null) {
			constructEdge(v,graph,table,rowNumber,objectToFunctionMap,coObjecttoFunctionMap);
		} else { 
			// the edge and functions exist, operate on the values.
			operateEdgeFunctions(va,graph,table,rowNumber);
		}
	}

	private void constructEdge(CoValued cv, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber, HashMap objectToFunctionMap, HashMap coObjecttoFunctionMap){


		ValueAttributes va = new ValueAttributes(graph.getEdgeCount());

		final int fv = ((ValueAttributes) objectToFunctionMap.get(cv.firstValue)).getRowNumber();
		final int sv = ((ValueAttributes) objectToFunctionMap.get(cv.secondValue)).getRowNumber();

		final Edge e = graph.addEdge(graph.getNode(fv), graph.getNode(sv));
		for (int column = 0; column < e.getColumnCount(); column++) {
			final String cn = e.getColumnName(column);
			if (metaEdgeColumnNameToFunctionMap.get(cn) != null) {
				// if the current column name matches one of the column
				// names specified in the Properties, get the column
				// index of that column name.
				final int index = e.getColumnIndex(cn);
				createFunction(va, (String) metaEdgeColumnNameToFunctionMap.get(cn), e
						.getColumnType(cn), index);
				// get the type of function using the column name
				// get the data type of column
				// create a function and add it to the ValueAttributes object,
				// associating the column index with the function.
				final String operateColumn = (String) (orginalEdgeColumnToFunctionMap
						.get(cn));
				// Find the column the function is to operate on and operate.
				va.getFunction(index).operate(table.get(rowNumber, operateColumn));
				// Set that value at index.
				e.set(index, va.getFunction(index).getResult());
			}
		}
		// associate the ValueAttributes object with an Edge.
		coObjecttoFunctionMap.put(cv, va);
	}

	public String getSplitString(){
		return this.splitString;
	}

	public Properties getMetaData(){
		return this.functionDefinitions;
	}

}
