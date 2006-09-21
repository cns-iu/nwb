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
    	
    	int numNodes = getInt("numNodes");	   
    	Graph graph = (Graph)data[0].getData();
		ErrorTolerance et = new ErrorTolerance(graph, numNodes);
		boolean isDone = et.testErrorTolerance();
		Data[] newdata=null;
		if (isDone) {

		    Data model = new BasicData(et.getGraph(),Graph.class.getName());
		    Dictionary map = model.getMetaData();
		    map.put(DataProperty.MODIFIED, new Boolean(true));
		    map.put(DataProperty.PARENT, data[0]);
		    map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		    map.put(DataProperty.LABEL, "Error Tolerance");
		    newdata=new Data[]{model};
				
		}
		return newdata;
    
    }
    
    private int getInt(String mystring) {
		return ((Integer) parameters.get(mystring)).intValue();
	}
}