package edu.iu.iv.errortolerance;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.graph.Graph;

public class ErrorToleranceAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public ErrorToleranceAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	
    	int numNodes = ((Integer)parameters.get("numNodes")).intValue();	   
    	Graph graph = (Graph)data[0].getData();
		
		Data[] newdata=null;
		

		    Data model = new BasicData(ErrorTolerance.testErrorTolerance(graph, numNodes),Graph.class.getName());
		    Dictionary map = model.getMetadata();
		    map.put(DataProperty.MODIFIED, new Boolean(true));
		    map.put(DataProperty.PARENT, data[0]);
		    map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		    map.put(DataProperty.LABEL, "Random Node Deletion (Error Tolerance)");
		    newdata=new Data[]{model};
				
		
		return newdata;
    
    }
}