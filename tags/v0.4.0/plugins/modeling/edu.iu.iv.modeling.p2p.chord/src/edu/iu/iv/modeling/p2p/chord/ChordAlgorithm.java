package edu.iu.iv.modeling.p2p.chord;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.uci.ics.jung.graph.Graph;

public class ChordAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    private LogService logger;
    
    public ChordAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	Integer networkSize = (Integer) parameters.get("networkSize");
	    Integer fingerTableSize = (Integer) parameters.get("fingerTableSize");

		ChordNetwork c = new ChordNetwork(networkSize.intValue(), fingerTableSize.intValue(),logger);
		boolean is_done = c.buildChordNetwork();
		if (is_done) {
			
			    BasicData dm = new BasicData(c.getGraph(),Graph.class.getName());
			    Dictionary map = dm.getMetaData();
			    map.put(DataProperty.LABEL,"The CHORD Network Modeling");
			    map.put(DataProperty.TYPE,DataProperty.NETWORK_TYPE);
		
			println("Input Parameter: " + c.getGraph().numVertices() + " nodes");
			println("Network Properties: " + c.getGraph().numEdges() + " edges, " + 
					"min degree = " + c.getMinDegree() +
					", max degree = " + c.getMaxDegree() +
					", average degree = " + c.getAvgDegree() );
			return new Data[]{dm};
		} else {			
			println("CHORD Network not built. "+c.getFailReason());
			return null;
		}
	        
    }
    private void println(String string) {
    	 LogService log = (LogService) context.getService(LogService.class.getName());
         log.log(LogService.LOG_INFO, string);
    }
}