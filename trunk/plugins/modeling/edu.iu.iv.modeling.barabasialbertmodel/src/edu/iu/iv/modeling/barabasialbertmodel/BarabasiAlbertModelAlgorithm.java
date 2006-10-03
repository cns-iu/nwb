package edu.iu.iv.modeling.barabasialbertmodel;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
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

    public Data[] execute() {
    	int numTimeSteps = getInt("timeSteps");
	    int numInitialNodes = getInt("nodes");
	    int numEdges = getInt("edges");
	    boolean useSeed = ((Boolean) parameters.get("useSeed")).booleanValue();
	    
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
		    dm.getMetaData().put(DataProperty.LABEL,"Barabasi Albert Scale-Free Network Model");
		    dm.getMetaData().put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
		
	    
		    return new Data[]{dm};
	}
	
    private int getInt(String key) {
	    return ((Integer) parameters.get(key)).intValue();
	}
}