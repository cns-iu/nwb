package edu.iu.nwb.preprocessing.trimedges;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.expression.BooleanLiteral;
import prefuse.data.tuple.TupleSet;
import prefuse.data.util.Sort;

public class Trim implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public Trim(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	Graph graph = (Graph) data[0].getData();
    	
    	int leave = ((Integer) parameters.get("leave")).intValue();
		
		
		Table nodeTable = graph.getNodeTable().getSchema().instantiate();
		Table edgeTable = graph.getEdgeTable().getSchema().instantiate();
		
		String sourceField = graph.getEdgeSourceField();
		String targetField = graph.getEdgeTargetField();
		
		String nodeField = graph.getNodeKeyField();
		
		Iterator nodes = graph.getNodeTable().iterator();
		while(nodes.hasNext()) {
			nodeTable.addTuple(graph.getNodeTable().getTuple(((Integer) nodes.next()).intValue()));
			
		}
		
		Iterator edges = graph.getEdgeTable().iterator();
		while(edges.hasNext()) {
			Tuple tuple = graph.getEdgeTable().getTuple(((Integer) edges.next()).intValue());
			edgeTable.addTuple(tuple);
		}
		
		
		
		final Graph resultGraph = new Graph(
				nodeTable,
				edgeTable,
				graph.isDirected(),
				graph.getNodeKeyField(),
				graph.getEdgeSourceField(),
				graph.getEdgeTargetField());
		
		
		Table sorted = resultGraph.getNodeTable().select(BooleanLiteral.TRUE, new Sort() {
			public Comparator getComparator(TupleSet ts) {
				return new Comparator() {

					public int compare(Object one, Object two) {
						
						Tuple first = (Tuple) one;
						Tuple second = (Tuple) two;
						
						int firstDegree = resultGraph.getDegree(first.getRow());
						int secondDegree = resultGraph.getDegree(second.getRow());
						
						
						if(firstDegree < secondDegree) {
							return -1;
						} else if(firstDegree > secondDegree) {
							return 1;
						} else {
							return 0;
						}
					}
					
				};
			}
		});
		
		Random randomizer = new Random();
		
		Iterator iter = sorted.tuplesReversed(); //largest first
		while(iter.hasNext()) {
			Tuple tuple = (Tuple) iter.next();
			Node node = resultGraph.getNode(tuple.getRow());
			List connected = new ArrayList();
			Iterator connectedIter = node.edges();
			while(connectedIter.hasNext()) {
				connected.add(connectedIter.next());
			}
			while(node.getDegree() > leave) {
				
				Edge removed = (Edge) connected.remove(randomizer.nextInt(connected.size()));
				resultGraph.removeEdge(removed);
				
			}
		}
		
		
		Data result = new BasicData(resultGraph, Graph.class.getName());
		Dictionary metadata = result.getMetadata();
		metadata.put(DataProperty.LABEL, "Degree reduced to " + leave);
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		
		return new Data[] { result };
    }
}