package edu.iu.nwb.analysis.selfloops;

import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Graph;
import prefuse.data.Table;
import prefuse.data.Tuple;

public class Remove implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public Remove(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	Graph graph = (Graph) data[0].getData();
		
		
		Table nodeTable = graph.getNodeTable().getSchema().instantiate();
		Table edgeTable = graph.getEdgeTable().getSchema().instantiate();
		
		String sourceField = graph.getEdgeSourceField();
		String targetField = graph.getEdgeTargetField();
		
		Iterator nodes = graph.getNodeTable().iterator();
		while(nodes.hasNext()) {
			nodeTable.addTuple(graph.getNodeTable().getTuple(((Integer) nodes.next()).intValue()));
			
		}
		
		Iterator edges = graph.getEdgeTable().iterator();
		while(edges.hasNext()) {
			Tuple tuple = graph.getEdgeTable().getTuple(((Integer) edges.next()).intValue());
			if(!tuple.get(sourceField).equals(tuple.get(targetField))) {
				edgeTable.addTuple(tuple);
			}
		}
		
		
		
		Graph resultGraph = new Graph(
				nodeTable,
				edgeTable,
				graph.isDirected(),
				graph.getNodeKeyField(),
				graph.getEdgeSourceField(),
				graph.getEdgeTargetField());
		
		
		
		Data result = new BasicData(resultGraph, Graph.class.getName());
		Dictionary metadata = result.getMetadata();
		metadata.put(DataProperty.LABEL, "Without self loops");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		
		return new Data[] { result };
    }
}