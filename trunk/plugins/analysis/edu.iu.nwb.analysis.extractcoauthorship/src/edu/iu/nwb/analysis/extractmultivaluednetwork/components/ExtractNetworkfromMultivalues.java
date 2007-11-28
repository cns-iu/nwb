package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;

public class ExtractNetworkfromMultivalues {
	private final Map metaEdgeColumnNameToFunctionMap = new HashMap();
	private final Map metaNodeColumnNameToFunctionMap = new HashMap();
	private final Map nodeFunctionMap = new HashMap();
	private final Map edgeFunctionMap = new HashMap();
	private final prefuse.data.Table objectTable;
	private final prefuse.data.Graph coObjectNetwork;

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
		coObjectNetwork = constructGraph(pdt, columnName, split, metaData,
				isDirected);
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

	public prefuse.data.Graph constructGraph(prefuse.data.Table pdt,
			String columnName, String split, Properties metaData,
			boolean isDirected) {
		final Schema inputNodeSchema = pdt.getSchema();

		final Schema outputNodeSchema = createNodeSchema(inputNodeSchema,
				metaData);
		final Schema outputEdgeSchema = createEdgeSchema(inputNodeSchema,
				metaData);

		// printColumnNames(outputNodeSchema);
		// printColumnTypes(outputNodeSchema);
		// printColumnNames(outputEdgeSchema);
		// printColumnTypes(outputEdgeSchema);
		final Graph outputGraph = new Graph(outputNodeSchema.instantiate(),
				outputEdgeSchema.instantiate(), isDirected);

		constructNodesandEdges(pdt, columnName, split, metaData, outputGraph,
				isDirected);

		return outputGraph;

	}

	private void constructNodesandEdges(prefuse.data.Table pdt,
			String columnName, String split, Properties metaData,
			prefuse.data.Graph g, boolean directed) {
		final HashMap valueList = new HashMap();
		final HashMap coValueList = new HashMap();
		Node n;
		for (int h = 0; h < pdt.getRowCount(); h++) {
			final String s = (String) pdt.get(h, columnName);

			// Here we ensure that we correctly capture regex metacharacters.
			// This means we can't split on regex, but I think that is
			// acceptable.
			final Pattern p = Pattern.compile("\\Q" + split + "\\E");
			final Object[] Objects = p.split(s);

			for (int i = Objects.length - 1; i > 0; i--) {
				ValueAttributes va1 = (ValueAttributes) valueList
						.get(Objects[i]);
				// If we don't find a ValueAttributes object, create one.
				if (va1 == null) {
					n = g.addNode();
					n.set(0, Objects[i]);
					va1 = new ValueAttributes(g.getNodeCount() - 1);
					//After creating the object, populate it with the necessary functions
					createNodeFunctions(n, pdt, h, va1);
					valueList.put(Objects[i], va1);

				} else { 
					// The ValueAttributes object exists, execute the
					// functions and associate the modified ValueAttributes object
					// with original Object.
					operateNodeFunctions(va1, g, pdt, h);
					valueList.put(Objects[i], va1);
				}
				
				// if there is more than one value, create unique edges between each
				// value.
				for (int j = 0; j < i; j++) {
					va1 = (ValueAttributes) valueList.get(Objects[j]);
					// If we don't find a ValueAttributes object, create one.
					if (va1 == null) {
						n = g.addNode();
						n.set(0, Objects[j]);
						va1 = new ValueAttributes(g.getNodeCount() - 1);
						createNodeFunctions(n, pdt, h, va1);
						valueList.put(Objects[j], va1);
						
					} else {
						// The ValueAttributes object exists, execute the
						// functions and associate the modified ValueAttributes object
						// with original Object.
						operateNodeFunctions(va1, g, pdt, h);
						valueList.put(Objects[j], va1);
					}

					final CoValued v = new CoValued(Objects[j], Objects[i],
							directed);
				
					va1 = (ValueAttributes) coValueList.get(v);
					
					if (va1 == null) {
						// if the edge does not exist, create it and the necessary functions
						va1 = new ValueAttributes(g.getEdgeCount());

						final int fv = ((ValueAttributes) valueList
								.get(v.firstValue)).getRowNumber();
						final int sv = ((ValueAttributes) valueList
								.get(v.secondValue)).getRowNumber();

						final Edge e = g.addEdge(g.getNode(fv), g.getNode(sv));
						for (int k = 0; k < e.getColumnCount(); k++) {
							final String cn = e.getColumnName(k);
							if (metaEdgeColumnNameToFunctionMap.get(cn) != null) {
								// if the current column name matches one of the column
								// names specified in the Properties, get the column
								// index of that column name.
								final int index = e.getColumnIndex(cn);
								createFunction(va1, (String) metaEdgeColumnNameToFunctionMap.get(cn), e
										.getColumnType(cn), index);
								// get the type of function using the column name
								// get the data type of column
								// create a function and add it to the ValueAttributes object,
								// associating the column index with the function.
								final String operateColumn = (String) (edgeFunctionMap
										.get(cn));
								// Find the column the function is to operate on and operate.
								va1.getFunction(index).operate(pdt.get(h, operateColumn));
								// Set that value at index.
								e.set(index, va1.getFunction(index).getResult());
							}
						}
						// associate the ValueAttributes object with an Edge.
						coValueList.put(v, va1);
					} else { // the edge and functions exist, operate on the values.
						final Edge e = g.getEdge(va1.getRowNumber());
						for (int k = 0; k < e.getColumnCount(); k++) {
							final String cn = e.getColumnName(k);
							if (metaEdgeColumnNameToFunctionMap.get(cn) != null) {
								final int index = e.getColumnIndex(cn);
								final String operateColumn = (String) edgeFunctionMap
										.get(cn);
								va1.getFunction(index).operate(pdt.get(h, operateColumn));
								
								e.set(index, va1.getFunction(index).getResult());
							}
						}

					}
				}

			}

		}

	}

	public prefuse.data.Table constructTable(prefuse.data.Graph g) {
		Table outputTable = new Table();
		outputTable = createTableSchema(g.getNodeTable().getSchema(),
				outputTable);
		outputTable = populateTable(outputTable, g);
		return outputTable;
	}

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

	private Schema createEdgeSchema(Schema joinSchema, Properties metaData) {
		final Schema output = new Schema();
		output.addColumn("source", int.class);
		output.addColumn("target", int.class);

		for (final Iterator it = metaData.keySet().iterator(); it.hasNext();) {
			final String key = (String) it.next();

			// need to check against existing column names.
			if (key.startsWith("edge.")) {
				String calculateColumnName = metaData.getProperty(key);
				final int lastIndex = calculateColumnName.lastIndexOf(".");
				final int firstIndex = key.indexOf(".");
				final String function = calculateColumnName
						.substring(lastIndex + 1);
				calculateColumnName = calculateColumnName.substring(0,
						lastIndex);
				final Class columnType = joinSchema
						.getColumnType(calculateColumnName);

				createColumn(key.substring(firstIndex + 1),
						calculateColumnName, function, columnType, output);
				metaEdgeColumnNameToFunctionMap.put(key.substring(firstIndex + 1), function);
				edgeFunctionMap.put(key.substring(firstIndex + 1),
						calculateColumnName);

				// output.addColumn(key.substring(key.indexOf(".")+1),
				// joinSchema.getColumnType(calculateColumnName));
			}
		}

		return output;
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
				final String operateColumn = (String) (nodeFunctionMap.get(cn));
				va.getFunction(n.getColumnIndex(cn)).operate(
						t.get(rowNumber, operateColumn));
				n.set(cn, va.getFunction(n.getColumnIndex(cn)).getResult());
			}
		}
	}

	private Schema createNodeSchema(Schema schema, Properties metaData) {
		final Schema output = new Schema();
		output.addColumn("label", String.class);
		for (final Iterator it = metaData.keySet().iterator(); it.hasNext();) {
			final String key = (String) it.next();

			// need to add checking against existing column names.

			if (!key.startsWith("edge")) {
				String calculateColumnName = metaData.getProperty(key);
				final int lastIndex = calculateColumnName.lastIndexOf(".");
				final String function = calculateColumnName
						.substring(lastIndex + 1);
				calculateColumnName = calculateColumnName.substring(0,
						lastIndex);
				final Class columnType = schema
						.getColumnType(calculateColumnName);

				createColumn(key, calculateColumnName, function, columnType,
						output);
				metaNodeColumnNameToFunctionMap.put(key, function);
				nodeFunctionMap.put(key, calculateColumnName);

				// output.addColumn(key, schema.getColumnType(newColumnName));
			}
		}

		return output;
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
				final String operateColumn = (String) (nodeFunctionMap.get((n
						.getColumnName(k))));
				uf.operate(t.get(rowNumber, operateColumn));
				n.set(k, uf.getResult());
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

}
