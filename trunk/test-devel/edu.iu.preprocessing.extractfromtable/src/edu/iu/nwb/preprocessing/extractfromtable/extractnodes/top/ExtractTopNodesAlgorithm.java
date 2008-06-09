package edu.iu.nwb.preprocessing.extractfromtable.extractnodes.top;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.preprocessing.extractfromtable.Extract;
import edu.iu.nwb.preprocessing.extractfromtable.GraphDataFormatter;
import edu.iu.nwb.preprocessing.extractfromtable.GraphUtil;
import edu.iu.nwb.preprocessing.extractfromtable.NewExtract;

public class ExtractTopNodesAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private int numTopNodes;
    private boolean ascendingOrDescending;
    private String numericAttribute;
    
    public ExtractTopNodesAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
 	   this.numTopNodes = ((Double) parameters.get("numTopNodes")).intValue();
       this.ascendingOrDescending = ((Boolean) parameters.get("ascendingOrDescending")).booleanValue();
       this.numericAttribute = (String) parameters.get("numericAttribute");
    }

    public Data[] execute() {
    
    	Graph graph = (Graph) data[0].getData();
    	Graph extractedGraph = NewExtract.extractTopNodes(graph, numTopNodes, this.numericAttribute, this.ascendingOrDescending);
    	Data[] extractedGraphData = formatAsData(extractedGraph);
    	return extractedGraphData;
    }
  
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	if (this.ascendingOrDescending) {
    		label.append("top ");
    	} else {
    		label.append("bottom ");
    	}
    	label.append("" + this.numTopNodes);
    	label.append(" nodes by " + this.numericAttribute);
    	Data[] data = 
    		GraphDataFormatter.formatExtractedGraphAsData(extractedGraph, label.toString(), this.data[0]);
    	return data;
    }
}