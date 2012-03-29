package edu.iu.iv.modeling.p2p.randomnetwork;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class RandomNetworkAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public RandomNetworkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService)context.getService(LogService.class.getName());
        
    }

    public Data[] execute() {
    	int networkSize = ((Integer) parameters.get("networkSize")).intValue();
	    double probability = ((Double) parameters.get("probability")).doubleValue();
	    
		RandomNetwork rn = new RandomNetwork(networkSize, probability);
		rn.buildRandomNetwork();
		
		Data dm = new BasicData(rn.getGraph(), Graph.class.getName());
	    dm.getMetadata().put(DataProperty.LABEL,"Random Network");
	    dm.getMetadata().put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
		
		println("Input Parameter: " + rn.getGraph().numVertices() + " nodes, " +
				"Wiring Probability = " + rn.getWiringProbability()	);
		println("Network Properties: " + rn.getGraph().numEdges() + " edges, " + 
				"min degree = " + rn.getMinDegree() +
				", max degree = " + rn.getMaxDegree() +
				", average degree = " + rn.getAvgDegree() );
	
		return new Data[]{dm};
	}
    private void println(String string) {
        log.log(LogService.LOG_INFO, string);
    }
	   
}