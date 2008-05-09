package edu.iu.nwb.preprocessing.extractfromtable.extractedges.abovebelow;

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

public class ExtractEdgesAboveBelowAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private Integer fromThisNum;
    private Boolean aboveOrBelow;
    private String numericAttribute;
    
    public ExtractEdgesAboveBelowAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
        this.fromThisNum = (Integer) parameters.get("fromThisNum");
        this.aboveOrBelow = (Boolean) parameters.get("aboveOrBelow");
        this.numericAttribute = (String) parameters.get("numericAttribute");
    }

    public Data[] execute() {
     	Graph graph = (Graph) data[0].getData();
    	Graph extractedGraph = NewExtract.extractEdgesAbove(graph, this.fromThisNum.intValue(), this.numericAttribute, this.aboveOrBelow.booleanValue());
    	Data[] extractedGraphData = formatAsData(extractedGraph);
    	return extractedGraphData;
//    	
//    	Graph graph = (Graph) data[0].getData();
//		Table edgeTable = graph.getEdgeTable();
//		GraphUtil.printTable(edgeTable);
//    	Table extractedEdgeTable = Extract.extractAboveOrBelowEdges(edgeTable, fromThisNum.intValue(), this.numericAttribute, this.aboveOrBelow.booleanValue());  
//    	Graph extractedGraph = GraphUtil.copyAndMakeGraph(graph.getNodeTable(), extractedEdgeTable, graph.isDirected());
//    	Data[] extractedGraphData = formatAsData(extractedGraph);
//    	return extractedGraphData;
    }
  
    
    private Data[] formatAsData(Graph extractedGraph) {
    	StringBuilder label = new StringBuilder();
    	label.append ("all edges ");
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