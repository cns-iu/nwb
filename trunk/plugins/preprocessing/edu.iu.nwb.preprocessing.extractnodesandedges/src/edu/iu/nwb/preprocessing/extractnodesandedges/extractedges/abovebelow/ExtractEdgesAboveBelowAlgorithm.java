package edu.iu.nwb.preprocessing.extractnodesandedges.extractedges.abovebelow;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import edu.iu.nwb.preprocessing.extractnodesandedges.GraphDataFormatter;
import edu.uci.ics.jung.graph.Graph;

public class ExtractEdgesAboveBelowAlgorithm implements Algorithm {
	private Data inputData;
	private Graph originalGraph;

    private Double startingNumber;
    private Boolean belowInstead;
    private String column;

    public ExtractEdgesAboveBelowAlgorithm(
    		Data inputData,
    		Graph originalGraph,
    		Double startingNumber,
    		Boolean belowInstead,
    		String column) {
        this.inputData = inputData;
        this.originalGraph = originalGraph;
        this.startingNumber = startingNumber;
        this.belowInstead = belowInstead;
        this.column = column;
    }

    public Data[] execute() {
    	try {
    	Graph newGraph = filter(this.originalGraph);
    	Data[] newGraphData = formatAsData(newGraph);

    	return newGraphData;
    	} catch (Exception e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }

    private Graph filter(Graph graph) {
    	EdgeThresholdFilter filter = null;

    	if (belowInstead.booleanValue() == false) {
    		filter = new EdgeNumericDecorationFilter();
    	} else {
    		filter = new InverseEdgeNumericDecorationFilter();
    	}
    	
    	filter.setDecorationKey(column);
    	filter.setThreshold(startingNumber.doubleValue());
    	Graph newGraph = filter.filter(graph).assemble();

    	return newGraph;
    }
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	label.append("all edges with " + this.column);

    	if (this.belowInstead.booleanValue() == false) {
    		label.append(" above ");
    	} else {
    		label.append(" below ");
    	}

    	label.append("" + this.startingNumber);
    	Data[] data = GraphDataFormatter.formatExtractedGraphAsData(
    		extractedGraph, label.toString(), this.inputData);

    	return data;
    }
}