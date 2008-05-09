package edu.iu.nwb.preprocessing.extractfromtable.extractnodes.abovebelow;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;

import prefuse.data.Graph;
import edu.iu.nwb.preprocessing.extractfromtable.GraphDataFormatter;
import edu.iu.nwb.preprocessing.extractfromtable.NewExtract;

public class ExtractNodesAboveBelowAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private Integer fromThisNum;
    private Boolean aboveOrBelow;
    private String numericAttribute;
    
    public ExtractNodesAboveBelowAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.fromThisNum = (Integer) parameters.get("fromThisNum");
        this.aboveOrBelow = (Boolean) parameters.get("aboveOrBelow");
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }

    public Data[] execute() {
    	Graph graph = (Graph) data[0].getData();
    	Graph extractedGraph = NewExtract.extractNodesAbove(graph, this.fromThisNum.intValue(), this.numericAttribute, this.aboveOrBelow.booleanValue());
    	Data[] extractedGraphData = formatAsData(extractedGraph);
    	return extractedGraphData;
    	
//    	Graph graph = (Graph) data[0].getData();
//		Table edgeTable = graph.getEdgeTable();
//		GraphUtil.printTable(edgeTable);
//    	Table extractedEdgeTable = Extract.extractAboveOrBelowEdges(edgeTable, fromThisNum.intValue(), this.numericAttribute, this.aboveOrBelow.booleanValue());  
//    	Graph extractedGraph = GraphUtil.copyAndMakeGraph(graph.getNodeTable(), extractedEdgeTable, graph.isDirected());
//    	Data[] extractedGraphData = formatAsData(extractedGraph);
//    	return extractedGraphData;
    	
//    	Graph graph = (Graph) data[0].getData();
//		Table nodeTable = graph.getNodeTable();
//    	Table extractedNodeTable = Extract.extractAboveOrBelow(nodeTable, fromThisNum.intValue(), this.numericAttribute, this.aboveOrBelow.booleanValue());
//     	Graph extractedGraph = GraphUtil.copyAndMakeGraph(extractedNodeTable, graph.getEdgeTable(), graph.isDirected());
//    	Data[] extractedGraphData = formatAsData(extractedGraph);
//    	return extractedGraphData;
    }
  
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	label.append ("all nodes ");
    	if (this.aboveOrBelow.booleanValue()) {
    		label.append("above ");
    	} else {
    		label.append("below ");
    	}
    	label.append("" + this.fromThisNum);
    	label.append(" by " + this.numericAttribute);
    	Data[] data = 
    		GraphDataFormatter.formatExtractedGraphAsData(extractedGraph, label.toString(), this.data[0]);
    	return data;
    }
}