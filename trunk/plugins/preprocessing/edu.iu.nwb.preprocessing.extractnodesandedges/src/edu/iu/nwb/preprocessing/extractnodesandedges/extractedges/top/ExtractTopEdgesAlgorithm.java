package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.top;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import edu.iu.nwb.preprocessing.extractnodesandedges.BinaryHeap;
import edu.iu.nwb.preprocessing.extractnodesandedges.GraphDataFormatter;
import edu.iu.nwb.preprocessing.extractnodesandedges.PriorityQueue;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.utils.GraphUtils;

public class ExtractTopEdgesAlgorithm implements Algorithm {	
    private Data[] inData;
    private int numTopEdges;
    private boolean fromBottomInstead;
    private String numericAttribute;
    
    private boolean noParams = false;
    
    public ExtractTopEdgesAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters) {
        this.inData = data;
        //if parameter values are not defined...
        if (parameters.get("numTopEdges") == null) {
        	//skip initialization and prepare to not execute
        	noParams = true;
        	return; 
        }
        
        this.numTopEdges = ((Integer) parameters.get("numTopEdges")).intValue();
        this.fromBottomInstead = ((Boolean) parameters.get("fromBottomInstead")).booleanValue();
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }

    public Data[] execute() {
    	if (noParams) return null;
    	Graph graph = (Graph) inData[0].getData();
    	Graph extractedGraph = filter(graph);
    	Data[] extractedGraphData = formatAsData(extractedGraph);
    	return extractedGraphData;
    }
    
    private Graph filter(Graph g) {
    	Graph gToModify = (Graph) g.copy();
    	PriorityQueue edgesByRank = new BinaryHeap();
    	//for each edge...
    	for (@SuppressWarnings("rawtypes")
				Iterator edgeIt = gToModify.getEdges().iterator(); edgeIt.hasNext();) {
    		Edge e = (Edge) edgeIt.next();
    		//add the edge to the priority queue with rank according to specified numeric attribute
    		edgesByRank.insert(new ComparableEdge(e, numericAttribute));
    	}
    
    	Set<Edge> edgesToRemove = new HashSet<Edge>();
    	//if we want to keep the top X...
    	if (! fromBottomInstead) {
    		//delete from the bottom up, until we have X left
    		while (edgesByRank.size() > numTopEdges) {
    			Edge e =  ((ComparableEdge) edgesByRank.findMin()).getEdge();
    			edgesToRemove.add(e);
    			edgesByRank.deleteMin();
    		}
    	} //else if want to keep the bottom X...
    	else {//bottomInstead == FROM_BOTTOM
    		//skip the first X from the bottom up
    		for (int ii = 0; ii < numTopEdges && (!edgesByRank.isEmpty()); ii++) {
    			edgesByRank.deleteMin();
    		}
    		//then delete the rest
    		while (!edgesByRank.isEmpty()) {
    			Edge e =  ((ComparableEdge) edgesByRank.findMin()).getEdge();
    			edgesToRemove.add(e);
    			edgesByRank.deleteMin();
    		}
    	}
		GraphUtils.removeEdges(gToModify,edgesToRemove);
		return gToModify;
    }
  
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	if (this.fromBottomInstead) {
    		label.append("bottom ");
    	} else {
    		label.append("top ");
    	}
    	label.append("" + this.numTopEdges);
    	label.append(" edges by " + this.numericAttribute);
    	Data[] data = 
    		GraphDataFormatter.formatExtractedGraphAsData(extractedGraph, label.toString(), this.inData[0]);
    	return data;
    }
}