package edu.iu.nwb.analysis.extractdirectednetfromtable.components;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.components.AggregateFunctionMappings;
import edu.iu.nwb.analysis.extractnetfromtable.components.ValueAttributes;

public class NodeContainer {

	protected static Node createNode(String label, prefuse.data.Graph graph, prefuse.data.Table table, int rowNumber, 
			AggregateFunctionMappings nodeFunctionMappings){
		int nodeNumber = graph.addNodeRow();
		Node n = graph.getNode(nodeNumber);
		n.set(0, label);
		ValueAttributes va = new ValueAttributes(nodeNumber);
		va = FunctionContainer.mutateFunctions(n,table,rowNumber,va,nodeFunctionMappings);
		nodeFunctionMappings.addFunctionRow(label, va);
		return n;
	}
	
	protected static Node mutateNode(String tableValue, Graph g, Table t, int rowNumber, AggregateFunctionMappings afm){
		ValueAttributes va = afm.getFunctionRow(tableValue);
		Node n;
		// If we don't find a ValueAttributes object, we haven't seen this node before; create a new one.
		if(va == null){
			n = createNode(tableValue,g,t,rowNumber,afm);
		}
		else{
			int nodeNumber = va.getRowNumber();
			n = g.getNode(nodeNumber);
			FunctionContainer.mutateFunctions((Tuple)n,t,rowNumber,va,afm);
		}
		
		return n;
	}
	
}
