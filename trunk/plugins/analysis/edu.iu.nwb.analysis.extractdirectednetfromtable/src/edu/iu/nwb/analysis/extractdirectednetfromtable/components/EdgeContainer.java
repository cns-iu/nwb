package edu.iu.nwb.analysis.extractdirectednetfromtable.components;

import java.util.Vector;

import prefuse.data.Edge;
import prefuse.data.Node;
import prefuse.data.Tuple;
import edu.iu.nwb.analysis.extractnetfromtable.components.AggregateFunctionMappings;
import edu.iu.nwb.analysis.extractnetfromtable.components.ValueAttributes;

public class EdgeContainer {
	
	private static void createEdge(Vector edgeVector, prefuse.data.Graph graph, prefuse.data.Table table, 
			int rowNumber, AggregateFunctionMappings afm){
		
		Node source = (Node)edgeVector.get(0);
		Node target = (Node)edgeVector.get(1);	

		final Edge edg = graph.addEdge(source,target);

		ValueAttributes va = new ValueAttributes(edg.getRow());
		va = FunctionContainer.mutateFunctions(edg, table, rowNumber, va, afm);
		afm.addFunctionRow(edgeVector, va);
	}
	
	protected static void mutateEdge(Node source, Node target, prefuse.data.Graph graph, prefuse.data.Table table, 
			int rowNumber, AggregateFunctionMappings afm){
		final Vector edgeVector = new Vector(2);
		edgeVector.add(source);
		edgeVector.add(target);

		ValueAttributes va = afm.getFunctionRow(edgeVector);
		// If we don't find a ValueAttributes object, we haven't seen this edge before; create a new one.
		if (va == null) {
			createEdge(edgeVector,graph,table,rowNumber,afm);
		} else { 
			int edgeNumber = va.getRowNumber();
			FunctionContainer.mutateFunctions((Tuple)graph.getEdge(edgeNumber),table, rowNumber,va,afm);
		}
	}
	
	


}
