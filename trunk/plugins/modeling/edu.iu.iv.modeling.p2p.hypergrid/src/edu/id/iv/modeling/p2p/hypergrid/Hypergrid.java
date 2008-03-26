package edu.id.iv.modeling.p2p.hypergrid;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;




public class Hypergrid implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    LogService log;
    
    public Hypergrid(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	int networkSize = getInt("networkSize");
	    int degree = getInt("degree");
	    
		HypergridNetwork c = new HypergridNetwork(networkSize, degree, log);
		boolean is_done = c.buildHyperGridNetwork();
		
		
		if (is_done) {
			
			Data dm = new BasicData(c.getGraph(), Graph.class.getName());
			dm.getMetadata().put(DataProperty.LABEL,"Hypergrid Network Model");
		    dm.getMetadata().put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
		    
			println("Input Parameter: " + c.getGraph().numVertices() + " nodes");
			println("Network Properties: " + c.getGraph().numEdges() + " edges, " + 
					"min degree = " + c.getMinDegree() +
					", max degree = " + c.getMaxDegree() +
					", average degree = " + c.getAvgDegree() );
			return new Data[]{dm};
		   
		} else {
			throw new AlgorithmExecutionException("Hypergrid Network not built. "+c.getFailReason());
			
		}		
		
	}
	
    private int getInt(String string) {
	    return ((Integer) parameters.get(string)).intValue();
	}
    
    private void println(String string) {
        log.log(LogService.LOG_INFO, string);
    }
	
	
}