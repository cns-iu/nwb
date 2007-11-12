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
	private static Map edgeMap = new HashMap();
	private static Map nodeMap = new HashMap();
	private static Map nodeFunctionMap = new HashMap();
	private static Map edgeFunctionMap = new HashMap();


	// a tester function to see that the correct attributes are present in the schema;
	private static void printColumnNames(Schema schema){
		//	for(int h = 0; h < pdt.getRowCount(); h++){
		for(int i = 0; i < schema.getColumnCount(); i++){
			System.out.println(schema.getColumnName(i));
		}
		//}
	}

	// a tester function to see that the correct function types are present in the schema;
	private static void printColumnTypes(Schema schema){
		for(int i = 0; i < schema.getColumnCount(); i++){
			System.out.println(schema.getColumnType(i));
		}
	}

	public static prefuse.data.Graph constructGraph(prefuse.data.Table pdt, String columnName, String split, Properties metaData, boolean directed){
		Schema inputNodeSchema = pdt.getSchema();

		Schema outputNodeSchema = createNodeSchema(inputNodeSchema,metaData);
		Schema outputEdgeSchema = createEdgeSchema(inputNodeSchema,metaData);

		//printColumnNames(outputNodeSchema);
		//printColumnTypes(outputNodeSchema);
		//printColumnNames(outputEdgeSchema);
		//printColumnTypes(outputEdgeSchema);
		Graph outputGraph = new Graph(outputNodeSchema.instantiate(), outputEdgeSchema.instantiate(), directed);


		constructNodesandEdges(pdt,columnName,split,metaData,outputGraph,directed);

		return outputGraph;

	}
	
	public static prefuse.data.Table constructTable(prefuse.data.Graph g){
		Table outputTable = new Table();
		outputTable = createTableSchema(g.getNodeTable().getSchema(),outputTable);
		outputTable = populateTable(outputTable,g);
		return outputTable;
	}
	
	private static Table createTableSchema(Schema graphSchema, Table t){
		//Schema outputSchema = new Schema();
		for(int i = 0; i < graphSchema.getColumnCount(); i++){
			t.addColumn(graphSchema.getColumnName(i), graphSchema.getColumnType(i));
		}
		t.addColumn("uniqueIndex", Integer.class);
		t.addColumn("combineValues", String.class, "*");
		
		return t;
	}
	
	private static Table populateTable(Table t, Graph g){
		
		for(Iterator it = g.nodes(); it.hasNext();){
			Node n = (Node)it.next();
			t.addRow();
			for(int i = 0; i < n.getColumnCount(); i++){
				t.set(t.getRowCount()-1, i, n.get(i));
		
			}
			
			t.set(t.getRowCount()-1, "uniqueIndex", new Integer(t.getRowCount()));
		}
		
		return t;
	}


	private static Schema createEdgeSchema(Schema joinSchema, Properties metaData){
		Schema output = new Schema();
		output.addColumn("source", int.class);
		output.addColumn("target", int.class);

		for(Iterator it = metaData.keySet().iterator(); it.hasNext();){
			String key = (String)it.next();

			//need to check against existing column names.
			if(key.startsWith("edge.")){
				String calculateColumnName = metaData.getProperty(key);
				int lastIndex = calculateColumnName.lastIndexOf(".");
				int firstIndex = key.indexOf(".");
				String function = calculateColumnName.substring(lastIndex+1);
				calculateColumnName = calculateColumnName.substring(0,lastIndex);
				Class columnType = joinSchema.getColumnType(calculateColumnName);

				createColumn(key.substring(firstIndex+1),calculateColumnName,function,columnType,output);
				edgeMap.put(key.substring(firstIndex+1), function);
				edgeFunctionMap.put(key.substring(firstIndex+1), calculateColumnName);

				//output.addColumn(key.substring(key.indexOf(".")+1), joinSchema.getColumnType(calculateColumnName));
			}
		}

		return output;
	}

	private static Schema createNodeSchema(Schema schema, Properties metaData){
		Schema output = new Schema();
		output.addColumn("label",String.class);
		for(Iterator it = metaData.keySet().iterator(); it.hasNext();){
			String key = (String)it.next();

			//need to add checking against existing column names.

			if(!key.startsWith("edge")){
				String calculateColumnName = metaData.getProperty(key);
				int lastIndex = calculateColumnName.lastIndexOf(".");
				String function = calculateColumnName.substring(lastIndex+1);
				calculateColumnName = calculateColumnName.substring(0,lastIndex);
				Class columnType = schema.getColumnType(calculateColumnName);


				createColumn(key,calculateColumnName,function,columnType,output);
				nodeMap.put(key, function);
				nodeFunctionMap.put(key, calculateColumnName);

				//output.addColumn(key, schema.getColumnType(newColumnName));
			}
		}

		return output;
	}


	private static void createColumn(String newColumnName, String calculateColumnName, String function, Class columnType,Schema newSchema){
		Class finalType = null;
		if(function.equalsIgnoreCase("count")){
			finalType = int.class;
		}
		else if(function.equalsIgnoreCase("sum")){
			finalType = columnType;
		}
		else if(function.equalsIgnoreCase("arithmeticmean")){
			if(columnType.equals(int.class) || columnType.equals(long.class)){
				finalType = double.class; //we may want to change this to allow more options, such as float?
			}
			else{
				finalType = columnType;
			}
		}
		else if(function.equalsIgnoreCase("geometricmean")){
			if(columnType.equals(int.class) || columnType.equals(long.class)){
				finalType = float.class;
			}
			else{
				finalType = columnType;
			}
		}



		newSchema.addColumn(newColumnName, finalType);
	}


	private static void constructNodesandEdges(prefuse.data.Table pdt,String columnName, String split, Properties metaData,prefuse.data.Graph g, boolean directed){
		HashMap valueList = new HashMap();
		HashMap coValueList = new HashMap();
		Node n;
		//int totalObjects = 0;
		for(int h = 0; h < pdt.getRowCount(); h++){
			String s = (String)pdt.get(h, columnName);

			//Here we ensure that we correctly capture regex metacharacters.
			//This means we can't split on regex, but I think that is acceptable.
			Pattern p = Pattern.compile("\\Q"+split+"\\E");
			Object[] Objects = p.split(s);


			for(int i = Objects.length-1; i > 0; i--){
				ValueAttributes va1 = (ValueAttributes)valueList.get(Objects[i]);
				if(va1 == null){
					n = g.addNode();
					n.set(0, Objects[i]);
					va1 = new ValueAttributes(g.getNodeCount()-1);
					createNodeFunctions(n,pdt,h,va1);
					valueList.put(Objects[i], va1);

				}
				else{
					operateNodeFunctions(va1,g,pdt,h);
					valueList.put(Objects[i], va1);
				}
				for(int j = 0; j < i; j++){
					va1 = (ValueAttributes)valueList.get(Objects[j]);
					if(va1 == null){
						n = g.addNode();
						n.set(0, Objects[j]);
						va1 = new ValueAttributes(g.getNodeCount()-1);
						createNodeFunctions(n,pdt,h,va1);
						valueList.put(Objects[j], va1);
						//	totalObjects++;
					}
					else{
						operateNodeFunctions(va1,g,pdt,h);
						valueList.put(Objects[j],va1);
					}
				
					CoValued v = new CoValued(Objects[j],Objects[i], directed);
					va1 = (ValueAttributes)coValueList.get(v);
					if(va1 == null){
						//String ss = (String)v.firstValue;
						va1 = new ValueAttributes(g.getEdgeCount());

						int fv = ((ValueAttributes)valueList.get(v.firstValue)).getRowNumber();
						int sv = ((ValueAttributes)valueList.get(v.secondValue)).getRowNumber();
						
						Edge e = g.addEdge(g.getNode(fv), g.getNode(sv));
						for(int k = 0; k < e.getColumnCount(); k++){
							String cn = e.getColumnName(k);
							if(edgeMap.get(cn) != null){
								int index = e.getColumnIndex(cn);
								createFunction(va1,(String)edgeMap.get(cn),e.getColumnType(cn),index);
								String operateColumn = (String)(edgeFunctionMap.get(cn));
								
								va1.getFunction(index).operate(pdt.get(h, operateColumn));
							
								e.set(index, va1.getFunction(index).getResult());
							}
						}
						coValueList.put(v, va1);
					}
					else{
						Edge e = g.getEdge(va1.getRowNumber());
						for(int k = 0; k < e.getColumnCount(); k++){
							String cn = e.getColumnName(k);
							if(edgeMap.get(cn) != null){
								int index = e.getColumnIndex(cn);
								String operateColumn = (String)edgeFunctionMap.get(cn);
								va1.getFunction(index).operate(pdt.get(h, operateColumn));
								e.set(index, va1.getFunction(index).getResult());
							}
						}

					}
				}

			}


		}

	}

	
	private static ValueAttributes createFunction(ValueAttributes va, String functionName, Class type, int columnNumber){
		
		String name = functionName.toLowerCase();
		if(name.equals("count")){
			va.addFunction(columnNumber, new Count());
		}
		else{
			
			if (name.equals("arithmeticmean")){

				if(type.equals(int.class) || type.equals(Integer.class))
					va.addFunction(columnNumber, new DoubleArithmeticMean());
				if(type.equals(double.class) || type.equals(Double.class))
					va.addFunction(columnNumber, new DoubleArithmeticMean());
				if(type.equals(float.class) || type.equals(Float.class))
					va.addFunction(columnNumber, new FloatArithmeticMean());
			}
			else if (name.equals("sum")){
				if(type.equals(int.class) || type.equals(Integer.class))
					va.addFunction(columnNumber, new IntegerSum());
				if(type.equals(double.class) || type.equals(Double.class))
					va.addFunction(columnNumber, new DoubleSum());
				if(type.equals(float.class) || type.equals(Float.class))
					va.addFunction(columnNumber, new FloatSum());
			}
			else if (name.equals("max")){
				if(type.equals(int.class) || type.equals(Integer.class))
					va.addFunction(columnNumber, new IntegerMax());
				if(type.equals(double.class) || type.equals(Double.class))
					va.addFunction(columnNumber, new DoubleMax());
				if(type.equals(float.class) || type.equals(Float.class))
					va.addFunction(columnNumber, new FloatMax());
			}
			else if (name.equals("min")){
				if(type.equals(int.class) || type.equals(Integer.class))
					va.addFunction(columnNumber, new IntegerMin());
				if(type.equals(double.class) || type.equals(Double.class))
					va.addFunction(columnNumber, new DoubleMin());
				if(type.equals(float.class) || type.equals(Float.class))
					va.addFunction(columnNumber, new FloatMin());
			}
		

			
		}

		return va;
	}
	
	private static void createNodeFunctions(Node n, prefuse.data.Table t, int rowNumber, ValueAttributes va){
		for(int k = 0; k < n.getColumnCount(); k++){
			String cn = n.getColumnName(k);

			if(nodeMap.get(cn) != null){
				createFunction(va,(String)nodeMap.get(cn),n.getColumnType(cn),n.getColumnIndex(cn));
				String operateColumn = (String)(nodeFunctionMap.get(cn));
				va.getFunction(n.getColumnIndex(cn)).operate(t.get(rowNumber, operateColumn));
				n.set(cn, va.getFunction(n.getColumnIndex(cn)).getResult());
			}
		}
	}
	
	private static void operateNodeFunctions(ValueAttributes va, prefuse.data.Graph g, prefuse.data.Table t, int rowNumber){
		Node n = g.getNode(va.getRowNumber());	
		for(int k = 0; k < n.getColumnCount(); k++){

			UtilityFunction uf = (UtilityFunction)va.getFunction(k);
			if(uf != null){
				String operateColumn = (String)(nodeFunctionMap.get((n.getColumnName(k))));
				uf.operate(t.get(rowNumber, operateColumn));
				n.set(k, uf.getResult());
			}
		}
	}
	

}
