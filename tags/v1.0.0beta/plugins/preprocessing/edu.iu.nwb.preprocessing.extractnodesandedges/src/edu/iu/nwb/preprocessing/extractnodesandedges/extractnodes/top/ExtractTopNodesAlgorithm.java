package edu.iu.nwb.preprocessing.extractnodesandedges.extractnodes.top;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import edu.iu.nwb.preprocessing.extractnodesandedges.BinaryHeap;
import edu.iu.nwb.preprocessing.extractnodesandedges.GraphDataFormatter;
import edu.iu.nwb.preprocessing.extractnodesandedges.PriorityQueue;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphUtils;

public class ExtractTopNodesAlgorithm implements Algorithm {
	
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private int numTopNodes;
    private boolean fromBottomInstead;
    private String numericAttribute;
    
    private boolean noParams = false;
    
    public ExtractTopNodesAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        //if parameter values are not defined...
        if (parameters.get("numTopNodes") == null) {
        	//skip initialization and prepare to not execute
        	noParams = true;
        	return; 
        }
        
        this.numTopNodes = ((Integer) parameters.get("numTopNodes")).intValue();
        this.fromBottomInstead = ((Boolean) parameters.get("fromBottomInstead")).booleanValue();
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }

    public Data[] execute() {
    	if (noParams) return null;
    	Graph graph = (Graph) data[0].getData();
    	Graph extractedGraph = filter(graph);
    	Data[] extractedGraphData = formatAsData(extractedGraph);
    	return extractedGraphData;
    }
    
    private Graph filter(Graph g) {
    	Graph gToModify = (Graph) g.copy();
    	PriorityQueue nodesByRank = new BinaryHeap();
    	//for each node...
    	for (Iterator nodeIt = gToModify.getVertices().iterator(); nodeIt.hasNext();) {
    		Vertex v = (Vertex) nodeIt.next();
    		//add the node to the priority queue with rank according to specified numeric attribute
    		nodesByRank.insert(new ComparableNode(v, numericAttribute));
    	}
    
    	Set nodesToRemove = new HashSet();
    	//if we want to keep the top X...
    	if (fromBottomInstead != true) {
    		//delete from the bottom up, until we have X left
    		while (nodesByRank.size() > numTopNodes) {
    			Vertex v =  ((ComparableNode) nodesByRank.findMin()).getNode();
    			nodesToRemove.add(v);
    			nodesByRank.deleteMin();
    		}
    	} //else if want to keep the bottom X...
    	else {
    		//skip the first X from the bottom up
    		for (int ii = 0; ii < numTopNodes && (!nodesByRank.isEmpty()); ii++) {
    			nodesByRank.deleteMin();
    		}
    		//then delete the rest
    		while (!nodesByRank.isEmpty()) {
    			Vertex v =  ((ComparableNode) nodesByRank.findMin()).getNode();
    			nodesToRemove.add(v);
    			nodesByRank.deleteMin();
    		}
    	}
		GraphUtils.removeVertices(gToModify,nodesToRemove);
		return gToModify;
    }
  
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	if (this.fromBottomInstead) {
    		label.append("bottom ");
    	} else {
    		label.append("top ");
    	}
    	label.append("" + this.numTopNodes);
    	label.append(" nodes by " + this.numericAttribute);
    	Data[] data = 
    		GraphDataFormatter.formatExtractedGraphAsData(extractedGraph, label.toString(), this.data[0]);
    	return data;
    }
}