package edu.iu.iv.modeling.barabasialbertmodel;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.graph.Graph;

public class BarabasiAlbertModelAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public BarabasiAlbertModelAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	int numTimeSteps = getInt("timeSteps");
	    int numInitialNodes = getInt("nodes");
	    int numEdges = getInt("edges");
	    boolean useSeed = ((Boolean) parameters.get("useSeed")).booleanValue();
	    
        if (numEdges > numInitialNodes) {
            throw new AlgorithmExecutionException("Illegal Argument: number of " +
                    "edges ("+numEdges+") is > number of initial nodes ("+
                    numInitialNodes+")");
        }
        
        BarabasiAlbertModelGenerator generator = null ;
        if (useSeed) {
            int seed = getInt("seed");
            generator = new BarabasiAlbertModelGenerator(numInitialNodes, numEdges, seed);
        } else {
            generator = new BarabasiAlbertModelGenerator(numInitialNodes, numEdges);
        }
        
        generator.evolveGraph(numTimeSteps);
        Graph graph = generator.generateGraph();
	    
		
	    Data dm = new BasicData(graph, Graph.class.getName());
	    dm.getMetadata().put(DataProperty.LABEL,"Barabasi Albert Scale-Free Network Model");
	    dm.getMetadata().put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
	
    
	    return new Data[]{dm};
	}
	
    private int getInt(String key) {
	    return ((Integer) parameters.get(key)).intValue();
	}
}