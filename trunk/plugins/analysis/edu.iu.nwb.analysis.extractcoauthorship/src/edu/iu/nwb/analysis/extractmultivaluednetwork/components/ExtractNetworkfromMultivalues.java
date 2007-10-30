package edu.iu.nwb.analysis.extractmultivaluednetwork.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;

public class ExtractNetworkfromMultivalues {
	HashMap valueList;
	HashSet coValueList;
	HashMap valueAttributes;
	prefuse.data.Table table;
	
	
	public ExtractNetworkfromMultivalues(prefuse.data.Table pdt, String columnName, String split, Properties metaData){
		valueList = new HashMap();
		coValueList = new HashSet();
		table = pdt;
		constructNodesandEdges(pdt,columnName, split);
		//printCoValues();
	}
	
	// a tester function to see that the correct attributes are present in the schema;
	private void printColumnNames(prefuse.data.Table pdt){
	//	for(int h = 0; h < pdt.getRowCount(); h++){
			for(int i = 0; i < pdt.getColumnCount(); i++){
				System.out.println(pdt.getColumnName(i));
			}
		//}
	}
	
	private prefuse.data.Graph constructGraph(prefuse.data.Table pdt){
		Schema inputNodeSchema = pdt.getSchema();
		
		Schema outputNodeSchema = createNodeSchema(inputNodeSchema);
		Schema outputEdgeSchema = createEdgeSchema(null);
		
		Graph outputGraph = new Graph(outputNodeSchema.instantiate(), outputEdgeSchema.instantiate(), false);
		//place all the authors as nodes and assign them the correct row number for later access
		int i = 0;
		for(Iterator it = valueList.keySet().iterator(); it.hasNext();){
			Object o = it.next();
		Node n = outputGraph.addNode();
		n.set(0, o);
		valueList.put(o, new Integer(i));
		i++;
		}
		for(Iterator it = coValueList.iterator(); it.hasNext();){
			CoValued cv = (CoValued)it.next();
			
			i = ((Integer)valueList.get(cv.firstValue)).intValue();
			int j = ((Integer)valueList.get(cv.secondValue)).intValue();
			outputGraph.addEdge(outputGraph.getNode(i), outputGraph.getNode(j));
		}
		
		return outputGraph;
		
	}
	
	public prefuse.data.Graph constructGraph(){
		return constructGraph(table);
	}
	
	private Schema createEdgeSchema(Schema joinSchema){
		Schema output = new Schema();
		output.addColumn("source", int.class);
		output.addColumn("target", int.class);
		
		return output;
	}
	
	private Schema createNodeSchema(Schema schema){
		Schema output = new Schema();
		
		/*for(int ii = 0; ii < schema.getColumnCount(); ii++) {
    		String columnName = schema.getColumnName(ii);
    		
			if(output.getColumnIndex(columnName) == -1) {
    			output.addColumn(columnName, schema.getColumnType(columnName), schema.getDefault(columnName));
    		}
    	}*/
		output.addColumn("label",String.class);
		
		return output;
	}
	
	
	
	private void constructNodesandEdges(prefuse.data.Table pdt,String columnName, String split){
		for(int h = 0; h < pdt.getRowCount(); h++){
			String s = (String)pdt.get(h, columnName);
			
			s = s.trim();
		 Object[] Objects = s.split("\\s*\\|\\s*");
			for(int i = 0; i < Objects.length; i++){
					valueList.put(Objects[i], new Integer(i+1));
				if(i > 0){
					coValueList.add(new CoValued(Objects[0],Objects[i]));
				}
			}
			
		}
		
	}
	
	// a tester function to see that I'm getting the correct values;
	private void printCoValues(){
		for(Iterator it = coValueList.iterator(); it.hasNext();){
			System.out.println(it.next());
		}
	}
	
	
}
