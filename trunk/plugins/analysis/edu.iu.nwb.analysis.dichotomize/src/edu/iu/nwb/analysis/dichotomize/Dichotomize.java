package edu.iu.nwb.analysis.dichotomize;

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

public class Dichotomize implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public Dichotomize(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
		Graph graph = (Graph) data[0].getData();
		String attribute = (String) parameters.get("attribute");
		String comparator = (String) parameters.get("comparator");
		double cutoff = ((Double) parameters.get("cutoff")).doubleValue();
		
		//empty tables with the same schemas as the originals
		Table nodeTable = graph.getNodeTable().getSchema().instantiate();
		Table edgeTable = graph.getEdgeTable().getSchema().instantiate();
		
		//keep all nodes
		Iterator nodes = graph.getNodeTable().iterator();
		while(nodes.hasNext()) {
			nodeTable.addTuple(graph.getNodeTable().getTuple(((Integer) nodes.next()).intValue()));
		}
		
		//keep all edges matching the criteria
		Iterator edges = graph.getEdgeTable().iterator();
		while(edges.hasNext()) {
			Tuple tuple = graph.getEdgeTable().getTuple(((Integer) edges.next()).intValue());
			Object value = tuple.get(attribute);
			if(value != null) {
				if(passes(comparator, ((Number) value).doubleValue(), cutoff)) {
					edgeTable.addTuple(tuple);
				}
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
		metadata.put(DataProperty.LABEL, "Only edges with " + attribute + " " + comparator + " " + cutoff);
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		
		return new Data[] { result };
	}
	
	private boolean passes(String comparator, double value, double cutoff) {
		
		//this could be expanded, of course
		if(">=".equals(comparator)) {
			if(value >= cutoff) {
				return true;
			} else {
				return false;
			}
		} else if(">".equals(comparator)) {
			if(value > cutoff) {
				return true;
			} else {
				return false;
			}
		}
		
		
		return true;
	}
}