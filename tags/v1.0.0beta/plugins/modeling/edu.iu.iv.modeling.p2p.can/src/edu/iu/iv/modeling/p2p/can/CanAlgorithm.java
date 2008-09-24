package edu.iu.iv.modeling.p2p.can;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class CanAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private LogService logger;
    
    public CanAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	Object size = parameters.get("Number of nodes");

		CanNetwork c = new CanNetwork(((Integer) size).intValue(),logger);
		
		boolean is_done = c.buildCanNetwork();		
		if (is_done) {
			    BasicData dm = new BasicData(c.getGraph(),Graph.class.getName());
			    Dictionary map = dm.getMetadata();
			    map.put(DataProperty.LABEL,"CAN Network Model");
			    map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
			
			println("");
			println("Network Properties: " + c.getGraph().numEdges() + " edges, " + 
			        "min degree = " + c.getMinDegree() +
			        ", max degree = " + c.getMaxDegree() +
			        ", average degree = " + c.getAvgDegree() );
			
			return new Data[]{dm};
		} else {			
			throw new AlgorithmExecutionException("CAN Network not built. "+c.getFailReason());
		}
    }
    private void println(String string) {
        LogService log = (LogService) context.getService(LogService.class.getName());
        log.log(LogService.LOG_INFO, string);
    }
}