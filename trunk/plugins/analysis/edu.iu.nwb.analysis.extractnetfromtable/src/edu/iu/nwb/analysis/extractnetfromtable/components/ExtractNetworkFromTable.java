package edu.iu.nwb.analysis.extractnetfromtable.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.osgi.service.log.LogService;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AggregateFunction;
import edu.iu.nwb.analysis.extractnetfromtable.aggregate.AssembleAggregateFunctions;

public class ExtractNetworkFromTable {

	private final prefuse.data.Table objectTable;
	private final prefuse.data.Graph coObjectNetwork;

	private Properties functionDefinitions;

	private String splitString;
	private AssembleAggregateFunctions abstractAFF;

	private LogService log;

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

	public ExtractNetworkFromTable(LogService log, prefuse.data.Table pdt,
			String columnName, String split, Properties metaData,
			boolean isDirected) throws InvalidColumnNameException{

		this.log = log;
		this.splitString = split;
		this.abstractAFF = AssembleAggregateFunctions.defaultAssembly();

		coObjectNetwork = initializeGraph(pdt, columnName, metaData);
		objectTable = constructTable(coObjectNetwork);
	}

	/***
	 * 
	 * Given a Table, create a new Graph with new Columns as defined by metaData.
	 * 
	 * @param pdt A Table
	 * @param columnName The name of a column within the table.
	 * @param split The string used to divide values within the provided column
	 * @param metaData A set of properties for operating on the columns in the Table.
	 * @param isDirected Whether or not the created edges should be treated as directed or undirected.
	 * @return
	 * 
	 * 
	 */

	private Graph initializeGraph(prefuse.data.Table pdt,
			String columnName, Properties properties) throws InvalidColumnNameException{

		final Schema inputSchema = pdt.getSchema();
		
			if(inputSchema.getColumnIndex(columnName) < 0)
				throw new InvalidColumnNameException(columnName + " was not a column in this table.\n");

		final Schema nodeSchema = createNodeSchema();
		final Schema edgeSchema = createEdgeSchema();
		
		AggregateFunctionMappings nodeAggregateFunctionMap = new AggregateFunctionMappings();
		AggregateFunctionMappings edgeAggregateFunctionMap = new AggregateFunctionMappings();
		
		AggregateFunctionMappings.parseProperties(inputSchema, nodeSchema, edgeSchema, properties, 
				nodeAggregateFunctionMap, edgeAggregateFunctionMap, log);

		final Graph outputGraph = new Graph(nodeSchema.instantiate(),
				edgeSchema.instantiate(), false);

		//constructNodesandEdges(pdt, columnName, outputGraph);

		return outputGraph;

	}

	private static void addDuplicateValueErrorMessage(String title, String col, HashMap errorMessages){
		//This needs to be generalized.
		String error = "The work:"+
		System.getProperty("line.separator")+
		"\t"+title+
		System.getProperty("line.separator")+
		"contains duplicate values in column: " + col +
		System.getProperty("line.separator")+
		"The work has been added with duplicates considered as a single value."+
		System.getProperty("line.separator")+
		"This may affect the accuracy of your data."+
		System.getProperty("line.separator")+
		System.getProperty("line.separator");;
		errorMessages.put(title, error);
	}

	private static void printNoValueToExtractError(String title, String col, LogService ls){
		//This needs to be generalized.
		String error = "The work:"+
		System.getProperty("line.separator")+
		"\t"+title+
		System.getProperty("line.separator")+
		"contains no values in column: "+ col +
		System.getProperty("line.separator")+
		"The work has not been added."+
		System.getProperty("line.separator")+
		System.getProperty("line.separator");
		ls.log(LogService.LOG_WARNING, error);
	}
	
/*
	private void constructNodesandEdges(prefuse.data.Table pdt,
			String columnName, prefuse.data.Graph g) {
		boolean dupValues = false;
		final HashMap dupValuesErrorMessages = new HashMap();
		for (Iterator it = pdt.rows(); it.hasNext();){
			int row = ((Integer)it.next()).intValue();
			final String s = (String) pdt.get(row, columnName);

			Set seenObject = new HashSet();

			// Here we ensure that we correctly capture regex metacharacters.
			// This means we can't split on regex, but I think that is
			// acceptable.

			if(s != null){ //no values to extract from
				final Pattern p = Pattern.compile("\\Q" + this.splitString + "\\E");
				final String[] objects = p.split(s);

				for (int i = objects.length - 1; i >= 0; i--) {
					if(seenObject.add(objects[i])){ //no duplicate nodes.
						constructAndModifyNode(objects[i],g,pdt,row);
					}
					// if there is more than one value, create unique edges between each
					// value.
					for (int j = 0; j < i; j++) {
						if(!objects[j].equals(objects[i])){
							if(seenObject.add(objects[j])){ //no duplicate nodes.
								constructAndModifyNode(objects[j],g,pdt,row);
							}
							//create or modify an edge as necessary
							constructAndModifyEdge(objects[j],objects[i],g,pdt,row);
						}else{
							dupValues = true; //detected a self-loop
						}
					}
				}
				if(dupValues){
					//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
					String title = "unknown";
					ExtractNetworkFromTable.addDuplicateValueErrorMessage(title, columnName, dupValuesErrorMessages);
					dupValues = false;
				}
			}
			else{
				//String title = (String)pdt.get(row,pdt.getColumnNumber("TI"));
				String title = "unknown";
				ExtractNetworkFromTable.printNoValueToExtractError(title, columnName, this.log);
			}
		}
		for(Iterator dupIter = dupValuesErrorMessages.keySet().iterator(); dupIter.hasNext();){
			this.log.log(LogService.LOG_WARNING, (String)dupValuesErrorMessages.get(dupIter.next()));
		}
	}

	private Node createNode(String label, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber){
		int nodeNumber = graph.addNodeRow();
		Node n = graph.getNode(nodeNumber);
		n.set(0, label);
		ValueAttributes va = new ValueAttributes(nodeNumber);
		va = createFunctions(n,table,rowNumber,va);
		this.nodeFunctionMappings.addFunctionRow(label, va);
		return n;
	}

	/***
	 * 
	 * A table containing the nodes and node attributes found in the provided
	 * Graph.
	 * 
	 * @param graph
	 * @return
	 * 
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
	 * We add a new column to the schema based on the Aggregate Function File. We set the type of the column based on the type return by the given FunctionFactory.
	 * 
	 * @param newColumnName
	 * @param calculateColumnName
	 * @param function
	 * @param columnType
	 * @param newSchema

	 */

	private void createColumn(String newColumnName, String calculateColumnName, String function, Class columnType, Schema newSchema) {	
		Class finalType = null;
		finalType = this.abstractAFF.getAggregateFunction(function, columnType).getType();
		newSchema.addColumn(newColumnName, finalType);
	}
	
	private static Schema createNodeSchema(){
		Schema nodeSchema = new Schema();
		nodeSchema.addColumn("label", String.class);
		return nodeSchema;
	}

	private static Schema createEdgeSchema(){
		Schema edgeSchema = new Schema();
		edgeSchema.addColumn("source",int.class);
		edgeSchema.addColumn("target",int.class);
		return edgeSchema;
	}

	
/*
	private ValueAttributes createFunctions(Tuple tup, prefuse.data.Table t, int rowNumber, ValueAttributes va){
		AggregateFunction af;
		boolean isEdge = false;
		String operateColumn = null;
		
		for(int k = 0; k < tup.getColumnCount(); k++){
			final String colName = tup.getColumnName(k);
			if(tup instanceof Edge){
				af = this.abstractAFF.getAggregateFunction(this.edgeFunctionMappings.getFunctionFromColumnName(colName), tup.getColumnType(k));
				isEdge = true;
			}
			else{
				af = this.abstractAFF.getAggregateFunction(this.nodeFunctionMappings.getFunctionFromColumnName(colName), tup.getColumnType(k));
				isEdge = false;
			}
			if(af != null){
				if(isEdge){
					operateColumn = this.edgeFunctionMappings.getOriginalColumnFromFunctionColumn(colName);
				}
				else{
					operateColumn = this.nodeFunctionMappings.getOriginalColumnFromFunctionColumn(colName);
				}
				af.operate(t.get(rowNumber, operateColumn));
				tup.set(k, af.getResult());
				va.addFunction(k, af);
			}
		}
		return va;
	}
*/
	private Table createTableSchema(Schema graphSchema, Table t) {
		for (int i = 0; i < graphSchema.getColumnCount(); i++) {
			t.addColumn(graphSchema.getColumnName(i), graphSchema.getColumnType(i));
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

/*
	private void operateFunctions(ValueAttributes va, prefuse.data.Graph graph, prefuse.data.Table table, Tuple tup, int rowNumber){
		AggregateFunction af = null;
		String operateColumn;
		
		for(int column = 0; column < tup.getColumnCount(); column++){
			final String colName = tup.getColumnName(column);
			af = va.getFunction(column);
			if(af != null){
				if(tup instanceof Edge){
					operateColumn = this.edgeFunctionMappings.getOriginalColumnFromFunctionColumn(colName);
				}
				else {
					operateColumn = this.nodeFunctionMappings.getOriginalColumnFromFunctionColumn(colName);
				}
				af.operate(table.get(rowNumber, operateColumn));
				tup.set(column, af.getResult());
			}
		}
	}
*/
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
/*
	private void constructAndModifyNode(String tableValue, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber){
		ValueAttributes va = this.nodeFunctionMappings.getFunctionRow(tableValue);
		// If we don't find a ValueAttributes object, we haven't seen this node before; create a new one.
		if(va == null){
			createNode(tableValue,graph,table,rowNumber);
		}
		else{
			int nodeNumber = va.getRowNumber();
			operateFunctions(va,graph,table,graph.getNode(nodeNumber),rowNumber);
		}
	}

	private void constructAndModifyEdge(String tableValue1, String tableValue2, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber){
		final TreeSet v = new TreeSet();
		v.add(tableValue1);
		v.add(tableValue2);

		ValueAttributes va = this.edgeFunctionMappings.getFunctionRow(v);
		// If we don't find a ValueAttributes object, we haven't seen this edge before; create a new one.
		if (va == null) {
			createEdge(v,graph,table,rowNumber);
		} else { 
			int edgeNumber = va.getRowNumber();
			operateFunctions(va,graph,table,graph.getEdge(edgeNumber),rowNumber);
		}
	}

	private void createEdge(TreeSet cv, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber){
		Object[] valueArray = cv.toArray();
		String firstValue = valueArray[0].toString();
		String secondValue = valueArray[1].toString();

		int fv = this.nodeFunctionMappings.getFunctionRow(firstValue).getRowNumber();
		int sv = this.nodeFunctionMappings.getFunctionRow(secondValue).getRowNumber();

		final int edgeRow = graph.addEdge(fv,sv);

		ValueAttributes va = new ValueAttributes(edgeRow);
		va = this.createFunctions(graph.getEdge(edgeRow), table, rowNumber, va);
		this.edgeFunctionMappings.addFunctionRow(cv, va);
	}
*/
	public String getSplitString(){
		return this.splitString;
	}

	public Properties getMetaData(){
		return this.functionDefinitions;
	}

}
